package cc.ibooker.ibookereditorklib

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.v4.widget.NestedScrollView
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

/**
 * 书客编辑器 - 预览界面 - 自定义WebView
 * Created by 邹峰立 on 2018/2/11.
 */
class IbookerEditorPreView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : NestedScrollView(context, attrs, defStyleAttr) {
    var ibookerTitleTv: TextView? = null
    var lineView: View? = null
    var ibookerEditorWebView: IbookerEditorWebView? = null

    init {

        isVerticalScrollBarEnabled = false
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        init(context)
    }

    /**
     * 初始化
     */
    private fun init(context: Context) {
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        linearLayout.orientation = LinearLayout.VERTICAL
        addView(linearLayout)

        ibookerTitleTv = TextView(context)
        val titleParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, IbookerEditorUtil.dpToPx(context, 50f))
        val dp10 = IbookerEditorUtil.dpToPx(context, 10f)
        titleParams.leftMargin = dp10
        titleParams.rightMargin = dp10
        ibookerTitleTv!!.layoutParams = titleParams
        ibookerTitleTv!!.setBackgroundColor(resources.getColor(android.R.color.transparent))
        ibookerTitleTv!!.setSingleLine(true)
        ibookerTitleTv!!.setLines(1)
        ibookerTitleTv!!.ellipsize = TextUtils.TruncateAt.END
        ibookerTitleTv!!.setTextColor(Color.parseColor("#444444"))
        ibookerTitleTv!!.textSize = 18f
        ibookerTitleTv!!.setLineSpacing(4f, 1.3f)
        ibookerTitleTv!!.hint = "标题"
        ibookerTitleTv!!.gravity = Gravity.CENTER_VERTICAL
        linearLayout.addView(ibookerTitleTv)

        lineView = View(context)
        lineView!!.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1)
        lineView!!.setBackgroundColor(Color.parseColor("#BABABA"))
        linearLayout.addView(lineView)

        ibookerEditorWebView = IbookerEditorWebView(context)
        ibookerEditorWebView!!.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        linearLayout.addView(ibookerEditorWebView)
    }

    /**
     * 设置WebView控件背景颜色
     *
     * @param color 背景颜色
     */
    fun setIbookerEditorWebViewBackgroundColor(@ColorInt color: Int): IbookerEditorPreView {
        ibookerEditorWebView!!.setBackgroundColor(color)
        return this
    }

    /**
     * 设置标题显示 或者隐藏
     *
     * @param visibility View.GONE,View.VISIBLE,View.INVISIBLE
     */
    fun setIbookerTitleTdVisibility(visibility: Int): IbookerEditorPreView {
        if (visibility == View.GONE || visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            if (ibookerTitleTv != null)
                ibookerTitleTv!!.visibility = visibility
            if (lineView != null)
                lineView!!.visibility = visibility
        }
        return this
    }

    /**
     * 设置标题字体大小
     *
     * @param size 字体大小
     */
    fun setIbookerTitleTdTextSize(size: Float): IbookerEditorPreView {
        ibookerTitleTv!!.textSize = size
        return this
    }

    /**
     * 设置标题字体颜色
     *
     * @param color 字体颜色
     */
    fun setIbookerTitleTdTextColor(@ColorInt color: Int): IbookerEditorPreView {
        ibookerTitleTv!!.setTextColor(color)
        return this
    }

    /**
     * 设置标题hint内容
     *
     * @param hint hint内容
     */
    fun setIbookerTitleTdHint(hint: CharSequence): IbookerEditorPreView {
        ibookerTitleTv!!.hint = hint
        return this
    }

    /**
     * 设置标题hint颜色
     *
     * @param color hint颜色
     */
    fun setIbookerTitleTdHintTextColor(@ColorInt color: Int): IbookerEditorPreView {
        ibookerTitleTv!!.setHintTextColor(color)
        return this
    }

    /**
     * 设置线条的背景颜色
     *
     * @param color 颜色
     */
    fun setLineViewBackgroundColor(@ColorInt color: Int): IbookerEditorPreView {
        lineView!!.setBackgroundColor(color)
        return this
    }

    /**
     * 设置线条显示或者隐藏
     *
     * @param visibility View.GONE,View.VISIBLE,View.INVISIBLE
     */
    fun setLineViewVisibility(visibility: Int): IbookerEditorPreView {
        if (visibility == View.GONE || visibility == View.VISIBLE || visibility == View.INVISIBLE)
            lineView!!.visibility = visibility
        return this
    }
}
