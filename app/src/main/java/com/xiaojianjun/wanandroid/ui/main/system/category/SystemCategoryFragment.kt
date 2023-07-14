package com.xiaojianjun.wanandroid.ui.main.system.category

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xiaojianjun.wanandroid.App
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.databinding.FragmentSystemCategoryBinding
import com.xiaojianjun.wanandroid.ext.dpToPx
import com.xiaojianjun.wanandroid.model.bean.Category
import com.xiaojianjun.wanandroid.ui.main.system.SystemFragment
import com.xiaojianjun.wanandroid.util.getSreenHeight


/**
 * Created by yangfeihu on 2019-11-17.
 */
class SystemCategoryFragment : BottomSheetDialogFragment() {

    companion object {

        const val CATEGORY_LIST = "categoryList"

        fun newInstance(
            categoryList: ArrayList<Category>
        ): SystemCategoryFragment {
            return SystemCategoryFragment().apply {
                arguments = Bundle().apply { putParcelableArrayList(CATEGORY_LIST, categoryList) }
            }
        }
    }


    lateinit var mBinding: FragmentSystemCategoryBinding


    private var height: Int? = null
    private var behavior: BottomSheetBehavior<View>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_system_category, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryList: ArrayList<Category> = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        val checked = (parentFragment as SystemFragment).getCurrentChecked()
        SystemCategoryAdapter(R.layout.item_system_category, categoryList, checked).also {
            it.onCheckedListener = { position ->
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                view.postDelayed({
                    (parentFragment as SystemFragment).check(position)
                }, 300)
            }
            mBinding.recyclerView.adapter = it
        }
        view.post {
            (mBinding.recyclerView.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(checked.first, 0)
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet: View = (dialog as BottomSheetDialog).delegate
            .findViewById(com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        behavior = BottomSheetBehavior.from(bottomSheet)
        height?.let { behavior?.peekHeight = it }
        dialog?.window?.let {
            it.setGravity(Gravity.BOTTOM)
            it.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, height ?: ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    fun show(manager: FragmentManager, height: Int? = null) {
        this.height = height ?: getSreenHeight(App.instance) - 48.dpToPx().toInt()
        if (!this.isAdded) {
            super.show(manager, "SystemCategoryFragment")
        }
    }

}