package com.example.famchat.api

import android.text.TextUtils
import com.example.famchat.BuildConfig
import com.example.famchat.api.exception.BaseNetworkException
import com.example.famchat.config.Constants
import com.example.famchat.model.reponse.BaseResponse
import com.example.famchat.model.request.ChangUserInfoRequest
import com.example.famchat.model.request.CheckSignatureStatusRequest
import com.example.famchat.model.request.ForgotPasswordRequest
import com.example.famchat.model.request.LoginRequest
import com.example.famchat.model.request.RegisterAccountRequest
import com.example.famchat.model.request.ResendOTPRequest
import com.example.famchat.model.request.VerifyOTPRequest
import com.example.famchat.utils.LogUtil
import com.example.famchat.utils.PreferencesUtils
import com.example.famchat.viewmodel.GlobalValue
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import okhttp3.Call
import okhttp3.Callback
import okio.Buffer
import okio.BufferedSource
import java.io.IOException


class ApiHelper {

    private val apiService: ApiService = RetrofitBuilder.apiService

    /**
     * Base handler response from api
     * Required all request api must use to handler token Invalid/expired
     * */
    private suspend inline fun <reified T> safeCallApi(
        call: suspend () -> Response<T>
    ): Resource<T> {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (exception: Exception) {
            return Resource.Error(
                BaseNetworkException(
                    responseMessage = exception.message,
                    exceptionName = exception.javaClass.name
                )
            )
        }
        LogUtil.logI(
            String.format(
                "<-- %s %s", response.raw().request.method,
                response.raw().request.url
            ),
            "okhttp.OkHttpClient"
        )
        response.raw().request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            val bodyStr = buffer.readUtf8()
            LogUtil.logI("Body: $bodyStr", "okhttp.OkHttpClient")
        }

        if (response.isSuccessful) {
            response.body()?.let {
                LogUtil.logI(Gson().toJson(it).toString(), "okhttp.OkHttpClient")
                return Resource.Success(it)
            } ?: run {
                /**
                 * Error response no body
                 * */
                return Resource.Error(
                    BaseNetworkException(
                        responseMessage = "Response without body",
                        responseCode = 200
                    )
                )
            }
        } else {
            /**
             * Error response don't success
             * */
            val baseResponseEncrypt = try {
                Gson().fromJson(response.errorBody()?.string(), BaseResponse::class.java)
            } catch (ex: Exception) {
                null
            }
            LogUtil.logI(
                if (TextUtils.isEmpty(response.errorBody()?.string())) {
                    response.toString()
                } else {
                    response.errorBody()?.string()
                },
                "okhttp.OkHttpClient"
            )
            return Resource.Error(
                BaseNetworkException(
                    if (!TextUtils.isEmpty(baseResponseEncrypt?.code)) {
                        baseResponseEncrypt?.code
                    } else {
                        baseResponseEncrypt?.message ?: ""
                    },
                    response.code()
                )
            )
        }
    }
    fun sendChatMessage(
        jsonBody: String,
        onEachChunk: (String) -> Unit,
        onSuccess: (() -> Unit)? = null,
        onError: (Throwable) -> Unit
    ) {
        val client = OkHttpClient()

        val requestBody = jsonBody.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(BuildConfig.BASE_SERVER_URL + "/api/agents/chat")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                try {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code ${response.code}")
                    }

                    val source: BufferedSource = response.body?.source()
                        ?: throw IOException("Empty body")


                    val fullContent = StringBuilder()

                    while (!source.exhausted()) {

                        val chunk = source.readUtf8Line()
                        if (!chunk.isNullOrBlank()) {
                            fullContent.append(chunk)
                            LogUtil.logD(chunk, "Text receiver")
                            onEachChunk(chunk)
                        }
                    }




//                    val jsonList = extractAllJsonObjects(fullContent.toString())
//
//                    jsonList.forEach { json ->
//                        try {
//                            val gson = Gson()
//                            val jobResponse = gson.fromJson(json, JobResponse::class.java)
//                            LogUtil.logD(jobResponse.toString(), "Parser thanh công.")
//                        } catch (e: Exception) {
//                            LogUtil.logD("ParseError", "Không tìm thấy JSON trong đoạn text.")
//                        }
//                    }


                    onSuccess?.invoke()

                } catch (e: Exception) {
                    onError(e)
                }
            }
        })
    }

    fun extractJsonFromText(input: String): String? {
        val startIndex = input.indexOf("{\"form\"")
        if (startIndex == -1) return null

        var openBraces = 0
        for (i in startIndex until input.length) {
            when (input[i]) {
                '{' -> openBraces++
                '}' -> openBraces--
            }
            if (openBraces == 0) {
                return input.substring(startIndex, i + 1)
            }
        }
        return null
    }

    fun extractAllJsonObjects(text: String): List<String> {
        val results = mutableListOf<String>()
        var i = 0

        while (i < text.length) {
            if (text[i] == '{') {
                var open = 1
                var j = i + 1
                var insideString = false
                var escape = false

                while (j < text.length && open > 0) {
                    val c = text[j]

                    if (escape) {
                        escape = false
                    } else {
                        when (c) {
                            '\\' -> escape = true
                            '"' -> insideString = !insideString
                            '{' -> if (!insideString) open++
                            '}' -> if (!insideString) open--
                        }
                    }

                    j++
                }

                if (open == 0) {
                    results.add(text.substring(i, j))
                    i = j
                } else {
                    // unmatched bracket
                    break
                }
            } else {
                i++
            }
        }

        return results
    }




    /**
     * check signature status
     * */
    suspend fun getSignatureStatus(transactionId: String?) =
        safeCallApi {
            apiService.getSignatureStatus(
                CheckSignatureStatusRequest(transactionId)
            )
        }

    /**
     * Call login
     * */
    suspend fun login(email: String, password: String?, signInType: String? = "") = safeCallApi {
        val loginRequest = LoginRequest(
            email = email,
            password = password,
            signInType = signInType
        )
        apiService.login(loginRequest)
    }
    /**
     * Call register account
     * */
    suspend fun registerAccount(
        email: String,
        phoneNumber: String? = "",
        phoneCountryCode: String? = "",
        avatar: String? = "",
        password: String? = "",
        fullName: String? = "",
        googleAccount: String? = "",
        language: String? = "",
    ) = safeCallApi {
        val request = RegisterAccountRequest(
            email = email,
            phoneNumber = phoneNumber,
            phoneCountryCode = phoneCountryCode,
            avatar = avatar,
            password = password,
            fullName = fullName,
            googleAccount = googleAccount,
            language = language,
        )
        apiService.registerAccount(request)
    }

    /**
     * Call resend otp
     * */
    suspend fun resendOTP(email: String, phoneNumber: String?, forceResend: Boolean? = false) =
        safeCallApi {
            val request = ResendOTPRequest(
                email = email,
                phoneNumber = phoneNumber,
                forceResend = forceResend
            )
            apiService.resendOTP(request)
        }

    /**
     * Call verify otp
     * */
    suspend fun verifyOTP(email: String, phoneNumber: String?, otp: String) = safeCallApi {
        val request = VerifyOTPRequest(
            email = email,
            phoneNumber = phoneNumber,
            otp = otp
        )
        apiService.verifyOTP(request)
    }


    /**
     * Call forgot password
     * */
    suspend fun forgotPassword(email: String, password: String?, otp: String) = safeCallApi {
        val request = ForgotPasswordRequest(
            email = email,
            password = password,
            otp = otp
        )
        apiService.forgotPassword(request)
    }




    /**
     * get user information
     * */
    suspend fun userProfile() = safeCallApi {
        apiService.userProfile()
    }


}
