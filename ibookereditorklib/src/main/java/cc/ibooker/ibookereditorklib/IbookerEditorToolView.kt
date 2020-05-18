package cc.ibooker.ibookereditorklib

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.LinearLayout

/**
 * 书客编辑器工具栏
 * Created by 邹峰立 on 2018/1/17.
 */
class IbookerEditorToolView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : HorizontalScrollView(context, attrs, defStyleAttr) {
    // getter
    private var toolLayout: LinearLayout? = null
    var boldIBtn: ImageButton? = null
    var italicIBtn: ImageButton? = null
    var strikeoutIBtn: ImageButton? = null
    var underlineIBtn: ImageButton? = null
    var capitalsIBtn: ImageButton? = null
    var uppercaseIBtn: ImageButton? = null
    var lowercaseIBtn: ImageButton? = null
    var h1IBtn: ImageButton? = null
    var h2IBtn: ImageButton? = null
    var h3IBtn: ImageButton? = null
    var h4IBtn: ImageButton? = null
    var h5IBtn: ImageButton? = null
    var h6IBtn: ImageButton? = null
    var linkIBtn: ImageButton? = null
    var quoteIBtn: ImageButton? = null
    var codeIBtn: ImageButton? = null
    var imguIBtn: ImageButton? = null
    var olIBtn: ImageButton? = null
    var ulIBtn: ImageButton? = null
    var unselectedIBtn: ImageButton? = null
    var selectedIBtn: ImageButton? = null
    var tableIBtn: ImageButton? = null
    var htmlIBtn: ImageButton? = null
    var hrIBtn: ImageButton? = null
    var emojiIBtn: ImageButton? = null

    private var onToolClickListener: OnToolClickListener? = null
    private var onToolLongClickListener: OnToolLongClickListener? = null

    private var dp13 = 0

    init {
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setBackgroundResource(R.drawable.bg_ibooker_editor_tool)
        isVerticalScrollBarEnabled = false
        clipToPadding = true

        dp13 = IbookerEditorUtil.dpToPx(context, 13f)

        init(context)
    }

    // 初始化
    private fun init(context: Context) {
        toolLayout = LinearLayout(context)
        toolLayout!!.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        toolLayout!!.minimumHeight = IbookerEditorUtil.dpToPx(context, 46f)
        toolLayout!!.gravity = Gravity.CENTER_VERTICAL
        toolLayout!!.orientation = LinearLayout.HORIZONTAL
        addView(toolLayout)

        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val dp6 = IbookerEditorUtil.dpToPx(context, 6f)
        layoutParams.setMargins(dp6, dp6, dp6, dp6)
        // 粗体
        boldIBtn = ImageButton(context)
        setImageBtn(boldIBtn!!, layoutParams, R.drawable.draw_bold, resources.getString(R.string.bold), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_BOLD)
        toolLayout!!.addView(boldIBtn)

        // 斜体
        italicIBtn = ImageButton(context)
        setImageBtn(italicIBtn!!, layoutParams, R.drawable.draw_italic, resources.getString(R.string.italic), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_ITALIC)
        toolLayout!!.addView(italicIBtn)

        // 删除线
        strikeoutIBtn = ImageButton(context)
        setImageBtn(strikeoutIBtn!!, layoutParams, R.drawable.draw_strikeout, resources.getString(R.string.strikeout), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_STRIKEOUT)
        toolLayout!!.addView(strikeoutIBtn)

        // 下划线
        underlineIBtn = ImageButton(context)
        setImageBtn(underlineIBtn!!, layoutParams, R.drawable.draw_underline, resources.getString(R.string.underline), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UNDERLINE)
        toolLayout!!.addView(underlineIBtn)

        // 单词首字母大写
        capitalsIBtn = ImageButton(context)
        setImageBtn(capitalsIBtn!!, layoutParams, R.drawable.draw_capitals, resources.getString(R.string.capitals), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_CAPITALS)
        toolLayout!!.addView(capitalsIBtn)

        // 单词转大写
        uppercaseIBtn = ImageButton(context)
        setImageBtn(uppercaseIBtn!!, layoutParams, R.drawable.draw_uppercase, resources.getString(R.string.uppercase), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UPPERCASE)
        toolLayout!!.addView(uppercaseIBtn)

        // 单词转小写
        lowercaseIBtn = ImageButton(context)
        setImageBtn(lowercaseIBtn!!, layoutParams, R.drawable.draw_lowercase, resources.getString(R.string.lowercase), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_LOWERCASE)
        toolLayout!!.addView(lowercaseIBtn)

        // 一级标题
        h1IBtn = ImageButton(context)
        setImageBtn(h1IBtn!!, layoutParams, R.drawable.draw_h1, resources.getString(R.string.h1), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H1)
        toolLayout!!.addView(h1IBtn)

        // 二级标题
        h2IBtn = ImageButton(context)
        setImageBtn(h2IBtn!!, layoutParams, R.drawable.draw_h2, resources.getString(R.string.h2), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H2)
        toolLayout!!.addView(h2IBtn)

        // 三级标题
        h3IBtn = ImageButton(context)
        setImageBtn(h3IBtn!!, layoutParams, R.drawable.draw_h3, resources.getString(R.string.h3), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H3)
        toolLayout!!.addView(h3IBtn)

        // 四级标题
        h4IBtn = ImageButton(context)
        setImageBtn(h4IBtn!!, layoutParams, R.drawable.draw_h4, resources.getString(R.string.h4), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H4)
        toolLayout!!.addView(h4IBtn)

        // 五级标题
        h5IBtn = ImageButton(context)
        setImageBtn(h5IBtn!!, layoutParams, R.drawable.draw_h5, resources.getString(R.string.h5), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H5)
        toolLayout!!.addView(h5IBtn)

        // 六级标题
        h6IBtn = ImageButton(context)
        setImageBtn(h6IBtn!!, layoutParams, R.drawable.draw_h6, resources.getString(R.string.h6), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H6)
        toolLayout!!.addView(h6IBtn)

        // 链接
        linkIBtn = ImageButton(context)
        setImageBtn(linkIBtn!!, layoutParams, R.drawable.draw_link, resources.getString(R.string.link), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_LINK)
        toolLayout!!.addView(linkIBtn)

        // 引用
        quoteIBtn = ImageButton(context)
        setImageBtn(quoteIBtn!!, layoutParams, R.drawable.draw_quote, resources.getString(R.string.quote), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_QUOTE)
        toolLayout!!.addView(quoteIBtn)

        // 代码
        codeIBtn = ImageButton(context)
        setImageBtn(codeIBtn!!, layoutParams, R.drawable.draw_code, resources.getString(R.string.code), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_CODE)
        toolLayout!!.addView(codeIBtn)

        // 图片
        imguIBtn = ImageButton(context)
        setImageBtn(imguIBtn!!, layoutParams, R.drawable.draw_img_u, resources.getString(R.string.img_u), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_IMG_U)
        toolLayout!!.addView(imguIBtn)

        // 数字列表
        olIBtn = ImageButton(context)
        setImageBtn(olIBtn!!, layoutParams, R.drawable.draw_ol, resources.getString(R.string.ol), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_OL)
        toolLayout!!.addView(olIBtn)

        // 普通列表
        ulIBtn = ImageButton(context)
        setImageBtn(ulIBtn!!, layoutParams, R.drawable.draw_ul, resources.getString(R.string.ul), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UL)
        toolLayout!!.addView(ulIBtn)

        // 列表未选中
        unselectedIBtn = ImageButton(context)
        setImageBtn(unselectedIBtn!!, layoutParams, R.drawable.draw_unselected, resources.getString(R.string.unselected), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UNSELECTED)
        toolLayout!!.addView(unselectedIBtn)

        // 列表选中
        selectedIBtn = ImageButton(context)
        setImageBtn(selectedIBtn!!, layoutParams, R.drawable.draw_selected, resources.getString(R.string.selected), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_SELECTED)
        toolLayout!!.addView(selectedIBtn)

        // 表格
        tableIBtn = ImageButton(context)
        setImageBtn(tableIBtn!!, layoutParams, R.drawable.draw_table, resources.getString(R.string.table), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_TABLE)
        toolLayout!!.addView(tableIBtn)

        // HTML
        htmlIBtn = ImageButton(context)
        setImageBtn(htmlIBtn!!, layoutParams, R.drawable.draw_html, resources.getString(R.string.html), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_HTML)
        toolLayout!!.addView(htmlIBtn)

        // 分割线
        hrIBtn = ImageButton(context)
        setImageBtn(hrIBtn!!, layoutParams, R.drawable.draw_hr, resources.getString(R.string.hr), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_HR)
        toolLayout!!.addView(hrIBtn)

        // 表情
        emojiIBtn = ImageButton(context)
        setImageBtn(emojiIBtn!!, layoutParams, R.drawable.draw_emoji, resources.getString(R.string.emoji), IbookerEditorEnum.TOOLVIEW_TAG.IBTN_EMOJI)
        toolLayout!!.addView(emojiIBtn)
    }

    /**
     * 设置ImageButton
     *
     * @param imageButton        目标ImageButton
     * @param layoutParams       ImageButton布局
     * @param resid              ImageButton背景资源
     * @param contentDescription ImageButton描述信息
     * @param tag                ImageButton的tag信息
     */
    private fun setImageBtn(
            imageButton: ImageButton,
            layoutParams: ViewGroup.LayoutParams,
            @DrawableRes resid: Int,
            contentDescription: CharSequence,
            tag: Any) {
        imageButton.layoutParams = layoutParams
        imageButton.setBackgroundResource(resid)
        imageButton.contentDescription = contentDescription
        imageButton.setPadding(dp13, dp13, dp13, dp13)
        imageButton.tag = tag
        imageButton.setOnClickListener { v ->
            if (onToolClickListener != null)
                onToolClickListener!!.onToolClick(v.tag)
        }
        imageButton.setOnLongClickListener { v ->
            if (onToolLongClickListener != null)
                onToolLongClickListener!!.onToolLongClick(v.tag)
            true
        }
    }

    // 粗体
    fun setBoldIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        boldIBtn!!.setImageResource(resId)
        return this
    }

    fun setBoldIBtnVisibility(visibility: Int): IbookerEditorToolView {
        boldIBtn!!.visibility = visibility
        return this
    }

    // 斜体
    fun setItalicIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        italicIBtn!!.setImageResource(resId)
        return this
    }

    fun setItalicIBtnVisibility(visibility: Int): IbookerEditorToolView {
        italicIBtn!!.visibility = visibility
        return this
    }

    // 删除线
    fun setStrikeoutIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        strikeoutIBtn!!.setImageResource(resId)
        return this
    }

    fun setStrikeoutIBtnVisibility(visibility: Int): IbookerEditorToolView {
        strikeoutIBtn!!.visibility = visibility
        return this
    }

    // 下划线
    fun setUnderlineIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        underlineIBtn!!.setImageResource(resId)
        return this
    }

    fun setUnderlineIBtnVisibility(visibility: Int): IbookerEditorToolView {
        underlineIBtn!!.visibility = visibility
        return this
    }

    // 单词首字母大写
    fun setCapitalsIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        capitalsIBtn!!.setImageResource(resId)
        return this
    }

    fun setCapitalsIBtnVisibility(visibility: Int): IbookerEditorToolView {
        capitalsIBtn!!.visibility = visibility
        return this
    }

    // 单词转大写
    fun setUppercaseIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        uppercaseIBtn!!.setImageResource(resId)
        return this
    }

    fun setUppercaseIBtnVisibility(visibility: Int): IbookerEditorToolView {
        uppercaseIBtn!!.visibility = visibility
        return this
    }

    // 单词转小写
    fun setLowercaseIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        lowercaseIBtn!!.setImageResource(resId)
        return this
    }

    fun setLowercaseIBtnVisibility(visibility: Int): IbookerEditorToolView {
        lowercaseIBtn!!.visibility = visibility
        return this
    }

    // 一级标题
    fun setH1IBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        h1IBtn!!.setImageResource(resId)
        return this
    }

    fun setH1IBtnVisibility(visibility: Int): IbookerEditorToolView {
        h1IBtn!!.visibility = visibility
        return this
    }

    // 二级标题
    fun setH2IBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        h2IBtn!!.setImageResource(resId)
        return this
    }

    fun setH2IBtnVisibility(visibility: Int): IbookerEditorToolView {
        h2IBtn!!.visibility = visibility
        return this
    }

    // 三级标题
    fun setH3IBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        h3IBtn!!.setImageResource(resId)
        return this
    }

    fun setH3IBtnVisibility(visibility: Int): IbookerEditorToolView {
        h3IBtn!!.visibility = visibility
        return this
    }

    // 四级标题
    fun setH4IBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        h4IBtn!!.setImageResource(resId)
        return this
    }

    fun setH4IBtnVisibility(visibility: Int): IbookerEditorToolView {
        h4IBtn!!.visibility = visibility
        return this
    }

    // 五级标题
    fun setH5IBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        h5IBtn!!.setImageResource(resId)
        return this
    }

    fun setH5IBtnVisibility(visibility: Int): IbookerEditorToolView {
        h5IBtn!!.visibility = visibility
        return this
    }

    // 六级标题
    fun setH6IBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        h6IBtn!!.setImageResource(resId)
        return this
    }

    fun setH6IBtnVisibility(visibility: Int): IbookerEditorToolView {
        h6IBtn!!.visibility = visibility
        return this
    }

    // 链接
    fun setLinkIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        linkIBtn!!.setImageResource(resId)
        return this
    }

    fun setLinkIBtnVisibility(visibility: Int): IbookerEditorToolView {
        linkIBtn!!.visibility = visibility
        return this
    }

    // 引用
    fun setQuoteIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        quoteIBtn!!.setImageResource(resId)
        return this
    }

    fun setQuoteIBtnVisibility(visibility: Int): IbookerEditorToolView {
        quoteIBtn!!.visibility = visibility
        return this
    }

    // 代码
    fun setCodeIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        codeIBtn!!.setImageResource(resId)
        return this
    }

    fun setCodeIBtnVisibility(visibility: Int): IbookerEditorToolView {
        codeIBtn!!.visibility = visibility
        return this
    }

    // 图片
    fun setImguIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        imguIBtn!!.setImageResource(resId)
        return this
    }

    fun setImguIBtnVisibility(visibility: Int): IbookerEditorToolView {
        imguIBtn!!.visibility = visibility
        return this
    }

    // 数字列表
    fun setOlIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        olIBtn!!.setImageResource(resId)
        return this
    }

    fun setOlIBtnVisibility(visibility: Int): IbookerEditorToolView {
        olIBtn!!.visibility = visibility
        return this
    }

    // 普通列表
    fun setUlIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        ulIBtn!!.setImageResource(resId)
        return this
    }

    fun setUlIBtnVisibility(visibility: Int): IbookerEditorToolView {
        ulIBtn!!.visibility = visibility
        return this
    }

    // 列表未选中
    fun setUnselectedIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        unselectedIBtn!!.setImageResource(resId)
        return this
    }

    fun setUnselectedIBtnVisibility(visibility: Int): IbookerEditorToolView {
        unselectedIBtn!!.visibility = visibility
        return this
    }

    // 列表选中
    fun setSelectedIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        selectedIBtn!!.setImageResource(resId)
        return this
    }

    fun setSelectedIBtnVisibility(visibility: Int): IbookerEditorToolView {
        selectedIBtn!!.visibility = visibility
        return this
    }

    // 表格
    fun setTableIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        tableIBtn!!.setImageResource(resId)
        return this
    }

    fun setTableIBtnVisibility(visibility: Int): IbookerEditorToolView {
        tableIBtn!!.visibility = visibility
        return this
    }

    // HTML
    fun setHtmlIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        htmlIBtn!!.setImageResource(resId)
        return this
    }

    fun setHtmlIBtnVisibility(visibility: Int): IbookerEditorToolView {
        htmlIBtn!!.visibility = visibility
        return this
    }

    // 分割线
    fun setHrIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        hrIBtn!!.setImageResource(resId)
        return this
    }

    fun setHrIBtnVisibility(visibility: Int): IbookerEditorToolView {
        hrIBtn!!.visibility = visibility
        return this
    }

    // 表情
    fun setEmojiIBtnImageResource(@DrawableRes resId: Int): IbookerEditorToolView {
        emojiIBtn!!.setImageResource(resId)
        return this
    }

    fun setEmojiIBtnVisibility(visibility: Int): IbookerEditorToolView {
        emojiIBtn!!.visibility = visibility
        return this
    }

    /**
     * 点击事件监听
     */
    interface OnToolClickListener {
        fun onToolClick(tag: Any)
    }

    /**
     * 长按事件监听
     */
    interface OnToolLongClickListener {
        fun onToolLongClick(tag: Any)
    }

    fun setOnToolClickListener(onToolClickListener: OnToolClickListener): IbookerEditorToolView {
        this.onToolClickListener = onToolClickListener
        return this
    }

    fun setOnToolLongClickListener(onToolLongClickListener: OnToolLongClickListener): IbookerEditorToolView {
        this.onToolLongClickListener = onToolLongClickListener
        return this
    }
}// 构造方法