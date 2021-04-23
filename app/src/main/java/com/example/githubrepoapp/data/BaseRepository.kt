package com.example.githubrepoapp.data

import com.example.githubrepoapp.RateLimitException
import retrofit2.Response
import java.lang.RuntimeException

open class BaseRepository {

    fun <T> processResponse(response: Response<T>): T? {
        if(response.isSuccessful) {
            return response.body()
        } else {
            if(response.headers().get("X-RateLimit-Remaining")?.toInt() == 0) {
                //been rate limited
                throw RateLimitException()
            } else
                throw RuntimeException(response.message())
        }
    }
}