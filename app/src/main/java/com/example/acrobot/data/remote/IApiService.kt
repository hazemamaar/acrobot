package com.example.acrobot.data.remote

import com.example.acrobot.data.models.RegisterModel
import com.example.acrobot.data.models.TokenModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IApiService {
    @POST("register")
    suspend fun registration(@Body registerModel:RegisterModel): Response<String>

    @POST("login")
    suspend fun login(@Body loginModel: RegisterModel): Response<TokenModel>
}