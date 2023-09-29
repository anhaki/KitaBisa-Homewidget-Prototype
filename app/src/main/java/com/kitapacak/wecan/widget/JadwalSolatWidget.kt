package com.kitapacak.wecan.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import com.kitapacak.wecan.API.ApiConfig
import com.kitapacak.wecan.DonasiOtomatisActivity
import com.kitapacak.wecan.IsiSaldoActivity
import com.kitapacak.wecan.R
import com.kitapacak.wecan.Response.JadwalSolatResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * Implementation of App Widget functionality.
 */
class JadwalSolat : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val buildCheck = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE else 0

    //Buat Intent ke Isi Saldo Activity
    val intentToIsiSaldo = Intent(context, IsiSaldoActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context,0,intentToIsiSaldo,buildCheck)

    //Intent ke Activity Donasi Otomatis
    val intentToDO = Intent(context,DonasiOtomatisActivity::class.java)
    val pendingIntent2 = PendingIntent.getActivity(context,0,intentToDO,buildCheck)

    val todayDate = getCurrentDate()
    val nowTime = getCurrentTime()

    //digunakan untuk parse ke tipe Date
    val timeFormatter = SimpleDateFormat("HH:mm", Locale("id", "ID"))

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.jadwal_solat)

    //Connect to API
    val client = ApiConfig.getApiService().getJadwal("622", todayDate)
    client.enqueue(object : Callback<JadwalSolatResponse> {
        override fun onResponse(call: Call<JadwalSolatResponse>, response: Response<JadwalSolatResponse>) {
            if (response.isSuccessful) {
                val imsakTime = response.body()?.jadwal?.data?.imsak
                val subuhTime = response.body()?.jadwal?.data?.subuh
                val dzuhurTime = response.body()?.jadwal?.data?.dzuhur
                val asharTime = response.body()?.jadwal?.data?.ashar
                val maghribTime = response.body()?.jadwal?.data?.maghrib
                val isyaTime = response.body()?.jadwal?.data?.isya

                views.setTextViewText(R.id.timeImsak, imsakTime)
                views.setTextViewText(R.id.timeSubuh, subuhTime)
                views.setTextViewText(R.id.timeDzuhur, dzuhurTime)
                views.setTextViewText(R.id.timeAshar, asharTime)
                views.setTextViewText(R.id.timeMagrib, maghribTime)
                views.setTextViewText(R.id.timeIsya, isyaTime)

                //parse ke tipe Date agar dapat dibandingkan
                val parsedNowTime = timeFormatter.parse(nowTime)

                if (parsedNowTime != null) {
                    when{
                        parsedNowTime.before(subuhTime?.let { timeFormatter.parse(it) }) -> views.setTextViewText(R.id.nextSholat, "SUBUH $subuhTime")
                        parsedNowTime.before(dzuhurTime?.let { timeFormatter.parse(it) }) -> views.setTextViewText(R.id.nextSholat, "DZUHUR $dzuhurTime")
                        parsedNowTime.before(asharTime?.let { timeFormatter.parse(it) }) -> views.setTextViewText(R.id.nextSholat, "ASHAR $asharTime")
                        parsedNowTime.before(maghribTime?.let { timeFormatter.parse(it) }) -> views.setTextViewText(R.id.nextSholat, "MAGRIB $maghribTime")
                        parsedNowTime.before(isyaTime?.let { timeFormatter.parse(it) }) -> views.setTextViewText(R.id.nextSholat, "ISYA $isyaTime")
                    }
                }

                views.setTextViewText(R.id.mshDate, response.body()?.jadwal?.data?.tanggal + " M")
                appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views)
            } else {
                Log.e(TAG, "onFailure: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<JadwalSolatResponse>, t: Throwable) {
            Log.e(TAG, "onFailure: ${t.message}")
        }
    })

    views.setOnClickPendingIntent(R.id.btn_isi, pendingIntent)

    views.setOnClickPendingIntent(R.id.dono,pendingIntent2)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

fun getCurrentDate(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID")).format(Date())
}

fun getCurrentTime(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
}
