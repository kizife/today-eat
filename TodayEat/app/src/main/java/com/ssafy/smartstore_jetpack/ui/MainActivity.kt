package com.ssafy.smartstore_jetpack.ui

import HomeFragment
import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.base.BaseActivity
import com.ssafy.smartstore_jetpack.data.local.SharedPreferencesUtil
import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat
import com.ssafy.smartstore_jetpack.databinding.ActivityMainBinding
import com.ssafy.smartstore_jetpack.ui.ai.CategoryRecommend
import com.ssafy.smartstore_jetpack.ui.category.Category
import com.ssafy.smartstore_jetpack.ui.category.CategoryMain
import com.ssafy.smartstore_jetpack.ui.category.CategorySide
import com.ssafy.smartstore_jetpack.ui.category.CategorySoup
import com.ssafy.smartstore_jetpack.ui.category.DetailMain
import com.ssafy.smartstore_jetpack.ui.category.ProductDetail
import com.ssafy.smartstore_jetpack.ui.category.ProductReview
import com.ssafy.smartstore_jetpack.ui.my.MyPageFragment
import com.ssafy.smartstore_jetpack.ui.order.ShoppingListFragment
import com.ssafy.smartstore_jetpack.ui.search.SearchFragment

private const val TAG = "MainActivity_싸피"
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    var previousSelected = -1;
    private var recentPosition = 0

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pIntent: PendingIntent
    private lateinit var filters:Array<IntentFilter>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel.userId = SharedPreferencesUtil(this).getUser().id
        mainActivityViewModel.address = SharedPreferencesUtil(this).getUser().address
        // NFC 어댑터 초기화
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        setupNfcForegroundDispatch()

        currentActivity = this
        checkNotificationPermission()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "토큰 얻기 실패", task.exception)
                return@OnCompleteListener
            }
            Log.d(TAG, "token: ${task.result ?: "task.result is null"}")
        })
        createNotificationChannel(channel_id, "ssafy")
        createNotificationChannel("ssafy_channel", "ssafy")
        checkPermissions()


        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_main, HomeFragment())
            .commit()



        // 뒤로 가기 버튼을 오버라이드하여 첫 번째 탭으로 이동
        onBackPressedDispatcher.addCallback(this) {
            // 현재 선택된 탭 확인
            if (binding.bottomNavigation.selectedItemId != R.id.navigation_page_1) {
                // 첫 번째 탭으로 이동
                binding.bottomNavigation.selectedItemId = R.id.navigation_page_1
                hideBottomNav(false)
            } else {
                // 첫 번째 탭일 경우 기본 뒤로 가기 동작 호출
//                isEnabled = false // 이 콜백을 비활성화하고 기본 동작 실행
//                onBackPressedDispatcher.onBackPressed() // 기본 뒤로 가기 동작 수행
                moveTaskToBack(true)
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when(item.itemId){
                R.id.navigation_page_1 -> {
                    previousSelected = R.id.navigation_page_1
                    val fragmentTag = HomeFragment::class.java.simpleName
                    val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
                    if (fragment != null) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.anim_slide_in_from_left_fade_in,
                                R.anim.anim_fade_out
                            )
                            .remove(fragment) // 기존 프래그먼트 제거
                            .commit()
                    }
                    supportFragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.anim_slide_in_from_left_fade_in,
                        R.anim.anim_fade_out
                    )
                        .replace(R.id.frame_layout_main, HomeFragment(), fragmentTag)
                        .commit()
                    recentPosition = 0
                    true
                }
                R.id.navigation_page_2 -> {
                    previousSelected = R.id.navigation_page_2
                    val fragmentTag = SearchFragment::class.java.simpleName
                    val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)

                    if (fragment != null) {
                        if(recentPosition<1) {
                            supportFragmentManager.beginTransaction()
                                .setCustomAnimations(
                                    R.anim.anim_slide_in_from_right_fade_in,
                                    R.anim.anim_fade_out
                                )
                                .remove(fragment)
                                .commit()
                        } else {
                            supportFragmentManager.beginTransaction()
                                .setCustomAnimations(
                                    R.anim.anim_slide_in_from_left_fade_in,
                                    R.anim.anim_fade_out
                                )
                                .remove(fragment)
                                .commit()
                        }
                    }

                    if(recentPosition<1) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.anim_slide_in_from_right_fade_in,
                                R.anim.anim_fade_out
                            )
                            .replace(R.id.frame_layout_main, SearchFragment(), fragmentTag)
                            .commit()
                    } else {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.anim_slide_in_from_left_fade_in,
                                R.anim.anim_fade_out
                            )
                            .replace(R.id.frame_layout_main, SearchFragment(), fragmentTag)
                            .commit()
                    }
                    recentPosition = 1
                    true
                }

                R.id.blank -> {
                    binding.bottomNavigation.selectedItemId = previousSelected
                    val fragmentTag = Category::class.java.simpleName
                    val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)

                    if (fragment != null) {
                        if(recentPosition<2) {
                            supportFragmentManager.beginTransaction()
                                .setCustomAnimations(
                                    R.anim.anim_slide_in_from_right_fade_in,
                                    R.anim.anim_fade_out
                                )
                                .remove(fragment)
                                .commit()
                        } else if(recentPosition>2) {
                            supportFragmentManager.beginTransaction()
                                .setCustomAnimations(
                                    R.anim.anim_slide_in_from_left_fade_in,
                                    R.anim.anim_fade_out
                                )
                                .remove(fragment)
                                .commit()
                        }
                    }

                    if(recentPosition<2) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.anim_slide_in_from_right_fade_in,
                                R.anim.anim_fade_out
                            )
                            .replace(R.id.frame_layout_main, Category(), fragmentTag)
                            .commit()
                    } else if (recentPosition>2) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.anim_slide_in_from_left_fade_in,
                                R.anim.anim_fade_out
                            )
                            .replace(R.id.frame_layout_main, Category(), fragmentTag)
                            .commit()
                    }
                    recentPosition =2
                    true
                }

                R.id.navigation_page_3 -> {
                    binding.bottomNavigation.selectedItemId = previousSelected
                    val fragmentTag = ShoppingListFragment::class.java.simpleName
                    val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)

                    if (fragment != null) {
                        if(recentPosition<3) {
                            supportFragmentManager.beginTransaction()
                                .setCustomAnimations(
                                    R.anim.anim_slide_in_from_right_fade_in,
                                    R.anim.anim_fade_out
                                )
                                .remove(fragment)
                                .commit()
                        } else if(recentPosition>3) {
                            supportFragmentManager.beginTransaction()
                                .setCustomAnimations(
                                    R.anim.anim_slide_in_from_left_fade_in,
                                    R.anim.anim_fade_out
                                )
                                .remove(fragment)
                                .commit()
                        }
                    }

                    if(recentPosition<3) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.anim_slide_in_from_right_fade_in,
                                R.anim.anim_fade_out
                            )
                            .replace(R.id.frame_layout_main, ShoppingListFragment(), fragmentTag)
                            .commit()
                    } else if (recentPosition>3) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.anim_slide_in_from_left_fade_in,
                                R.anim.anim_fade_out
                            )
                            .replace(R.id.frame_layout_main, ShoppingListFragment(), fragmentTag)
                            .commit()
                    }
                    recentPosition =3
                    true
                }
                R.id.navigation_page_4 -> {
                    previousSelected = R.id.navigation_page_4
                    val fragmentTag = MyPageFragment::class.java.simpleName
                    val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)

                    if (fragment != null) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.anim_slide_in_from_right_fade_in,
                                R.anim.anim_fade_out
                            )
                            .remove(fragment)
                            .commit()
                    }

                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.anim_slide_in_from_right_fade_in,
                            R.anim.anim_fade_out
                        )
                        .replace(R.id.frame_layout_main, MyPageFragment(), fragmentTag)
                        .commit()
                    recentPosition=4
                    true
                }
                else -> false
            }
        }

        binding.categoryFab.setOnClickListener {

            previousSelected = R.id.category_fab
            val fragmentTag = Category::class.java.simpleName
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.anim_slide_in_from_left_fade_in,
                    R.anim.anim_fade_out
                )
                .replace(R.id.frame_layout_main, Category(), fragmentTag)
                .commit()
            true

        }


        binding.bottomNavigation.setOnItemReselectedListener { item ->
            // 재선택시 다시 랜더링 하지 않기 위해 수정
            if(binding.bottomNavigation.selectedItemId != item.itemId){
                binding.bottomNavigation.selectedItemId = item.itemId
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (nfcAdapter.isEnabled) {
            nfcAdapter.enableForegroundDispatch(this, pIntent, filters, null)
        }
    }
    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }

    private fun setupNfcForegroundDispatch() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        // Intent filters for NFC
        val ndefFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            addDataType("text/*")
        }
        filters = arrayOf(ndefFilter)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent: $intent")
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {

            val msg = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val messages = msg?.get(0) as NdefMessage
            val recode = messages.records[0]
            val num = String(recode.payload, 3, recode.payload.size - 3).toInt()
            Log.d(TAG, "getNfcTableNum: $num")
            showDialogNfc(num)

        }
    }


    private fun showDialogNfc(num: Int) {
        val customView = layoutInflater.inflate(R.layout.dialog_how_to_eat, null)
        customView.setBackgroundResource(R.drawable.rounded_dialog_background)

        AlertDialog.Builder(this)  // Activity를 참조
            .setView(customView)
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    fun opneFragmentBanner(index: String){
        val transaction = supportFragmentManager.beginTransaction()
        when{
            index == "Main" ->{

                val bottomNavigationView = this.findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNavigationView.selectedItemId = R.id.blank

                transaction.replace(R.id.frame_layout_main, CategoryMain())
                transaction.commit()

            }

            index == "Side" ->{

                val bottomNavigationView = this.findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNavigationView.selectedItemId = R.id.blank

                transaction.replace(R.id.frame_layout_main, CategorySide())
                transaction.commit()
            }

            index == "Soup" ->{
                val bottomNavigationView = this.findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNavigationView.selectedItemId = R.id.blank
                transaction.replace(R.id.frame_layout_main, CategorySoup())
                transaction.commit()
            }
            index == "Category" ->{
                val bottomNavigationView = this.findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNavigationView.selectedItemId = R.id.blank
                transaction.replace(R.id.frame_layout_main, Category())
                transaction.commit()
            }

            index == "Home" ->{
                val bottomNavigationView = this.findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNavigationView.selectedItemId = R.id.navigation_page_1
                transaction.replace(R.id.frame_layout_main, HomeFragment())
                transaction.commit()
            }

            index == "CategoryRecommend" ->{
                val bottomNavigationView = this.findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNavigationView.selectedItemId = R.id.blank
                transaction.replace(R.id.frame_layout_main, CategoryRecommend())
                transaction.commit()
            }
        }

    }

    fun openDetailFragment(index: String, product: ProductTodayeat) {
        val transaction = supportFragmentManager.beginTransaction()

        when{
           index == "detailMain" ->{

               val detailMain = DetailMain().apply {
                   arguments = Bundle().apply {
                       putParcelable("product", product) // 키와 값으로 전달
                   }
               }
               val bottomNavigationView = this.findViewById<BottomNavigationView>(R.id.bottom_navigation)
               bottomNavigationView.selectedItemId = R.id.blank

               transaction.replace(R.id.frame_layout_main, detailMain)
               transaction.commit()
           }

            index == "productDetail" ->{
                val productDetailFragment = ProductDetail().apply {
                    arguments = Bundle().apply {
                        putParcelable("product", product) // 키와 값으로 전달
                    }
                }
                val bottomNavigationView = this.findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNavigationView.selectedItemId = R.id.blank

                transaction.replace(R.id.detail_review_frame, productDetailFragment)
                transaction.commit()
            }

            index == "productReview" ->{
                val productReviewFragment = ProductReview().apply {
                    arguments = Bundle().apply {
                        putParcelable("product", product) // 키와 값으로 전달
                    }
                }
                val bottomNavigationView = this.findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNavigationView.selectedItemId = R.id.blank

                transaction.replace(R.id.detail_review_frame, productReviewFragment)
                transaction.commit()
            }
        }

    }

    fun updateBottomNavigationSelection(itemId: Int) {
        binding.bottomNavigation.selectedItemId = itemId
    }

    fun hideBottomNav(state : Boolean){
        if(state) {
            binding.bottomNavigation.visibility = View.GONE
            binding.categoryFab.visibility = View.GONE
        }
        else {
            binding.bottomNavigation.visibility = View.VISIBLE
            binding.categoryFab.visibility = View.VISIBLE
        }
    }

    // NotificationChannel 설정
    private fun createNotificationChannel(id: String, name: String) {

        val importance = NotificationManager.IMPORTANCE_DEFAULT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(id, name, importance).apply {
                description = "TodayEat 앱에서 사용하는 알림 채널입니다."
            }
            val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkPermissions(){

    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }


    companion object {
        const val channel_id = "ssafy_channel"
        var currentActivity: MainActivity? = null

        fun createNotification(
            context: Context,
            title: String?,
            content: String?,
            pendingIntent: PendingIntent
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 없으면 알림 생성하지 않음
                    return
                }
            }

            val builder = NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.infoicon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            with(NotificationManagerCompat.from(context)) {
                notify(101, builder.build())
            }
        }
    }

}