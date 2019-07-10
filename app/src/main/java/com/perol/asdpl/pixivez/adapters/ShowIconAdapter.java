package com.perol.asdpl.pixivez.adapters;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.perol.asdpl.pixivez.R;

import java.util.List;

public class ShowIconAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {


    public ShowIconAdapter(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setImageResource(R.id.icon, item);
        helper.addOnClickListener(R.id.icon);
    }
}
