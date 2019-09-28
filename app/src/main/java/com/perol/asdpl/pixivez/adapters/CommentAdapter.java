package com.perol.asdpl.pixivez.adapters;

import android.content.Context;
import androidx.annotation.Nullable;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.perol.asdpl.pixivez.R;
import com.perol.asdpl.pixivez.responses.IllustCommentsResponse;
import com.perol.asdpl.pixivez.services.GlideApp;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by asdpl on 2018/2/15.
 */

public class CommentAdapter extends BaseQuickAdapter<IllustCommentsResponse.CommentsBean,BaseViewHolder> {

private  Context context;
    public CommentAdapter(int layoutResId, @Nullable List<IllustCommentsResponse.CommentsBean> data,Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, IllustCommentsResponse.CommentsBean item) {
        helper.addOnClickListener(R.id.commentuserimage);
        helper.addOnClickListener(R.id.repley_to_hit);
        helper.setText(R.id.commentdate,item.getDate());
if (item.getParent_comment().getUser()!=null)
        helper.setText(R.id.commentusername,item.getUser().getName()+" to "+item.getParent_comment().getUser().getName());
else  helper.setText(R.id.commentusername,item.getUser().getName());
        helper.setText(R.id.commentdetail,item.getComment());
        if (!item.getUser().getProfile_image_urls().getMedium().contentEquals("https://source.pixiv.net/common/images/no_profile.png")) {
            GlideApp.with(context).load(item.getUser().getProfile_image_urls().getMedium()).placeholder(R.mipmap.ic_noimage_foreground).circleCrop().into((ImageView) helper.getView(R.id.commentuserimage));
        }
        else GlideApp.with(context).load(R.mipmap.ic_noimage_round).into((ImageView) helper.getView(R.id.commentuserimage));
    }
}
