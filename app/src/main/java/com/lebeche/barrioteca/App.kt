package com.lebeche.barrioteca

import android.app.Application
import com.lebeche.barrioteca.notif.Notifications
import com.lebeche.barrioteca.sync.SyncWorker

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            Notifications.ensureChannel(this)
            SyncWorker.schedule(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
