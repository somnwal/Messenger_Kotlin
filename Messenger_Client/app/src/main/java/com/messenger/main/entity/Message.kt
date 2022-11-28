package com.messenger.main.entity

import com.google.gson.annotations.SerializedName

data class Message (
    @SerializedName("from_user")
    var fromUser: String,

    @SerializedName("to_user")
    var toUser: String,

    @SerializedName("msg")
    var msg: String,

    @SerializedName("date")
    var date: String
)