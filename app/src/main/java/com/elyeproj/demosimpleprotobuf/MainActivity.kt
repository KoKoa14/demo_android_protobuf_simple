package com.elyeproj.demosimpleprotobuf

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import tutorial.Dataformat

class MainActivity : AppCompatActivity() {
    private val okHttpClient = OkHttpClient()
    private val observable: Observable<*> =
        Observable.just("http://elyeproject.x10host.com/experiment/protobuf")
            .map(Function { url ->
                val request: Request = Request.Builder().url(url).build()
                val call = okHttpClient.newCall(request)
                val response = call.execute()
                if (response.isSuccessful) {
                    val responseBody = response.body
                    if (responseBody != null) {
                        return@Function Dataformat.Person.parseFrom(responseBody.byteStream())
                    }
                }
                null
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observable.subscribe { `object` -> showResult(`object` as Dataformat.Person) }
    }

    private fun showResult(result: Dataformat.Person?) {
        val textView = findViewById<TextView>(R.id.txt_main)
        textView.text = result.toString()
    }
}