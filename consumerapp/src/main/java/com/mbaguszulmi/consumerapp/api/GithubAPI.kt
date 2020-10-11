package com.mbaguszulmi.consumerapp.api

import com.mbaguszulmi.consumerapp.model.GithubUser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class GithubAPI {
    fun getInterceptor() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "token d3a86f3535707d842dc5c3a4586b1fccf51e1bca")
                .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService() = getRetrofit().create(Service::class.java)

    interface Service {
        @GET("users/{username}")
        fun getUser(@Path("username") username: String): Call<GithubUser>

        @GET("users/{username}/followers")
        fun getFollowers(@Path("username") username: String): Call<List<GithubUser>>

        @GET("users/{username}/following")
        fun getFollowing(@Path("username") username: String): Call<List<GithubUser>>
    }
}