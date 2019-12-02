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

package com.perol.asdpl.pixivez.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.responses.BookMarkDetailResponse

class TagsAdapter(
    layoutResId: Int,
    data: List<BookMarkDetailResponse.BookmarkDetailBean.TagsBean>?
) :
    BaseQuickAdapter<BookMarkDetailResponse.BookmarkDetailBean.TagsBean, BaseViewHolder>(
        layoutResId,
        data
    ) {
    override fun convert(
        helper: BaseViewHolder,
        item: BookMarkDetailResponse.BookmarkDetailBean.TagsBean
    ) {
        helper.setText(R.id.textview_tag1, item.name)
            .setChecked(R.id.checkBox, item.isIs_registered)
            .setOnCheckedChangeListener(R.id.checkBox) { buttonView, isChecked ->
                item.isIs_registered = isChecked
            }
    }

}
