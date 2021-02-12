package com.smssender.app.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import com.smssender.app.api.Api
import com.smssender.app.data.UserData
import com.smssender.app.makeJsonObjectFromArray
import org.json.JSONArray


class NetworReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("D_NetworReceiver","onReceive: network changed");
        if (isOnline(context!!)){
            val no_sync_messages=UserData.getArrayNoneSyncSms(context)!!
            if (no_sync_messages.length()!=0){
                Api.sendMessage(makeJsonObjectFromArray(no_sync_messages),{
                    if (it==Api.Result.OK){
                        UserData.saveArrayNoneSyncSms(context, JSONArray())
                    }
                })
            }
        }
    }
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isConnected
    }
}