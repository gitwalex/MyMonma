package com.gerwalex.mymonma.ext

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object FileExt {

    /**
     * Kopiert ein File. Die Pruefung, ob das Zielfile z.B. wegen Berechtigungen erstellt werden
     * kann obliegt der aufrufenden Klasse.
     *
     * @param src  File, welches kopiert werden soll
     * @param dest TargetFile
     * @throws IOException Bei Fehlern.
     */
    @Throws(IOException::class)
    fun File.copy(dest: File?) {
        try {
            FileInputStream(this).channel.use { inChannel ->
                FileOutputStream(dest).channel.use { outChannel ->
                    inChannel.transferTo(
                        0,
                        inChannel.size(),
                        outChannel
                    )
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * Archiviert files in ein file-Archiv
     * @param filesToZip Liste der Files, die dem archiv hinzugefuegt werden sollen.
     * @throws IllegalArgumentException wenn target ein Directory ist.
     */

    @Throws(IOException::class)
    fun File.addToZip(vararg filesToZip: File) {
        val dir = absoluteFile
        require(!isDirectory) { "Target darf kein Directory sein" }
        var zos: ZipOutputStream? = null
        try {
            val fout = FileOutputStream(this)
            zos = ZipOutputStream(BufferedOutputStream(fout))
            addZipFiles(zos, *filesToZip)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            zos?.close()
        }
    }

    /**
     * Fuegt dem ZipOutputStream alle Files der Filelist hinzu.
     *
     * @param out      ZipOutputStream
     * @param fileList Liste der Files. Ist ein File ein Directory, werden alle Fileses dieses Directoreis
     * rekursiv hnzugefuegt.
     */
    @Throws(IOException::class)
    private fun addZipFiles(out: ZipOutputStream, vararg fileList: File) {
        val BUFFERSIZE = 8192
        var stream: BufferedInputStream
        val data = ByteArray(BUFFERSIZE)
        for (file in fileList) {
            if (file.isDirectory) {
                val files = file.listFiles()
                if (files != null) {
                    addZipFiles(out, *files)
                }
            } else {
                stream = BufferedInputStream(
                    FileInputStream(file.path),
                    BUFFERSIZE
                )
                val entry = ZipEntry(file.name)
                entry.time = file.lastModified() // to keep modification time
                // after unzipping
                out.putNextEntry(entry)
                var length: Int
                while (stream.read(data).also { length = it } > 0) {
                    out.write(data, 0, length)
                }
                stream.close()
            }
        }
    }


}