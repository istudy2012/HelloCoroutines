package com.shiq.demo.hellocoroutines

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.shiq.demo.hellocoroutines.http.HttpCallback
import com.shiq.demo.hellocoroutines.http.HttpService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        test()
    }

    private fun test() {
        button_view.setOnClickListener {
            runBlocking {
                GlobalScope.launch(Dispatchers.Main) {
                    Log.d("test", "onClick")
                    Log.d("test", "runBlocking current thread: ${Thread.currentThread().name}")
                    runBlocking {
                        button_view.text = "" + System.currentTimeMillis()
                        Log.d("test", "runBlocking2 current thread: ${Thread.currentThread().name}")
                    }
                }
            }

            HttpService.get("https://raw.github.com/square/okhttp/master/README.md",
                object : HttpCallback {
                    override fun onStart() {
                        Log.d("test", "onStart")
                    }

                    override fun onFinish() {
                        Log.d("test", "onFinish")
                    }

                    override fun onFailure(statusCode: Int, message: String?) {
                        Log.d("test", "onFailure: statusCode=$statusCode, message=$message")
                    }

                    override fun onSuccess(message: String?) {
                        Log.d("test", "onFailure: onSuccess=$message")
                    }
                })

        }
    }

}
