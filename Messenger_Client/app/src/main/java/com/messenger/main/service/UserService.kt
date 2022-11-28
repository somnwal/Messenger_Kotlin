package com.messenger.main.service

import com.messenger.main.entity.Response
import com.messenger.main.entity.User
import io.reactivex.Observable
import retrofit2.http.*

interface UserService {
    @Headers("accept: application/json", "content-type: application/json")
    @POST("/api/users")
    fun register(@Body body: Map<String, String>): Observable<Response>

    @Headers("accept: application/json", "content-type: application/json")
    @POST("/api/users/token")
    fun updateToken(@Body body: Map<String, String>): Observable<Unit>

    @Headers("accept: application/json", "content-type: application/json")
    @GET("/api/users/{id}")
    fun getUser(@Path("id") id: String): Observable<User>
}