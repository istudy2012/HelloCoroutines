package com.shiq.demo.hellocoroutines.http

import okhttp3.*
import java.io.IOException

object HttpService {

    private val httpClient = OkHttpClient()

    fun get(url: String, callback: HttpCallback?) {
        val request = Request.Builder()
            .get()
            .url(url)
            .build()

        httpClient.newCall(request).enqueue(OkHttpCallbackAdapter(callback))
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