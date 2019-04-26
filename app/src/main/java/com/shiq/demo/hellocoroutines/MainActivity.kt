package com.shiq.demo.hellocoroutines

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

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
        }
    }

}
