package com.example.androiddemo.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class ImageLoader (private val context: Context) {

    private val maxCacheSize = 50 * 1024 * 1024
    private val cache: LinkedHashMap<String, WeakReference<Bitmap>> = object : LinkedHashMap<String, WeakReference<Bitmap>>(
        16,
        0.75f,
        true
    ) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, WeakReference<Bitmap>>?): Boolean {
            return cacheSize() > maxCacheSize
        }
    }

    private val cacheMutex = Mutex()

    suspend fun loadImage(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            var bitmap: Bitmap? = null
            cacheMutex.withLock {
                bitmap = cache[url]?.get()
            }
            if (bitmap == null) {
                bitmap = downloadImage(url)
                if (bitmap != null) {
                    cacheMutex.withLock {
                        cache[url] = WeakReference(bitmap)
                    }
                }
            }
            bitmap
        }
    }

    private suspend fun downloadImage(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            var bitmap: Bitmap? = null
            var connection: HttpURLConnection? = null
            try {
                val urlConnection = URL(url)
                connection = urlConnection.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()

                    // Save image to cache directory
                    if (bitmap != null) {
                        saveBitmapToCache(url, bitmap!!)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
            bitmap
        }
    }

    private fun saveBitmapToCache(url: String, bitmap: Bitmap) {
        val fileName = url.hashCode().toString()
        val file = File(context.cacheDir, fileName)
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream)
        }
    }

    private fun cacheSize(): Int {
        var size = 0
        for ((_, value) in cache) {
            value.get()?.let {
                size += it.byteCount
            }
        }
        return size
    }
}