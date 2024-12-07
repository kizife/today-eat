package com.ssafy.smartstore_jetpack.ui

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.base.BaseActivity
import com.ssafy.smartstore_jetpack.databinding.FragmentBaseBinding
import com.ssafy.smartstore_jetpack.ui.login.JoinFragment
import com.ssafy.smartstore_jetpack.ui.login.LoginFragment
import com.ssafy.smartstore_jetpack.ui.login.StartFragment
import com.ssafy.smartstore_jetpack.ui.order.ShoppingListFragment
import kotlin.math.log

private const val TAG = "StartActivity_싸피"

class StartActivity : BaseActivity<FragmentBaseBinding>(FragmentBaseBinding::inflate) {

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pIntent: PendingIntent
    private lateinit var filters:Array<IntentFilter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productNum = intent.getIntExtra("NfcProduct", 0)
        if (productNum != 0){
            showDialogNfc(productNum)
        }


        // NFC 어댑터 초기화
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        setupNfcForegroundDispatch()

        //로그인 된 상태인지 확인
        val user = ApplicationClass.sharedPreferencesUtil.getUser()

        //로그인 상태 확인. id가 있다면 로그인 된 상태
        if (user.id != ""){
            openFragment(1)
        } else {
            // 가장 첫 화면은 홈 화면의 Fragment로 지정
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.anim_slide_in_from_left_fade_in,
                    R.anim.anim_fade_out
                )
                .replace(R.id.frame_layout_start, StartFragment())
                .commit()
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
        val intent = Intent(this, StartActivity::class.java).apply {
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


    fun openFragment(int: Int){
        val transaction = supportFragmentManager.beginTransaction()
        when(int){
            1 -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
            }
            2 -> transaction
                .setCustomAnimations(
                    R.anim.anim_slide_in_from_right_fade_in,
                    R.anim.anim_fade_out
                ).replace(R.id.frame_layout_start, JoinFragment())
                .addToBackStack(null)

            3 -> {
                // 회원가입한 뒤 돌아오면, 2번에서 addToBackStack해 놓은게 남아 있어서,
                // stack을 날려 줘야 한다. stack날리기.
                supportFragmentManager.popBackStack()
                transaction
                    .setCustomAnimations(
                        R.anim.anim_slide_in_from_right_fade_in,
                        R.anim.anim_fade_out
                    ).replace(R.id.frame_layout_start, LoginFragment())
            }

            4-> {
                transaction.setCustomAnimations(
                    R.anim.anim_slide_in_from_right_fade_in,
                    R.anim.anim_fade_out
                ).replace(R.id.frame_layout_start, LoginFragment())

                    .addToBackStack(null)
            }
        }
        transaction.commit()
    }

}