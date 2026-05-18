package com.example.clavarisalmussafesapp

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException

object DataLoader {

    private val client = OkHttpClient()
    
    // URLs Raw de GitHub para que la app pueda leer el JSON correctamente
    private const val NEWS_URL = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/news.json"
    private const val EVENTS_URL = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/events.json"

    suspend fun loadNews(context: Context): List<NewsPost> = withContext(Dispatchers.IO) {
        val newsList = mutableListOf<NewsPost>()
        
        val jsonString = try {
            val request = Request.Builder().url(NEWS_URL).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body?.string()
            }
        } catch (e: Exception) {
            try {
                context.assets.open("news.json").bufferedReader().use { it.readText() }
            } catch (ioe: Exception) {
                null
            }
        }

        jsonString?.let {
            try {
                val jsonArray = JSONArray(it)
                // Guardamos el contador actual para evitar notificaciones redundantes
                val sharedPrefs = context.getSharedPreferences("clavaris_prefs", Context.MODE_PRIVATE)
                sharedPrefs.edit().putInt("last_news_count", jsonArray.length()).apply()

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    newsList.add(
                        NewsPost(
                            title = obj.getString("title"),
                            description = obj.getString("description"),
                            imageResName = if (obj.has("imageResName")) obj.getString("imageResName") else null,
                            imageUrl = if (obj.has("imageUrl")) obj.getString("imageUrl") else null
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        newsList
    }

    suspend fun loadEvents(context: Context): List<CalendarEvent> = withContext(Dispatchers.IO) {
        val eventList = mutableListOf<CalendarEvent>()
        
        val jsonString = try {
            val request = Request.Builder().url(EVENTS_URL).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body?.string()
            }
        } catch (e: Exception) {
            try {
                context.assets.open("events.json").bufferedReader().use { it.readText() }
            } catch (ioe: Exception) {
                null
            }
        }

        jsonString?.let {
            try {
                val jsonArray = JSONArray(it)
                // Guardamos el contador actual
                val sharedPrefs = context.getSharedPreferences("clavaris_prefs", Context.MODE_PRIVATE)
                sharedPrefs.edit().putInt("last_events_count", jsonArray.length()).apply()

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    eventList.add(
                        CalendarEvent(
                            date = obj.getString("date"),
                            title = obj.getString("title"),
                            time = obj.getString("time"),
                            type = EventType.valueOf(obj.getString("type"))
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        eventList
    }
}
