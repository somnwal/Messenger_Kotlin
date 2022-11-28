package com.messenger.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.messenger.main.adapter.MessageAdapter
import com.messenger.main.databinding.ActivityMessageBinding
import com.messenger.main.entity.Message
import com.messenger.main.pref.PreferenceApplication
import com.messenger.main.retrofit.RetrofitInstance
import com.messenger.main.service.MessageService
import com.messenger.main.service.UserService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MessageActivtiy : AppCompatActivity() {

    private var disposable: Disposable? = null
    private lateinit var binding: ActivityMessageBinding
    private var userService = RetrofitInstance.retrofit.create(UserService::class.java)
    private var messageService = RetrofitInstance.retrofit.create(MessageService::class.java)

    private lateinit var toUser: String
    private lateinit var toUserName: String

    private var messageList = mutableListOf<Message>()
    private var lastDate = ""

    private lateinit var messageCoroutine: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toUser = intent.getStringExtra("to_user").toString()
        toUserName = intent.getStringExtra("to_user_name").toString()

        title = "$toUserName 님과의 대화"

        // adapter 설정
        val layoutManager = LinearLayoutManager(this@MessageActivtiy)
        binding.messageView.layoutManager = layoutManager
        binding.messageView.adapter =
            MessageAdapter(messageList, this@MessageActivtiy, toUserName)

        // 초기 메시지 불러오기
        getMessages(lastDate)

        // 전송 버튼 눌렀을 때
        binding.buttonSend.setOnClickListener {
            if (binding.editTextMessage.text.isNotEmpty()) {
                sendMessage()
                binding.editTextMessage.text.clear()
            }
        }

        // 0.5초마다 메시지 불러오기
        messageCoroutine = CoroutineScope(Dispatchers.Default + Job()).launch {
            while (true) {
                delay(500)
                Log.d("message", "lastDate: $lastDate")
                getMessages(lastDate)
            }
        }

    }

    override fun onDestroy() {
        messageCoroutine.cancel()

        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMessages(date: String) {

        var map: Map<String, String> = if (date.isEmpty()) {
            mapOf(
                "from_user" to PreferenceApplication.prefs.user,
                "to_user" to toUser
            )
        } else {
            mapOf(
                "from_user" to PreferenceApplication.prefs.user,
                "to_user" to toUser,
                "date" to date
            )
        }

        disposable = messageService.getAll(map)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.isNotEmpty()) {
                    addToMessageList(it)
                    binding.messageView.scrollToPosition(messageList.size - 1)
                }
            }, {
                Log.e("error", "${it.message}")
            }, {

            })
    }

    private fun sendMessage() {
        val map = mapOf(
            "from_user" to PreferenceApplication.prefs.user,
            "to_user" to toUser,
            "msg" to binding.editTextMessage.text.toString()
        )

        disposable = messageService.sendMessage(map)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
            }, {
                Log.e("error", "${it.message}")
            }, {

            })
    }

    private fun addToMessageList(newList: MutableList<Message>) {
        newList.forEach {
            messageList.add(it)
        }

        lastDate = messageList.last().date
        binding.messageView.adapter?.notifyItemChanged(0)
    }
}