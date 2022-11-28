package com.messenger.main.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.messenger.main.MessageActivtiy
import com.messenger.main.databinding.RecyclerChatroomBinding
import com.messenger.main.entity.ChatRoom
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class ChatRoomViewHolder(val binding: RecyclerChatroomBinding) : RecyclerView.ViewHolder(binding.root)

// Adapter 구성
class ChatRoomAdapter(val datas: MutableList<ChatRoom>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 각 항목의 뷰 홀더를 준비해서 리턴, 자동으로 재사용.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatRoomViewHolder(
            RecyclerChatroomBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    // 각각의 항목 구성
    // 첫번째 매개변수가 onCreateViewHolder 에서 리턴 시킨 객체다.
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as ChatRoomViewHolder).binding

        val tmp = datas[position].date.split('.')
        val parsedDate = LocalDateTime.parse(tmp[0])
            .atZone(ZoneOffset.UTC)
            .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
            .toLocalDateTime()

        val date = parsedDate.toString()

        binding.textName.text = "${datas[position].toUserName} 님과의 대화"
        binding.textContent.text = datas[position].lastMessage
        binding.textDate.text = date.replace("T", " ")

        binding.root.setOnClickListener {
            val i = Intent(context, MessageActivtiy::class.java)
            i.putExtra("to_user", datas[position].toUser)
            i.putExtra("to_user_name", datas[position].toUserName)

            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}