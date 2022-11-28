package com.messenger.main.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.messenger.main.pref.PreferenceApplication
import com.messenger.main.retrofit.RetrofitInstance
import com.messenger.main.service.UserService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.create

class PushService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        Log.d("push", "new token : $p0")

        val user = PreferenceApplication.prefs.user

        if (user.isNotEmpty()) {
            updateToken(user, p0)
        }

        PreferenceApplication.prefs.token = p0
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("push", "message:${p0.data}")

        val manager =
            getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "messenger",
                "messenger",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "messenger"
            }

            manager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(this, "messenger")
        } else {
            builder = NotificationCompat.Builder(this)
        }

        builder.run {
            setSmallIcon(android.R.drawable.ic_notification_overlay)
            setWhen(System.currentTimeMillis())
            setContentTitle(p0.data["title"])
            setContentText(p0.data["value"])
        }

        manager.notify(11, builder.build())
    }

    private fun updateToken(id: String, token: String) {
        val userService = RetrofitInstance.retrofit.create(UserService::class.java)

        val map = mapOf(
            "id" to id,
            "token" to token
        )

        val disposable = userService.updateToken(map)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({

            }, {

            }, {

            })

        disposable?.let { disposable!!.dispose() }
    }
}