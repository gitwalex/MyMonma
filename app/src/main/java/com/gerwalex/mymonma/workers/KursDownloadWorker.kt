package com.gerwalex.mymonma.workers

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.datastore.preferences.core.edit
import androidx.work.*
import com.gerwalex.mymonma.database.room.FileUtils
import com.gerwalex.mymonma.ext.dataStore
import com.gerwalex.mymonma.ext.preferences
import com.gerwalex.mymonma.importer.FinanztreffOnline
import com.gerwalex.mymonma.main.App
import com.gerwalex.mymonma.preferences.PreferenceKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.sql.Date
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.set

class KursDownloadWorker(private val context: Context, params: WorkerParameters) : CoroutineWorker(
    context, params
) {

    private val messages: MutableList<String> = ArrayList()
    override suspend fun doWork(): Result {
        val weekday = GregorianCalendar
            .getInstance()
            .get(Calendar.DAY_OF_WEEK)
        when (weekday) {
            Calendar.SUNDAY, Calendar.SATURDAY -> {
            }

            else -> {
                withContext(Dispatchers.IO) {
                    executeDownload()
                }
            }
        }
        submitDelayed(context)
        return Result.success()
    }


    @WorkerThread
    fun executeDownload(): Result {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier { hostname, _ ->
                Log.d("gerwalex", hostname)
                "alt.finanztreff.de" == hostname
            }
            CookieHandler.setDefault(CookieManager(null, CookiePolicy.ACCEPT_ALL))
            val sb = StringBuilder()
            val user = context.preferences.getString("user", "")!!
            val pw = context.preferences.getString("pw", "")!!
            if (login(user, pw)) {
                Thread.sleep(1000)
                loadKurseAsCSV()
            }
            for (s in messages) {
                sb
                    .append(s)
                    .append(App.linefeed)
            }
            Log.d("gerwalex", sb.toString())
            //
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Result.success()
    }

    @Throws(IOException::class)
    private fun loadKurseAsCSV() {
        val list = makeHttpRequest(
            "https://alt.finanztreff.de/depot_portfolio.htn?sektion=masterportfolio&mpId=507&ansicht=ascii",
            "GET", null
        )
        if (list.size > 0) {
            val df = Date(System.currentTimeMillis())
            val fn = FinanztreffOnline.prefix + df + ".csv"
            val file = File(App.getAppDownloadDir(context), fn)
            if (FileUtils.writeFile(file, list)) {
                messages.add("Download file $fn")
                FinanztreffOnline(context)
                    .doImport(messages)
            }
        }
    }

    @Throws(IOException::class)
    private fun login(user: String, pw: String): Boolean {
        val params = HashMap<String, String>()
        params["lg"] = user.trim { it <= ' ' }
        params["pw"] = pw
        params["savelg"] = "1"
        params["sektion"] = "login"
        params["Anmelden"] = "go"
        val list = makeHttpRequest(LOGIN_URL, "POST", params)
        for (s in list) {
            if (s.contains("angemeldet")) {
                return true
            }
        }
        return false
    }

    companion object {

        private const val LOGIN_URL = "https://alt.finanztreff.de/anmeldung.htn?"
        private val tag: String = MaintenanceWorker::class.java.name
        fun submitDelayed(context: Context): Operation {
            val cal = GregorianCalendar.getInstance().apply {
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                if (get(Calendar.HOUR_OF_DAY) >= 17) { // nächster Lauf Morgen, 0800 Uhr
                    set(Calendar.HOUR_OF_DAY, 8)
                    set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
                } else {
                    set(Calendar.HOUR_OF_DAY, 17)
                }
            }

            val delay = cal.time.time - System.currentTimeMillis()
            CoroutineScope(Dispatchers.IO).launch {
                context.dataStore.edit { it[PreferenceKey.NextKursDownload] = cal.time.time }
            }

            return submit(context, delay)
        }


        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(tag)
        }

        @JvmStatic
        fun submit(context: Context, delay: Long = 0): Operation {
            val constraints = Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequest
                .Builder(KursDownloadWorker::class.java)
                .setConstraints(constraints)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()
            return WorkManager
                .getInstance(context)
                .enqueueUniqueWork(
                    tag,
                    ExistingWorkPolicy.REPLACE,
                    request
                )
        }


    }

    /**
     * Fuehrt einen HTTP-request durch
     *
     * @param url    URL
     * @param method Requestmethod: POST oder GET
     * @param params parameter, wie sie fuer den Request benoetigt werden
     */
    @Throws(IOException::class)
    fun makeHttpRequest(
        url: String, method: String,
        params: HashMap<String, String>?
    ): List<String> {
        val result: MutableList<String> = ArrayList()
        val sbParams = java.lang.StringBuilder()
        var i = 0
        val charset = "UTF-8"
        if (params != null) {
            for (key in params.keys) {
                try {
                    if (i != 0) {
                        sbParams.append("&")
                    }
                    sbParams.append(key).append("=").append(URLEncoder.encode(params[key], charset))
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                i++
            }
        }
        var conn: HttpURLConnection? = null
        when (method) {
            "POST" -> {
                val urlObj = URL(url)
                conn = urlObj.openConnection() as HttpsURLConnection
                conn.doOutput = true
                conn.requestMethod = "POST"
                conn.setRequestProperty("Accept-Charset", charset)
                conn.readTimeout = 10000
                conn.connectTimeout = 15000
                conn.connect()
                val paramsString = sbParams.toString()
                val wr = DataOutputStream(conn.outputStream)
                wr.writeBytes(paramsString)
                wr.flush()
                wr.close()
            }

            "GET" -> {
                // request method is GET
                var myurl = url
                if (sbParams.length != 0) {
                    myurl += "?$sbParams"
                }
                val urlObj = URL(myurl)
                conn = urlObj.openConnection() as HttpsURLConnection
                conn.doOutput = false
                conn.requestMethod = "GET"
                conn.setRequestProperty("Accept-Charset", charset)
                conn.readTimeout = 10000
                conn.connectTimeout = 15000
                conn.connect()
            }

            else -> throw IllegalArgumentException("Method nicht bekannt (Nur POST ooder GET moeglich $method")
        }
        //Receive the response from the server
        val `in`: InputStream = BufferedInputStream(conn.inputStream)
        val reader = BufferedReader(InputStreamReader(`in`))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            line?.let {
                result.add(it)

            }
        }
        conn.disconnect()
        return result
    }

}