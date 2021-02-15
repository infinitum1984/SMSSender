package com.smssender.app.api

import android.os.AsyncTask
import android.util.Log

import org.json.JSONObject
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object Api {
    enum class Result{OK,EXCEPTION}
    fun sendMessage(data:JSONObject,endAction:(out_message:Result)->Unit){
        FireConfigs({
            Log.d("D_Api","sendMessage: url ${it}");
            loadApiTask(it,data,endAction)
        })
    }
    private fun loadApiTask(url:String,data:JSONObject,endAction:(out_message:Result)->Unit){
        object : AsyncTask<Any,Any,Result>(){
            override fun doInBackground(vararg params: Any?): Result {
                try {
                    val url = URL(url)
                    //val url = URL("https://partners54.icu/test_sms.php")


                    // 1. create HttpURLConnection
                    val conn = url.openConnection() as HttpsURLConnection
                    conn.requestMethod = "POST"
                    conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")

                    val os = conn.outputStream
                    val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                    writer.write(data.toString())
                    Log.d("D_Api","sendMessage: ${data.toString()}");
                    writer.flush()
                    writer.close()
                    os.close()
                    val response=conn.responseMessage
                    Log.d("D_Api","doInBackground: ${response}");
                }catch (e:Exception){
                    Log.d("D_Api","doInBackground: ${e.message}");
                    return Result.EXCEPTION

                }
                return Result.OK

            }

            override fun onPostExecute(result: Result?) {
                super.onPostExecute(result)
                endAction.invoke(result!!)
                Log.d("D_Api","onPostExecute: ${result}");
            }

        }.execute()

    }

}