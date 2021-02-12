package com.smssender.app.data

import android.content.Context
import org.json.JSONArray

object UserData {

    fun saveLogin(context: Context, phone:String){
        context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).edit()
                .putString("login",phone).commit()
    }

    fun getLogin(context: Context):String? {
        return context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).getString("login","")
    }


    fun savePhoneNumber1(context: Context, phone:String){
        context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).edit()
            .putString("phone1",phone).commit()
    }

    fun getPhoneNumber1(context: Context):String? {
        return context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).getString("phone1","")
    }

    fun savePhoneNumber2(context: Context, phone:String){
        context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).edit()
                .putString("phone2",phone).commit()
    }

    fun getPhoneNumber2(context: Context):String? {
        return context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).getString("phone2","")
    }
    fun savePhoneNumber3(context: Context, phone:String){
        context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).edit()
                .putString("phone3",phone).commit()
    }
    fun getPhoneNumber3(context: Context):String? {
        return context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).getString("phone3","")
    }

    fun savePhoneNumber4(context: Context, phone:String){
        context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).edit()
                .putString("phone4",phone).commit()
    }
    fun getPhoneNumber4(context: Context):String? {
        return context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).getString("phone4","")
    }


    fun saveArrayNoneSyncSms(context: Context, array:JSONArray){
        context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).edit()
                .putString("sms_sync",array.toString()).commit()
    }
    fun getArrayNoneSyncSms(context: Context):JSONArray? {
        val string_arr=context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).getString("sms_sync","")
        if (string_arr.isNullOrEmpty()){
            return JSONArray()
        }
        val sms_array=JSONArray(string_arr)

        return sms_array
    }
    fun saveIsSendSms(context: Context, is_send:Boolean){
        context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).edit()
                .putBoolean("is_send",is_send).commit()
    }
    fun getIsSendSms(context: Context):Boolean{
        return context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE).getBoolean("is_send",true)
    }

}