package com.messenger.main.service

import com.messenger.main.entity.ChatRoom
import com.messenger.main.entity.Message
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MessageService {
    @Headers("accept: application/json", "content-type: application/json")
    @POST("/api/message/all")
    fun getAll(@Body body: Map<String, String>): Observable<MutableList<Message>>

    @Headers("accept: application/json", "content-type: application/json")
    @POST("/api/message/send")
    fun sendMessage(@Body body: Map<String, String>): Observable<Message>
}