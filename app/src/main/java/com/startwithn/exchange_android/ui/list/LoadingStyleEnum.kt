package com.startwithn.exchange_android.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.startwithn.exchange_android.ui.list.viewholder.ItemViewHolder
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.ItemRvLoadingBinding
import com.startwithn.exchange_android.databinding.ItemSkBalanceBinding
import com.startwithn.exchange_android.databinding.ItemSkBannerBinding
import com.startwithn.exchange_android.databinding.ItemSkTransactionBinding

enum class LoadingStyleEnum {
    PROGRESS,
    SK_BANNER,
    SK_BALANCE,
    SK_TRANSACTION;
}

fun ViewGroup.createLoadingViewHolder(): ItemViewHolder<ItemRvLoadingBinding> {
    val binding: ItemRvLoadingBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_rv_loading, this, false)
    return ItemViewHolder(binding)
}

fun ViewGroup.createBannerLoadingViewHolder(): ItemViewHolder<ItemSkBannerBinding> {
    val binding: ItemSkBannerBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_sk_banner, this, false)
    return ItemViewHolder(binding)
}

fun ViewGroup.createBalanceLoadingViewHolder(): ItemViewHolder<ItemSkBalanceBinding> {
    val binding: ItemSkBalanceBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_sk_balance, this, false)
    return ItemViewHolder(binding)
}

fun ViewGroup.createTransactionLoadingViewHolder(): ItemViewHolder<ItemSkTransactionBinding> {
    val binding: ItemSkTransactionBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_sk_transaction, this, false)
    return ItemViewHolder(binding)
}

