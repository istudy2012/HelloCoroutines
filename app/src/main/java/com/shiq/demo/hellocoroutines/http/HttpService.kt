package com.shiq.demo.hellocoroutines.http

import android.util.Log
import okhttp3.*
import java.io.IOException

object HttpService {

    private val MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8")  // 请求体是json

    private val httpClient = OkHttpClient()

    fun get(url: String, callback: HttpCallback?) {
        val request = Request.Builder()
            .get()
            .url(url)
            .build()

        httpClient.newCall(request).enqueue(OkHttpCallbackAdapter(callback))
    }

    suspend fun getSync(url: String): HttpResponse {
        val request = Request.Builder()
            .get()
            .url(url)
            .build()

        return httpClient.newCall(request).await()
    }

    suspend fun postSync(url: String, body: String): HttpResponse {
        val request = Request.Builder()
            .post(RequestBody.create(MEDIA_TYPE_JSON, body))
            .url(url)
            .build()

        return httpClient.newCall(request).await()
    }

}

suspend fun Call.await(): HttpResponse = kotlin.coroutines.suspendCoroutine {
    enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.d("test", "Call.await() onFailure current thread: ${Thread.currentThread().name}")

            it.resumeWith(Result.success(HttpResponse(LocalStatusCode.IO_EXCEPTION, e.message)))
        }

        override fun onResponse(call: Call, response: Response) {
            Log.d("test", "Call.await() onResponse current thread: ${Thread.currentThread().name}")

            it.resumeWith(Result.success(HttpResponse(response.code(), response.body()?.string())))
        }
    })
}

class HttpResponse constructor(private val statusCode: Int, private val response: String?) {

    fun isSuccessful(): Boolean {
        return statusCode in 200..299
    }

    fun getResponse(): String? {
        return response
    }

    fun getMessage(): String? {
        return response
    }

}

class OkHttpCallbackAdapter(private val httpCallback: HttpCallback?) : Callback {

    init {
        httpCallback?.onStart()
    }

    override fun onFailure(call: Call, e: IOException) {
        httpCallback?.let {
            it.onFinish()
            it.onFailure(LocalStatusCode.IO_EXCEPTION, e.message)
        }
    }

    override fun onResponse(call: Call, response: Response) {
        httpCallback?.let {
            it.onFinish()
            if (response.isSuccessful) {
                it.onSuccess(response.body()?.string())
            } else {
                it.onFailure(response.code(), response.body()?.string())
            }
        }
    }
}

interface HttpCallback {

    fun onStart()

    fun onFinish()

    fun onFailure(statusCode: Int, message: String?)

    fun onSuccess(message: String?)

}
