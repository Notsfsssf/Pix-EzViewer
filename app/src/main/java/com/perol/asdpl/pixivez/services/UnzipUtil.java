package com.perol.asdpl.pixivez.services;

import android.util.Log;

import net.lingala.zip4j.ZipFile;

import java.io.File;


/**
 * Created by Notsfsssf on 2018/3/27.
 */

public class UnzipUtil {
    public static final String TAG="ZIP";
    public UnzipUtil(){

    }

    public static void UnZipFolder(File file, String outPathString) throws Exception {
        new ZipFile(file).extractAll(outPathString);

    }

}
