package com.perol.asdpl.pixivez.dialog;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.perol.asdpl.pixivez.R;
import com.perol.asdpl.pixivez.adapters.ShowIconAdapter;

import java.util.ArrayList;
import java.util.List;

public class IconDialog extends DialogFragment {

    Button add;
    private int selectposition=0;

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
        recyclerviewIcon=v.findViewById(R.id.recyclerview_icon);
        add=v.findViewById(R.id.add);
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
                add.setText("更换为第"+(position+1)+"个");
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
