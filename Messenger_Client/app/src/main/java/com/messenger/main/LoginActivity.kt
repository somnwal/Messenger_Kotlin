package com.messenger.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.messenger.main.databinding.ActivityLoginBinding
import com.messenger.main.pref.PreferenceApplication
import com.messenger.main.retrofit.RetrofitInstance
import com.messenger.main.service.AuthService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginActivity : AppCompatActivity() {
    private var disposable: Disposable? = null
    private val authService = RetrofitInstance.retrofit.create(AuthService::class.java)
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val map = mapOf(
                "id" to binding.editTextID.text.toString(),
                "password" to binding.editTextPassword.text.toString(),
                "token" to PreferenceApplication.prefs.token
            )

            login(map)
        }

        binding.buttonRegister.setOnClickListener {
            val i = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(i)
        }
    }

    override fun onDestroy() {
        disposable?.let { disposable!!.dispose() }
        super.onDestroy()
    }

    private fun login(map: Map<String, String>) {
        // 로그인 실행
        Log.d("test", map.toString())

        disposable = authService.login(map)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                PreferenceApplication.prefs.user = it.id
                PreferenceApplication.prefs.userName = it.name

                val i = Intent(
                    this@LoginActivity,
                    ChatRoomActivity::class.java
                )

                startActivity(i)
                finish()
            }, {
                AlertDialog
                    .Builder(this@LoginActivity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("알림")
                    .setMessage("아이디 혹은 비밀번호가 일치하지 않습니다.")
                    .setPositiveButton("확인", null)
                    .show()
            }, {

            })
    }
}