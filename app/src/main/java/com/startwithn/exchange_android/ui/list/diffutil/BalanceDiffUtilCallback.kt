package com.startwithn.exchange_android.ui.list.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.startwithn.exchange_android.model.response.UserModel

class BalanceDiffUtilCallback(
    var oldItemList: MutableList<UserModel.CustomerBalance>,
    var newItemList: MutableList<UserModel.CustomerBalance>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItemList.size

    override fun getNewListSize(): Int = newItemList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItemList[oldItemPosition].balance == newItemList[newItemPosition].balance

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemList[oldItemPosition]
        val newItem = newItemList[newItemPosition]
        return oldItem.balance == newItem.balance
    }

}