package com.czl.module_user.ui.fragment

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.TodoBean
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.adapter.UserTodoAdapter
import com.czl.module_user.databinding.UserFragmentTodoBinding
import com.czl.module_user.viewmodel.UserTodoViewModel


/**
 * @author Alwyn
 * @Date 2021/1/5
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_TODO)
class UserTodoFragment : BaseFragment<UserFragmentTodoBinding, UserTodoViewModel>() {
    private lateinit var mAdapter: UserTodoAdapter
    override fun initContentView(): Int {
        return R.layout.user_fragment_todo
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("待办清单")
        viewModel.ivToolbarIconRes.set(R.drawable.ic_nav)
        initAdapter()
        binding.smartCommon.autoRefresh()
    }

    private fun initAdapter() {
        mAdapter = UserTodoAdapter(this)
        mAdapter.setDiffCallback(mAdapter.diffConfig)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = mAdapter
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            setDemoLayoutReference(R.layout.user_item_todo_skeleton)
            showShimmerAdapter()
        }
    }

    override fun initViewObservable() {
        viewModel.uc.refreshCompleteEvent.observe(this, { data ->
            val smartCommon = binding.smartCommon
            if (data == null) {
                smartCommon.finishRefresh(false)
                smartCommon.finishLoadMore(false)
                return@observe
            }
            if (viewModel.currentPage == 1) {
                binding.ryCommon.hideShimmerAdapter()
                if (!mAdapter.hasEmptyView()) {
                    val emptyView = View.inflate(context, R.layout.common_empty_layout, null)
                    emptyView.findViewById<ViewGroup>(R.id.ll_empty).setOnClickListener {
                        smartCommon.autoRefresh()
                    }
                    mAdapter.setEmptyView(emptyView)
                }
                mAdapter.setDiffNewData(data.datas as MutableList<TodoBean.Data>)
                if (data.over) smartCommon.finishRefreshWithNoMoreData()
                else smartCommon.finishRefresh(true)
                return@observe
            }
            if (data.over) smartCommon.finishLoadMoreWithNoMoreData()
            else smartCommon.finishLoadMore(true)
            mAdapter.addData(data.datas)
        })
    }
}