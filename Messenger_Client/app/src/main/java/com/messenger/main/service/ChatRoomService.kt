package com.messenger.main.service

import com.messenger.main.entity.ChatRoom
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatRoomService {
    @Headers("accept: application/json", "content-type: application/json")
    @POST("/api/message/rooms")
    fun getAll(@Body body: Map<String, String>): Observable<MutableList<ChatRoom>>
}