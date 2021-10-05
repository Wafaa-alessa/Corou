package com.example.corou


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var text:TextView
    private lateinit var buttonGet:Button
    val GetURL = "https://api.adviceslip.com/advice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text = findViewById(R.id.text)
        buttonGet = findViewById(R.id.buttonGet)
        buttonGet.setOnClickListener(){
            requestApi()
        }
    }
    private fun requestApi()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val data = async {
                fetchRandomAdvice()
            }.await()
            if (data.isNotEmpty())
            {
                AdviceText(data)
            }
        }
    }
    private fun fetchRandomAdvice():String{
        var response=""
        try {
            response =URL(GetURL).readText(Charsets.UTF_8)

        }catch (e:Exception)
        {
            println("Error $e")
        }
        return response
    }
    private suspend fun AdviceText(data:String)
    {
        withContext(Dispatchers.Main)
        {
            val jsonObject = JSONObject(data)
            val slip = jsonObject.getJSONObject("slip")
            val id = slip.getInt("id")
            val advice = slip.getString("advice")
            text.text = advice
        }
    }
}