package com.czl.module_user.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.UserShareBean
import com.czl.lib_base.databinding.CommonRecycleviewBinding
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.adapter.UserShareAdapter
import com.czl.module_user.viewmodel.UserShareVm
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author Alwyn
 * @Date 2020/11/21
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_SHARE)
class UserShareFragment : BaseFragment<CommonRecycleviewBinding, UserShareVm>() {
    override fun initContentView(): Int {
        return R.layout.common_recycleview
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("我的分享")
        binding.smartCommon.autoRefresh()
    }

    override fun initViewObservable() {
        val mAdapter = UserShareAdapter(this)
        mAdapter.setDiffCallback(mAdapter.diffConfig)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            setDemoLayoutReference(R.layout.user_item_share_skeleton)
            showShimmerAdapter()
        }
        viewModel.loadDataCompleteEvent.observe(this, { data ->
            handleRecyclerviewData(
                data == null, data?.datas as MutableList<*>?,
                mAdapter,
                binding.ryCommon,
                binding.smartCommon,
                viewModel.currentPage,
                data!!.over, 1
            )
        })
        viewModel.uC.getScrollTopEvent().observe(this, {
            binding.ryCommon.smoothScrollToPosition(0)
        })
    }

}