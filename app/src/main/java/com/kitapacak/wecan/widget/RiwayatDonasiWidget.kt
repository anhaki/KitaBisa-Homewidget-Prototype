package com.kitapacak.wecan.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.kitapacak.wecan.DonasiOtomatisActivity
import com.kitapacak.wecan.IsiSaldoActivity
import com.kitapacak.wecan.R

/**
 * Implementation of App Widget functionality.
 */
class RiwayatDonasiWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget2(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget2(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.riwayat_donasi_widget)

    val buildCheck = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE else 0

    //Buat Intent ke Isi Saldo Activity
    val intentToIsiSaldo = Intent(context, IsiSaldoActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context,0,intentToIsiSaldo,buildCheck)
    views.setOnClickPendingIntent(R.id.btn_isi2,pendingIntent)

    //Intent ke Activity Donasi Otomatis
    val intentToDO = Intent(context, DonasiOtomatisActivity::class.java)
    val pendingIntent2 = PendingIntent.getActivity(context,0,intentToDO,buildCheck)
    views.setOnClickPendingIntent(R.id.donasiOtomatis,pendingIntent2)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}