package com.miyin.zhenbaoqi.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.utils.DensityUtils
import java.util.*

/**
 * 滑动 TabLayout, 对于 ViewPager 的依赖性强
 */
class SlidingTabLayout @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : HorizontalScrollView(mContext, attrs, defStyleAttr), androidx.viewpager.widget.ViewPager.OnPageChangeListener {

    private var mViewPager: androidx.viewpager.widget.ViewPager? = null
    private val mTabsContainer: LinearLayout
    private var mCurrentTab: Int = 0
    private var mCurrentPositionOffset: Float = 0.toFloat()
    private var tabCount: Int = 0

    /**
     * 用于绘制显示器
     */
    private val mIndicatorRect = Rect()
    /**
     * 用于实现滚动居中
     */
    private val mTabRect = Rect()
    private val mIndicatorDrawable = GradientDrawable()

    private val mRectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mDividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrianglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrianglePath = Path()
    private var mIndicatorStyle = STYLE_NORMAL

    private var mTabPadding: Float = 0.toFloat()
    private var mTabSpaceEqual: Boolean = false
    private var mTabWidth: Float = 0.toFloat()

    /**
     * indicator
     */
    private var mIndicatorColor: Int = 0
    private var mIndicatorHeight: Float = 0.toFloat()
    private var mIndicatorWidth: Float = 0.toFloat()
    private var mIndicatorCornerRadius: Float = 0.toFloat()
    private var mIndicatorMarginLeft: Float = 0.toFloat()
    private var mIndicatorMarginTop: Float = 0.toFloat()
    private var mIndicatorMarginRight: Float = 0.toFloat()
    private var mIndicatorMarginBottom: Float = 0.toFloat()
    private var mIndicatorGravity: Int = 0
    private var mIndicatorWidthEqualTitle: Boolean = false

    /**
     * underline
     */
    private var mUnderlineColor: Int = 0
    private var mUnderlineHeight: Float = 0.toFloat()
    private var mUnderlineGravity: Int = 0

    /**
     * divider
     */
    private var mDividerColor: Int = 0
    private var mDividerWidth: Float = 0.toFloat()
    private var mDividerPadding: Float = 0.toFloat()
    var mNormalTextSize: Float = 0.toFloat()
        private set
    var mSelectTextSize: Float = 0.toFloat()
        private set
    private var mTextSelectColor: Int = 0
    var mTextNormalColor: Int = 0
        private set
    private var mTextBold: Int = 0
    private var mTextAllCaps: Boolean = false

    private var mLastScrollX: Int = 0
    private var mSnapOnTabClick: Boolean = false

    private var margin: Float = 0.toFloat()

    var currentTab: Int
        get() = mCurrentTab
        set(currentTab) {
            mCurrentTab = currentTab
            mViewPager!!.currentItem = currentTab
        }

    var indicatorStyle: Int
        get() = mIndicatorStyle
        set(indicatorStyle) {
            mIndicatorStyle = indicatorStyle
            invalidate()
        }

    var tabPadding: Float
        get() = mTabPadding
        set(tabPadding) {
            mTabPadding = DensityUtils.dp2px(tabPadding).toFloat()
            updateTabStyles()
        }

    var isTabSpaceEqual: Boolean
        get() = mTabSpaceEqual
        set(tabSpaceEqual) {
            mTabSpaceEqual = tabSpaceEqual
            updateTabStyles()
        }

    var tabWidth: Float
        get() = mTabWidth
        set(tabWidth) {
            this.mTabWidth = DensityUtils.dp2px(tabWidth).toFloat()
            updateTabStyles()
        }

    var indicatorColor: Int
        get() = mIndicatorColor
        set(indicatorColor) {
            mIndicatorColor = indicatorColor
            invalidate()
        }

    var indicatorHeight: Float
        get() = mIndicatorHeight
        set(indicatorHeight) {
            mIndicatorHeight = DensityUtils.dp2px(indicatorHeight).toFloat()
            invalidate()
        }

    var indicatorWidth: Float
        get() = mIndicatorWidth
        set(indicatorWidth) {
            mIndicatorWidth = DensityUtils.dp2px(indicatorWidth).toFloat()
            invalidate()
        }

    var indicatorCornerRadius: Float
        get() = mIndicatorCornerRadius
        set(indicatorCornerRadius) {
            mIndicatorCornerRadius = DensityUtils.dp2px(indicatorCornerRadius).toFloat()
            invalidate()
        }

    var underlineColor: Int
        get() = mUnderlineColor
        set(underlineColor) {
            mUnderlineColor = underlineColor
            invalidate()
        }

    var underlineHeight: Float
        get() = mUnderlineHeight
        set(underlineHeight) {
            mUnderlineHeight = DensityUtils.dp2px(underlineHeight).toFloat()
            invalidate()
        }

    var dividerColor: Int
        get() = mDividerColor
        set(dividerColor) {
            mDividerColor = dividerColor
            invalidate()
        }

    var dividerWidth: Float
        get() = mDividerWidth
        set(dividerWidth) {
            mDividerWidth = DensityUtils.dp2px(dividerWidth).toFloat()
            invalidate()
        }

    var dividerPadding: Float
        get() = mDividerPadding
        set(dividerPadding) {
            mDividerPadding = DensityUtils.dp2px(dividerPadding).toFloat()
            invalidate()
        }

    var textSelectColor: Int
        get() = mTextSelectColor
        set(textSelectColor) {
            mTextSelectColor = textSelectColor
            updateTabStyles()
        }

    var textBold: Int
        get() = mTextBold
        set(textBold) {
            mTextBold = textBold
            updateTabStyles()
        }

    var isTextAllCaps: Boolean
        get() = mTextAllCaps
        set(textAllCaps) {
            mTextAllCaps = textAllCaps
            updateTabStyles()
        }

    private var mTextGravity = TEXT_GRAVITY_CENTER
    private var mTextMargin = 0
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mListener: OnTabSelectListener? = null

    init {
        isFillViewport = true//设置滚动视图是否可以伸缩其内容以填充视口
        setWillNotDraw(false)//重写onDraw方法,需要调用这个方法来清除flag
        clipChildren = false
        clipToPadding = false
        mTabsContainer = LinearLayout(mContext)
        addView(mTabsContainer)

        obtainAttributes(mContext, attrs)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.SlidingTabLayout).apply {

            mIndicatorStyle = getInt(R.styleable.SlidingTabLayout_tl_indicator_style, STYLE_NORMAL)
            mIndicatorColor = getColor(R.styleable.SlidingTabLayout_tl_indicator_color, Color.parseColor(if (mIndicatorStyle == STYLE_BLOCK) "#4B6A87" else "#ffffff"))
            mIndicatorHeight = getDimension(R.styleable.SlidingTabLayout_tl_indicator_height, DensityUtils.dp2px((if (mIndicatorStyle == STYLE_TRIANGLE) 4 else if (mIndicatorStyle == STYLE_BLOCK) -1 else 2).toFloat()).toFloat())
            mIndicatorWidth = getDimension(R.styleable.SlidingTabLayout_tl_indicator_width, DensityUtils.dp2px((if (mIndicatorStyle == STYLE_TRIANGLE) 10 else -1).toFloat()).toFloat())
            mIndicatorCornerRadius = getDimension(R.styleable.SlidingTabLayout_tl_indicator_corner_radius, DensityUtils.dp2px((if (mIndicatorStyle == STYLE_BLOCK) -1 else 0).toFloat()).toFloat())
            mIndicatorMarginLeft = getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_left, DensityUtils.dp2px(0f).toFloat())
            mIndicatorMarginTop = getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_top, DensityUtils.dp2px((if (mIndicatorStyle == STYLE_BLOCK) 7 else 0).toFloat()).toFloat())
            mIndicatorMarginRight = getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_right, DensityUtils.dp2px(0f).toFloat())
            mIndicatorMarginBottom = getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_bottom, DensityUtils.dp2px((if (mIndicatorStyle == STYLE_BLOCK) 7 else 0).toFloat()).toFloat())
            mIndicatorGravity = getInt(R.styleable.SlidingTabLayout_tl_indicator_gravity, Gravity.BOTTOM)
            mIndicatorWidthEqualTitle = getBoolean(R.styleable.SlidingTabLayout_tl_indicator_width_equal_title, false)

            mUnderlineColor = getColor(R.styleable.SlidingTabLayout_tl_underline_color, Color.parseColor("#ffffff"))
            mUnderlineHeight = getDimension(R.styleable.SlidingTabLayout_tl_underline_height, DensityUtils.dp2px(0f).toFloat())
            mUnderlineGravity = getInt(R.styleable.SlidingTabLayout_tl_underline_gravity, Gravity.BOTTOM)

            mDividerColor = getColor(R.styleable.SlidingTabLayout_tl_divider_color, Color.parseColor("#ffffff"))
            mDividerWidth = getDimension(R.styleable.SlidingTabLayout_tl_divider_width, DensityUtils.dp2px(0f).toFloat())
            mDividerPadding = getDimension(R.styleable.SlidingTabLayout_tl_divider_padding, DensityUtils.dp2px(12f).toFloat())

            mNormalTextSize = getDimension(R.styleable.SlidingTabLayout_tl_normal_text_size, DensityUtils.sp2px(14f).toFloat())
            mSelectTextSize = getDimension(R.styleable.SlidingTabLayout_tl_select_text_size, 0f)
            mTextSelectColor = getColor(R.styleable.SlidingTabLayout_tl_select_text_color, Color.parseColor("#ffffff"))
            mTextNormalColor = getColor(R.styleable.SlidingTabLayout_tl_normal_text_color, Color.parseColor("#AAffffff"))
            mTextBold = getInt(R.styleable.SlidingTabLayout_tl_textBold, TEXT_BOLD_NONE)
            mTextAllCaps = getBoolean(R.styleable.SlidingTabLayout_tl_textAllCaps, false)
            mTextGravity = getInt(R.styleable.SlidingTabLayout_tl_text_gravity, TEXT_GRAVITY_CENTER)
            mTextMargin = getDimensionPixelOffset(R.styleable.SlidingTabLayout_tl_text_margin, 0)

            mTabSpaceEqual = getBoolean(R.styleable.SlidingTabLayout_tl_tab_space_equal, false)
            mTabWidth = getDimension(R.styleable.SlidingTabLayout_tl_tab_width, DensityUtils.dp2px(-1f).toFloat())
            mTabPadding = getDimension(R.styleable.SlidingTabLayout_tl_tab_padding, (if (mTabSpaceEqual || mTabWidth > 0) DensityUtils.dp2px(0f) else DensityUtils.dp2px(20f)).toFloat())

            recycle()
        }
    }

    /**
     * 关联ViewPager
     */
    fun setViewPager(vp: androidx.viewpager.widget.ViewPager?) {
        if (vp == null || vp.adapter == null) {
            throw IllegalStateException("ViewPager or ViewPager adapter can not be NULL !")
        }

        mViewPager = vp

        mViewPager?.removeOnPageChangeListener(this)
        mViewPager?.addOnPageChangeListener(this)
        notifyDataSetChanged()
    }

    /**
     * 更新数据
     */
    private fun notifyDataSetChanged() {
        mTabsContainer.removeAllViews()
        this.tabCount = mViewPager?.adapter!!.count
        var tabView: View
        for (i in 0 until tabCount) {
            tabView = View.inflate(mContext, R.layout.layout_tab, null)
            val pageTitle = mViewPager?.adapter!!.getPageTitle(i)
            addTab(i, pageTitle!!.toString(), tabView)
        }

        updateTabStyles()
    }

    fun addNewTab(title: String) {
        val tabView = View.inflate(mContext, R.layout.layout_tab, null)

        val pageTitle = mViewPager?.adapter!!.getPageTitle(tabCount)
        addTab(tabCount, pageTitle!!.toString(), tabView)
        tabCount = mViewPager?.adapter!!.count

        updateTabStyles()
    }

    /**
     * 创建并添加tab
     */
    private fun addTab(position: Int, title: String?, tabView: View) {
        val tvTabTitle = tabView.findViewById<TextView>(R.id.tv_tab_title)
        if (tvTabTitle != null) {
            if (title != null) tvTabTitle.text = title
        }

        tabView.setOnClickListener {
            if (position != -1) {
                if (mViewPager?.currentItem != position) {
                    if (mSnapOnTabClick) {
                        mViewPager?.setCurrentItem(position, false)
                    } else {
                        mViewPager?.currentItem = position
                    }
                    mListener?.onTabSelect(position)
                } else {
                    mListener?.onTabReselect(position)
                }
            }
        }

        /* 每一个 Tab 的布局参数 */
        var lpTab = if (mTabSpaceEqual)
            LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT, 1.0f)
        else
            LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT)
        if (mTabWidth > 0) {
            lpTab = LinearLayout.LayoutParams(mTabWidth.toInt(), FrameLayout.LayoutParams.MATCH_PARENT)
        }

        val tabParams = (tabView.findViewById<TextView>(R.id.tv_tab_title).layoutParams as FrameLayout.LayoutParams).apply {
            when (mTextGravity) {
                TEXT_GRAVITY_TOP -> {
                    gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                    topMargin = mTextMargin
                }
                TEXT_GRAVITY_CENTER -> {
                    gravity = Gravity.CENTER
                }
                TEXT_GRAVITY_BOTTOM -> {
                    gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                    bottomMargin = mTextMargin
                }
            }
        }
        tabView.layoutParams = tabParams

        mTabsContainer.addView(tabView, position, lpTab)
    }

    private fun updateTabStyles() {
        for (i in 0 until tabCount) {
            val v = mTabsContainer.getChildAt(i)
            val tvTabTitle = v.findViewById<TextView>(R.id.tv_tab_title)
            tvTabTitle?.run {
                setTextColor(if (i == mCurrentTab) mTextSelectColor else mTextNormalColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, if (i == mCurrentTab && mSelectTextSize != 0f) mSelectTextSize else mNormalTextSize)
                setPadding(mTabPadding.toInt(), 0, mTabPadding.toInt(), 0)
                if (mTextAllCaps) {
                    tvTabTitle.text = tvTabTitle.text.toString().toUpperCase(Locale.getDefault())
                }

                if (mTextBold == TEXT_BOLD_BOTH) {
                    tvTabTitle.paint.isFakeBoldText = true
                } else if (mTextBold == TEXT_BOLD_NONE) {
                    tvTabTitle.paint.isFakeBoldText = false
                }
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        /*
         * position: 当前 View 的位置
         * mCurrentPositionOffset: 当前 View 的偏移量比例.[0,1)
         */
        mCurrentTab = position
        mCurrentPositionOffset = positionOffset
        scrollToCurrentTab()
        invalidate()
    }

    override fun onPageSelected(position: Int) {
        updateTabSelection(position)
    }

    override fun onPageScrollStateChanged(state: Int) {}

    /**
     * HorizontalScrollView 滚到当前 tab 并且居中显示
     */
    private fun scrollToCurrentTab() {
        if (tabCount <= 0) {
            return
        }

        val tabView = mTabsContainer.getChildAt(mCurrentTab)
        val offset = (mCurrentPositionOffset * tabView.width).toInt()
        /* 当前 Tab 的 left + 当前 Tab 的 Width 乘以 positionOffset */
        var newScrollX = mTabsContainer.getChildAt(mCurrentTab).left + offset

        if (mCurrentTab > 0 || offset > 0) {
            /* HorizontalScrollView 移动到当前 tab 并居中 */
            newScrollX -= width / 2 - paddingLeft
            calcIndicatorRect()
            newScrollX += (mTabRect.right - mTabRect.left) / 2
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX
            /*
             * scrollTo（int x, int y）: x,y代表的不是坐标点,而是偏移量
             * x: 表示离起始位置的x水平方向的偏移量
             * y: 表示离起始位置的y垂直方向的偏移量
             */
            scrollTo(newScrollX, 0)
        }

        if (mNormalTextSize != mSelectTextSize && mSelectTextSize != 0f) {
            when (mCurrentTab) {
                0 -> {
                    val nextTabView = mTabsContainer.getChildAt(mCurrentTab + 1)

                    tabView.findViewById<TextView>(R.id.tv_tab_title)
                            .setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectTextSize - Math.abs((mSelectTextSize - mNormalTextSize) * mCurrentPositionOffset))
                    nextTabView.findViewById<TextView>(R.id.tv_tab_title)
                            .setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize + Math.abs((mSelectTextSize - mNormalTextSize) * mCurrentPositionOffset))
                }
                tabCount - 1 -> {
                    val preTabView = mTabsContainer.getChildAt(mCurrentTab - 1)

                    tabView.findViewById<TextView>(R.id.tv_tab_title)
                            .setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectTextSize - Math.abs((mSelectTextSize - mNormalTextSize) * mCurrentPositionOffset))
                    preTabView.findViewById<TextView>(R.id.tv_tab_title)
                            .setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize + Math.abs((mSelectTextSize - mNormalTextSize) * mCurrentPositionOffset))
                }
                else -> {
                    val nextTabView = mTabsContainer.getChildAt(mCurrentTab + 1)

                    tabView.findViewById<TextView>(R.id.tv_tab_title)
                            .setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectTextSize - Math.abs((mSelectTextSize - mNormalTextSize) * mCurrentPositionOffset))
                    nextTabView.findViewById<TextView>(R.id.tv_tab_title)
                            .setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize + Math.abs((mSelectTextSize - mNormalTextSize) * mCurrentPositionOffset))
                }
            }
        }
    }

    private fun updateTabSelection(position: Int) {
        for (i in 0 until tabCount) {
            val tabView = mTabsContainer.getChildAt(i)
            val isSelect = i == position
            val tabTitle = tabView.findViewById<TextView>(R.id.tv_tab_title)

            if (tabTitle != null) {
                tabTitle.setTextColor(if (isSelect) mTextSelectColor else mTextNormalColor)
//                if (mSelectTextSize != 0f) {
//                    tabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, if (isSelect) mSelectTextSize else mNormalTextSize)
//                }
                if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                    tabTitle.paint.isFakeBoldText = isSelect
                }
            }
        }
    }

    private fun calcIndicatorRect() {
        val currentTabView = mTabsContainer.getChildAt(mCurrentTab)
        var left = currentTabView.left.toFloat()
        var right = currentTabView.right.toFloat()

        // for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            val tabTitle = currentTabView.findViewById<TextView>(R.id.tv_tab_title)
            mTextPaint.textSize = mNormalTextSize
            val textWidth = mTextPaint.measureText(tabTitle.text.toString())
            margin = (right - left - textWidth) / 2
        }

        if (mCurrentTab < tabCount - 1) {
            val nextTabView = mTabsContainer.getChildAt(mCurrentTab + 1)
            val nextTabLeft = nextTabView.left.toFloat()
            val nextTabRight = nextTabView.right.toFloat()

            left += mCurrentPositionOffset * (nextTabLeft - left)
            right += mCurrentPositionOffset * (nextTabRight - right)

            // for mIndicatorWidthEqualTitle
            if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
                val nextTabTitle = nextTabView.findViewById<TextView>(R.id.tv_tab_title)
                mTextPaint.textSize = mNormalTextSize
                val nextTextWidth = mTextPaint.measureText(nextTabTitle.text.toString())
                val nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2
                margin += mCurrentPositionOffset * (nextMargin - margin)
            }
        }

        mIndicatorRect.left = left.toInt()
        mIndicatorRect.right = right.toInt()
        // for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            mIndicatorRect.left = (left + margin - 1).toInt()
            mIndicatorRect.right = (right - margin - 1f).toInt()
        }

        mTabRect.left = left.toInt()
        mTabRect.right = right.toInt()

        if (mIndicatorWidth < 0) {   // indicatorWidth 小于 0 时

        } else {// indicatorWidth 大于 0 时,圆角矩形以及三角形
            var indicatorLeft = currentTabView.left + (currentTabView.width - mIndicatorWidth) / 2

            if (mCurrentTab < tabCount - 1) {
                val nextTab = mTabsContainer.getChildAt(mCurrentTab + 1)
                indicatorLeft += mCurrentPositionOffset * (currentTabView.width / 2 + nextTab.width / 2)
            }

            mIndicatorRect.left = indicatorLeft.toInt()
            mIndicatorRect.right = (mIndicatorRect.left + mIndicatorWidth).toInt()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isInEditMode || tabCount <= 0) {
            return
        }

        val height = height
        val paddingLeft = paddingLeft
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.strokeWidth = mDividerWidth
            mDividerPaint.color = mDividerColor
            (0 until tabCount - 1)
                    .map { mTabsContainer.getChildAt(it) }
                    .forEach { canvas.drawLine((paddingLeft + it.right).toFloat(), mDividerPadding, (paddingLeft + it.right).toFloat(), height - mDividerPadding, mDividerPaint) }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.color = mUnderlineColor
            if (mUnderlineGravity == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft.toFloat(), height - mUnderlineHeight, (mTabsContainer.width + paddingLeft).toFloat(), height.toFloat(), mRectPaint)
            } else {
                canvas.drawRect(paddingLeft.toFloat(), 0f, (mTabsContainer.width + paddingLeft).toFloat(), mUnderlineHeight, mRectPaint)
            }
        }

        // draw indicator line

        calcIndicatorRect()
        if (mIndicatorStyle == STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                mTrianglePaint.color = mIndicatorColor
                mTrianglePath.reset()
                mTrianglePath.moveTo((paddingLeft + mIndicatorRect.left).toFloat(), height.toFloat())
                mTrianglePath.lineTo((paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2).toFloat(), height - mIndicatorHeight)
                mTrianglePath.lineTo((paddingLeft + mIndicatorRect.right).toFloat(), height.toFloat())
                mTrianglePath.close()
                canvas.drawPath(mTrianglePath, mTrianglePaint)
            }
        } else if (mIndicatorStyle == STYLE_BLOCK) {
            if (mIndicatorHeight < 0) {
                mIndicatorHeight = height.toFloat() - mIndicatorMarginTop - mIndicatorMarginBottom
            }

            if (mIndicatorHeight > 0) {
                if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                    mIndicatorCornerRadius = mIndicatorHeight / 2
                }

                mIndicatorDrawable.setColor(mIndicatorColor)
                mIndicatorDrawable.setBounds(paddingLeft + mIndicatorMarginLeft.toInt() + mIndicatorRect.left,
                        mIndicatorMarginTop.toInt(), (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight).toInt(),
                        (mIndicatorMarginTop + mIndicatorHeight).toInt())
                mIndicatorDrawable.cornerRadius = mIndicatorCornerRadius
                mIndicatorDrawable.draw(canvas)
            }
        } else {
            if (mIndicatorHeight > 0) {
                mIndicatorDrawable.setColor(mIndicatorColor)

                if (mIndicatorGravity == Gravity.BOTTOM) {
                    mIndicatorDrawable.setBounds(paddingLeft + mIndicatorMarginLeft.toInt() + mIndicatorRect.left,
                            height - mIndicatorHeight.toInt() - mIndicatorMarginBottom.toInt(),
                            paddingLeft + mIndicatorRect.right - mIndicatorMarginRight.toInt(),
                            height - mIndicatorMarginBottom.toInt())
                } else {
                    mIndicatorDrawable.setBounds(paddingLeft + mIndicatorMarginLeft.toInt() + mIndicatorRect.left,
                            mIndicatorMarginTop.toInt(),
                            paddingLeft + mIndicatorRect.right - mIndicatorMarginRight.toInt(),
                            mIndicatorHeight.toInt() + mIndicatorMarginTop.toInt())
                }
                mIndicatorDrawable.cornerRadius = mIndicatorCornerRadius
                mIndicatorDrawable.draw(canvas)
            }
        }
    }

    fun setCurrentTab(currentTab: Int, smoothScroll: Boolean) {
        mCurrentTab = currentTab
        mViewPager?.setCurrentItem(currentTab, smoothScroll)
    }

    fun setIndicatorGravity(indicatorGravity: Int) {
        mIndicatorGravity = indicatorGravity
        invalidate()
    }

    fun setIndicatorMargin(indicatorMarginLeft: Float, indicatorMarginTop: Float,
                           indicatorMarginRight: Float, indicatorMarginBottom: Float) {
        mIndicatorMarginLeft = DensityUtils.dp2px(indicatorMarginLeft).toFloat()
        mIndicatorMarginTop = DensityUtils.dp2px(indicatorMarginTop).toFloat()
        mIndicatorMarginRight = DensityUtils.dp2px(indicatorMarginRight).toFloat()
        mIndicatorMarginBottom = DensityUtils.dp2px(indicatorMarginBottom).toFloat()
        invalidate()
    }

    fun setIndicatorWidthEqualTitle(indicatorWidthEqualTitle: Boolean) {
        mIndicatorWidthEqualTitle = indicatorWidthEqualTitle
        invalidate()
    }

    fun setUnderlineGravity(underlineGravity: Int) {
        mUnderlineGravity = underlineGravity
        invalidate()
    }

    fun setTextsize(textSize: Float) {
        mNormalTextSize = DensityUtils.sp2px(textSize).toFloat()
        updateTabStyles()
    }

    fun setTextUnselectColor(textUnSelectColor: Int) {
        mTextNormalColor = textUnSelectColor
        updateTabStyles()
    }

    fun setSnapOnTabClick(snapOnTabClick: Boolean) {
        mSnapOnTabClick = snapOnTabClick
    }

    fun getTitleView(tab: Int): TextView {
        val tabView = mTabsContainer.getChildAt(tab)
        return tabView.findViewById(R.id.tv_tab_title)
    }

    fun setOnTabSelectListener(listener: OnTabSelectListener) {
        mListener = listener
    }

    interface OnTabSelectListener {
        fun onTabSelect(position: Int)

        fun onTabReselect(position: Int)
    }

    companion object {
        private const val STYLE_NORMAL = 0
        private const val STYLE_TRIANGLE = 1
        private const val STYLE_BLOCK = 2

        private const val TEXT_BOLD_NONE = 0
        private const val TEXT_BOLD_WHEN_SELECT = 1
        private const val TEXT_BOLD_BOTH = 2

        private const val TEXT_GRAVITY_TOP = 0
        private const val TEXT_GRAVITY_CENTER = 1
        private const val TEXT_GRAVITY_BOTTOM = 2
    }

}
