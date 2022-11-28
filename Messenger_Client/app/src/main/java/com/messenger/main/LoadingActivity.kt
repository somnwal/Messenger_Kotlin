package com.messenger.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.messenger.main.pref.PreferenceApplication

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        //Log.d("push", "token : ${PreferenceApplication.prefs.token}")

        val initThread: Thread = object : Thread() {
            override fun run() {
                try {
                    super.run()

                    val user = PreferenceApplication.prefs.user

                    //Log.d("test", "[$user]" ?: "0")

                    sleep(1000)

                    if (user.isEmpty()) {
                        val i = Intent(
                            this@LoadingActivity,
                            LoginActivity::class.java
                        )
                        startActivity(i)
                        finish()
                    } else {
                        val i = Intent(
                            this@LoadingActivity,
                            ChatRoomActivity::class.java

                        )
                        startActivity(i)
                        finish()
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        initThread.start()
    }
}