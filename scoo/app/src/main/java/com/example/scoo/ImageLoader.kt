package com.example.scoo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.ImageView
import java.lang.Exception
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.net.HttpURLConnection as HttpURLConnection1

public class ImageLoader() {
    //图片缓存
   lateinit var mImageCache : LruCache<String, Bitmap>;
   val mExecutorService : ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    init {
        initImageCache()
    }

    fun initImageCache() : Unit {
        //计算可使用的最大内存
        val maxMemory : Int = (Runtime.getRuntime().maxMemory() / 1024).toInt();
        val cacheSize : Int =maxMemory / 4;
        mImageCache =object : LruCache<String,Bitmap>(cacheSize){
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return if (value != null) {
                    value.rowBytes * value.height / 1024;
                }else {
                    super.sizeOf(key, value)
                }
            }
        }
    }

     fun displayImage(url : String,  imageView : ImageView) : Unit {
         imageView.tag = url;
         mExecutorService.submit( object : Runnable {
             override fun run() {
                 var bitmap: Bitmap? = downloadImage(url) ?: return
                 if (imageView.tag.equals(url)) {
                     imageView.setImageBitmap(bitmap)
                 }
                 mImageCache.put(url,bitmap)
             }

         })
    }

    fun downloadImage(imageUrl : String) : Bitmap? {
        var bitmap : Bitmap? = null
        try {
            var url  = URL(imageUrl)
            val conn : HttpURLConnection1 = url.openConnection() as java.net.HttpURLConnection
            bitmap = BitmapFactory.decodeStream(conn.inputStream)
            conn.disconnect()
        }catch (e : Exception) {
          e.printStackTrace()
        }
        return bitmap
    }
}
