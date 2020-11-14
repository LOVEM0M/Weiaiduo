package com.miyin.zhenbaoqi.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.AlignmentSpan
import android.text.style.BackgroundColorSpan
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.LeadingMarginSpan
import android.text.style.LineHeightSpan
import android.text.style.MaskFilterSpan
import android.text.style.RelativeSizeSpan
import android.text.style.ReplacementSpan
import android.text.style.ScaleXSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.text.style.UpdateAppearance
import android.widget.TextView

import com.miyin.zhenbaoqi.App
import com.orhanobut.logger.Logger

import java.io.Serializable
import java.lang.ref.WeakReference

class SpanUtils() {

    private var mTextView: TextView? = null
    private var mText: CharSequence? = null
    private var flag: Int = 0
    private var foregroundColor: Int = 0
    private var backgroundColor: Int = 0
    private var lineHeight: Int = 0
    private var alignLine: Int = 0
    private var quoteColor: Int = 0
    private var stripeWidth: Int = 0
    private var quoteGapWidth: Int = 0
    private var first: Int = 0
    private var rest: Int = 0
    private var bulletColor: Int = 0
    private var bulletRadius: Int = 0
    private var bulletGapWidth: Int = 0
    private var fontSize: Int = 0
    private var fontSizeIsDp: Boolean = false
    private var proportion: Float = 0.toFloat()
    private var xProportion: Float = 0.toFloat()
    private var isStrikethrough: Boolean = false
    private var isUnderline: Boolean = false
    private var isSuperscript: Boolean = false
    private var isSubscript: Boolean = false
    private var isBold: Boolean = false
    private var isItalic: Boolean = false
    private var isBoldItalic: Boolean = false
    private var fontFamily: String? = null
    private var typeface: Typeface? = null
    private var alignment: Layout.Alignment? = null
    private var verticalAlign: Int = 0
    private var clickSpan: ClickableSpan? = null
    private var url: String? = null
    private var blurRadius: Float = 0.toFloat()
    private var style: BlurMaskFilter.Blur? = null
    private var shader: Shader? = null
    private var shadowRadius: Float = 0.toFloat()
    private var shadowDx: Float = 0.toFloat()
    private var shadowDy: Float = 0.toFloat()
    private var shadowColor: Int = 0
    private var spans: Array<out Any>? = null

    private var imageBitmap: Bitmap? = null
    private var imageDrawable: Drawable? = null
    private var imageUri: Uri? = null
    private var imageResourceId: Int = 0
    private var alignImage: Int = 0

    private var spaceSize: Int = 0
    private var spaceColor: Int = 0

    private val mBuilder: SerializableSpannableStringBuilder

    private var mType: Int = 0
    private val mTypeCharSequence = 0
    private val mTypeImage = 1
    private val mTypeSpace = 2

    private constructor(textView: TextView) : this() {
        mTextView = textView
    }

    @IntDef(ALIGN_BOTTOM, ALIGN_BASELINE, ALIGN_CENTER, ALIGN_TOP)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Align

    init {
        mBuilder = SerializableSpannableStringBuilder()
        mText = ""
        mType = -1
        setDefault()
    }

    private fun setDefault() {
        flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        foregroundColor = COLOR_DEFAULT
        backgroundColor = COLOR_DEFAULT
        lineHeight = -1
        quoteColor = COLOR_DEFAULT
        first = -1
        bulletColor = COLOR_DEFAULT
        fontSize = -1
        proportion = -1f
        xProportion = -1f
        isStrikethrough = false
        isUnderline = false
        isSuperscript = false
        isSubscript = false
        isBold = false
        isItalic = false
        isBoldItalic = false
        fontFamily = null
        typeface = null
        alignment = null
        verticalAlign = -1
        clickSpan = null
        url = null
        blurRadius = -1f
        shader = null
        shadowRadius = -1f
        spans = null

        imageBitmap = null
        imageDrawable = null
        imageUri = null
        imageResourceId = -1

        spaceSize = -1
    }

    fun setFlag(flag: Int): SpanUtils {
        this.flag = flag
        return this
    }

    fun setForegroundColor(@ColorInt color: Int): SpanUtils {
        this.foregroundColor = color
        return this
    }

    fun setBackgroundColor(@ColorInt color: Int): SpanUtils {
        this.backgroundColor = color
        return this
    }

    @JvmOverloads
    fun setLineHeight(@IntRange(from = 0) lineHeight: Int, @Align align: Int = ALIGN_CENTER): SpanUtils {
        this.lineHeight = lineHeight
        this.alignLine = align
        return this
    }

    @JvmOverloads
    fun setQuoteColor(@ColorInt color: Int, @IntRange(from = 1) stripeWidth: Int = 2, @IntRange(from = 0) gapWidth: Int = 2): SpanUtils {
        this.quoteColor = color
        this.stripeWidth = stripeWidth
        this.quoteGapWidth = gapWidth
        return this
    }

    fun setLeadingMargin(@IntRange(from = 0) first: Int,
                         @IntRange(from = 0) rest: Int): SpanUtils {
        this.first = first
        this.rest = rest
        return this
    }

    fun setBullet(@IntRange(from = 0) gapWidth: Int): SpanUtils {
        return setBullet(0, 3, gapWidth)
    }

    fun setBullet(@ColorInt color: Int, @IntRange(from = 0) radius: Int, @IntRange(from = 0) gapWidth: Int): SpanUtils {
        this.bulletColor = color
        this.bulletRadius = radius
        this.bulletGapWidth = gapWidth
        return this
    }

    @JvmOverloads
    fun setFontSize(@IntRange(from = 0) size: Int, isSp: Boolean = false): SpanUtils {
        this.fontSize = size
        this.fontSizeIsDp = isSp
        return this
    }

    fun setFontProportion(proportion: Float): SpanUtils {
        this.proportion = proportion
        return this
    }

    fun setFontXProportion(proportion: Float): SpanUtils {
        this.xProportion = proportion
        return this
    }

    fun setStrikethrough(): SpanUtils {
        this.isStrikethrough = true
        return this
    }

    fun setUnderline(): SpanUtils {
        this.isUnderline = true
        return this
    }

    fun setSuperscript(): SpanUtils {
        this.isSuperscript = true
        return this
    }

    fun setSubscript(): SpanUtils {
        this.isSubscript = true
        return this
    }

    fun setBold(): SpanUtils {
        isBold = true
        return this
    }

    fun setItalic(): SpanUtils {
        isItalic = true
        return this
    }

    fun setBoldItalic(): SpanUtils {
        isBoldItalic = true
        return this
    }

    fun setFontFamily(fontFamily: String): SpanUtils {
        this.fontFamily = fontFamily
        return this
    }

    fun setTypeface(typeface: Typeface): SpanUtils {
        this.typeface = typeface
        return this
    }

    fun setHorizontalAlign(alignment: Layout.Alignment): SpanUtils {
        this.alignment = alignment
        return this
    }

    fun setVerticalAlign(@Align align: Int): SpanUtils {
        this.verticalAlign = align
        return this
    }

    fun setClickSpan(clickSpan: ClickableSpan): SpanUtils {
        if (mTextView != null && mTextView?.movementMethod == null) {
            mTextView?.movementMethod = LinkMovementMethod.getInstance()
        }
        this.clickSpan = clickSpan
        return this
    }

    fun setUrl(url: String): SpanUtils {
        if (mTextView != null && mTextView?.movementMethod == null) {
            mTextView?.movementMethod = LinkMovementMethod.getInstance()
        }
        this.url = url
        return this
    }

    fun setBlur(@FloatRange(from = 0.0, fromInclusive = false) radius: Float, style: BlurMaskFilter.Blur): SpanUtils {
        this.blurRadius = radius
        this.style = style
        return this
    }

    fun setShader(shader: Shader): SpanUtils {
        this.shader = shader
        return this
    }

    fun setShadow(@FloatRange(from = 0.0, fromInclusive = false) radius: Float, dx: Float, dy: Float, shadowColor: Int): SpanUtils {
        this.shadowRadius = radius
        this.shadowDx = dx
        this.shadowDy = dy
        this.shadowColor = shadowColor
        return this
    }

    fun setSpans(vararg spans: Any): SpanUtils {
        if (spans.isNotEmpty()) {
            this.spans = spans
        }
        return this
    }

    fun append(text: CharSequence): SpanUtils {
        apply(mTypeCharSequence)
        mText = text
        return this
    }

    fun appendLine(): SpanUtils {
        apply(mTypeCharSequence)
        mText = LINE_SEPARATOR
        return this
    }

    fun appendLine(text: CharSequence): SpanUtils {
        apply(mTypeCharSequence)
        mText = text.toString() + LINE_SEPARATOR
        return this
    }

    @JvmOverloads
    fun appendImage(bitmap: Bitmap, @Align align: Int = ALIGN_BOTTOM): SpanUtils {
        apply(mTypeImage)
        this.imageBitmap = bitmap
        this.alignImage = align
        return this
    }

    @JvmOverloads
    fun appendImage(drawable: Drawable, @Align align: Int = ALIGN_BOTTOM): SpanUtils {
        apply(mTypeImage)
        this.imageDrawable = drawable
        this.alignImage = align
        return this
    }

    @JvmOverloads
    fun appendImage(uri: Uri, @Align align: Int = ALIGN_BOTTOM): SpanUtils {
        apply(mTypeImage)
        this.imageUri = uri
        this.alignImage = align
        return this
    }

    @JvmOverloads
    fun appendImage(@DrawableRes resourceId: Int, @Align align: Int = ALIGN_BOTTOM): SpanUtils {
        apply(mTypeImage)
        this.imageResourceId = resourceId
        this.alignImage = align
        return this
    }

    @JvmOverloads
    fun appendSpace(@IntRange(from = 0) size: Int, @ColorInt color: Int = Color.TRANSPARENT): SpanUtils {
        apply(mTypeSpace)
        spaceSize = size
        spaceColor = color
        return this
    }

    private fun apply(type: Int) {
        applyLast()
        mType = type
    }

    fun get(): SpannableStringBuilder {
        return mBuilder
    }

    fun create(): SpannableStringBuilder {
        applyLast()
        if (mTextView != null) {
            mTextView?.text = mBuilder
        }
        return mBuilder
    }

    private fun applyLast() {
        when (mType) {
            mTypeCharSequence -> updateCharCharSequence()
            mTypeImage -> updateImage()
            mTypeSpace -> updateSpace()
        }
        setDefault()
    }

    private fun updateCharCharSequence() {
        if (mText!!.isEmpty()) return
        var start = mBuilder.length
        if (start == 0 && lineHeight != -1) {
            mBuilder.append(Character.toString(2.toChar()))
                    .append("\n")
                    .setSpan(AbsoluteSizeSpan(0), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            start = 2
        }
        mBuilder.append(mText)
        val end = mBuilder.length
        if (verticalAlign != -1) {
            mBuilder.setSpan(VerticalAlignSpan(verticalAlign), start, end, flag)
        }
        if (foregroundColor != COLOR_DEFAULT) {
            mBuilder.setSpan(ForegroundColorSpan(foregroundColor), start, end, flag)
        }
        if (backgroundColor != COLOR_DEFAULT) {
            mBuilder.setSpan(BackgroundColorSpan(backgroundColor), start, end, flag)
        }
        if (first != -1) {
            mBuilder.setSpan(LeadingMarginSpan.Standard(first, rest), start, end, flag)
        }
        if (quoteColor != COLOR_DEFAULT) {
            mBuilder.setSpan(CustomQuoteSpan(quoteColor, stripeWidth, quoteGapWidth), start, end, flag)
        }
        if (bulletColor != COLOR_DEFAULT) {
            mBuilder.setSpan(CustomBulletSpan(bulletColor, bulletRadius, bulletGapWidth), start, end, flag)
        }
        if (fontSize != -1) {
            mBuilder.setSpan(AbsoluteSizeSpan(fontSize, fontSizeIsDp), start, end, flag)
        }
        if (proportion != -1f) {
            mBuilder.setSpan(RelativeSizeSpan(proportion), start, end, flag)
        }
        if (xProportion != -1f) {
            mBuilder.setSpan(ScaleXSpan(xProportion), start, end, flag)
        }
        if (lineHeight != -1) {
            mBuilder.setSpan(CustomLineHeightSpan(lineHeight, alignLine), start, end, flag)
        }
        if (isStrikethrough) {
            mBuilder.setSpan(StrikethroughSpan(), start, end, flag)
        }
        if (isUnderline) {
            mBuilder.setSpan(UnderlineSpan(), start, end, flag)
        }
        if (isSuperscript) {
            mBuilder.setSpan(SuperscriptSpan(), start, end, flag)
        }
        if (isSubscript) {
            mBuilder.setSpan(SubscriptSpan(), start, end, flag)
        }
        if (isBold) {
            mBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, flag)
        }
        if (isItalic) {
            mBuilder.setSpan(StyleSpan(Typeface.ITALIC), start, end, flag)
        }
        if (isBoldItalic) {
            mBuilder.setSpan(StyleSpan(Typeface.BOLD_ITALIC), start, end, flag)
        }
        if (fontFamily != null) {
            mBuilder.setSpan(TypefaceSpan(fontFamily), start, end, flag)
        }
        if (typeface != null) {
            mBuilder.setSpan(CustomTypefaceSpan(typeface!!), start, end, flag)
        }
        if (alignment != null) {
            mBuilder.setSpan(AlignmentSpan.Standard(alignment!!), start, end, flag)
        }
        if (clickSpan != null) {
            mBuilder.setSpan(clickSpan, start, end, flag)
        }
        if (url != null) {
            mBuilder.setSpan(URLSpan(url), start, end, flag)
        }
        if (blurRadius != -1f) {
            mBuilder.setSpan(MaskFilterSpan(BlurMaskFilter(blurRadius, style)), start, end, flag)
        }
        if (shader != null) {
            mBuilder.setSpan(ShaderSpan(shader!!), start, end, flag)
        }
        if (shadowRadius != -1f) {
            mBuilder.setSpan(ShadowSpan(shadowRadius, shadowDx, shadowDy, shadowColor), start, end, flag)
        }
        if (spans != null) {
            for (span in spans!!) {
                mBuilder.setSpan(span, start, end, flag)
            }
        }
    }

    private fun updateImage() {
        val start = mBuilder.length
        mText = "<img>"
        updateCharCharSequence()
        val end = mBuilder.length
        when {
            imageBitmap != null -> mBuilder.setSpan(CustomImageSpan(imageBitmap!!, alignImage), start, end, flag)
            imageDrawable != null -> mBuilder.setSpan(CustomImageSpan(imageDrawable!!, alignImage), start, end, flag)
            imageUri != null -> mBuilder.setSpan(CustomImageSpan(imageUri!!, alignImage), start, end, flag)
            imageResourceId != -1 -> mBuilder.setSpan(CustomImageSpan(imageResourceId, alignImage), start, end, flag)
        }
    }

    private fun updateSpace() {
        val start = mBuilder.length
        mText = "< >"
        updateCharCharSequence()
        val end = mBuilder.length
        mBuilder.setSpan(SpaceSpan(spaceSize, spaceColor), start, end, flag)
    }

    internal class VerticalAlignSpan(val mVerticalAlignment: Int) : ReplacementSpan() {

        override fun getSize(paint: Paint, content: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
            var text = content
            text = text.subSequence(start, end)
            return paint.measureText(text.toString()).toInt()
        }

        override fun draw(canvas: Canvas, content: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
            var text = content
            text = text.subSequence(start, end)
            val fm = paint.fontMetricsInt

            canvas.drawText(text.toString(), x, (y - ((y + fm.descent + y + fm.ascent) / 2 - (bottom + top) / 2)).toFloat(), paint)
        }

        companion object {
            val ALIGN_CENTER = 2
            val ALIGN_TOP = 3
        }
    }

    internal class CustomLineHeightSpan(private val height: Int, private val mVerticalAlignment: Int) : LineHeightSpan {

        override fun chooseHeight(text: CharSequence, start: Int, end: Int, spanstartv: Int, v: Int, fm: Paint.FontMetricsInt) {
            if (sfm == null) {
                sfm = Paint.FontMetricsInt()
                sfm!!.top = fm.top
                sfm!!.ascent = fm.ascent
                sfm!!.descent = fm.descent
                sfm!!.bottom = fm.bottom
                sfm!!.leading = fm.leading
            } else {
                fm.top = sfm!!.top
                fm.ascent = sfm!!.ascent
                fm.descent = sfm!!.descent
                fm.bottom = sfm!!.bottom
                fm.leading = sfm!!.leading
            }
            var need = height - (v + fm.descent - fm.ascent - spanstartv)
            if (need > 0) {
                when (mVerticalAlignment) {
                    ALIGN_TOP -> fm.descent += need
                    ALIGN_CENTER -> {
                        fm.descent += need / 2
                        fm.ascent -= need / 2
                    }
                    else -> fm.ascent -= need
                }
            }
            need = height - (v + fm.bottom - fm.top - spanstartv)
            if (need > 0) {
                when (mVerticalAlignment) {
                    ALIGN_TOP -> fm.bottom += need
                    ALIGN_CENTER -> {
                        fm.bottom += need / 2
                        fm.top -= need / 2
                    }
                    else -> fm.top -= need
                }
            }
            if (end == (text as Spanned).getSpanEnd(this)) {
                sfm = null
            }
        }

        companion object {
            const val ALIGN_CENTER = 2
            const val ALIGN_TOP = 3
            var sfm: Paint.FontMetricsInt? = null
        }
    }

    internal class SpaceSpan constructor(private val width: Int, color: Int = Color.TRANSPARENT) : ReplacementSpan() {
        private val paint = Paint()

        init {
            paint.color = color
            paint.style = Paint.Style.FILL
        }

        override fun getSize(paint: Paint, text: CharSequence,
                             @IntRange(from = 0) start: Int,
                             @IntRange(from = 0) end: Int,
                             fm: Paint.FontMetricsInt?): Int {
            return width
        }

        override fun draw(canvas: Canvas, text: CharSequence,
                          @IntRange(from = 0) start: Int,
                          @IntRange(from = 0) end: Int,
                          x: Float, top: Int, y: Int, bottom: Int,
                          paint: Paint) {
            canvas.drawRect(x, top.toFloat(), x + width, bottom.toFloat(), this.paint)
        }
    }

    internal class CustomQuoteSpan(private val color: Int, private val stripeWidth: Int, private val gapWidth: Int) : LeadingMarginSpan {

        override fun getLeadingMargin(first: Boolean): Int {
            return stripeWidth + gapWidth
        }

        override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int,
                                       top: Int, baseline: Int, bottom: Int,
                                       text: CharSequence, start: Int, end: Int,
                                       first: Boolean, layout: Layout) {
            val style = p.style
            val color = p.color

            p.style = Paint.Style.FILL
            p.color = this.color

            c.drawRect(x.toFloat(), top.toFloat(), (x + dir * stripeWidth).toFloat(), bottom.toFloat(), p)

            p.style = style
            p.color = color
        }
    }

    internal class CustomBulletSpan(private val color: Int, private val radius: Int, private val gapWidth: Int) : LeadingMarginSpan {

        private var sBulletPath: Path? = null

        override fun getLeadingMargin(first: Boolean): Int {
            return 2 * radius + gapWidth
        }

        override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int,
                                       top: Int, baseline: Int, bottom: Int,
                                       text: CharSequence, start: Int, end: Int,
                                       first: Boolean, l: Layout) {
            if ((text as Spanned).getSpanStart(this) == start) {
                val style = p.style
                val oldColor = p.color
                p.color = color
                p.style = Paint.Style.FILL
                if (c.isHardwareAccelerated) {
                    if (sBulletPath == null) {
                        sBulletPath = Path()
                        sBulletPath!!.addCircle(0.0f, 0.0f, radius.toFloat(), Path.Direction.CW)
                    }
                    c.save()
                    c.translate((x + dir * radius).toFloat(), (top + bottom) / 2.0f)
                    c.drawPath(sBulletPath!!, p)
                    c.restore()
                } else {
                    c.drawCircle((x + dir * radius).toFloat(), (top + bottom) / 2.0f, radius.toFloat(), p)
                }
                p.color = oldColor
                p.style = style
            }
        }
    }

    @SuppressLint("ParcelCreator")
    internal class CustomTypefaceSpan constructor(private val newType: Typeface) : TypefaceSpan("") {

        override fun updateDrawState(textPaint: TextPaint) {
            apply(textPaint, newType)
        }

        override fun updateMeasureState(paint: TextPaint) {
            apply(paint, newType)
        }

        private fun apply(paint: Paint, tf: Typeface) {
            val oldStyle: Int
            val old = paint.typeface
            oldStyle = old?.style ?: 0

            val fake = oldStyle and tf.style.inv()
            if (fake and Typeface.BOLD != 0) {
                paint.isFakeBoldText = true
            }

            if (fake and Typeface.ITALIC != 0) {
                paint.textSkewX = -0.25f
            }

            paint.shader
            paint.typeface = tf
        }
    }

    internal class CustomImageSpan : CustomDynamicDrawableSpan {
        private var mDrawable: Drawable? = null
        private var mContentUri: Uri? = null
        private var mResourceId: Int = 0

        override val drawable: Drawable
            get() {
                var drawable: Drawable? = null
                when {
                    mDrawable != null -> drawable = mDrawable
                    mContentUri != null -> {
                        val bitmap: Bitmap
                        try {
                            val `is` = App.context.contentResolver.openInputStream(mContentUri!!)
                            bitmap = BitmapFactory.decodeStream(`is`)
                            drawable = BitmapDrawable(App.context.resources, bitmap)
                            drawable.setBounds(
                                    0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight
                            )
                            `is`?.close()
                        } catch (e: Exception) {
                            Logger.e("Failed to loaded content $mContentUri", e)
                        }

                    }
                    else -> try {
                        drawable = ContextCompat.getDrawable(App.context, mResourceId)
                        drawable!!.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                    } catch (e: Exception) {
                        Logger.e("Unable to find resource: $mResourceId")
                    }
                }
                return drawable!!
            }

        constructor(b: Bitmap, verticalAlignment: Int) : super(verticalAlignment) {
            mDrawable = BitmapDrawable(App.context.resources, b)
            mDrawable!!.setBounds(0, 0, mDrawable!!.intrinsicWidth, mDrawable!!.intrinsicHeight)
        }

        constructor(d: Drawable, verticalAlignment: Int) : super(verticalAlignment) {
            mDrawable = d
            mDrawable!!.setBounds(0, 0, mDrawable!!.intrinsicWidth, mDrawable!!.intrinsicHeight)
        }

        constructor(uri: Uri, verticalAlignment: Int) : super(verticalAlignment) {
            mContentUri = uri
        }

        constructor(@DrawableRes resourceId: Int, verticalAlignment: Int) : super(verticalAlignment) {
            mResourceId = resourceId
        }
    }

    internal abstract class CustomDynamicDrawableSpan : ReplacementSpan {
        private val mVerticalAlignment: Int

        abstract val drawable: Drawable

        private val cachedDrawable: Drawable
            get() {
                val wr = mDrawableRef
                var d: Drawable? = null
                if (wr != null) {
                    d = wr.get()
                }
                if (d == null) {
                    d = drawable
                    mDrawableRef = WeakReference(d)
                }
                return d
            }

        private var mDrawableRef: WeakReference<Drawable>? = null

        private constructor() {
            mVerticalAlignment = ALIGN_BOTTOM
        }

        internal constructor(verticalAlignment: Int) {
            mVerticalAlignment = verticalAlignment
        }

        override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
            val d = cachedDrawable
            val rect = d.bounds
            if (fm != null) {
                val lineHeight = fm.bottom - fm.top
                if (lineHeight < rect.height()) {
                    when (mVerticalAlignment) {
                        ALIGN_TOP -> {
                            fm.top = fm.top
                            fm.bottom = rect.height() + fm.top
                        }
                        ALIGN_CENTER -> {
                            fm.top = -rect.height() / 2 - lineHeight / 4
                            fm.bottom = rect.height() / 2 - lineHeight / 4
                        }
                        else -> {
                            fm.top = -rect.height() + fm.bottom
                            fm.bottom = fm.bottom
                        }
                    }
                    fm.ascent = fm.top
                    fm.descent = fm.bottom
                }
            }
            return rect.right
        }

        override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
            val d = cachedDrawable
            val rect = d.bounds
            canvas.save()
            val transY: Float
            val lineHeight = bottom - top

            if (rect.height() < lineHeight) {
                transY = when (mVerticalAlignment) {
                    ALIGN_TOP -> top.toFloat()
                    ALIGN_CENTER -> ((bottom + top - rect.height()) / 2).toFloat()
                    ALIGN_BASELINE -> (y - rect.height()).toFloat()
                    else -> (bottom - rect.height()).toFloat()
                }
                canvas.translate(x, transY)
            } else {
                canvas.translate(x, top.toFloat())
            }
            d.draw(canvas)
            canvas.restore()
        }

        companion object {
            const val ALIGN_BOTTOM = 0
            const val ALIGN_BASELINE = 1
            const val ALIGN_CENTER = 2
            const val ALIGN_TOP = 3
        }
    }

    internal class ShaderSpan(private val mShader: Shader) : CharacterStyle(), UpdateAppearance {
        override fun updateDrawState(tp: TextPaint) {
            tp.shader = mShader
        }
    }

    internal class ShadowSpan(private val radius: Float, private val dx: Float, private val dy: Float, private val shadowColor: Int) : CharacterStyle(), UpdateAppearance {
        override fun updateDrawState(tp: TextPaint) {
            tp.setShadowLayer(radius, dx, dy, shadowColor)
        }
    }

    private class SerializableSpannableStringBuilder : SpannableStringBuilder(), Serializable {
        companion object {
            private const val serialVersionUID = 4909567650765875771L
        }
    }

    companion object {

        private const val COLOR_DEFAULT = -0x1000001

        const val ALIGN_BOTTOM = 0
        const val ALIGN_BASELINE = 1
        const val ALIGN_CENTER = 2
        const val ALIGN_TOP = 3

        private val LINE_SEPARATOR = System.getProperty("line.separator")

        fun with(textView: TextView): SpanUtils {
            return SpanUtils(textView)
        }
    }

}
