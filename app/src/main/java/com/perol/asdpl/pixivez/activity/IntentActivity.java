package com.perol.asdpl.pixivez.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.perol.asdpl.pixivez.R;

public class IntentActivity extends RinkActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);
        Uri uri = getIntent().getData();
        if (uri != null) {
            String test1 = uri.getQueryParameter("illust_id");
            if (test1 != null) {
                Bundle bundle = new Bundle();
                long[] arrayList=new long[1];
                arrayList[arrayList.length-1]=Long.parseLong(test1);
                bundle.putLongArray("illustlist", arrayList);
                bundle.putLong("illustid", Long.parseLong(test1));
                Intent intent2 = new Intent(getApplicationContext(), PictureActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent2.putExtras(bundle);
                startActivity(intent2);
                finish();
            }
            String id = uri.getQueryParameter("id");
            if (id != null) {

                Intent intent1 = new Intent(getApplicationContext(), UserMActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("data",Long.parseLong(id));
                startActivity(intent1);
                finish();
            }
        }


    }


}
