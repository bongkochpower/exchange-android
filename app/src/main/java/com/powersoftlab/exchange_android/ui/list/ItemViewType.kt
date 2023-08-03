package com.powersoftlab.exchange_android.ui.list

open class ItemViewType(var type: Int)

class SpaceIVT : ItemViewType(ViewType.SPACE)

class LoadingIVT : ItemViewType(ViewType.LOAD_MORE)

class Item1IVT<T>(var data: T?, viewType: Int = ViewType.ITEM_1) : ItemViewType(viewType)

class Item2IVT<T>(var data: T?) : ItemViewType(ViewType.ITEM_2)

class Item3IVT<T>(var data: T?) : ItemViewType(ViewType.ITEM_3)

class Item4IVT<T>(var data: T?) : ItemViewType(ViewType.ITEM_4)

class Item5IVT<T>(var data: T?) : ItemViewType(ViewType.ITEM_5)