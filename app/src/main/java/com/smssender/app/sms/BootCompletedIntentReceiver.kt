package com.smssender.app.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class BootCompletedIntentReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if ("android.intent.action.BOOT_COMPLETED" == intent!!.action) {
            val pushIntent = Intent(context, TimerService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context!!.startForegroundService(pushIntent)
            }else{
                context!!.startService(pushIntent)
            }
            val push2Intent = Intent(context, SmsListener::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context!!.startForegroundService(push2Intent)
            }else{
                context!!.startService(push2Intent)
            }

        }
    }
}