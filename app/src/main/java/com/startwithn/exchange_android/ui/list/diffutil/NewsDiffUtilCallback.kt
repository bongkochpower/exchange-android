//package com.feyverly.mvvm.ui.list.diffutil
//
//import androidx.recyclerview.widget.DiffUtil
//import com.feyverly.mvip.core_data.model.response.news.NewsModel
//
//class NewsDiffUtilCallback(
//    var oldItemList: MutableList<NewsModel>,
//    var newItemList: MutableList<NewsModel>
//) : DiffUtil.Callback() {
//    override fun getOldListSize(): Int = oldItemList.size
//
//    override fun getNewListSize(): Int = newItemList.size
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
//        oldItemList[oldItemPosition].id == newItemList[newItemPosition].id
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        val oldItem = oldItemList[oldItemPosition]
//        val newItem = newItemList[newItemPosition]
//        return oldItem.headlines == newItem.headlines
//                && oldItem.content == newItem.content
//                && oldItem.shortContent == newItem.shortContent
//    }
//
//}