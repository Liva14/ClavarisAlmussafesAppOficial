package com.example.clavarisalmussafesapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * Utilidad para gestionar las notificaciones de la aplicación.
 */
object NotificationHelper {
    const val CHANNEL_ID = "CLAVARIS_CH"
    private const val CHANNEL_NAME = "Actualitzacions Clavaris"
    private const val CHANNEL_DESC = "Notificacions de noves notícies i esdeveniments"

    /**
     * Crea el canal de notificaciones necesario para Android 8.0+.
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Envía una notificación al usuario.
     * Al pulsarla, abre la actividad principal de la app.
     */
    fun sendNotification(context: Context, title: String, message: String) {
        // Intent para abrir la app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Configuración de la notificación
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Notifica con un ID basado en el tiempo actual para evitar colisiones
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
