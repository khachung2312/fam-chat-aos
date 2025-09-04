package com.example.famchat.api

import com.example.famchat.model.reponse.BaseResponse
import com.example.famchat.model.reponse.CheckSignatureStatusResponse
import com.example.famchat.model.reponse.LoginResponse
import com.example.famchat.model.reponse.RegisterAccountResponse
import com.example.famchat.model.reponse.UserResponse
import com.example.famchat.model.request.ChangUserInfoRequest
import com.example.famchat.model.request.ChatRequest
import com.example.famchat.model.request.CheckSignatureStatusRequest
import com.example.famchat.model.request.ForgotPasswordRequest
import com.example.famchat.model.request.LoginRequest
import com.example.famchat.model.request.RegisterAccountRequest
import com.example.famchat.model.request.ResendOTPRequest
import com.example.famchat.model.request.VerifyOTPRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("/api/agents/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<BaseResponse<LoginResponse>>

    @POST("/api/agents/chat")
    suspend fun sendChatMessage(@Body chatRequest: ChatRequest): Response<BaseResponse<LoginResponse>>

    @POST("/api/agents/chat")
    suspend fun getSignatureStatus(@Body loginRequest: CheckSignatureStatusRequest): Response<BaseResponse<CheckSignatureStatusResponse>>

    @GET("/api/agents/chat")
    suspend fun userProfile(): Response<BaseResponse<UserResponse>>

    @POST("/api/auth/register")
    suspend fun registerAccount(@Body request: RegisterAccountRequest): Response<BaseResponse<RegisterAccountResponse>>

    @POST("/api/auth/resend-otp")
    suspend fun resendOTP(@Body request: ResendOTPRequest): Response<BaseResponse<LoginResponse>>

    @POST("/api/auth/verify-otp")
    suspend fun verifyOTP(@Body request: VerifyOTPRequest): Response<BaseResponse<LoginResponse>>

    @POST("/api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<BaseResponse<LoginResponse>>


}