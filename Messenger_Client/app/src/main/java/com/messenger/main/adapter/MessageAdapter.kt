package com.messenger.main.adapter

import android.content.Context
import android.opengl.Visibility
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.messenger.main.databinding.RecyclerMessageBinding
import com.messenger.main.entity.ChatRoom
import com.messenger.main.entity.Message
import com.messenger.main.pref.PreferenceApplication
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class MessageViewHolder(val binding: RecyclerMessageBinding) : RecyclerView.ViewHolder(binding.root)

// Adapter 구성
class MessageAdapter(
    val datas: MutableList<Message>,
    val context: Context,
    val toUserName: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 각 항목의 뷰 홀더를 준비해서 리턴, 자동으로 재사용.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            RecyclerMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    // 각각의 항목 구성
    // 첫번째 매개변수가 onCreateViewHolder 에서 리턴 시킨 객체다.
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MessageViewHolder).binding

        val fromUser = datas[position].fromUser

        val tmp = datas[position].date.split('.')
        val parsedDate = LocalDateTime.parse(tmp[0])
            .atZone(ZoneOffset.UTC)
            .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
            .toLocalDateTime()

        val date = parsedDate.toString()

        // 내 메시지 일때
        if (fromUser == PreferenceApplication.prefs.user) {
            binding.textFromUserContent.text = datas[position].msg

            binding.textFromUserDate.text = date.replace("T", " ")

            binding.textToUserName.visibility = View.GONE
            binding.textToUserContent.visibility = View.GONE
            binding.textToUserDate.visibility = View.GONE
            binding.toUserContentView.visibility = View.GONE

            binding.textFromUserContent.visibility = View.VISIBLE
            binding.textFromUserDate.visibility = View.VISIBLE
            binding.textFromUserName.visibility = View.VISIBLE
            binding.fromUserContentView.visibility = View.VISIBLE
        } else {
            binding.textToUserName.text = "$toUserName (${datas[position].fromUser})"
            binding.textToUserContent.text = datas[position].msg
            binding.textToUserDate.text = date.replace("T", " ")

            binding.textFromUserContent.visibility = View.GONE
            binding.textFromUserDate.visibility = View.GONE
            binding.textFromUserName.visibility = View.GONE
            binding.fromUserContentView.visibility = View.GONE

            binding.textToUserName.visibility = View.VISIBLE
            binding.textToUserContent.visibility = View.VISIBLE
            binding.textToUserDate.visibility = View.VISIBLE
            binding.toUserContentView.visibility = View.VISIBLE

        }

    }

    override fun getItemCount(): Int {
        return datas.size
    }
}