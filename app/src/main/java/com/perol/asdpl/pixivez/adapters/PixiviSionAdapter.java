package com.perol.asdpl.pixivez.adapters;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.perol.asdpl.pixivez.R;
import com.perol.asdpl.pixivez.responses.SpotlightResponse;
import com.perol.asdpl.pixivez.services.GlideApp;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PixiviSionAdapter extends BaseQuickAdapter<SpotlightResponse.SpotlightArticlesBean, BaseViewHolder> {
    private Context context;

    public PixiviSionAdapter(int layoutResId, @Nullable List<SpotlightResponse.SpotlightArticlesBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
        this.openLoadAnimation(BaseQuickAdapter.SCALEIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpotlightResponse.SpotlightArticlesBean item) {
        helper.setText(R.id.textView__pixivision_title, item.getTitle()).addOnClickListener(R.id.imageView_pixivision);
        ImageView imageView = helper.getView(R.id.imageView_pixivision);

        GlideApp.with(context).load(item.getThumbnail()).transition(withCrossFade()).into(imageView);

    }
}
