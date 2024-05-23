package com.smart.house.frontend.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class AuthRequest(val username: String, val password: String, val email: String)
data class LoginResponse(val token: String)

data class RegisterResponse(val success: Boolean)

interface ApiService {
    @POST("/login")
    fun login(@Body request: AuthRequest): Call<LoginResponse>

    @POST("/register")
    fun register(@Body request: AuthRequest): Call<RegisterResponse>
}
