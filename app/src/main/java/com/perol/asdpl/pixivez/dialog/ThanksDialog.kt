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

package com.perol.asdpl.pixivez.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.ThanksAdapter

class ThanksDialog : DialogFragment() {
    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, "ThanksDialogFragment")
    }

    val array = listOf(
        "Leajen",
        "欣殿",
        "风间仓平",
        "张孟航",
        "给",
        "傲天",
        "折翼天使",
        "不见",
        "freecloud",
        "炫酷的云宝",
        "不会系鞋带的柴犬",
        "得年",
        "猫铳",
        "Cyue",
        "张维杰",
        "清月",
        "黑色阳光",
        "DJMonet",
        "怡桧",
        "Fluency",
        "轻风拂水光",
        "千岛之间",
        "夏",
        "妄二郎",
        "繁华—落幕",
        "二十面体",
        "手抖了?",
        "VIP",
        "通梓",
        "大为",
        "帅千",
        "蕉迟但到",
        "槿",
        "桃花煮雨",
        "毛巾",
        "洋",
        "吾姓朱",
        "昕",
        "他说他是",
        "tra",
        "雨嘉",
        "○+●",
        "浅梦灬深海",
        "mxd",
        "劲",
        "龙战八荒",
        "长歌行",
        "墨黑",
        "晨凯",
        "空想 ",
        "superstarcar",
        "懿",
        "鹏飞",
        "六焰飞鱼",
        "QAQ",
        "夏天",
        "Ag",
        "野",
        "小威",
        "Mercis",
        "LIMBO",
        "VIG曦遥",
        "撸猫",
        "z7ui",
        "*焯森",
        "林",
        "幽魂小瑾",
        "Vincentzyx",
        "WJM",
        "奈",
        "Artorius",
        "moreki",
        "布鲁斯赧",
        "一只贱贱的熊",
        "翱",
        "DYCxhn",
        "圈",
        "indigle",
        "m*6",
        "*了",
        "*蛙",
        "NATSU",
        "SunREVEN",
        "Keter",
        "程岚",
        "略略略",
        "志毅",
        "墨烯",
        "清DU欢",
        "f*i",
        "*永健",
        "*恒",
        "*泽",
        "玄冰封月",
        "*念",
        "*宇",
        "*外",
        "*魔",
        "*熙",
        "*恒",
        "*迹",
        "路过的你",
        "皮皮弛",
        "*萍",
        "?",
        "韦涛涛",
        "?",
        "*文亮",
        "不可思议的世界",
        "*",
        "l*c",
        "D*h",
        " *柄",
        "s*r",
        "o*n(lightinkqian)",
        "天辰",
        "青楼自管弦",
        "不可思议的世界",
        "*文亮",
        "?",
        "怎么就没电了",
        "*色",
        "*e",
        "*喵",
        "不屈意志",
        "alchimie",
        "なつき",
        "灏",
        "猛犸牙",
        "404",
        "横竖都是二",
        "平 安",
        "zwk",
        "*",
        "*子",
        "*机",
        "*环",
        "浪*a",
        "M*Q",
        "智聪",
        "6TBWhite",
        " Stardust",
        "哈哈",
        "晏子敬",
        "君不知",
        "avileng",
        "那你可zun棒哦",
        "小杸",
        "恩文",
        "X*N",
        "*吧",
        "*弦",
        "I*",
        "*月",
        "C*n",
        "*军",
        "*糕",
        "*坑",
        "*白",
        "*梦",
        "*罗",
        "*格",
        "*.",
        "*蒂",
        "S*H",
        "*喵",
        "*l",
        "*莲",
        "Nero",
        "*琦公",
        "明杰",
        "*志强",
        "勒紧裤腰",
        "*泽鸿",
        "bilibili村长",
        "*承",
        "*",
        "*雷",
        "d*t",
        "*喵",
        ":*|",
        "辰",
        "*子予",
        "志鹏",
        "2az",
        "Myth",
        "Namelost",
        "韵秋",
        "卷柏泡茶",
        "闪耀的shining",
        "相信自己",
        "钰"
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_thanks, null)
        val re = view.findViewById<RecyclerView>(R.id.list)
        re.adapter = ThanksAdapter(R.layout.simple_list_item, array).apply {
            setHeaderView(inflater.inflate(R.layout.dialog_thanks_header, null))
        }
        re.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        builder.setView(view)
        return builder.create()
    }
}