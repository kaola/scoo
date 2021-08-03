package com.example.scoo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class DiskCache {
    @JvmField
    val cacheDir : String = "sdcard/cache"

    fun get(url : String) : Bitmap{
        return BitmapFactory.decodeFile(cacheDir + url)
    }

    fun put(url : String,bmp : Bitmap) {
       var fileOutputStream : FileOutputStream? = null
        try {
            fileOutputStream  = FileOutputStream(cacheDir+url)
            bmp.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream)
        }catch (e : FileNotFoundException) {
            e.printStackTrace()
        }finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                }catch (e : IOException){
                    e.printStackTrace()
                }
            }
        }
    }

}