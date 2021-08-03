package com.example.scoo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

public class ImageLoaderTwo {
    //内存缓存
    val mImageCache : ImageCache = ImageCache()
    //sd 卡缓存
    val mDiskCache : DiskCache = DiskCache()
    //是否使用sdka缓存
    var isUseDiskCache : Boolean = false

    //线程池，线程数量为cpu的数量
    var mExecutorService : ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

   fun displayImage(url : String,imageView : ImageView) {
       //判断使用哪种缓存
       var bitmap : Bitmap = if (isUseDiskCache) mDiskCache.get(url) else mImageCache.get(url)
       if (bitmap != null) {
           imageView.setImageBitmap(bitmap)
           return
       }
       imageView.tag = url
       mExecutorService.submit(object : Runnable {
           override fun run() {
               TODO("Not yet implemented")
               var bitmap : Bitmap? = downloadImage(url) ?: return
               if (imageView.tag.equals(url)) {
                   imageView.setImageBitmap(bitmap)
               }
               if (bitmap != null) {
                   mImageCache.put(url,bitmap)
               }
           }
       })
   }

    fun downloadImage(imageUrl : String) : Bitmap? {
        var bitmap : Bitmap? = null;
        try {
            val url : URL = URL(imageUrl)
            val conn : HttpURLConnection = url.openConnection() as HttpURLConnection
            bitmap = BitmapFactory.decodeStream(conn.inputStream)
            conn.disconnect()
        }catch (e : Exception){
            e.printStackTrace()
        }
        return bitmap
    }

    fun useDiskCache(isuseDiskCache: Boolean) {
        this.isUseDiskCache = isuseDiskCache
    }
}