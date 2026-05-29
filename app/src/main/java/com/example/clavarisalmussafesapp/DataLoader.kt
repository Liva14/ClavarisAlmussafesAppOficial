package com.example.clavarisalmussafesapp

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Gestor de carga de datos de la aplicación.
 * Se encarga de obtener información desde GitHub (remoto) o desde los Assets (local) como respaldo.
 */
object DataLoader {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()
    
    private const val NEWS_URL = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/news.json"
    private const val EVENTS_URL = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/events.json"
    private const val LOTTERIES_URL = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/lotteries.json"
    private const val SPONSORS_URL = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/sponsors.json"

    /**
     * Carga las noticias. Intenta primero la red y luego los assets locales.
     */
    suspend fun loadNews(context: Context): List<NewsPost> = withContext(Dispatchers.IO) {
        var newsList = mutableListOf<NewsPost>()
        
        var jsonString = try {
            val request = Request.Builder()
                .url(NEWS_URL)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .addHeader("Cache-Control", "no-cache")
                .build()
            
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Error: $response")
                response.body?.string()
            }
        } catch (e: Exception) {
            null
        }

        if (jsonString != null) {
            newsList = parseNewsJson(jsonString, context)
        }

        if (newsList.isEmpty()) {
            jsonString = try {
                context.assets.open("news.json").bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                null
            }
            if (jsonString != null) {
                newsList = parseNewsJson(jsonString, context)
            }
        }
        
        newsList
    }

    /**
     * Parsea el JSON de noticias y gestiona el envío de notificaciones si hay contenido nuevo.
     */
    private fun parseNewsJson(jsonString: String, context: Context): MutableList<NewsPost> {
        val list = mutableListOf<NewsPost>()
        try {
            var sanitizedJson = jsonString.trim()
            if (sanitizedJson.endsWith(",")) sanitizedJson = sanitizedJson.substring(0, sanitizedJson.length - 1)
            if (!sanitizedJson.endsWith("]")) sanitizedJson += "]"
            
            val jsonArray = JSONArray(sanitizedJson)
            val sharedPrefs = context.getSharedPreferences("clavaris_prefs", Context.MODE_PRIVATE)
            val lastCount = sharedPrefs.getInt("last_news_count", 0)
            val currentCount = jsonArray.length()
            
            if (currentCount > lastCount && lastCount != 0) {
                val settingsPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                if (settingsPrefs.getBoolean("notifications_enabled", false)) {
                    val firstItem = jsonArray.getJSONObject(0)
                    NotificationHelper.sendNotification(
                        context,
                        "Nova notícia: ${firstItem.getString("title")}",
                        "S'ha publicat una nova notícia en l'app."
                    )
                }
            }
            sharedPrefs.edit().putInt("last_news_count", currentCount).apply()

            for (i in 0 until currentCount) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
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
        return list
    }

    /**
     * Carga los eventos del calendario desde remoto o local.
     */
    suspend fun loadEvents(context: Context): List<CalendarEvent> = withContext(Dispatchers.IO) {
        var eventList = mutableListOf<CalendarEvent>()
        
        var jsonString = try {
            val request = Request.Builder()
                .url(EVENTS_URL)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .addHeader("Cache-Control", "no-cache")
                .build()
            
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Error: $response")
                response.body?.string()
            }
        } catch (e: Exception) {
            null
        }

        if (jsonString != null) {
            eventList = parseEventsJson(jsonString, context)
        }

        if (eventList.isEmpty()) {
            jsonString = try {
                context.assets.open("events.json").bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                null
            }
            if (jsonString != null) {
                eventList = parseEventsJson(jsonString, context)
            }
        }
        
        eventList
    }

    /**
     * Parsea el JSON de eventos y notifica al usuario si hay actualizaciones.
     */
    private fun parseEventsJson(jsonString: String, context: Context): MutableList<CalendarEvent> {
        val list = mutableListOf<CalendarEvent>()
        try {
            var sanitizedJson = jsonString.trim()
            if (sanitizedJson.endsWith(",")) sanitizedJson = sanitizedJson.substring(0, sanitizedJson.length - 1)
            if (!sanitizedJson.endsWith("]")) sanitizedJson += "]"
            
            val jsonArray = JSONArray(sanitizedJson)
            val sharedPrefs = context.getSharedPreferences("clavaris_prefs", Context.MODE_PRIVATE)
            val lastCount = sharedPrefs.getInt("last_events_count", 0)
            val currentCount = jsonArray.length()

            if (currentCount > lastCount && lastCount != 0) {
                val settingsPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                if (settingsPrefs.getBoolean("notifications_enabled", false)) {
                    NotificationHelper.sendNotification(context, "Nou esdeveniment", "S'han afegit nous esdeveniments al calendari.")
                }
            }
            sharedPrefs.edit().putInt("last_events_count", currentCount).apply()

            for (i in 0 until currentCount) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
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
        return list
    }

    /**
     * Carga las rifas desde remoto o local.
     */
    suspend fun loadLotteries(context: Context): List<LotteryPost> = withContext(Dispatchers.IO) {
        var lotteryList = mutableListOf<LotteryPost>()
        
        var jsonString = try {
            val request = Request.Builder()
                .url(LOTTERIES_URL)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .addHeader("Cache-Control", "no-cache")
                .build()
            
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) response.body?.string() else null
            }
        } catch (e: Exception) { null }

        if (jsonString != null) {
            lotteryList = parseLotteriesJson(jsonString, context)
        }

        if (lotteryList.isEmpty()) {
            jsonString = try {
                context.assets.open("lotteries.json").bufferedReader().use { it.readText() }
            } catch (e: Exception) { null }
            if (jsonString != null) {
                lotteryList = parseLotteriesJson(jsonString, context)
            }
        }
        lotteryList
    }

    /**
     * Parsea el JSON de rifas y gestiona notificaciones.
     */
    private fun parseLotteriesJson(jsonString: String, context: Context): MutableList<LotteryPost> {
        val list = mutableListOf<LotteryPost>()
        try {
            var sanitizedJson = jsonString.trim()
            if (sanitizedJson.endsWith(",")) sanitizedJson = sanitizedJson.substring(0, sanitizedJson.length - 1)
            if (!sanitizedJson.endsWith("]")) sanitizedJson += "]"

            val jsonArray = JSONArray(sanitizedJson)
            val sharedPrefs = context.getSharedPreferences("clavaris_prefs", Context.MODE_PRIVATE)
            val lastCount = sharedPrefs.getInt("last_lotteries_count", 0)
            val currentCount = jsonArray.length()

            if (currentCount > lastCount && lastCount != 0) {
                val settingsPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                if (settingsPrefs.getBoolean("notifications_enabled", false)) {
                    val firstItem = jsonArray.getJSONObject(0)
                    NotificationHelper.sendNotification(context, "Nova rifa: ${firstItem.getString("title")}", "S'ha publicat una nova rifa en l'app.")
                }
            }
            sharedPrefs.edit().putInt("last_lotteries_count", currentCount).apply()

            for (i in 0 until currentCount) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    LotteryPost(
                        title = obj.getString("title"),
                        description = obj.getString("description"),
                        imageResName = if (obj.has("imageResName")) obj.getString("imageResName") else null,
                        imageUrl = if (obj.has("imageUrl")) obj.getString("imageUrl") else null
                    )
                )
            }
        } catch (e: Exception) { }
        return list
    }

    /**
     * Carga la lista de patrocinadores desde remoto o local.
     */
    suspend fun loadSponsors(context: Context): List<Sponsor> = withContext(Dispatchers.IO) {
        var sponsorList = mutableListOf<Sponsor>()
        
        var jsonString = try {
            val request = Request.Builder()
                .url(SPONSORS_URL)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .addHeader("Cache-Control", "no-cache")
                .build()
            
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) response.body?.string() else null
            }
        } catch (e: Exception) { null }

        if (jsonString != null) {
            sponsorList = parseSponsorsJson(jsonString)
        }

        if (sponsorList.isEmpty()) {
            jsonString = try {
                context.assets.open("sponsors.json").bufferedReader().use { it.readText() }
            } catch (e: Exception) { null }
            if (jsonString != null) {
                sponsorList = parseSponsorsJson(jsonString)
            }
        }
        sponsorList
    }

    /**
     * Parsea el JSON de patrocinadores.
     */
    private fun parseSponsorsJson(jsonString: String): MutableList<Sponsor> {
        val list = mutableListOf<Sponsor>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    Sponsor(
                        name = obj.getString("name"),
                        imageUrl = if (obj.has("imageUrl")) obj.getString("imageUrl") else null,
                        imageResName = if (obj.has("imageResName")) obj.getString("imageResName") else null
                    )
                )
            }
        } catch (e: Exception) { }
        return list
    }
}
