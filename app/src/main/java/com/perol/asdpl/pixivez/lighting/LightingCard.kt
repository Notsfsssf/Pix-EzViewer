package com.perol.asdpl.pixivez.lighting

import com.facebook.litho.*
import com.facebook.litho.widget.Card
import com.facebook.litho.widget.Image
import com.facebook.litho.widget.Text
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.responses.Illust

class LightingCard(var illust: Illust) : KComponent({
    val isFavourite by useState { false }
    val star =
        drawableRes(if (isFavourite.value) R.drawable.heart_red else R.drawable.ic_action_heart)
    Card {
        Column {
            GlideImage.create(this@Card).imageUrl(illust.image_urls.medium)
            Row {
                Column {
                    Text(illust.title, textSize = 16.sp)
                    Text(illust.user.name, textSize = 14.sp)
                }
                Image(star!!)
            }

        }
    }
})