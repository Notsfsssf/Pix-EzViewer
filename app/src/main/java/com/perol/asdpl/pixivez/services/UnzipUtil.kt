package com.perol.asdpl.pixivez.services

import android.util.Log

import net.lingala.zip4j.ZipFile

import java.io.File


/**
 * Created by Notsfsssf on 2018/3/27.
 */

class UnzipUtil {
    companion object {
        val TAG = "ZIP"

        @Throws(Exception::class)
        fun UnZipFolder(file: File, outPathString: String) {
            ZipFile(file).extractAll(outPathString)
        }
    }

}
