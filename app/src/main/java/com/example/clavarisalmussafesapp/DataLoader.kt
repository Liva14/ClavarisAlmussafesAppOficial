package com.example.clavarisalmussafesapp

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

object DataLoader {

    private val client = OkHttpClient()
    
    // URLs en formato RAW para obtener el JSON directamente de GitHub
    private const val NEWS_URL = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/news.json"
    private const val EVENTS_URL = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/events.json"

    suspend fun loadNews(context: Context): List<NewsPost> = withContext(Dispatchers.IO) {
        val newsList = mutableListOf<NewsPost>()
        
        // Intentar cargar desde la red
        val jsonString = try {
            val request = Request.Builder().url(NEWS_URL).build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.string()
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }

        // Si falla la red o devuelve null, cargar de local (assets) como fallback
        val finalJsonString = jsonString ?: try {
            context.assets.open("news.json").bufferedReader().use { it.readText() }
        } catch (ioe: Exception) {
            null
        }

        finalJsonString?.let {
            try {
                val jsonArray = JSONArray(it)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    newsList.add(
                        NewsPost(
                            title = obj.optString("title", ""),
                            description = obj.optString("description", ""),
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
                if (response.isSuccessful) {
                    response.body?.string()
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }

        // Si falla la red o devuelve null, cargar de local (assets) como fallback
        val finalJsonString = jsonString ?: try {
            context.assets.open("events.json").bufferedReader().use { it.readText() }
        } catch (ioe: Exception) {
            null
        }

        finalJsonString?.let {
            try {
                val jsonArray = JSONArray(it)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    try {
                        val typeStr = obj.optString("type", "EVENT")
                        val eventType = try {
                            EventType.valueOf(typeStr)
                        } catch (e: IllegalArgumentException) {
                            EventType.EVENT
                        }

                        eventList.add(
                            CalendarEvent(
                                date = obj.optString("date", ""),
                                title = obj.optString("title", ""),
                                time = obj.optString("time", ""),
                                type = eventType
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        eventList
    }
}
