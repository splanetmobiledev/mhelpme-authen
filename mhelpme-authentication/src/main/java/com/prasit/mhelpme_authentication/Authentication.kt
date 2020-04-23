package com.prasit.mhelpme_authentication

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

class Authentication : AppCompatActivity() {

    companion object {
        var OnAuthenListenerGobal: ((Authen) -> Unit)? = null
    }

    fun requestLogin(activity: Activity, OnAuthenListener: ((Authen) -> Unit)) {
        activity.let {
            OnAuthenListenerGobal = OnAuthenListener
            var packageName = "com.prasit.m_helpme"
            if (activity.getPackageManager().getLaunchIntentForPackage(packageName) != null) {
                try {
                    var url = "mhelpme://request_login"
                    var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    activity.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse("market://details?id=" + packageName))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    activity.startActivity(intent)
                }
            } else {
                var intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse("market://details?id=" + packageName))
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                activity.startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val uri = intent.data
            uri!!.let {
                val profileId = uri.getQueryParameter("profileId")
                val phone = uri.getQueryParameter("phone")
                val entityProfileId = uri.getQueryParameter("entityProfileId")

                OnAuthenListenerGobal.let {
                    OnAuthenListenerGobal!!.invoke(Authen(profileId!!, phone!!, entityProfileId!!))
                }
                finish()
            }
        }
    }
}
