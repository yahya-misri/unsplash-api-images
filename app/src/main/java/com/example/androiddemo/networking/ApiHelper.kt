package com.example.androiddemo.networking

import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import com.example.androiddemo.model.ResponseMain
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


class ApiHelper {
    private val TAG = ApiHelper::class.java.simpleName

    suspend fun <T : Any> handleApi(
        page: Int, clientId: String,
    ): NetworkResult<List<ResponseMain>> {
        return try {
            if (Build.VERSION.SDK_INT > 9) {
                val policy = ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
            }

            val url = URL("https://api.unsplash.com/photos?page=$page&client_id=$clientId")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = false
            Log.e(TAG, "Request :${url.toString()}")

            // Check if the connection is successful
            val responseCode = httpURLConnection.responseCode
            val response = httpURLConnection.inputStream.bufferedReader()
                .use { it.readText() }
            //Log.e(TAG, "Response :$responseCode :: $response")
            when (responseCode) {
                200 -> {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(JsonParser.parseString(response))
                    val objectList =
                        gson.fromJson(prettyJson, Array<ResponseMain>::class.java).asList()

                    // val output=fromJson<List<ResponseMain>>(prettyJson)
                    val output = prettyJson.toKotlinObject<List<ResponseMain>>()
                    NetworkResult.Success(objectList)
                }

                else -> {
                    NetworkResult.Error(code = responseCode, message = response)
                }
            }


        } catch (e: Exception) {
            Log.e(TAG, "Exception :${e.printStackTrace()} ")
            NetworkResult.Error(code = 101, message = e.message)
        } catch (e: Throwable) {
            Log.e(TAG, "Throwable :${e.message} ")
            NetworkResult.Exception(e)
        }
    }

    inline fun <reified T : Any> String.toKotlinObject(): T =
        Gson().fromJson(this, T::class.java)


}