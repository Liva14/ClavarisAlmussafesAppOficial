package com.example.clavarisalmussafesapp

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import okhttp3.OkHttpClient
import okhttp3.Request

class UpdateWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val client = OkHttpClient()

    override suspend fun doWork(): Result {
        checkNews()
        checkEvents()
        return Result.success()
    }

    private suspend fun checkNews() {
        val url = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/news.json"
        try {
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    // Usamos el DataLoader para parsear y lanzar notificaciones si procede
                    DataLoader.loadNews(applicationContext)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun checkEvents() {
        val url = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/events.json"
        try {
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    DataLoader.loadEvents(applicationContext)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
