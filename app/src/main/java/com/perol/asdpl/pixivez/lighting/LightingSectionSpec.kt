package com.perol.asdpl.pixivez.lighting

import com.facebook.litho.annotations.State
import com.facebook.litho.sections.Children
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.annotations.GroupSectionSpec
import com.facebook.litho.sections.annotations.OnCreateChildren
import com.facebook.litho.sections.common.SingleComponentSection
import com.perol.asdpl.pixivez.responses.Illust


@GroupSectionSpec
object LightingSectionSpec {
    @OnCreateChildren
    fun onCreateChildren(c: SectionContext?, @State illusts: List<Illust>): Children {
        val builder = Children.create()
        for (i in illusts) {
            builder.child(
                SingleComponentSection.create(c)
                    .key(i.toString())
                    .component(LightingCard(i))
            )
        }
        return builder.build()
    }
}