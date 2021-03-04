package com.parth.clevertapuninstall

import android.app.Application
import android.util.Log
import com.clevertap.android.sdk.ActivityLifecycleCallback
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.SyncListener
import com.google.firebase.analytics.FirebaseAnalytics
import org.json.JSONObject
import java.util.*

class MyApplication : Application() {
    var listener: SyncListener? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate() {
        ActivityLifecycleCallback.register(this)
        super.onCreate()
        listener = object : SyncListener {
            override fun profileDataUpdated(updates: JSONObject) {}
            override fun profileDidInitialize(CleverTapID: String) {
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
                mFirebaseAnalytics!!.setUserProperty("ct_objectId", Objects.requireNonNull(CleverTapAPI.getDefaultInstance(applicationContext)).cleverTapID)
                Log.e("ClevertapTest", "Clevertap id is $CleverTapID")
            }
        }
        CleverTapAPI.getDefaultInstance(applicationContext)?.syncListener = listener
    }
}