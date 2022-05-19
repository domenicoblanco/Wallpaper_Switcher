package it.domenicoblanco.wallpaperswitcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StartAtBoot : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            context.startService(Intent(context, ChangeTheme::class.java))
        }
    }
}