package com.startwithn.exchange_android.ui.list.adapter

import android.annotation.SuppressLint
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.ItemRvSpaceBinding
import com.startwithn.exchange_android.ext.handleStateLoading
import com.startwithn.exchange_android.ext.setItemSlideUp
import com.startwithn.exchange_android.ui.list.viewholder.ItemViewHolder
import com.startwithn.exchange_android.ui.list.Item1IVT
import com.startwithn.exchange_android.ui.list.ItemViewType
import com.startwithn.exchange_android.ui.list.LoadingStyleEnum
import com.startwithn.exchange_android.ui.list.SpaceIVT
import com.startwithn.exchange_android.ui.list.ViewType
import com.startwithn.exchange_android.ui.list.createBannerLoadingViewHolder
import com.startwithn.exchange_android.ui.list.createLoadingViewHolder

class SimpleRecyclerViewAdapter<T, B : ViewDataBinding>(
    @LayoutRes private val layout: Int,
    private val itemViewType: Int = ViewType.ITEM_1,
    private val isRunAnimation: Boolean = false,
    private val isInfinite: Boolean = false /*for banner*/,
    private val loadingStyleEnum: LoadingStyleEnum = LoadingStyleEnum.PROGRESS
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isLoading = false
        set(value) {
            field = handleStateLoading(mList, value)
        }

    private var mList: MutableList<ItemViewType> = mutableListOf()

    private var mViewCallBack: ((binding: B, item: T?, position: Int) -> Unit)? = null
    private var mLoadingCallBack: ((holder: RecyclerView.ViewHolder, position: Int) -> Unit)? = null

    //for animation
    private var mLastPosition = 0

    private lateinit var mRecyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.mRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemViewType -> {
                val binding = DataBindingUtil.inflate<B>(
                    LayoutInflater.from(parent.context),
                    layout,
                    parent,
                    false
                )
                ItemViewHolder<B>(binding)
            }

            ViewType.LOAD_MORE -> {
                when (loadingStyleEnum) {
                    LoadingStyleEnum.SK_BANNER -> {
                        parent.createBannerLoadingViewHolder()
                    }
                    else -> {
                        parent.createLoadingViewHolder()
                    }
                }
            }

            ViewType.SPACE -> {
                val binding: ItemRvSpaceBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_rv_space,
                    parent,
                    false
                )
                ItemViewHolder(binding)
            }

            else -> throw NullPointerException("ViewType not match $viewType")
        }
    }

    override fun getItemCount(): Int {
        return if (isInfinite) {
            if (mList.size > 0) {
                Integer.MAX_VALUE
            } else {
                0
            }
        } else {
            mList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
//        return mList[position].type
        return if (isInfinite) {
            mList[position % mList.size].type
        } else {
            mList[position].type
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mList.isNotEmpty()) {

            var actualPosition = position
            if (isInfinite) {
                actualPosition = position % mList.size
            }

            when (holder.itemViewType) {
                itemViewType -> {
                    bindView(
                        holder as ItemViewHolder<B>,
                        mList[actualPosition] as Item1IVT<T>,
                        actualPosition
                    )
                    runAnimation(holder.itemView, actualPosition, mLastPosition)
                }

                ViewType.LOAD_MORE -> {
                    mLoadingCallBack?.invoke(holder, actualPosition)
                }
            }
        }
    }

    private fun bindView(holder: ItemViewHolder<B>, item: Item1IVT<T>, position: Int) {
        val binding = holder.binding
        mViewCallBack?.invoke(binding, item.data, position)
    }

    private fun runAnimation(view: View, position: Int, lastPosition: Int) {
        if (isRunAnimation) {
            mLastPosition =
                mRecyclerView.setItemSlideUp(view, position, lastPosition)
        }
    }

    private fun convertList(list: MutableList<T>): MutableList<ItemViewType> {
        return list.map { Item1IVT(it, itemViewType) }.toMutableList()
    }

    /*add*/
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(isRefresh: Boolean, newList: MutableList<T>) {
        removeBottomSpace()
        if (isRefresh) {
            this.mList.removeAll { it.type == itemViewType }
        }

        this.mList.addAll(convertList(newList))
        notifyDataSetChanged()
    }

    fun add(position: Int? = null, item: T) {
        removeBottomSpace()
        if (position != null) {
            this.mList.add(position, Item1IVT(item, itemViewType))
            notifyDataSetChanged()
        } else {
            this.mList.add(Item1IVT(item, itemViewType))
            notifyItemInserted(itemCount)
        }
    }

    fun addList(position: Int? = null, newList: MutableList<T>) {
        removeBottomSpace()
        if (position != null) {
            this.mList.addAll(position, convertList(newList))
        } else {
            this.mList.addAll(convertList(newList))
        }
        notifyDataSetChanged()
    }

    fun addBottomSpace() {
        val spaceView = mList.find { it is SpaceIVT }
        if (spaceView == null) {
            mList.add(SpaceIVT())
            notifyItemInserted(itemCount)
        }
    }

    private fun removeBottomSpace() {
        val spaceView = mList.find { it is SpaceIVT }
        if (spaceView != null) {
            val index = mList.indexOf(spaceView)
            mList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    /*update*/
    @SuppressLint("NotifyDataSetChanged")
    fun update(isUpdateAll: Boolean, item: T, position: Int) {
        this.mList[position] = Item1IVT(item)
        if (!isUpdateAll) {
            notifyItemChanged(position)
        } else {
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(updateList: MutableList<T>, isNotifyDataSetChanged: Boolean = false) {
        removeBottomSpace()
        this.mList = convertList(updateList)
        if (isNotifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /*get*/
    fun get(position: Int): ItemViewType {
        return mList[position]
    }

    fun getAll(): MutableList<ItemViewType> {
        return this.mList
    }

    fun getOriginalList(): MutableList<T> =
        this.mList.filterIsInstance<Item1IVT<T>>().map { item1IVT -> item1IVT.data!! }
            .toMutableList()


    fun getBinding(position: Int): B? {
        return DataBindingUtil.bind(mRecyclerView.getChildAt(position))
    }

    /*remove*/
    fun remove(position: Int) {
        this.mList.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        this.mList = mutableListOf()
        notifyDataSetChanged()
    }

    /*bind*/
    fun onBindView(cb: (binding: B, item: T?, position: Int) -> Unit) {
        this.mViewCallBack = cb
    }

    fun onBindLoading(cb: (holder: RecyclerView.ViewHolder, position: Int) -> Unit) {
        this.mLoadingCallBack = cb
    }

}