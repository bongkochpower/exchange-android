package com.startwithn.exchange_android.ui.list.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class EqualSpacingItemDecoration(private val spacing: Int, private var displayMode: Int = -1) :
    ItemDecoration() {

    companion object {
        /*general*/
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        const val GRID = 2

        /*only 2*/
        const val GRID_TWO_SPAN_COUNT = 3

        /*horizontal child half*/
        const val HORIZONTAL_INNER_HALF = 4

        /*no out padding (recyclerview inside)*/
        const val INNER_HORIZONTAL = 5
        const val INNER_VERTICAL = 6
        const val INNER_GRID_TWO_SPAN_COUNT = 7

        /*concat adapter*/
        const val CONCAT_ADAPTER_VERTICAL = 8

        /*reverse*/
        const val VERTICAL_REVERSE = 9
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildViewHolder(view).bindingAdapterPosition
        val itemCount = state.itemCount
        setSpacingForDirection(outRect, view, parent, position, itemCount)
    }

    private fun setSpacingForDirection(
        outRect: Rect,
        view: View,
        recyclerView: RecyclerView,
        position: Int,
        itemCount: Int
    ) {

        // Resolve display mode automatically
        if (displayMode == -1) {
            recyclerView.layoutManager?.let {
                displayMode = resolveDisplayMode(it)
            }
        }

        when (displayMode) {
            HORIZONTAL -> {
                outRect.left = spacing
                outRect.right = if (position == itemCount - 1) spacing else 0
            }
            VERTICAL -> {
                outRect.left = spacing
                outRect.right = spacing
                outRect.top = if (position == 0) spacing else spacing / 2
                outRect.bottom = if (position == itemCount - 1) spacing / 2 else spacing
            }
            GRID -> {
                val halfSpacing = spacing / 2
                recyclerView.setPadding(0, halfSpacing, 0, halfSpacing)
                outRect.left = halfSpacing
                outRect.right = halfSpacing
                outRect.bottom = halfSpacing
                outRect.top = halfSpacing
            }
            GRID_TWO_SPAN_COUNT -> {
                val halfSpacing = spacing / 2
                recyclerView.setPadding(0, halfSpacing, 0, halfSpacing)
                outRect.left = if (position % 2 == 0) spacing else halfSpacing
                outRect.right = if (position % 2 != 0) spacing else halfSpacing
                outRect.top = halfSpacing
                outRect.bottom = halfSpacing
            }
            HORIZONTAL_INNER_HALF -> {
                val halfSpacing = spacing / 2
                outRect.left = if (position == 0) spacing else halfSpacing
                outRect.right = if (position == itemCount - 1) spacing else 0
            }
            INNER_HORIZONTAL -> {
                outRect.left = if (position == 0) 0 else spacing
                outRect.right = 0
            }
            INNER_VERTICAL -> {
                outRect.top = if (position == 0) 0 else spacing
            }
            INNER_GRID_TWO_SPAN_COUNT -> {
                val halfSpacing = spacing / 2
                outRect.left = if (position % 2 == 0) 0 else halfSpacing
                outRect.right = if (position % 2 != 0) 0 else halfSpacing
                outRect.top = halfSpacing
                outRect.bottom = halfSpacing
            }
            CONCAT_ADAPTER_VERTICAL -> {
                outRect.left = spacing
                outRect.right = spacing
                outRect.top = spacing
                outRect.bottom = 0
            }
            VERTICAL_REVERSE -> {
                outRect.left = spacing
                outRect.right = spacing
                outRect.top = if (position == itemCount - 1) spacing * 2 else spacing
                outRect.bottom = if (position == 0) spacing * 2 else spacing
            }
        }
    }

    private fun resolveDisplayMode(layoutManager: RecyclerView.LayoutManager): Int {
        if (layoutManager is GridLayoutManager) return GRID
        return if (layoutManager.canScrollHorizontally()) HORIZONTAL else VERTICAL
    }
}