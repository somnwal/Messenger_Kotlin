package com.messenger.main.entity

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    var id: String,

    @SerializedName("password")
    var password: String,

    @SerializedName("name")
    var name: String
)