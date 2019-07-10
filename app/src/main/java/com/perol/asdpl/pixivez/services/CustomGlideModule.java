package com.perol.asdpl.pixivez.services;


import android.content.Context;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.perol.asdpl.pixivez.networks.OkHttpUrlLoader;

import java.io.InputStream;


/**
 * Created by asdpl on 2018/2/23.
 */
@GlideModule
public class CustomGlideModule extends AppGlideModule {

//    @Override
//    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
//        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
//                .setMemoryCacheScreens(2)
//                .build();
//        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
//        MemorySizeCalculator calculator2 = new MemorySizeCalculator.Builder(context)
//                .setBitmapPoolScreens(3)
//                .build();
//        builder.setBitmapPool(new LruBitmapPool(calculator2.getBitmapPoolSize()));
//
//    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide,
                                   @NonNull Registry registry) {
        registry.prepend(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}
