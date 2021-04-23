package com.example.githubrepoapp

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GithubUserApi {

    @GET("/search/users")
    fun getGithubRepositories(@Query("q") query: String): Single<GithubUsers>

    @GET("/users")
    fun getUserRepoCount(@Query("username") username: String): Single<RepoCount>

    companion object {

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        private val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader("Authorization", "token $token").build()
                it.proceed(request)
            }.build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

        fun provideGithubService() = retrofit.create(GithubUserApi::class.java)
    }
}