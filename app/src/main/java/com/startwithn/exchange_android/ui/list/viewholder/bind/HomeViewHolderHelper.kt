//package com.feyverly.mvip.presentation.list.viewholder.bind
//
//import android.app.Activity
//import android.content.Context
//import android.widget.Toast
//import androidx.core.view.isVisible
//import androidx.lifecycle.Lifecycle
//import com.feyverly.mvip.R
//import com.startwithn.exchange_android.ext.loadImage
//import com.startwithn.exchange_android.ext.setItemPadding
//import com.startwithn.exchange_android.ext.setOnTouchAnimation
//import com.feyverly.mvvm.common.manager.ViewPagerManager
//import com.feyverly.mvvm.common.manager.ExoPlayerManager
//import com.feyverly.mvvm.common.model.FakeModel
//import com.feyverly.mvip.core_data.model.response.banner.BannerModel
//import com.feyverly.mvip.core_data.model.response.news.NewsDetailModel
//import com.feyverly.mvip.core_data.model.response.news.NewsModel
//import com.feyverly.mvip.core_data.model.response.promotion.PromotionDetailModel
//import com.feyverly.mvip.core_data.model.response.promotion.PromotionModel
//import com.feyverly.mvip.databinding.*
//import com.startwithn.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter
//import com.startwithn.exchange_android.ui.list.itemdecoration.EqualSpacingItemDecoration
//import com.feyverly.mvvm.ui.navigator.AppNavigator
//
//object HomeViewHolderHelper {
//
//    /*TODO support vdo*/
//    fun SimpleRecyclerViewAdapter<MutableList<BannerModel>, ItemRvHomeBannerBinding>.initHomeBanner(
//        context: Context,
//        lifecycle: Lifecycle
//    ) {
//        onBindView { binding, item, position ->
//            with(binding) {
//                if (!item.isNullOrEmpty()) {
//                    /*initial adapter*/
//                    val bannerAdapter =
//                        SimpleRecyclerViewAdapter<BannerModel, ItemRvBannerBinding>(
//                            layout = R.layout.item_rv_banner,
//                            isInfinite = true
//                        )
//                    bannerAdapter.onBindView { binding, item, position ->
//                        with(binding) {
//                            item?.let {
//
//                                ivBanner.isVisible = false
//                                playerView.isVisible = false
//
//                                /*TODO check image or vdo*/
//                                val type = "image"
//
//                                when (BannerModeEnum.from(type)) {
//                                    BannerModeEnum.IMAGE -> {
//                                        ivBanner.isVisible = true
//                                        ivBanner.loadImage(item.mediaPath)
//                                    }
//                                    BannerModeEnum.VDO -> {
//                                        playerView.isVisible = true
//                                        /*mock vdo url*/
//                                        val vdoUrl = FakeModel.getVideo()
//                                        val exoPlayerManager =
//                                            ExoPlayerManager(lifecycle, playerView)
//                                        exoPlayerManager.initializePlayer(
//                                            url = vdoUrl,
//                                            isPlayWhenReady = false,
//                                            isRepeat = true
//                                        )
//                                    }
//                                }
//
//                                root.setOnClickListener {
//                                    if (BannerModeEnum.from(type) == BannerModeEnum.VDO) {
//                                        val isPlaying: Boolean =
//                                            playerView.player?.isPlaying ?: false
//                                        if (isPlaying) {
//                                            playerView.player?.pause()
//                                        } else {
//                                            playerView.player?.play()
//                                        }
//                                    } else {
//                                        /*TODO check action type*/
////                                AppNavigator(context as Activity).goToNewsDetail(id)
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    bannerAdapter.submitList(true, item)
//
//                    /*apply viewpager & indicator*/
//                    viewPager.apply {
//                        adapter = bannerAdapter
//                        offscreenPageLimit = item.size
//                    }
////                    indicator.count = item.size
//
//                    var lastPosition = 0
////                    isAutoScroll = false,
//                    ViewPagerManager(
//                        lifecycle = lifecycle,
//                        viewPager = viewPager,
//                        totalSize = item.size,
//                        indicator = indicator,
//                        isAutoScroll = true,
//                        isInfinite = true
//                    ).start(
//                        onPageSelectedCallback = { position ->
//                            try {
//                                /*pause*/
//                                if (lastPosition != position) {
//                                    bannerAdapter.getBinding(lastPosition)?.playerView?.player?.pause()
//                                }
//                                lastPosition = position
//
////                                /*resume*/
////                                /*TODO check current position is image or vdo*/
////                                var type = "image"
//////                                if (position % 2 == 0) {
//////                                    type = "vdo"
//////                                }
////                                if (BannerModeEnum.from(type) == BannerModeEnum.VDO) {
////                                    bannerAdapter.getBinding(position)?.playerView?.player?.play()
////                                }
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }
//
//    fun SimpleRecyclerViewAdapter<MutableList<PromotionModel>, ItemRvHomePromotionBinding>.initHomePromotion(
//        context: Context
//    ) {
//        onBindView { binding, item, position ->
//            with(binding) {
//                item?.let {
//                    /*navigator*/
//                    groupNavigator.setNavigatorOnClickListener {
//                        AppNavigator(context as Activity).goToPromotion()
//                    }
//
//                    /*preview list*/
//                    val itemAdapter =
//                        SimpleRecyclerViewAdapter<PromotionModel, ItemRvPreviewPromotionBinding>(
//                            layout = R.layout.item_rv_preview_promotion
//                        )
//                    itemAdapter.onBindView { binding, item, position ->
//                        with(binding) {
//                            item?.let {
//                                val id = item.id ?: 0
//                                val imageUrl = item.imagePath
//                                val title = item.headlines
//
//                                ivBanner.loadImage(imageUrl)
//                                tvTitle.text = title
//
//                                root.apply {
//                                    setOnClickListener {
//                                        AppNavigator(context as Activity).goToPromotionDetail(
//                                            PromotionDetailModel.map(item)
//                                        )
//                                    }
//                                    setOnTouchAnimation()
//                                }
//                            }
//                        }
//                    }
//                    recyclerView.apply {
//                        adapter = itemAdapter
//                        setItemPadding(12f, EqualSpacingItemDecoration.INNER_HORIZONTAL)
//                    }
//                    itemAdapter.submitList(true, item)
//                }
//            }
//        }
//    }
//
//
//    fun SimpleRecyclerViewAdapter<MutableList<NewsModel>, ItemRvHomeNewsBinding>.initHomeNews(
//        context: Context
//    ) {
//        onBindView { binding, item, position ->
//            with(binding) {
//                item?.let {
//                    /*navigator*/
//                    groupNavigator.setNavigatorOnClickListener {
//                        AppNavigator(context as Activity).goToNews()
//                    }
//
//                    /*highlight*/
//                    val newsHighlightModel = item[0]
////                    val highlightId = newsHighlightModel.id ?: 0
//                    val imageUrl = newsHighlightModel.imagePath
//
//                    ivHighlight.loadImage(imageUrl)
//                    cvHighlight.apply {
//                        setOnClickListener {
//                            AppNavigator(context as Activity).goToNewsDetail(
//                                NewsDetailModel.map(
//                                    newsHighlightModel
//                                )
//                            )
//                        }
//                        setOnTouchAnimation()
//                    }
//
//                    /*preview list*/
//                    val itemAdapter =
//                        SimpleRecyclerViewAdapter<NewsModel, ItemRvPreviewNewsBinding>(layout = R.layout.item_rv_preview_news)
//                    itemAdapter.onBindView { binding, item, position ->
//                        with(binding) {
//                            item?.let {
////                                val id = item.id ?: 0
//                                val imagePreviewUrl = item.imagePath
//
//                                ivBanner.loadImage(imagePreviewUrl)
//                                root.apply {
//                                    setOnClickListener {
//                                        AppNavigator(context as Activity).goToNewsDetail(
//                                            NewsDetailModel.map(item)
//                                        )
//                                    }
//                                    setOnTouchAnimation()
//                                }
//                            }
//                        }
//                    }
//                    recyclerView.apply {
//                        adapter = itemAdapter
//                        setItemPadding(12f, EqualSpacingItemDecoration.INNER_HORIZONTAL)
//                    }
//                    itemAdapter.submitList(true, item.drop(1).toMutableList())
//                }
//            }
//        }
//    }
//
//    /*TODO*/
//    fun SimpleRecyclerViewAdapter<String, ItemRvHomePromotionBinding>.initHomeGame(context: Context) {
//        onBindView { binding, item, position ->
//            with(binding) {
//                item?.let {
//                    /*navigator*/
//                    groupNavigator.setTitle(context.getString(R.string.title_new_game))
//                    groupNavigator.setNavigatorOnClickListener {
//                        AppNavigator(context as Activity).goToGame()
//                    }
//
//                    /*preview list*/
//                    val itemAdapter =
//                        SimpleRecyclerViewAdapter<String, ItemRvPreviewPromotionBinding>(layout = R.layout.item_rv_preview_promotion)
//                    itemAdapter.onBindView { binding, item, position ->
//                        with(binding) {
//                            /*mock*/
//                            val id = 0
//                            val imageUrl = FakeModel.getCover()
//                            val title = "Game $id"
//                            val url =
//                                "https://feyverly.com/crm/?gclid=EAIaIQobChMIpOnXz46Y9gIVxDUrCh18cQxoEAAYASAAEgIVjvD_BwE"
//
//                            ivBanner.loadImage(imageUrl)
//                            tvTitle.text = title
//
//                            root.apply {
//                                setOnClickListener {
//                                    /*TODO game detail*/
//                                    AppNavigator(context as Activity).goToInternalWebView(
//                                        url,
//                                        title
//                                    )
//                                }
//                                setOnTouchAnimation()
//                            }
//                        }
//                    }
//
//                    recyclerView.apply {
//                        adapter = itemAdapter
//                        setItemPadding(12f, EqualSpacingItemDecoration.INNER_HORIZONTAL)
//                    }
//                    /*mock*/
//                    itemAdapter.submitList(true, mutableListOf("", "", ""))
//                }
//            }
//        }
//    }
//
//    /*TODO*/
//    fun SimpleRecyclerViewAdapter<String, ItemRvHomeProductBinding>.initHomeProduct(context: Context) {
//        onBindView { binding, item, position ->
//            with(binding) {
//
//                groupNavigator.setNavigatorOnClickListener {
//                    Toast.makeText(
//                        context,
//                        context.getString(R.string.message_coming_soon),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                item?.let {
//                    /*preview list*/
//                    val itemAdapter =
//                        SimpleRecyclerViewAdapter<String, ItemRvPreviewProductBinding>(layout = R.layout.item_rv_preview_product)
//                    itemAdapter.onBindView { binding, item, position ->
//                        with(binding) {
//                            /*mock*/
//                            val imageUrl = FakeModel.getCover()
//                            val title = FakeModel.getLoremMessage()
//
//                            ivBanner.loadImage(imageUrl)
//                            tvTitle.text = title
//
//                            root.apply {
//                                setOnClickListener {
//                                    /*TODO go to product detail*/
//                                }
//                                setOnTouchAnimation()
//                            }
//                        }
//                    }
//
//                    recyclerView.apply {
//                        adapter = itemAdapter
//                        setItemPadding(12f, EqualSpacingItemDecoration.INNER_GRID_TWO_SPAN_COUNT)
//                    }
//                    /*mock*/
//                    itemAdapter.submitList(true, mutableListOf("", "", "", ""))
//                }
//            }
//        }
//    }
//}