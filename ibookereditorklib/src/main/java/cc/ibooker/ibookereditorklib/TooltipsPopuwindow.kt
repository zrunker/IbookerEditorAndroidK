package cc.ibooker.ibookereditorklib

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView

/**
 * Tooltips 提示框
 */
class TooltipsPopuwindow @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : PopupWindow(context, attrs, defStyleAttr) {
    private val tooltipsTv: TextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.ibooker_editor_layout_tooltips, null)
        tooltipsTv = view.findViewById(R.id.tv_name)
        contentView = view
        isFocusable = true
        setBackgroundDrawable(BitmapDrawable())
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        width = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    fun setTooltipsTv(text: String): TooltipsPopuwindow {
        tooltipsTv.text = text
        return this
    }

    /**
     * 显示在指定View的正上方
     *
     * @param view    指定的View
     * @param yOffset Y轴偏移量
     */
    fun showViewTop(context: Context?, view: View, yOffset: Int): TooltipsPopuwindow {
        if (context != null) {
            // 获取需要在其上方显示的控件的位置信息
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            // 获取自身的长宽高
            this.contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val popupHeight = this.contentView.measuredHeight
            showAtLocation(view, Gravity.NO_GRAVITY, location[0] - 5, location[1] - popupHeight - yOffset)
        }
        return this
    }

}
