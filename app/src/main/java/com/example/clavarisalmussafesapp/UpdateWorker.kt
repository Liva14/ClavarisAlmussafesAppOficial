package com.example.clavarisalmussafesapp

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Trabajador en segundo plano que verifica periódicamente si hay nuevas noticias o eventos.
 */
class UpdateWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val client = OkHttpClient()

    override suspend fun doWork(): Result {
        // Ejecuta las verificaciones de contenido
        checkNews()
        checkEvents()
        return Result.success()
    }

    /**
     * Comprueba si hay noticias nuevas descargando el JSON remoto.
     */
    private suspend fun checkNews() {
        val url = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/news.json"
        try {
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    // El DataLoader se encarga de comparar con la versión local y notificar
                    DataLoader.loadNews(applicationContext)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Comprueba si hay eventos nuevos en el calendario.
     */
    private suspend fun checkEvents() {
        val url = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/assets/events.json"
        try {
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    // El DataLoader se encarga de comparar con la versión local y notificar
                    DataLoader.loadEvents(applicationContext)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
