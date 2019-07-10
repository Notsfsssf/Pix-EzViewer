package com.perol.asdpl.pixivez.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import com.perol.asdpl.pixivez.R;
import com.perol.asdpl.pixivez.objects.Toasty;
import com.perol.asdpl.pixivez.responses.IllustBean;
import com.perol.asdpl.pixivez.responses.UgoiraMetadataResponse;
import com.perol.asdpl.pixivez.services.AnimatedGifEncoder;
import com.perol.asdpl.pixivez.services.GlideApp;
import com.perol.asdpl.pixivez.services.PxEZApp;
import com.waynejo.androidndkgif.GifEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Notsfsssf on 2018/3/27.
 */

public class GifAdapter extends BaseQuickAdapter<File, BaseViewHolder> {
    private UgoiraMetadataResponse data;

    private File file;
    private String path, path2;

    public GifAdapter(int layoutResId, @Nullable List<File> data, UgoiraMetadataResponse ugoria, IllustBean large, Context applicationContext) {
        super(layoutResId, data);
        this.data = ugoria;
        this.path2 = PxEZApp.getStorepath() + "/" + large.getId() + ".gif";
        this.path = PxEZApp.instance.getCacheDir() + "/" + large.getId() + ".gif";
    }


    //    @Override
//    protected void convert(BaseViewHolder helper, AnimationDrawable item) {
////        ImageView imageView = helper.itemView.findViewById(R.id.imagelarge);
////
////        imageView.setImageDrawable(item);
////        helper.addOnClickListener(R.id.imagelarge);
//
//    }


    private void createGif(int delay) {
        File file1 = new File(path);
        if (!file1.exists()) {
            try {
                if (!file1.getParentFile().exists()) {
                    file1.getParentFile().mkdirs();
                }
                file1.createNewFile();

            } catch (Exception e) {
            }

        }
        try {
            ndkgifgenerator(delay);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void ndkgifgenerator(int delay) throws FileNotFoundException, RuntimeException {
        GifEncoder gifEncoder = new GifEncoder();
        File[] fs = file.listFiles();
        List<File> pics = new ArrayList<>(Arrays.asList(fs));
        if (pics.isEmpty()) {
            throw new RuntimeException("unzip files not found");
        }
        if (pics.size() < data.getUgoira_metadata().getFrames().size()) {
            throw new RuntimeException("something wrong in manga files");
        }
        Collections.sort(pics, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (int i = 0; i < pics.size(); i++) {
            if (pics.get(i).isFile()) {
                Bitmap bitmap = BitmapFactory.decodeFile(pics.get(i).getAbsolutePath());
                if (bitmap == null) {
                    continue;
                }
                if (i == 0) {
                    gifEncoder.init(bitmap.getWidth(), bitmap.getHeight(), path, GifEncoder.EncodingType.ENCODING_TYPE_STABLE_HIGH_MEMORY);
                }
                gifEncoder.encodeFrame(bitmap, delay);
            }

        }
        gifEncoder.close();
        Log.d("d", "I'm ok???????????????????????????");

    }

    private void bilibiliGenerator(int delay) {
//        BurstLinker burstLinrker = new BurstLinker();
//        try{
////            burstLinker.init(500,500,file);
//        }
    }

//    private void anigenerater(int delay) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
//
//        localAnimatedGifEncoder.start(baos);//start
//        localAnimatedGifEncoder.setQuality(30);
////            localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放
//        localAnimatedGifEncoder.setDelay(delay);
//        localAnimatedGifEncoder.setQuality(30);
//        localAnimatedGifEncoder.setRepeat(0);
//        File[] fs = file.listFiles();
//        List<File> pics = new ArrayList<>(Arrays.asList(fs));
//        Collections.sort(pics, new Comparator<File>() {
//            @Override
//            public int compare(File o1, File o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        });
//        if (pics.isEmpty()) {
//            return;
//
//        } else {
//
//            for (int i = 0; i < pics.size(); i++) {
//                Bitmap bitmap = BitmapFactory.decodeFile(pics.get(i).getAbsolutePath());
//                localAnimatedGifEncoder.addFrame(bitmap);
//
//            }
//        }
//        localAnimatedGifEncoder.finish();//finish
//        try {
//            FileOutputStream fos = new FileOutputStream(path);
//            baos.writeTo(fos);
//            baos.flush();
//            fos.flush();
//            baos.close();
//            fos.close();
//
//        } catch (IOException e) {
//            Toast.makeText(context, "发生OOM，请清除缓存后重试合成" + e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }


    private boolean imready = false;

    @Override
    protected void convert(final BaseViewHolder helper, File item) {

        helper.addOnLongClickListener(R.id.imageview_gifx);
        this.file = item;

        final SpinKitView spinKitView = helper.getView(R.id.progressbar_loadingx);
        spinKitView.setVisibility(View.VISIBLE);
        final ImageView imageView = helper.getView(R.id.imageview_gifx);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (imready)
                    Snackbar.make(v, mContext.getString(R.string.saveselectpic), Snackbar.LENGTH_LONG).setAction("确认", new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            Observable.create(new ObservableOnSubscribe<Uri>() {
                                @Override
                                public void subscribe(ObservableEmitter<Uri> emitter) throws Exception {
                                    File file = new File(path);
                                    File file1 = new File(path2);
                                    if (!file1.getParentFile().exists()) {
                                        file1.getParentFile().mkdir();
                                    }
                                    if (!file1.exists()) {
                                        InputStream input;
                                        OutputStream output;
                                        input = new FileInputStream(file);
                                        output = new FileOutputStream(file1);
                                        byte[] buf = new byte[1024];
                                        int bytesRead;
                                        while ((bytesRead = input.read(buf)) > 0) {
                                            output.write(buf, 0, bytesRead);
                                        }
                                        input.close();
                                        output.close();
                                        Uri uri = Uri.fromFile(file1);
                                        emitter.onNext(uri);
                                    } else {
                                        emitter.onComplete();
                                    }
                                }
                            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<Uri>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(Uri uri) {
                                            Snackbar.make(v, "成功！", Snackbar.LENGTH_SHORT).show();
                                            PxEZApp.instance.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Snackbar.make(v, "保存失败，请清除缓存或更改保存路径后重试" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onComplete() {
                                            Snackbar.make(v, "动图已存在", Snackbar.LENGTH_SHORT).show();
                                        }
                                    });


                        }
                    }).show();
                return false;
            }
        });
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                createGif(data.getUgoira_metadata().getFrames().get(0).getDelay());
                emitter.onNext("d");
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            imready = true;
                            spinKitView.setVisibility(View.GONE);
                            GlideApp.with(imageView.getContext()).asGif().load(path).skipMemoryCache(true).into(imageView);
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.Companion.error(PxEZApp.instance,e.getMessage()+ mContext.getString(R.string.errorgif)).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


}
