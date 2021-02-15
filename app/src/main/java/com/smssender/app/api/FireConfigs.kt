package com.smssender.app.api

import android.content.Context
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class FireConfigs(
    onCompleteAction:(url:String)->Unit
) {
    init {
        var fireUrl=""

        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        val configSettings = FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(2)
                        .build();

        mFirebaseRemoteConfig.fetch(0)

        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val updated = it.result
                    Log.d("D_FireConfigs", "Config params updated: $updated")
                    fireUrl = mFirebaseRemoteConfig.getString("URL")

                    onCompleteAction.invoke(fireUrl)
                } else {
                    onCompleteAction.invoke(fireUrl)

                    Log.d("D_FireConfigs", "Config params updated: error")

                }

            }

    }
}
