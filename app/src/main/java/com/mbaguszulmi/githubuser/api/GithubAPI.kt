package com.mbaguszulmi.githubuser.api

import com.mbaguszulmi.githubuser.model.GithubUser
import com.mbaguszulmi.githubuser.model.network.UserSearchResponse
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
                    .addHeader("Authorization", "token bc7c4badd465efa9d739e54040a11469226d525c")
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
        @GET("users")
        fun getUsers(): Call<List<GithubUser>>

        @GET("search/users")
        fun getUsers(@Query("q") query: String): Call<UserSearchResponse>

        @GET("users/{username}")
        fun getUser(@Path("username") username: String): Call<GithubUser>
    }
}