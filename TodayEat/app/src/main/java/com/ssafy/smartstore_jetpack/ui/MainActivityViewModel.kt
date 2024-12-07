package com.ssafy.smartstore_jetpack.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.data.model.dto.Combination
import com.ssafy.smartstore_jetpack.data.model.dto.OrderDetailInfoTodayeat
import com.ssafy.smartstore_jetpack.data.model.dto.OrderDetailTodayeat
import com.ssafy.smartstore_jetpack.data.model.dto.OrderTodayeat
import com.ssafy.smartstore_jetpack.data.model.dto.ShoppingCartTodayeat
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import com.ssafy.smartstoredb.service.NotificationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "MainActivityViewModel_싸피"
class MainActivityViewModel : ViewModel() {
    var userId = ""
    var diriveryTime = ""
    var address = ""

    private val _orderStatus = MutableLiveData<String>()

    fun setOrderStatus(status: String) {
        _orderStatus.value = status
    }

    private val _cartTotalPrice = MutableLiveData<Int>().apply { value = 0 }
    val cartTotalPrice: LiveData<Int> get() = _cartTotalPrice

    private lateinit var fcmToken: String

    // 선택된 productId
    private val _productId = MutableLiveData<Int>()
    val productId: LiveData<Int>
        get() = _productId

    val completedCombi = mutableListOf<Combination>()
    val quantityList = mutableListOf<Int>()
    val mainDish = mutableListOf<ShoppingCartTodayeat>()
    val sideDish = mutableListOf<ShoppingCartTodayeat>()
    val soupDish = mutableListOf<ShoppingCartTodayeat>()

    fun addShoppingList(shoppingCart: ShoppingCartTodayeat){
        when (shoppingCart.type) {
            "main" -> mainDish.add(shoppingCart)
            "side" -> sideDish.add(shoppingCart)
            "soup" -> soupDish.add(shoppingCart)
        }
        makeCombi()
        Log.d(TAG, "updated completedCombi : $completedCombi")
        Log.d(TAG, "updated quantityList : $quantityList")
    }

    fun makeCombi() {

        if (mainDish.isNotEmpty() && sideDish.size >= 2 && soupDish.isNotEmpty()) {
            Log.d(TAG, "makeCombi: $userId")

            CoroutineScope(Dispatchers.Main).launch {
                runCatching {

                    RetrofitUtil.combiService.insertCombi(
                        Combination(
                            dosirackPrice = calculateTotalPrice(mainDish, sideDish, soupDish),
                            dosirockId =  -1,
                            main = mainDish[0].productId,
                            sideDish1 = sideDish[0].productId,
                            sideDish2 = sideDish[1].productId,
                            soup = soupDish[0].productId,
                            userId = userId
                        )
                    )
                }.onSuccess {
                    checkIfSetIsComplete(it)
                }.onFailure { exception ->
                    Log.e(TAG, "makeCombi 실패: ${exception.message}")
                }
            }
        }

    }


    fun checkIfSetIsComplete(id:Int) {
        // main, side, soup로 나누어서 세트 조합 생성
        if (mainDish.isNotEmpty() && sideDish.size >= 2 && soupDish.isNotEmpty()) {
          makeCombi()
            completedCombi.add(
                Combination(
                    dosirackPrice = calculateTotalPrice(mainDish, sideDish, soupDish),
                    dosirockId = id,
                    main = mainDish[0].productId,
                    sideDish1 = sideDish[0].productId,
                    sideDish2 = sideDish[1].productId,
                    soup = soupDish[0].productId,
                    userId = userId
                )
            )
            // 리스트 초기화
            mainDish.clear()
            sideDish.clear()
            soupDish.clear()
        }
    }


    private fun calculateTotalPrice(
        mainDish: List<ShoppingCartTodayeat>,
        sideDish: List<ShoppingCartTodayeat>,
        soupDish: List<ShoppingCartTodayeat>
    ): Int {
        val mainPrice = mainDish.sumOf { it.productPrice }
        val sidePrice = sideDish.sumOf { it.productPrice }
        val soupPrice = soupDish.sumOf { it.productPrice }
        return mainPrice + sidePrice + soupPrice
    }

    fun updateCartTotalPrice() {
        val totalPrice = completedCombi.indices.sumOf { index ->
            val combi = completedCombi[index]
            val quantity = quantityList.getOrElse(index) { 1 } // 기본 수량을 1로 설정
            combi.dosirackPrice * quantity
        }
        // 총 가격을 LiveData에 저장
        _cartTotalPrice.value = totalPrice.toInt()
    }

    @SuppressLint("NewApi")
    fun getOrder(){

        // 현재 시간 구하기
        val currentDateTime = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val formattedDate = currentDateTime.format(formatter)

        val order = OrderTodayeat(
            orderId = 0,
            userId = userId,
            orderTime = formattedDate,
            address = address,
            completed = "N",
            deliveryTime = diriveryTime,
            details = getDetail(),
            detailInfos = getDetailInfo()
        )

        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                val orderId = RetrofitUtil.orderService.makeOrder(order)
                Log.d(TAG, "getOrder: 주문 완료~~")
                updateOrderStatus(orderId)
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    completedCombi.clear()
                    quantityList.clear()
                    setOrderStatus("주문이 완료되었습니다!")
                }

                Log.d(TAG, "주문을 성공한 겟오더 getOrder: $it , $order ")

            }.onFailure {
                Log.d(TAG, "실패한 겟오더 getOrder: ${it.message}")
                setOrderStatus("주문이 실패")
            }
        }
    }


     fun updateOrderStatus(orderId: Int) {
         viewModelScope.launch {
             runCatching {
                 delay(10000)
                 Log.d(TAG, "updateOrderStatus: 상태바꾸기 들어갑니다. $orderId ")
                 val response = RetrofitUtil.orderService.updateOrderStatus(orderId)
                 getFcmToken()
                 if (response.body() != 0) {
                     val status = response.body()
                     Log.d(TAG, "상태 업데이트 성공: $status")
                     sendOrderStatusNoti(orderId)

                 } else {
                     Log.e(TAG, "상태 업데이트 실패: ${response.errorBody()}")
                 }
             }.onFailure { exception ->
                 Log.e(TAG, "updateOrderStatus 실패: ${exception.message}")
             }
         }
    }

    fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

           fcmToken = task.result
            Log.d(TAG, "FCM Token: $fcmToken")

            sendPushNotification(fcmToken, "배달 완료", "고객님의 도시락이 도착했습니다!")
            setOrderStatus("배송이 완료되었습니다!")
        }
    }

    private fun sendPushNotification(fcmToken: String, title: String, body: String) {
        Log.d(TAG, "sendPushNotification: 알림좀보내주시면안되나요 ")
        Log.d(TAG, "sendPushNotification: $fcmToken, $title, $body")

        RetrofitUtil.fcmService.sendPushMessage(fcmToken, title, body)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>){}
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e(TAG, "sendPushNotification: 알림 전송 실패 - ${t.message}")
                }
            })
    }

    fun sendOrderStatusNoti(orderId: Int) {
        val user = ApplicationClass.sharedPreferencesUtil.getUser()
        val notificationTitle = "배달 완료🍱"
        val notificationBody = "${user.name}님의 도시락이 도착했습니다!"

        FirebaseMessaging.getInstance().send(RemoteMessage.Builder("653686631519@gcm.googleapis.com")
            .addData("title", notificationTitle)
            .addData("body", notificationBody)
            .build())
    }


    fun getDetail(): List<OrderDetailTodayeat> {
        val list = mutableListOf<OrderDetailTodayeat>()
        for (i in completedCombi.indices) {
            val combi = completedCombi[i]
            val q = quantityList[i] // 두 번째 리스트의 해당 항목

            Log.d(TAG, "getDetail: $q")

            // 두 리스트 항목을 사용하여 작업 수행
            val newOrder = OrderDetailTodayeat(combi.dosirockId, -1, -1, q, combi.dosirackPrice * q )
            list.add(newOrder)
        }
        return list // 수정: 빈 리스트가 아니라 실제로 채운 리스트를 반환
    }


    fun getDetailInfo():List<OrderDetailInfoTodayeat>{
        val list = mutableListOf<OrderDetailInfoTodayeat>()
//        updateCartTotalPrice()
        val total = cartTotalPrice.value!!
        for (combi in completedCombi) {
            val newOrder = OrderDetailInfoTodayeat(
                combi.dosirackPrice,
                combi.dosirockId,
                -1,
                "combi.main",
                -1,
                1,
                "combi.sideDish1",
                "combi.sideDish2",
                "combi.soup",
                total
            )
            list.add(newOrder)
        }
        return list
    }

}