package com.messenger.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.messenger.main.adapter.ChatRoomAdapter
import com.messenger.main.databinding.ActivityMainBinding
import com.messenger.main.entity.ChatRoom
import com.messenger.main.entity.User
import com.messenger.main.pref.PreferenceApplication
import com.messenger.main.retrofit.RetrofitInstance
import com.messenger.main.service.ChatRoomService
import com.messenger.main.service.UserService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*


class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val chatRoomService = RetrofitInstance.retrofit.create(ChatRoomService::class.java)
    private val userService = RetrofitInstance.retrofit.create(UserService::class.java)
    var disposable: Disposable? = null
    var userDisposable: Disposable? = null

    private var chatRoomList = mutableListOf<ChatRoom>()

    private lateinit var chatRoomCoroutine: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTextInfo.text =
            "${PreferenceApplication.prefs.userName}(${PreferenceApplication.prefs.user}) 님 환영합니다."

        val layoutManager = LinearLayoutManager(this@ChatRoomActivity)
        binding.chatRoomView.layoutManager = layoutManager
        binding.chatRoomView.adapter = ChatRoomAdapter(chatRoomList, this@ChatRoomActivity)

        getChatRoomList(PreferenceApplication.prefs.user)

        // 새 대화 버튼 이벤트
        binding.buttonNewChat.setOnClickListener {

            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.dialog_new_chat, null)

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("대화할 사용자의 ID를 입력해주세요")
                .setPositiveButton("확인") { _, _ ->

                    val textView: TextView = view.findViewById(R.id.editTextToUser)
                    val toUser = textView.text.toString()

                    if (toUser == PreferenceApplication.prefs.user) {
                        Toast.makeText(
                            this@ChatRoomActivity,
                            "자신과 대화할 수 없습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@setPositiveButton
                    }

                    // 인텐트로 넘기기

                    userDisposable = userService.getUser(toUser)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            val i = Intent(this@ChatRoomActivity, MessageActivtiy::class.java)
                            i.putExtra("to_user", it.id)
                            i.putExtra("to_user_name", it.name)

                            startActivity(i)
                        }, {
                            Toast.makeText(
                                this@ChatRoomActivity,
                                "해당하는 유저가 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }, {

                        })


                }
                .setNeutralButton("취소", null)
                .create()

            //  여백 눌러도 창 안없어지게
            alertDialog.setCancelable(false)

            alertDialog.setView(view)
            alertDialog.show()


        }

        chatRoomCoroutine = CoroutineScope(Dispatchers.Default + Job()).launch {
            while (true) {
                Log.d("chatroom", chatRoomList.toString())
                getChatRoomList(PreferenceApplication.prefs.user)
                delay(1000)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                PreferenceApplication.prefs.remove("user")

                val i = Intent(this@ChatRoomActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroy() {
        disposable?.let { disposable!!.dispose() }
        userDisposable?.let { userDisposable!!.dispose() }
        chatRoomCoroutine.cancel()

        super.onDestroy()
    }

    private fun getChatRoomList(fromUser: String) {
        val map = mapOf(
            "from_user" to fromUser
        )

        disposable = chatRoomService.getAll(map)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.isNotEmpty() && chatRoomList != it) {
                    updateChatRoom(it)
                }
            }, {
                Log.e("error_custom", it.message ?: "")
            }, {
            })
    }

    private fun updateChatRoom(list: MutableList<ChatRoom>) {
        if (chatRoomList.isEmpty()) {
            chatRoomList = list
            binding.chatRoomView.adapter = ChatRoomAdapter(chatRoomList, this@ChatRoomActivity)
        } else {
            chatRoomList.clear()
            chatRoomList.addAll(list)

            binding.chatRoomView.adapter?.notifyDataSetChanged()
        }
    }
}