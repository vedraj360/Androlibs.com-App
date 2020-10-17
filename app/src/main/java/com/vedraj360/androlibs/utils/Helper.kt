package com.vedraj360.androlibs.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import okhttp3.Cache
import okhttp3.OkHttpClient
import kotlin.math.pow
import kotlin.math.sqrt


class Helper {
    companion object {
        fun formatDate(date: String): String {
            val months = arrayOf<String>(
                "non",
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"
            )

            val s: List<String> = date.split("-")
            val month = months[s[1].toInt()]
            var singleDate = s[2]
            singleDate = "${singleDate[0]}${singleDate[1]}"
            val year = s[0]
            return "$singleDate $month, $year"
        }


        fun hasNetwork(context: Context?): Boolean {
            var result = false
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return result
        }

        fun okHttp(
            app: Context
        ): OkHttpClient {
            try {

            } catch (e: Exception) {

            }
            val cacheSize = (10 * 1024 * 1024).toLong()
            val myCache = Cache(app.cacheDir, cacheSize)

            return OkHttpClient.Builder()
                .cache(myCache)
                .addInterceptor { chain ->
                    val key = if (Keys.readKey() != null) {
                        Keys.readKey().toString()
                    } else {
                        "Retry"
                    }
                    var request = chain.request()
                    request = if (hasNetwork(app.applicationContext)) {
                        request.newBuilder().header("Cache-Control", "public, max-age=" + 60)
                            .addHeader(
                                "Authorization",
                                key
                            )
                            .build()
                    } else {
                        request.newBuilder().header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                        ).build()
                    }

                    chain.proceed(request)
                }
                .build()
        }

        fun checkIsTablet(activity: Activity): Boolean {
            val display = activity.windowManager.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            val widthInches = metrics.widthPixels / metrics.xdpi
            val heightInches = metrics.heightPixels / metrics.ydpi
            val diagonalInches = sqrt(
                widthInches.toDouble().pow(2.0) + heightInches.toDouble().pow(2.0)
            )
//            Log.e("HELPER", "checkIsTablet: $diagonalInches")
            return diagonalInches >= 8
        }
    }
}