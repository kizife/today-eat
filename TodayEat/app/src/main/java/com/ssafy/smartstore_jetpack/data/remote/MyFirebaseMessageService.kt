package com.ssafy.smartstore_jetpack.data.remote

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.session.MediaSession.Token
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.ui.MainActivity
import com.ssafy.smartstoredb.service.NotificationRequest
import com.ssafy.smartstoredb.service.TokenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

private const val TAG = "MyFirebaseMessageService_싸피"

class MyFirebaseMessageService : FirebaseMessagingService() {
    // 새로운 토큰이 생성될 때 마다 해당 콜백이 호출된다.
    override fun onNewToken(token: String) {
        super.onNewToken(token) //새롭게 발급받은 토큰은 서버로 전송,,
        Log.d(TAG, "onNewToken: $token")
        // 새로운 토큰 수신 시 서버로 전송

        sendTokenToServer(token)
    }

    // Foreground, Background 모두 처리하기 위해서는 data에 값을 담아서 넘긴다.
    //https://firebase.google.com/docs/cloud-messaging/android/receive
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        Log.d(TAG, "onMessageReceived: $remoteMessage")

        if (remoteMessage.data.isNotEmpty()) {
            // 메시지 데이터 처리
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            Log.d(TAG, "Received notification: Title = $title, Body = $body")
        }
        if (remoteMessage.notification != null) {
            // 알림 처리
            Log.d(TAG, "Received notification: ${remoteMessage.notification?.title}, ${remoteMessage.notification?.body}")
        }

        remoteMessage.notification?.let { message ->
            val messageTitle = message.title ?: "알림"
            val messageContent = message.body ?: ""
            saveMessageLocally(messageContent)
            showNotification(messageTitle, messageContent)

            sendNotificationToServer(messageTitle, messageContent)
        }
    }

    private fun sendNotificationToServer(title: String, body: String) {
        val notificationRequest = NotificationRequest(
            title = title,
            body = body,
            targetToken = "FCM_TARGET_TOKEN" // 적절한 타겟 FCM 토큰을 설정해야 합니다.
        )

        RetrofitUtil.fcmService.sendNotification(notificationRequest).enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "푸시 알림 전송 성공")
                } else {
                    Log.e(TAG, "푸시 알림 전송 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "푸시 알림 전송 실패: ${t.message}")
            }
        })
    }

    private fun saveMessageLocally(message: String) {

        val sharedPreferences = getSharedPreferences("NoticePreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val currentMessages = sharedPreferences.getStringSet("messages", mutableSetOf())
        currentMessages?.add(message)

        editor.putStringSet("messages", currentMessages)
        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(title: String, message: String) {
        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val mainPendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        MainActivity.createNotification(this, title, message, mainPendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            "SSAFY_CHANNEL",
            "ssafy",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)


    }

    private fun sendTokenToServer(token:String) {
        
        val tokenRequest = TokenRequest(token)
        RetrofitUtil.fcmService.uploadToken(tokenRequest).enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    Log.d(TAG, "onResponse: 토큰 서버로 전송 성공")
                } else {
                    Log.d(TAG, "onResponse: 토큰 서버 전송 실패")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "onFailure: 토큰전송실패 $${t.message}", )
            }
        })
    }

}