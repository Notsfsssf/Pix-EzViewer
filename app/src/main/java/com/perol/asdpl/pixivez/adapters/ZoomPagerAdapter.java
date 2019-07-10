package com.perol.asdpl.pixivez.adapters;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.perol.asdpl.pixivez.R;
import com.perol.asdpl.pixivez.services.GlideApp;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Notsfsssf on 2018/3/15.
 */

public class ZoomPagerAdapter extends PagerAdapter {
    private ArrayList<String> arrayList;
    private Context context;

    public ZoomPagerAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_pager_zoom, null, false);
        final SubsamplingScaleImageView photoView = view.findViewById(R.id.photoview_zoom);
        photoView.setEnabled(true);

        GlideApp.with(context).asFile().load(arrayList.get(position)).into(
                new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull final File resource, @Nullable Transition<? super File> transition) {
                        photoView.setImage(ImageSource.uri(Uri.fromFile(resource)));

                    }
                });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
