package com.smssender.app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.smssender.app.api.Api
import com.smssender.app.data.UserData
import com.smssender.app.sms.TimerService
import org.json.JSONArray


class MainActivity : AppCompatActivity(){
    lateinit var textLogin:TextView
    lateinit var editLogin:EditText

    lateinit var textPhone:TextView
    lateinit var editPhone:EditText

    lateinit var textPhone2:TextView
    lateinit var editPhone2:EditText

    lateinit var textPhone3:TextView
    lateinit var editPhone3:EditText

    lateinit var textPhone4:TextView
    lateinit var editPhone4:EditText

    lateinit var ibEdit:ImageButton
    lateinit var isSendSwitch:Switch
    lateinit var syncButton: ImageButton
    lateinit var tvNonSynsSms:TextView
    var isEdit=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS),
            1);
        textLogin=findViewById(R.id.tv_login)
        editLogin=findViewById(R.id.et_login)

        textPhone=findViewById(R.id.tv_phone1)
        editPhone=findViewById(R.id.et_phone1)

        textPhone2=findViewById(R.id.tv_phone2)
        editPhone2=findViewById(R.id.et_phone2)

        textPhone3=findViewById(R.id.tv_phone3)
        editPhone3=findViewById(R.id.et_phone3)

        textPhone4=findViewById(R.id.tv_phone4)
        editPhone4=findViewById(R.id.et_phone4)

        ibEdit=findViewById(R.id.ib_edit)
        syncButton=findViewById(R.id.ib_sync)
        isSendSwitch=findViewById(R.id.is_send_switch)
        tvNonSynsSms=findViewById(R.id.tv_sync_sms)
        updatePhones()
        ibEdit.setOnClickListener {
            if (isEdit){
                savePhone()
            }else{
                isEdit=true
                editLogin.visibility=View.VISIBLE
                textLogin.visibility=View.INVISIBLE

                editPhone.visibility=View.VISIBLE
                textPhone.visibility=View.INVISIBLE

                editPhone2.visibility=View.VISIBLE
                textPhone2.visibility=View.INVISIBLE

                editPhone3.visibility=View.VISIBLE
                textPhone3.visibility=View.INVISIBLE

                editPhone4.visibility=View.VISIBLE
                textPhone4.visibility=View.INVISIBLE

                editLogin.setText(UserData.getLogin(this))
                editPhone.setText(UserData.getPhoneNumber1(this))
                editPhone2.setText(UserData.getPhoneNumber2(this))
                editPhone3.setText(UserData.getPhoneNumber3(this))
                editPhone4.setText(UserData.getPhoneNumber4(this))

                ibEdit.setImageResource(R.drawable.ic_save)
            }
        }
        syncButton.setOnClickListener {
            val arraySync=UserData.getArrayNoneSyncSms(this)
            if (arraySync!!.length()!=0){
                Log.d("D_MainActivity","onCreate: ${arraySync}");
                Api.sendMessage(makeJsonObjectFromArray(arraySync),{
                    Log.d("D_MainActivity","onCreate: "+it);
                    if (it==Api.Result.OK) {
                        UserData.saveArrayNoneSyncSms(this, JSONArray())
                        updateNonSyncTv()
                        Toast.makeText(this,"Cинхронизация успешна.",Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(this,"Ошибка синхронизации!!",Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
        isSendSwitch.isChecked=UserData.getIsSendSms(this)
        isSendSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            UserData.saveIsSendSms(this,isChecked)
        }

        requestPermissions()
        updateNonSyncTv()
        startTimer()
    }
    private fun startTimer(){
        val pushIntent = Intent(this, TimerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(pushIntent)
        }else{
            startService(pushIntent)
        }
    }
    private fun updatePhones(){

        val login=UserData.getLogin(this)
        editLogin.visibility=View.INVISIBLE
        textLogin.visibility=View.VISIBLE
        if (!login.isNullOrEmpty()){
            textLogin.setText(login)
        }


        val phoneNumber1=UserData.getPhoneNumber1(this)
        editPhone.visibility=View.INVISIBLE
        textPhone.visibility=View.VISIBLE
        if (!phoneNumber1.isNullOrEmpty()){
            textPhone.setText(phoneNumber1)
        }

        val phoneNumber2=UserData.getPhoneNumber2(this)
        editPhone2.visibility=View.INVISIBLE
        textPhone2.visibility=View.VISIBLE

        if (!phoneNumber2.isNullOrEmpty()){
            textPhone2.setText(phoneNumber2)
        }

        val phoneNumber3=UserData.getPhoneNumber3(this)
        textPhone3.visibility=View.VISIBLE

        editPhone3.visibility=View.INVISIBLE
        if (!phoneNumber3.isNullOrEmpty()){
            textPhone3.setText(phoneNumber3)
        }

        val phoneNumber4=UserData.getPhoneNumber4(this)
        textPhone4.visibility=View.VISIBLE

        editPhone4.visibility=View.INVISIBLE
        if (!phoneNumber4.isNullOrEmpty()){
            textPhone4.setText(phoneNumber4)
        }

    }
    private fun updateNonSyncTv(){
        tvNonSynsSms.setText("Не синхронизированых смс: ${UserData.getArrayNoneSyncSms(this)?.length()}")
    }
    private fun requestPermissions(){
        val mPhoneNumber = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_SMS
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_PHONE_NUMBERS
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.RECEIVE_SMS
                )!=PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE ),
                    1);
        }else {
            val userPhone=mPhoneNumber.getLine1Number()
            Log.d("D_MainActivity","onCreate: ${userPhone}");

        }
    }
    private fun savePhone(){
        val phoneText1=editPhone.text.toString()
        val phoneText2=editPhone2.text.toString()
        val phoneText3=editPhone3.text.toString()
        val phoneText4=editPhone4.text.toString()
        val login=editLogin.text.toString()


        UserData.savePhoneNumber1(this,phoneText1)
        UserData.savePhoneNumber2(this,phoneText2)
        UserData.savePhoneNumber3(this,phoneText3)
        UserData.savePhoneNumber4(this,phoneText4)
        UserData.saveLogin(this,login)

        isEdit=false
            ibEdit.setImageResource(R.drawable.ic_edit)

        updatePhones()

    }



}