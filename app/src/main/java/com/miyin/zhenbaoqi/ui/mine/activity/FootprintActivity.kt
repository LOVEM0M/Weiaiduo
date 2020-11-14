package com.miyin.zhenbaoqi.ui.mine.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.miyin.greendao.FootprintEntityDao
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.bean.FootprintBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.sql.FootprintEntity
import com.miyin.zhenbaoqi.ui.mine.adapter.FootprintAdapter
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.SmoothLayoutManager
import com.orhanobut.logger.Logger
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_footprint.*
import kotlinx.android.synthetic.main.title_bar.*

class FootprintActivity : BaseActivity() {

    private val mDisposable = CompositeDisposable()
    private val mList = mutableListOf<FootprintBean>()
    private lateinit var mAdapter: FootprintAdapter

    override fun getContentView() = R.layout.activity_footprint

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("我的足迹", rightTitle = "删除")
        immersionBar { statusBarDarkFont(true) }

        recycler_view.run {
            mAdapter = FootprintAdapter(mList)
            adapter = mAdapter
            val gridLayoutManager = SmoothLayoutManager(context, 3)
            layoutManager = gridLayoutManager
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = if (mList.isEmpty()) 3 else if (mList[position].itemType == 0) 3 else 1
            }

            mAdapter.setOnItemChildClickListener { _, view, position ->
                val bean = mList[position]
                if (view.id == R.id.tv_title) {
                    if (mAdapter.getDeleteFlag()) {
                        if (bean.itemType == 0) {
                            if (bean.isSelect) {
                                mList.filter { bean.data == it.data }.forEach {
                                    it.isSelect = false
                                }
                                mAdapter.notifyDataSetChanged()
                            } else {
                                mList.filter { bean.data == it.data }.forEach {
                                    it.isSelect = true
                                }
                                mAdapter.notifyDataSetChanged()
                            }
                        } else if (bean.itemType == 1) {
                            bean.isSelect = !bean.isSelect

                            val size = mList.filter { (it.data == bean.data && it.isSelect != bean.isSelect && it.itemType == 1) }.size
                            if (size == 0) {
                                mList.firstOrNull { it.data == bean.data }?.isSelect = bean.isSelect
                            } else {
                                val firstOrNullBean = mList.firstOrNull { it.data == bean.data }
                                if (!bean.isSelect) {
                                    firstOrNullBean?.isSelect = false
                                }
                            }
                            mAdapter.notifyDataSetChanged()
                        }
                    }
                } else if (view.id == R.id.ll_container) {
                    startActivity<GoodsDetailActivity>("goods_id" to bean.bean?.goodsId)
                }
            }
        }

        calendar_view.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener {
            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                calendar?.run {
                    val month = if (month < 10) "0$month" else month.toString()
                    val day = if (day < 10) "0$day" else day.toString()
                    Logger.d("index == ${year}-${month}-${day}")
                    val index = mList.indexOfFirst { it.itemType == 0 && it.data == "$year-$month-$day" }
                    if (index != -1) {
                        recycler_view.smoothScrollToPosition(index)
                    }
                }
            }

            override fun onCalendarOutOfRange(calendar: Calendar?) {

            }
        })
    }

    override fun initData() {
        val disposable = Flowable.create(FlowableOnSubscribe<List<FootprintEntity>> {
            val dao = App.daoSession.footprintEntityDao
            val list = dao.queryBuilder()
                    .where(FootprintEntityDao.Properties.UserId.eq(SPUtils.getInt("user_id")))
                    .orderDesc()
                    .list()
            it.onNext(list)
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isEmpty()) {
                        mAdapter.setEmptyView(R.layout.view_empty, recycler_view)
                    } else {
                        var titleEntity: FootprintBean
                        var contentEntity: FootprintBean
                        var lastEntity: FootprintEntity
                        it.forEachIndexed { index, entity ->
                            if (index == 0) {
                                titleEntity = FootprintBean().apply {
                                    type = 0
                                    data = entity.browseDate
                                }
                                mList.add(titleEntity)

                                val calendar = Calendar().apply {
                                    val array = titleEntity.data?.split("-")
                                    if (!array.isNullOrEmpty()) {
                                        year = array[0].toInt()
                                        month = array[1].toInt()
                                        day = array[2].toInt()
                                    }
                                }
                                calendar_view.addSchemeDate(calendar)

                                contentEntity = FootprintBean().apply {
                                    type = 1
                                    data = entity.browseDate
                                    bean = entity
                                }
                                mList.add(contentEntity)
                            } else {
                                lastEntity = it[index - 1]
                                if (lastEntity.browseDate == entity.browseDate) {
                                    contentEntity = FootprintBean().apply {
                                        type = 1
                                        data = entity.browseDate
                                        bean = entity
                                    }
                                    mList.add(contentEntity)
                                } else {
                                    titleEntity = FootprintBean().apply {
                                        type = 0
                                        data = entity.browseDate
                                    }
                                    mList.add(titleEntity)

                                    val calendar = Calendar().apply {
                                        val array = titleEntity.data?.split("-")
                                        if (!array.isNullOrEmpty()) {
                                            year = array[0].toInt()
                                            month = array[1].toInt()
                                            day = array[2].toInt()
                                        }
                                    }
                                    calendar_view.addSchemeDate(calendar)

                                    contentEntity = FootprintBean().apply {
                                        type = 1
                                        data = entity.browseDate
                                        bean = entity
                                    }
                                    mList.add(contentEntity)
                                }
                            }
                        }

                        mAdapter.setNewData(mList)
                    }
                }, {
                    mAdapter.setEmptyView(R.layout.view_error, recycler_view)
                })
        mDisposable.add(disposable)
    }

    override fun onRightClick() {
        if (mAdapter.getDeleteFlag()) {
            val disposable = Flowable.create(FlowableOnSubscribe<Boolean> {
                val dao = App.daoSession.footprintEntityDao
                for (index in mList.size - 1 downTo 0) {
                    if (mList[index].isSelect) {
                        if (mList[index].itemType == 0) {
                            mList.removeAt(index)
                        } else {
                            dao.deleteByKey(mList[index].bean?.id)
                            mList.removeAt(index)
                        }
                    }
                }
                it.onNext(true)
            }, BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it) {
                            mAdapter.notifyDataSetChanged()
                            mAdapter.setDeleteFlag(false)
                            tv_right_title.text = "删除"
                        }
                        if (mList.isEmpty()) {
                            mAdapter.setEmptyView(R.layout.view_empty, recycler_view)
                        }
                    }, {

                    })
            mDisposable.add(disposable)
        } else {
            mAdapter.setDeleteFlag(true)
            tv_right_title.text = "完成"
        }
    }

    override fun onDestroy() {
        mDisposable.clear()
        super.onDestroy()
    }

}
