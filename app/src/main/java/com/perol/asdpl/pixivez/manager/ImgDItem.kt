package com.perol.asdpl.pixivez.manager

import com.arialyy.aria.core.common.AbsEntity
import com.arialyy.aria.core.download.DownloadEntity
import com.facebook.litho.Column
import com.facebook.litho.KComponent
import com.facebook.litho.Padding
import com.facebook.litho.dp
import com.facebook.litho.widget.Text
import com.perol.asdpl.pixivez.responses.Illust

class ImgDItem(absEntity: DownloadEntity, illust: Illust) : KComponent({

    Padding(16.dp) {

        Text(illust.title)

    }
})