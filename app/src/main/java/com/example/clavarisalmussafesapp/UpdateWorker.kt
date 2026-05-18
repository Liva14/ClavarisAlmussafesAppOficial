package com.example.clavarisalmussafesapp

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class UpdateWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val client = OkHttpClient()
    private val sharedPrefs = context.getSharedPreferences("clavaris_prefs", Context.MODE_PRIVATE)

    override suspend fun doWork(): Result {
        checkNews()
        checkEvents()
        return Result.success()
    }

    private fun checkNews() {
        val url = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/news.json"
        try {
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val json = response.body?.string() ?: return
                    val jsonArray = JSONArray(json)
                    val currentCount = jsonArray.length()
                    val lastCount = sharedPrefs.getInt("last_news_count", 0)

                    if (currentCount > lastCount && lastCount != 0) {
                        val firstItem = jsonArray.getJSONObject(0)
                        val title = firstItem.getString("title")
                        NotificationHelper.sendNotification(
                            applicationContext,
                            "Nova notícia: $title",
                            "S'ha publicat una nova notícia en l'app."
                        )
                    }
                    sharedPrefs.edit().putInt("last_news_count", currentCount).apply()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkEvents() {
        val url = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/events.json"
        try {
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val json = response.body?.string() ?: return
                    val jsonArray = JSONArray(json)
                    val currentCount = jsonArray.length()
                    val lastCount = sharedPrefs.getInt("last_events_count", 0)

                    if (currentCount > lastCount && lastCount != 0) {
                        NotificationHelper.sendNotification(
                            applicationContext,
                            "Nou esdeveniment",
                            "S'han afegit nous esdeveniments al calendari."
                        )
                    }
                    sharedPrefs.edit().putInt("last_events_count", currentCount).apply()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
