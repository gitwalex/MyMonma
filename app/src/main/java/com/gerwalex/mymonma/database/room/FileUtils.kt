package com.gerwalex.mymonma.database.room

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * Created by K.K. Ho on 3/9/2017.
 */
object FileUtils {
    fun loadCSVFile(`in`: InputStream, database: SupportSQLiteDatabase, tablename: String) {
        val cv = ContentValues()
        try {
            Log.d("gerwalex", "Lade Tabelle $tablename")
            val buffer = BufferedReader(InputStreamReader(`in`))
            val colnames = buffer.readLine().split(";".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            var line: String?
            while (buffer.readLine().also { line = it } != null) {
                line?.let { current ->
                    val csvcolumns: Array<String?> =
                        current.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    for (i in csvcolumns.indices) {
                        csvcolumns[i]?.let {
                            if (it.isNotEmpty()) {
                                cv.put(colnames[i], it)
                            }
                        }
                    }
                }
                database.insert(tablename, SQLiteDatabase.CONFLICT_NONE, cv)
                cv.clear()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d("gerwalex", "Werte: $cv")
        } finally {
            try {
                `in`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun getOutputFilePath(
        compressFormat: CompressFormat, outputDirPath: String,
        outFilename: String,
    ): String {
        val targetFileName: String
        val targetFileExtension = "." + compressFormat
            .name
            .lowercase(Locale.US)
            .replace("jpeg", "jpg")
        targetFileName = outFilename + targetFileExtension
        return outputDirPath + File.separator + targetFileName
    }

    @JvmStatic
    @Throws(IOException::class)
    fun writeBitmapToFile(
        bitmap: Bitmap,
        compressFormat: CompressFormat?,
        quality: Int,
        file: File
    ) {
        val directory = file.parentFile
        if (directory != null && !directory.exists()) {
            directory.mkdirs()
        }
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            bitmap.compress(compressFormat, quality, fileOutputStream)
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        }
    }
}