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
    
    // Cambia estas URLs por las de tu servidor o GitHub (ej: Raw de un Gist o Repositorio)
    private const val NEWS_URL = "https://Liva14.github.io/ClavarisApp/news.json"
    private const val EVENTS_URL = "https://Liva14.github.io/ClavarisApp/events.json"

    suspend fun loadNews(context: Context): List<NewsPost> = withContext(Dispatchers.IO) {
        val newsList = mutableListOf<NewsPost>()
        
        // Intentar cargar desde la red
        val jsonString = try {
            val request = Request.Builder().url(NEWS_URL).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body?.string()
            }
        } catch (e: Exception) {
            // Fallback: Si falla la red, cargar de local (assets)
            try {
                context.assets.open("news.json").bufferedReader().use { it.readText() }
            } catch (ioe: Exception) {
                null
            }
        }

        jsonString?.let {
            try {
                val jsonArray = JSONArray(it)
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
        
        // Intentar cargar desde la red
        val jsonString = try {
            val request = Request.Builder().url(EVENTS_URL).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body?.string()
            }
        } catch (e: Exception) {
            // Fallback: Si falla la red, cargar de local (assets)
            try {
                context.assets.open("events.json").bufferedReader().use { it.readText() }
            } catch (ioe: Exception) {
                null
            }
        }

        jsonString?.let {
            try {
                val jsonArray = JSONArray(it)
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
