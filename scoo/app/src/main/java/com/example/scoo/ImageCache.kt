package com.example.scoo

import android.graphics.Bitmap
import android.util.LruCache

public class ImageCache() {
    lateinit var mImageCache: LruCache<String, Bitmap>

    init {
        initImageCache()
    }

    fun initImageCache() {
        val maxMemory: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize: Int = maxMemory / 4

        mImageCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return if (value != null) {
                    value.rowBytes * value.height / 1024
                } else {
                    super.sizeOf(key, value)
                }
            }
        }
    }

    fun put(url: String, bitmap: Bitmap) {
        mImageCache.put(url, bitmap)
    }

    fun get(url: String): Bitmap {
        return mImageCache.get(url)
    }

}