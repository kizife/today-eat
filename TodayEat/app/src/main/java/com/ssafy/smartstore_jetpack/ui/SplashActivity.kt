package com.ssafy.smartstore_jetpack.ui

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.ui.login.StartFragment
import com.ssafy.smartstore_jetpack.ui.order.ShoppingListFragment

private const val TAG = "SplashActivity_싸피"

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Log.d(TAG, "onCreate: ${intent.action}")

        var product = 0

        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TAG_DISCOVERED == intent.action
        ) {
            Log.d(TAG, "onCreate: 인식함")
            product = getNfcTableNum(intent)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, StartActivity::class.java).apply {
                this.putExtra("NfcProduct", product )
            }

            startActivity(intent)
            finish()
        }, 1000)
    }

    private fun getNfcTableNum(intent: Intent): Int {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val msg = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

            val messages = msg?.get(0) as NdefMessage
            val recode = messages.records[0]

            val num = String(recode.payload, 3, recode.payload.size - 3).toInt()
            Log.d(TAG, "getNfcTableNum: $num")

            return num
        }else{
            return  0
        }

    }

}