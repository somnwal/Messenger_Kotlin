package com.messenger.main.entity

import com.google.gson.annotations.SerializedName

data class Response (
    @SerializedName("errors")
    val errors: MutableList<Error>
)