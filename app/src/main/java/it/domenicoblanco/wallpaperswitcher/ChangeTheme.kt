package it.domenicoblanco.wallpaperswitcher

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import java.io.File

class ChangeTheme : Service() {
    private lateinit var wallMan: WallpaperManager
    private lateinit var pendingIntent: PendingIntent

    private val notChan = NotificationChannel("wallpaper", "wallpaper", NotificationManager.IMPORTANCE_NONE)

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        wallMan =  WallpaperManager.getInstance(baseContext)

        pendingIntent = Intent(this, ChangeTheme::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        }

        notChan.description = "Wallpaper changer"
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(notChan)

        val notification: Notification = Notification.Builder(this, "wallpaper")
            .setContentTitle(getText(R.string.app_name))
            .setSmallIcon(androidx.activity.R.drawable.notification_icon_background)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        startForeground(1, notification)
        return START_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        when(newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                if (fileList().contains("dark"))
                    wallMan.setStream(File(filesDir, "dark").inputStream())
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                if (fileList().contains("light"))
                    wallMan.setStream(File(filesDir, "light").inputStream())
            }
        }
    }
}