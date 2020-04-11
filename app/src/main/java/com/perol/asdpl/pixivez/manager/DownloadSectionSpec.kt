package com.perol.asdpl.pixivez.manager

import com.facebook.litho.StateValue
import com.facebook.litho.annotations.OnCreateInitialState
import com.facebook.litho.annotations.OnUpdateState
import com.facebook.litho.annotations.Prop
import com.facebook.litho.annotations.State
import com.facebook.litho.sections.Children
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.annotations.GroupSectionSpec
import com.facebook.litho.sections.annotations.OnCreateChildren
import com.facebook.litho.sections.common.SingleComponentSection
import com.facebook.litho.widget.Text
import com.google.gson.Gson
import com.perol.asdpl.pixivez.responses.Illust

@GroupSectionSpec
object DownloadSectionSpec {
    @OnCreateInitialState
    fun onCreateInitialState(
        c: SectionContext,
        dataX: StateValue<List<Illust>>,
        @Prop d: List<Illust>
    ) {
        dataX.set(d)
    }

    @OnUpdateState
    fun updateDateState(dataX: StateValue<List<Illust>>) {
        dataX.set(dataX.get())
    }

    @OnCreateChildren
    fun onCreateChildren(c: SectionContext?, @State dataX: List<Illust>): Children {
        val builder = Children.create()
        for (i in dataX) {
            builder.child(
                SingleComponentSection.create(c)
                    .key(i.toString())
                    .component(Text.create(c))
            )
        }
        return builder.build()
    }
}