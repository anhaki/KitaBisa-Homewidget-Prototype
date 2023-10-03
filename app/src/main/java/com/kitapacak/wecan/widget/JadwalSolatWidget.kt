package com.kitapacak.wecan.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import com.kitapacak.wecan.API.ApiConfig
import com.kitapacak.wecan.DonasiOtomatisActivity
import com.kitapacak.wecan.IsiSaldoActivity
import com.kitapacak.wecan.R
import com.kitapacak.wecan.Response.JadwalSolatResponse
import com.kitapacak.wecan.Response.KodeKotaResponse
import com.kitapacak.wecan.SharedPref.CityModel
import com.kitapacak.wecan.SharedPref.CityPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.time.chrono.HijrahDate


/*
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
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.jadwal_solat)

    val cityPreference = CityPreference(context)
    val getCity: CityModel = cityPreference.getCity()

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


    //try to get cityId
    val cityId = ApiConfig.getCityIdService().getCityId(getCity.city.toString())
    cityId.enqueue(object : Callback<KodeKotaResponse> {
        override fun onResponse(call: Call<KodeKotaResponse>, response: Response<KodeKotaResponse>) {
            if (response.isSuccessful) {
                val idCity = response.body()?.kota?.get(0)?.id.toString()

                val client = ApiConfig.getApiService().getJadwal(idCity, todayDate.toString())
                client.enqueue(object : Callback<JadwalSolatResponse> {
                    override fun onResponse(call: Call<JadwalSolatResponse>, response: Response<JadwalSolatResponse>) {
                        if (response.isSuccessful) {
                            val imsakTime = response.body()?.jadwal?.data?.imsak
                            val subuhTime = response.body()?.jadwal?.data?.subuh
                            val dzuhurTime = response.body()?.jadwal?.data?.dzuhur
                            val asharTime = response.body()?.jadwal?.data?.ashar
                            val maghribTime = response.body()?.jadwal?.data?.maghrib
                            val isyaTime = response.body()?.jadwal?.data?.isya

                            if (imsakTime != null && subuhTime != null && dzuhurTime != null && asharTime != null &&
                                maghribTime != null && isyaTime != null) {
                                views.setTextViewText(R.id.timeImsak, imsakTime)
                                views.setTextViewText(R.id.timeSubuh, subuhTime)
                                views.setTextViewText(R.id.timeDzuhur, dzuhurTime)
                                views.setTextViewText(R.id.timeAshar, asharTime)
                                views.setTextViewText(R.id.timeMagrib, maghribTime)
                                views.setTextViewText(R.id.timeIsya, isyaTime)

                                //parse ke tipe Date agar dapat dibandingkan
                                val parsedNowTime = timeFormatter.parse(nowTime)

                                if (parsedNowTime != null) {
                                    when {
                                        parsedNowTime.before(subuhTime.let { timeFormatter.parse(it) }) -> {
                                            views.setTextViewText(R.id.nextSholat, "Subuh $subuhTime")
                                            views.setTextColor(R.id.timeSubuh, Color.parseColor("#2270AE"))
                                            views.setTextColor(R.id.timeIsya, Color.parseColor("#3E3E3E"))
                                        }

                                        parsedNowTime.before(dzuhurTime.let {timeFormatter.parse(it) }) ->{
                                            views.setTextViewText(R.id.nextSholat, "Dzuhur $dzuhurTime")
                                            views.setTextColor(R.id.timeDzuhur, Color.parseColor("#2270AE"))
                                            views.setTextColor(R.id.timeSubuh, Color.parseColor("#3E3E3E"))
                                        }

                                        parsedNowTime.before(asharTime.let {timeFormatter.parse(it) }) ->{
                                            views.setTextViewText(R.id.nextSholat, "Ashar $asharTime")
                                            views.setTextColor(R.id.timeAshar, Color.parseColor("#2270AE"))
                                            views.setTextColor(R.id.timeDzuhur, Color.parseColor("#3E3E3E"))
                                        }

                                        parsedNowTime.before(maghribTime.let {timeFormatter.parse(it) }) ->{
                                            views.setTextViewText(R.id.nextSholat, "Magrib $maghribTime")
                                            views.setTextColor(R.id.timeMagrib, Color.parseColor("#2270AE"))
                                            views.setTextColor(R.id.timeAshar, Color.parseColor("#3E3E3E"))
                                        }

                                        parsedNowTime.before(isyaTime.let {timeFormatter.parse(it) }) ->{
                                            views.setTextViewText(R.id.nextSholat, "Isya $isyaTime")
                                            views.setTextColor(R.id.timeIsya, Color.parseColor("#2270AE"))
                                            views.setTextColor(R.id.timeMagrib, Color.parseColor("#3E3E3E"))
                                        }
                                        else -> {
                                            val clientNext = ApiConfig.getApiService().getJadwal(idCity, todayDate?.plusDays(1).toString())
                                            clientNext.enqueue(object : Callback<JadwalSolatResponse> {
                                                override fun onResponse(call: Call<JadwalSolatResponse>, response: Response<JadwalSolatResponse>) {
                                                    if (response.isSuccessful) {
                                                        val nextSubuhTime = response.body()?.jadwal?.data?.subuh
                                                        views.setTextViewText(R.id.nextSholat, "Subuh $nextSubuhTime")
                                                        views.setTextColor(R.id.timeIsya, Color.parseColor("#3E3E3E"))
                                                        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views)
                                                    } else {
                                                        Log.e(TAG, "Failed to get next day's prayer times: ${response.message()}")
                                                    }
                                                }

                                                override fun onFailure(call: Call<JadwalSolatResponse>, t: Throwable) {
                                                    Log.e(TAG, "onFailure: ${t.message}")
                                                }
                                            })
                                        }

                                    }
                                }
                            }

                            try {
                                val hijriDate = getCurrentHijriDate()
                                val masehiDate = getCurrentMasehiDate()
                                views.setTextViewText(R.id.hjrDate, "$hijriDate H")
                                views.setTextViewText(R.id.mshDate,"$masehiDate M")
                            } catch (e: Exception) {
                                Log.e(TAG, "Error getting Hijri date: ${e.message}")
                                views.setTextViewText(R.id.hjrDate, "N/A")
                            }
                            appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views)
                        } else {
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<JadwalSolatResponse>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message}")
                    }
                })


            }
        }

        override fun onFailure(call: Call<KodeKotaResponse>, t: Throwable) {
            Log.e(TAG, "onFailure: ${t.message}", )
        }

    })
    views.setTextViewText(R.id.titleWaktu,"${getCity.city.toString()} \nWaktu Sholat selanjutnya")

        //Connect to API ::622 kode plg
    views.setOnClickPendingIntent(R.id.btn_isi, pendingIntent)

    views.setOnClickPendingIntent(R.id.dono,pendingIntent2)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)

}

fun getCurrentDate(): LocalDate? {
    return LocalDate.now()
}
private fun getCurrentMasehiDate(): String {
    val masehiFormat = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy",Locale("id", "ID") )
    val date = getCurrentDate()?.format(masehiFormat).toString()
    return date
}

fun getCurrentHijriDate(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")
    val gregorianString = getCurrentDate().toString()
    val gregorianDate = LocalDate.parse(gregorianString, dateFormatter)
    val islamicDate = HijrahDate.from(gregorianDate)

    val hijriDateFormatter = DateTimeFormatter.ofPattern("dd MMMM uuuu", Locale("id", "ID"))
    return islamicDate.format(hijriDateFormatter)
}

fun getCurrentTime(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
}


