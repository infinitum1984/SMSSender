package com.smssender.app.sms

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import androidx.core.app.NotificationCompat
import com.smssender.app.R
import com.smssender.app.api.Api
import com.smssender.app.data.UserData
import com.smssender.app.makeJsonObjectFromArray
import org.json.JSONArray
import java.util.*


class TimerService:Service() {
    var cur_cal: Calendar = Calendar.getInstance()
    private var wakeLock: WakeLock? = null

    override fun onCreate() {
        // TODO Auto-generated method stub
        super.onCreate()
        val intent = Intent(this, TimerService::class.java)
        val pintent = PendingIntent.getService(applicationContext,
                0, intent, 0)
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        cur_cal.setTimeInMillis(System.currentTimeMillis())
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis(),
                120000.toLong(), pintent)
        Log.d("D_TimerService","onCreate: ");
        showNotification()
    }

    override fun onStart(intent: Intent?, startId: Int) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId)
        checkSms()
        Log.d("D_TimerService","onStart: Timer work");
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("InvalidWakeLockTag")
    private fun showNotification(){
        //wake lock is need to keep timer alive when device goes to sleep mode
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PARTIAL_WAKE_LOCK_TAG");
        createNotificationChannel();
        val notification =  NotificationCompat.Builder(this, getString(R.string.app_name)).setContentTitle(getString(R.string.app_name))
        .setContentText("Content").build();

        startForeground(1001, notification);
    }
    fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence =getString(R.string.app_name)
            val description = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.app_name), name, importance)
            channel.description = description
            val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun checkSms(){
        val jsonArray=UserData.getArrayNoneSyncSms(this)

        if (jsonArray!!.length()==0) return

        Api.sendMessage(makeJsonObjectFromArray(jsonArray),{
            if (it==Api.Result.OK){
                Log.d("D_TimerService","checkSms: OK");

                UserData.saveArrayNoneSyncSms(this, JSONArray())
            }else{
                Log.d("D_TimerService","checkSms: Exception");
            }
        })

    }
}