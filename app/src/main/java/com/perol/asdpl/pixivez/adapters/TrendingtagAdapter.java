package com.perol.asdpl.pixivez.adapters;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.perol.asdpl.pixivez.R;
import com.perol.asdpl.pixivez.responses.TrendingtagResponse;
import com.perol.asdpl.pixivez.services.GlideApp;

import java.util.List;


/**
 * Created by asdpl on 2018/2/20.
 */

public class TrendingtagAdapter extends BaseQuickAdapter<TrendingtagResponse.TrendTagsBean, BaseViewHolder> {

    public TrendingtagAdapter(int layoutResId, @Nullable List<TrendingtagResponse.TrendTagsBean> data) {
        super(layoutResId, data);

    }


    @Override
    protected void convert(BaseViewHolder helper, TrendingtagResponse.TrendTagsBean item) {
        helper.setText(R.id.textview_tag, item.getTag());
        ImageView imageView =(ImageView) helper.itemView.findViewById(R.id.imageview_trendingtag);
        GlideApp.with(imageView.getContext()).load(item.getIllust().getImage_urls().getSquare_medium()).placeholder(R.mipmap.ic_noimage).into(imageView);
    }


}
