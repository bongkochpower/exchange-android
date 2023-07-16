package com.startwithn.exchange_android.ui.page.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.common.alert.AppAlert
import com.startwithn.exchange_android.common.navigator.AppNavigator
import com.startwithn.exchange_android.databinding.ActivityMainBinding
import com.startwithn.exchange_android.databinding.ItemRvBalanceBinding
import com.startwithn.exchange_android.databinding.ItemRvTransactionBinding
import com.startwithn.exchange_android.ext.logout
import com.startwithn.exchange_android.ext.setItemPadding
import com.startwithn.exchange_android.ext.setOnLoadMoreListener
import com.startwithn.exchange_android.model.response.TransactionsModel
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.ui.list.LoadingStyleEnum
import com.startwithn.exchange_android.ui.list.ViewType
import com.startwithn.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter
import com.startwithn.exchange_android.ui.list.diffutil.BalanceDiffUtilCallback
import com.startwithn.exchange_android.ui.list.itemdecoration.EqualSpacingItemDecoration
import com.startwithn.exchange_android.ui.list.viewholder.bind.MainViewHolderHelper.initBalance
import com.startwithn.exchange_android.ui.page.base.BaseActivity
import com.startwithn.exchange_android.ui.page.login.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    //private var navController: LiveData<NavController>? = null
    private val mainViewModel: MainViewModel by stateViewModel()

    private val balanceAdapter by lazy {
        SimpleRecyclerViewAdapter<UserModel.CustomerBalance, ItemRvBalanceBinding>(
            layout = R.layout.item_rv_balance,
            isRunAnimation = true
        )
    }

    private val lastTransactionAdapter by lazy {
        SimpleRecyclerViewAdapter<TransactionsModel, ItemRvTransactionBinding>(
            layout = R.layout.item_rv_transaction,
            isRunAnimation = true
        )
    }

    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java).apply {
            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }

    override fun getExtra(bundle: Bundle) {
        super.getExtra(bundle)
        bundle.apply {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.let {
            getExtra(it)
            //handleNotification()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            //setUpBottomNavigatorBar()
        }// Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation

        //setUpBottomNavigatorBar()
    }

    override fun onResume() {
        super.onResume()
        getUser()
        getLastTransaction()
    }

    override fun setUp() {
        binding.apply {
            activity = this@MainActivity
            layoutEmpty.isVisible = false

            rvBalance.apply {
                adapter = balanceAdapter
                setOnLoadMoreListener({ balanceAdapter.isLoading }, {
//                    val nextPage = rewardViewModel.page
//                    if (nextPage != null) {
//                        rewardViewModel.getReward(nextPage)
//                    } else {
//                        rewardAdapter.addBottomSpace()
//                    }
                })
            }

            rvTransactions.apply {
                adapter = lastTransactionAdapter
                setOnLoadMoreListener({ lastTransactionAdapter.isLoading }, {
                })
            }

            balanceAdapter.initBalance(this@MainActivity)
            //lastTransactionAdapter.initLastTransaction(this@MainActivity)
        }
    }

    override fun listener() {
        binding.apply {
            toolbar.setOnBackListener {
                open()
            }

            layoutNavigation.apply {
                btnClose.setOnClickListener {
                    close()
                }
                btnSetting.setOnClickListener {
                    AppNavigator(this@MainActivity).goToSettings()
                }
                btnEditProfile.setOnClickListener {

                }
                btnHelp.setOnClickListener {
                    AppNavigator(this@MainActivity).goToHelp()
                }
                btnLogout.setOnClickListener {
                    val msg = resources.getString(R.string.message_logout)
                    val confirm = resources.getString(R.string.button_confirm)
                    val cancel = resources.getString(R.string.button_cancel)

                    AppAlert.confirm(this@MainActivity, msg, confirm, {
                        logout()
                    }, cancel, {}).show(supportFragmentManager)
                }
            }

        }


    }

    override fun subscribe() {
        super.subscribe()

        mainViewModel.getMeResult().observe(this) {
            balanceAdapter.isLoading = false
            when (it) {
                is ResultWrapper.Loading -> {
                    //progressDialog.show(supportFragmentManager)
                    balanceAdapter.isLoading = true
                }

                is ResultWrapper.Success -> {
                    setUserBalance(it.response.data)
                }

                is ResultWrapper.GenericError -> {
                    AppAlert.alertGenericError(this,it.code, it.message).show(supportFragmentManager)

                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alertNetworkError(this).show(supportFragmentManager)
                }

                else -> {}
            }
        }

        mainViewModel.transactionRequestLiveData.observe(this) {
            lastTransactionAdapter.isLoading = false
            when (it) {
                is ResultWrapper.Loading -> {
                    lastTransactionAdapter.isLoading = true
                }
                is ResultWrapper.GenericError -> {
                    AppAlert.alertGenericError(this,it.code, it.message).show(supportFragmentManager)
                }
                is ResultWrapper.NetworkError -> {
                    AppAlert.alertNetworkError(this).show(supportFragmentManager)
                }
                else -> {
                    /*none*/
                }
            }
        }
        mainViewModel.transactionResultLiveData.observe(this) {
            it?.let {
                setLastTransactions(it)
            }
        }
    }

    fun gotoTopUp() {
        AppNavigator(this).goToTopUp()
    }

    fun gotoExchange() {
        AppNavigator(this).goToExchange()
    }

    fun gotoHistory() {
        AppNavigator(this).goToHistory()
    }

    private fun open() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun close() {
        binding.drawerLayout.closeDrawers()
    }

    private fun getUser() {
        balanceAdapter.submitList(true, mutableListOf())
        mainViewModel.getMe()
    }

    private fun getLastTransaction(){
        lastTransactionAdapter.submitList(true, mutableListOf())
        mainViewModel.getLastTransaction()
    }

    private fun setUserBalance(userModel: UserModel?){
        userModel?.customerBalances?.let { balance ->
//            val oldList = balanceAdapter.getOriginalList()
//            val diffResult = DiffUtil.calculateDiff(BalanceDiffUtilCallback(oldList, balance))
//            diffResult.dispatchUpdatesTo(balanceAdapter)
            balanceAdapter.submitList(false,balance)

        }
    }

    private fun setLastTransactions(transactions : MutableList<TransactionsModel>){
        //lastTransactionAdapter.submitList(false,transactions)
//        val oldList = lastTransactionAdapter.getOriginalList()
//        val diffResult = DiffUtil.calculateDiff(LastTransactionDiffUtilCallback(oldList, transactions))
//        diffResult.dispatchUpdatesTo(lastTransactionAdapter)
        lastTransactionAdapter.updateList(transactions)
    }


}