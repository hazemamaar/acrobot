package com.example.acrobot.data.remote

import com.example.acrobot.data.models.FormResponse
import com.example.acrobot.data.models.Profile
import com.example.acrobot.data.models.RegisterModel
import com.example.acrobot.data.models.RegistrationResponse
import com.example.acrobot.data.models.TokenModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IApiService {
    @POST("register")
    suspend fun registration(@Body registerModel:RegisterModel): Response<RegistrationResponse>

    @POST("login")
    suspend fun login(@Body loginModel: RegisterModel): Response<TokenModel>

    @POST("submit-form")
    suspend fun form(@Body request:HashMap<String,String>):Response<FormResponse>

    @PUT("profile/{userId}")
    suspend fun changeProfile(@Path("userId") userId:String,@Body registerModel:RegisterModel):Response<RegistrationResponse>

    @GET("profile/{userId}")
    suspend fun getProfile(@Path("userId") userId:String):Response<Profile>
}