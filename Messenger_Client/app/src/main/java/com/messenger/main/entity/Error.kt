package com.messenger.main.entity

import com.google.gson.annotations.SerializedName

data class Error (
    @SerializedName("msg")
    val msg: String
)