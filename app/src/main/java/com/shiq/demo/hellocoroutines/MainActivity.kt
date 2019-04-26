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
            GlobalScope.launch(Dispatchers.Main) {
                Log.d("test", "onClick")
                Log.d("test", "runBlocking current thread: ${Thread.currentThread().name}")
                val result = HttpService.getSync("https://raw.github.com/square/okhttp/master/README.md")
                if (result.isSuccessful()) {
                    Log.d("test", "getSync isSuccessful")
                    button_view.text = "success"
                } else {
                    Log.d("test", "getSync failure")
                    button_view.text = "failure"
                }
            }
        }
    }

}
