package com.perol.asdpl.pixivez.manager;

import android.util.Log;

import com.arialyy.aria.core.download.DownloadEntity;
import com.facebook.litho.StateValue;
import com.facebook.litho.annotations.OnCreateInitialState;
import com.facebook.litho.annotations.OnUpdateState;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.State;
import com.facebook.litho.sections.Children;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.annotations.GroupSectionSpec;
import com.facebook.litho.sections.annotations.OnCreateChildren;
import com.facebook.litho.sections.common.SingleComponentSection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perol.asdpl.pixivez.responses.Illust;

import java.util.List;

@GroupSectionSpec
public class AriaItemSectionSpec {
    @OnCreateInitialState
    public static void onCreateInitialState(
            SectionContext c,
            StateValue<List<DownloadEntity>> data,
            @Prop List<DownloadEntity> d
    ) {
        data.set(d);
    }

    @OnUpdateState
    public static void updateDateState(StateValue<List<DownloadEntity>> data) {
        data.set(data.get());
    }

    @OnCreateChildren
    public static Children onCreateChildren(SectionContext c, @State List<DownloadEntity> data) {
        Children.Builder builder = Children.create();
        for (int i = 0; i < data.size(); i++) {
            DownloadEntity downloadEntity = data.get(i);
            Log.d("downloadEntity.getStr()", downloadEntity.getStr());
            ImgDItem imgDItem = new ImgDItem(downloadEntity, new GsonBuilder().create().fromJson(downloadEntity.getStr(), Illust.class));
            builder.child(
                    SingleComponentSection.create(c)
                            .key(downloadEntity.toString())
                            .component(imgDItem)
            );
        }

        return builder.build();
    }
}
