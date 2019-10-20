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

package com.perol.asdpl.pixivez.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.perol.asdpl.pixivez.R;
import com.perol.asdpl.pixivez.adapters.ShowIconAdapter;

import java.util.ArrayList;
import java.util.List;

public class IconDialog extends DialogFragment {

    Button add;
    private int selectposition = 0;

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }

    public interface Callback {
        void onClick(int position);

    }

    public Callback callback;
    RecyclerView recyclerviewIcon;
    private View v;
    private Context content;

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "ViewDialogFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.icondialog, container, false);
        recyclerviewIcon = v.findViewById(R.id.recyclerview_icon);
        add = v.findViewById(R.id.add);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.content = getActivity().getApplicationContext();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.ic_launcher);
        list.add(R.mipmap.ic_launcherep);
        list.add(R.mipmap.ic_launchermd);
        ShowIconAdapter showIconAdapter = new ShowIconAdapter(R.layout.view_showicon_item, list);
        recyclerviewIcon.setLayoutManager(new LinearLayoutManager(content, RecyclerView.HORIZONTAL, false));
        recyclerviewIcon.setAdapter(showIconAdapter);
        showIconAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                selectposition = position;
                add.setText("更换为第" + (position + 1) + "个");
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callback != null) {
                    callback.onClick(selectposition);
                }
            }
        });
    }

}
