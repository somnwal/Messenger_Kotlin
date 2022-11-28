package com.messenger.main.service

import com.messenger.main.entity.User
import io.reactivex.Observable
import retrofit2.http.*

interface AuthService {

    @Headers("accept: application/json", "content-type: application/json")
    @POST("/api/auth")
    fun login(@Body body: Map<String, String>): Observable<User>
}