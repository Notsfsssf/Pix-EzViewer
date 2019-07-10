package com.perol.asdpl.pixivez.adapters;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.perol.asdpl.pixivez.R;
import com.perol.asdpl.pixivez.responses.IllustsBean;
import com.perol.asdpl.pixivez.services.GlideApp;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by asdpl on 2018/2/27.
 */

public class UserSearchillustAdapter extends BaseQuickAdapter<IllustsBean,BaseViewHolder> {

    public UserSearchillustAdapter(int layoutResId, @Nullable List<IllustsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IllustsBean item) {
        ImageView imageView =(ImageView) helper.getView(R.id.imageview_usersearchillust);
        GlideApp.with(imageView.getContext()).load(item.getImage_urls().getSquare_medium()).transition(withCrossFade()).into(imageView);
    }
}
