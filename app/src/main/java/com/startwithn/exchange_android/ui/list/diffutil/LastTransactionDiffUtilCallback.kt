package com.startwithn.exchange_android.ui.list.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.startwithn.exchange_android.model.response.TransactionsModel
import com.startwithn.exchange_android.model.response.UserModel

//class LastTransactionDiffUtilCallback(
//    var oldItemList: MutableList<TransactionsModel>,
//    var newItemList: MutableList<TransactionsModel>
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
//        return oldItem.createdAt == newItem.createdAt
//    }
//
//}