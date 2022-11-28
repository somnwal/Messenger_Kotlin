package com.messenger.main.entity

import com.google.gson.annotations.SerializedName

data class ChatRoom(
    @SerializedName("from_user")
    var fromUser: String,

    @SerializedName("to_user")
    var toUser: String,

    @SerializedName("from_user_name")
    var fromUserName: String,

    @SerializedName("to_user_name")
    var toUserName: String,

    @SerializedName("date")
    var date: String,

    @SerializedName("last_message")
    var lastMessage: String
)