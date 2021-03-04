package com.parth.clevertapuninstall

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.SyncListener
import com.google.firebase.analytics.FirebaseAnalytics
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {
    var cleverTapDefaultInstance: CleverTapAPI? = null
    var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val getCTIDbutton: Button
        val login: Button
        val name: EditText
        val email: EditText
        val phone: EditText
        val id: EditText

        setContentView(R.layout.activity_main)

        getCTIDbutton = findViewById(R.id.getctidbutton)

        val ctid: TextView = findViewById(R.id.ctidtext)

        login = findViewById(R.id.loginbutton)
        name = findViewById(R.id.uname)
        email = findViewById(R.id.emailtxt)
        phone = findViewById(R.id.phonetxt)
        id = findViewById(R.id.identity)

        //Clevertap
        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(applicationContext)
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG) //Set Log level to DEBUG log warnings or other important messages

        getCTIDbutton.setOnClickListener { ctid.text = cleverTapDefaultInstance?.cleverTapID }

        login?.setOnClickListener {
            val listener1: SyncListener = object : SyncListener {
                override fun profileDataUpdated(updates: JSONObject) {}
                override fun profileDidInitialize(CleverTapID: String) {
                    Log.e("BRANCH SDK", "I am in")
                    Log.e("ClevertapTest", "Clevertap id is $CleverTapID")
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
                    mFirebaseAnalytics!!.setUserProperty("ct_objectId", Objects.requireNonNull(CleverTapAPI.getDefaultInstance(applicationContext)).cleverTapID)
                }
            }

            CleverTapAPI.getDefaultInstance(applicationContext)?.syncListener = listener1

            val profileUpdate = HashMap<String, Any>()
            profileUpdate["Name"] = name.text.toString() // String
            profileUpdate["Identity"] = id.text.toString() // String or number
            profileUpdate["Email"] = email.text.toString() // Email address of the user
            profileUpdate["Phone"] = phone.text.toString() // Phone (with the country code, starting with +)
            cleverTapDefaultInstance?.onUserLogin(profileUpdate)
        }
    }
}


