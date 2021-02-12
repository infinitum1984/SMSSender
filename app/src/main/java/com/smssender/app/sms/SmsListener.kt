package com.smssender.app.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import com.smssender.app.api.Api
import com.smssender.app.data.UserData
import com.smssender.app.makeArrayElement
import com.smssender.app.makeJsonFromMessage


class SmsListener : BroadcastReceiver() {
    // Get the object of SmsManager
    val sms: SmsManager = SmsManager.getDefault()

    override fun onReceive(context: Context?, intent: Intent) {

        // Retrieves a map of extended data from the intent.
        if (!UserData.getIsSendSms(context!!)) return

        val bundle = intent.extras

        try {
            if (bundle != null) {
                val pdusObj =
                    bundle["pdus"] as Array<Any>?
                var slot = bundle.getInt("slot", -1)
                if (slot==-1){
                   slot=bundle.getInt("phone",-1)
                }
                Log.d("D_SmsListener","onReceive: slot ${slot}");
                for (i in pdusObj!!.indices) {
                    val currentMessage: SmsMessage =
                        SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                    val data= makeJsonFromMessage(context!!,currentMessage, slot)
                    Log.d("D_SmsListener","onReceive: ${currentMessage.userData}");

                    Api.sendMessage(data,{
                        Log.d("D_SmsListener","onReceive: ${it}");
                        if (it==Api.Result.EXCEPTION){
                            val savedArray=UserData.getArrayNoneSyncSms(context!!)
                            savedArray!!.put(makeArrayElement(context,currentMessage,slot))
                            UserData.saveArrayNoneSyncSms(context!!,savedArray)
                        }
                    })

                } // end for loop
            } // bundle is null
        } catch (e: Exception) {
            Log.e("SmsReceiver", "Exception smsReceiver$e")
        }
    }
}