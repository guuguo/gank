package com.guuguo.fuliba.ui.main

import com.bianla.commonlibrary.extension.BindingViewHolder
import com.bianla.commonlibrary.extension.generate
import com.chad.library.adapter.base.BaseQuickAdapter
import com.guuguo.baselib.extension.safe
import com.guuguo.baselib.mvvm.BaseFragment
import com.guuguo.baselib.mvvm.FullscreenWebViewActivity
import com.guuguo.fuliba.R
import com.guuguo.fuliba.databinding.FragmentFulibaBinding
import com.guuguo.fuliba.databinding.FulibaItemBinding
import com.guuguo.fuliba.model.FulibaItemBean
import com.guuguo.fuliba.ui.fuliba.repository.FulibaRepository
import kotlinx.coroutines.launch

class FulibaFragment : BaseFragment<FragmentFulibaBinding>() {
    override fun getLayoutResId() = R.layout.fragment_fuliba
    lateinit var adapter: BaseQuickAdapter<FulibaItemBean, BindingViewHolder>
    override fun initView() {
        super.initView()
        adapter = binding.recycler.generate(R.layout.fuliba_item) { h, t ->
            h.getBind<FulibaItemBinding>()?.bean = t
        }
        binding.refresh.setOnRefreshListener {
            loadData(true)
            binding.refresh.isRefreshing = false
        }
        adapter.setOnItemClickListener { _, view, position ->
            val bean = adapter.getItem(position)
            FullscreenWebViewActivity.intentTo(activity, bean?.url.safe())
        }
        adapter.setOnLoadMoreListener({
            page++
            request()
        }, binding.recycler)
    }

    private fun request() {
        launch {
            val list = FulibaRepository.getHomeList(page)
            list?.let { adapter.addData(list) }
            adapter.loadMoreComplete()
        }
    }

    var page = 1
    override fun loadData(isFromNet: Boolean) {
        super.loadData(isFromNet)
        request()
    }
}