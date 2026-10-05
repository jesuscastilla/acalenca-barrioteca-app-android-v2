package com.lebeche.barrioteca.notif

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lebeche.barrioteca.data.Prefs

class FcmService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("FCM", "New token: $token")
        // Guarda el token localmente.
        // TODO: Enviar este token al backend para que se asocie con la socia activa.
        Prefs.saveFcmToken(this, token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        // Extrae el título y el cuerpo, ya sea de notification (FCM consola) o de data (backend propio)
        val title = message.notification?.title ?: message.data["title"] ?: "Barrioteca Acalencá"
        val body = message.notification?.body ?: message.data["body"] ?: ""
        
        if (body.isNotBlank()) {
            val id = System.currentTimeMillis().toInt()
            Notifications.notify(this, title, body, id)
        }
    }
}
