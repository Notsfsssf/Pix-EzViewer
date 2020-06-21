package com.perol.asdpl.pixivez.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class SpacesItemDecoration(private val space: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = space
        val position = parent.getChildAdapterPosition(view)
        if (position == 0 || position == 1) {
            outRect.top = space
        }
        val params =
            view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        if (params.spanIndex % 2 == 0){
            outRect.left = space
            outRect.right = space / 2
        } else  {
            outRect.left = space / 2
            outRect.right = space
        }
    }

}