package com.example.famchat.api

import com.example.famchat.BuildConfig
import com.example.famchat.utils.LogUtil
import com.example.famchat.viewmodel.GlobalValue
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    val apiService: ApiService =
        getRetrofit(okHttpClientWithTimeOut(6)).create(ApiService::class.java)

    val apiServiceWait: ApiService =
        getRetrofit(okHttpClientWithTimeOut(10)).create(ApiService::class.java)

    private fun getRetrofit(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .registerTypeAdapter(
                Long::class.java,
                LongTypeAdapter()
            )
            .registerTypeAdapter(
                Int::class.java,
                IntegerTypeAdapter()
            )
            .registerTypeAdapter(
                Double::class.java,
                DoubleTypeAdapter()
            )
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    fun apiService(baseUrl: String): ApiService {
        return getRetrofit(baseUrl, okHttpClientWithTimeOut(6)).create(ApiService::class.java)
    }

    private fun getRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .registerTypeAdapter(
                Long::class.java,
                LongTypeAdapter()
            )
            .registerTypeAdapter(
                Int::class.java,
                IntegerTypeAdapter()
            )
            .registerTypeAdapter(
                Double::class.java,
                DoubleTypeAdapter()
            )
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    private fun okHttpClientWithTimeOut(timeout: Int): OkHttpClient {
        val builder = OkHttpClient.Builder()
        try {
            builder.hostnameVerifier { _, _ ->
                true
            }
                .addNetworkInterceptor(Interceptor { chain ->
                    val originalRequest = chain.request()
                    val newRequest = originalRequest.newBuilder().apply {
                        header("Accept-Language", GlobalValue.currentLanguage)
                        header(
                            "Authorization",
                            "Bearer ${GlobalValue.userData.accessTokenMySign}"
                        )
                        header("os", "android")
                        header("version", BuildConfig.VERSION_NAME)
                        header("identity", GlobalValue.userData.deviceIdMySign)
                    }
                    LogUtil.logI(
                        String.format("--> %s %s", originalRequest.method, originalRequest.url),
                        "okhttp.OkHttpClient"
                    )

                    chain.proceed(newRequest.build())
                })
                .readTimeout(timeout.toLong(), TimeUnit.MINUTES)
                .connectTimeout(timeout.toLong(), TimeUnit.MINUTES)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return builder.build()
    }

    class LongTypeAdapter : TypeAdapter<Long?>() {
        @Throws(IOException::class)
        override fun read(reader: JsonReader): Long? {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return null
            }
            val stringValue = reader.nextString()
            return try {
                java.lang.Long.valueOf(stringValue)
            } catch (e: NumberFormatException) {
                null
            }
        }

        @Throws(IOException::class)
        override fun write(writer: JsonWriter, value: Long?) {
            if (value == null) {
                writer.nullValue()
                return
            }
            writer.value(value)
        }
    }

    class IntegerTypeAdapter : TypeAdapter<Int?>() {
        @Throws(IOException::class)
        override fun read(reader: JsonReader): Int? {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return null
            }
            val stringValue = reader.nextString()
            return try {
                Integer.valueOf(stringValue)
            } catch (e: NumberFormatException) {
                null
            }
        }

        @Throws(IOException::class)
        override fun write(writer: JsonWriter, value: Int?) {
            if (value == null) {
                writer.nullValue()
                return
            }
            writer.value(value)
        }
    }

    class DoubleTypeAdapter : TypeAdapter<Double?>() {
        @Throws(IOException::class)
        override fun read(reader: JsonReader): Double? {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return null
            }
            val stringValue = reader.nextString()
            return try {
                java.lang.Double.valueOf(stringValue)
            } catch (e: NumberFormatException) {
                null
            }
        }

        @Throws(IOException::class)
        override fun write(writer: JsonWriter, value: Double?) {
            if (value == null) {
                writer.nullValue()
                return
            }
            writer.value(value)
        }
    }
}