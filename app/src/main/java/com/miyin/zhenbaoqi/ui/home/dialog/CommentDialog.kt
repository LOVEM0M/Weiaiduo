package com.miyin.zhenbaoqi.ui.home.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.bean.SingleCommentBean
import com.miyin.zhenbaoqi.bean.VideoCommentFirstBean
import com.miyin.zhenbaoqi.bean.VideoCommentSecondBean
import com.miyin.zhenbaoqi.bean.VideoCommentThirdBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ui.home.adapter.CommentAdapter
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.dialog_comment.*
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
@SuppressLint("SetTextI18n")
class CommentDialog : BottomSheetDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null
    private var mList = mutableListOf<MultiItemEntity>()
    private lateinit var mAdapter: CommentAdapter
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private var mIsScroll = false
    private var mIndex = 0
    private var mScrollDistance = 0
    private var mOnBehaviorChangedListener: OnBehaviorChangedListener? = null

    companion object {
        fun newInstance(list: List<MultiItemEntity>) = CommentDialog().apply {
            arguments = Bundle().apply {
                putSerializable("list", list as Serializable)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(true)
            window?.run {
                attributes.dimAmount = 0f
                setWindowAnimations(R.style.DialogAnimation)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.run {
            mList = (getSerializable("list") as List<MultiItemEntity>).toMutableList()
        }
        return inflater.inflate(R.layout.dialog_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.run {
            mAdapter = CommentAdapter(mList)
            adapter = mAdapter
            mLinearLayoutManager = LinearLayoutManager(context)
            layoutManager = mLinearLayoutManager
            mAdapter.setOnItemClickListener { _, _, position ->
                mIndex = position
                moveToPosition(position)

                when (val entity = mList[position]) {
                    is VideoCommentFirstBean -> {
                        showInputDialog(entity.id, entity.nickName ?: "")
                    }
                    is VideoCommentSecondBean -> {
                        //showInputDialog(entity.id, entity.nickName ?: "")
                    }
                    is VideoCommentThirdBean -> {
                    }
                }
            }
            tv_title.text = "${mList.size}条评论"

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (mIsScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                        mIsScroll = false
                        moveToPosition(mIndex)
                    }
                }
            })
        }

//        dialog?.setOnDismissListener {
//            Logger.d("changed == dismiss")
//            mOnBehaviorChangedListener?.changedState(null, BottomSheetBehavior.STATE_COLLAPSED)
//        }

        iv_close.setOnClickListener { dismiss() }
        tv_comment.setOnClickListener { showInputDialog(0, "") }
    }

    override fun onStart() {
        super.onStart()
        dialog?.findViewById<FrameLayout>(R.id.design_bottom_sheet)?.run {
            val layoutParams = layoutParams as CoordinatorLayout.LayoutParams
            val height = DensityUtils.getScreenHeight() / 3 * 2
            layoutParams.height = height
            val bottomSheetBehavior = BottomSheetBehavior.from(this)
            // 初始为展开状态
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.peekHeight = height
            mOnBehaviorChangedListener?.changedState(null, BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    mOnBehaviorChangedListener?.changedOffset(bottomSheet, slideOffset)
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        dismiss()
                    }
                    mOnBehaviorChangedListener?.changedState(bottomSheet, newState)
                }
            })
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Logger.d("changed == dismiss")
        mOnBehaviorChangedListener?.changedState(null, BottomSheetBehavior.STATE_COLLAPSED)
    }

    private fun moveToPosition(position: Int) {
        // 第一个可见位置
        val firstItem = recycler_view.getChildLayoutPosition(recycler_view.getChildAt(0))
        // 最后一个可见位置
        val lastItem = recycler_view.getChildLayoutPosition(recycler_view.getChildAt(recycler_view.childCount - 1))
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            recycler_view.smoothScrollToPosition(position)
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            val movePosition = position - firstItem
            if (movePosition >= 0 && movePosition < recycler_view.childCount) {
                mScrollDistance = recycler_view.getChildAt(movePosition).top
                recycler_view.smoothScrollBy(0, mScrollDistance)
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            recycler_view.smoothScrollToPosition(position)
            mIndex = position
            mIsScroll = true
        }
    }

    private fun showInputDialog(replyId: Int, name: String) {
        val arrayMap = ArrayMap<String, Any>().apply {
            put("reply_id", replyId)
            put("name", name)
        }
        mOnDialogCallback?.onDialog(arrayMap, 0)
    }

    fun resetScrollDistance() {
        recycler_view.smoothScrollBy(0, -mScrollDistance)
    }

    fun updateComment(bean:SingleCommentBean) {
        bean.data?.let {
            val firstBean = VideoCommentFirstBean().apply {
                id = it.id
                comment = it.reply_content
                headImg = it.head_img
                nickName = it.nick_name
                replyTime = if (it.reply_time.isNullOrEmpty()) 0 else it.reply_time!!.toLong()
            }
            mList.add(0, firstBean)
            mAdapter.notifyItemInserted(0)
            moveToPosition(0)

            tv_comment.text = null
            tv_title.text = "${mList.size}条评论"
        }
    }

    fun updateChildComment(bean: SingleCommentBean) {
        bean.data?.let {
            for (i in mList.size - 1 downTo 0) {
                val multiItemEntity = mList[i]
                if (multiItemEntity is VideoCommentFirstBean) {
                    if (it.reply_id == multiItemEntity.id) {
                        val secondBean = VideoCommentSecondBean().apply {
                            id = it.id
                            comment = it.reply_content
                            headImg = it.head_img
                            nickName = it.nick_name
                            replyTime = if (it.reply_time.isNullOrEmpty()) 0 else it.reply_time!!.toLong()
                            parentNickName = multiItemEntity.nickName
                        }
                        mList.add(i + 1, secondBean)
                        mAdapter.notifyItemInserted(i + 1)
                    }
                }
            }

            tv_comment.text = null
            tv_title.text = "${mList.size}条评论"
        }
    }

    fun setOnBehaviorChangedListener(onBehaviorChangedListener: OnBehaviorChangedListener) {
        mOnBehaviorChangedListener = onBehaviorChangedListener
    }

    interface OnBehaviorChangedListener {
        fun changedState(bottomSheet: View?, state: Int)

        fun changedOffset(bottomSheet: View?, slideOffset: Float)
    }

}
