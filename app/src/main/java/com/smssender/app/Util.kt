package com.smssender.app

import android.content.Context
import android.telephony.SmsMessage
import com.smssender.app.data.UserData
import org.json.JSONArray
import org.json.JSONObject

fun makeJsonFromMessage(context: Context, smsMessage: SmsMessage, slot:Int):JSONObject{

    val root=JSONObject()
    root.put("data",JSONArray(listOf(makeArrayElement(context,smsMessage,slot))))
    return root
}
fun makeArrayElement(context: Context, smsMessage: SmsMessage, slot:Int):JSONObject{
    val data=JSONObject()
    val phoneNumber: String = smsMessage.getDisplayOriginatingAddress()
    val message: String = smsMessage.getDisplayMessageBody()
    data.put("login", UserData.getLogin(context))
    var phone=""
    when(slot){
        0->phone= UserData.getPhoneNumber1(context).toString()
        1->phone=UserData.getPhoneNumber2(context).toString()
        2->phone=UserData.getPhoneNumber3(context).toString()
        3->phone=UserData.getPhoneNumber4(context).toString()
        else->phone= UserData.getPhoneNumber1(context).toString()
    }
    data.put("phone", phone)


    data.put("sender",phoneNumber)
    data.put("text",message)
    return data
}
fun makeJsonObjectFromArray(smsMessage: JSONArray):JSONObject{

    val root=JSONObject()
    root.put("data",smsMessage)
    return root
}