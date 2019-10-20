/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.adapters;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.perol.asdpl.pixivez.R;
import com.perol.asdpl.pixivez.responses.IllustCommentsResponse;
import com.perol.asdpl.pixivez.services.GlideApp;

import java.util.List;


public class CommentAdapter extends BaseQuickAdapter<IllustCommentsResponse.CommentsBean, BaseViewHolder> {

    private Context context;

    public CommentAdapter(int layoutResId, @Nullable List<IllustCommentsResponse.CommentsBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, IllustCommentsResponse.CommentsBean item) {
        helper.addOnClickListener(R.id.commentuserimage);
        helper.addOnClickListener(R.id.repley_to_hit);
        helper.setText(R.id.commentdate, item.getDate());
        if (item.getParent_comment().getUser() != null)
            helper.setText(R.id.commentusername, item.getUser().getName() + " to " + item.getParent_comment().getUser().getName());
        else helper.setText(R.id.commentusername, item.getUser().getName());
        helper.setText(R.id.commentdetail, item.getComment());
        if (!item.getUser().getProfile_image_urls().getMedium().contentEquals("https://source.pixiv.net/common/images/no_profile.png")) {
            GlideApp.with(context).load(item.getUser().getProfile_image_urls().getMedium()).placeholder(R.mipmap.ic_noimage_foreground).circleCrop().into((ImageView) helper.getView(R.id.commentuserimage));
        } else
            GlideApp.with(context).load(R.mipmap.ic_noimage_round).into((ImageView) helper.getView(R.id.commentuserimage));
    }
}
