package cc.ibooker.ibookereditorklib

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.annotation.ColorInt
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout

/**
 * 书客编辑器布局
 * Created by 邹峰立 on 2018/2/11.
 */
class IbookerEditorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), IbookerEditorTopView.OnTopClickListener, IbookerEditorToolView.OnToolClickListener {
    // 顶部控件
    // getter/setter
    var ibookerEditorTopView: IbookerEditorTopView? = null
    // 中间区域ViewPager
    var ibookerEditorVpView: IbookerEditorVpView? = null
    // 底部工具栏
    var ibookerEditorToolView: IbookerEditorToolView? = null
    // 底部工具栏-操作类
    private var ibookerEditorUtil: IbookerEditorUtil? = null

    // 工具栏进入和退出动画
    private var inAnim: Animation? = null
    private var outAnim: Animation? = null

    init {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        setBackgroundColor(Color.parseColor("#FFFFFF"))

        init(context, attrs)
    }

    // 初始化
    private fun init(context: Context, attrs: AttributeSet?) {
        // 顶部
        ibookerEditorTopView = IbookerEditorTopView(context)
        ibookerEditorTopView!!.setOnTopClickListener(this)
        addView(ibookerEditorTopView)
        // 中间区域ViewPager
        ibookerEditorVpView = IbookerEditorVpView(context)
        ibookerEditorVpView!!.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        ibookerEditorVpView!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                changeVpUpdateIbookerEditorTopView(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        ibookerEditorVpView!!.currentItem = 0
        changeVpUpdateIbookerEditorTopView(0)
        addView(ibookerEditorVpView)
        // 底部工具栏
        ibookerEditorToolView = IbookerEditorToolView(context)
        ibookerEditorToolView!!.setOnToolClickListener(this)
        addView(ibookerEditorToolView)
        // 底部工具栏 - 管理类
        ibookerEditorUtil = IbookerEditorUtil(ibookerEditorVpView!!.editView!!)

        // 进入和退出动画
        inAnim = AnimationUtils.loadAnimation(context, R.anim.ibooker_editor_toolview_in)
        outAnim = AnimationUtils.loadAnimation(context, R.anim.ibooker_editor_toolview_out)

        if (attrs != null) {
            // 获取自定义属性，并设置
            val ta = getContext().obtainStyledAttributes(attrs, R.styleable.IbookerEditorView)

            // 顶部工具栏
            val ibookerEditorTopViewVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorTopView_Visible, true)
            ibookerEditorTopView!!.visibility = if (ibookerEditorTopViewVisible) View.VISIBLE else View.GONE

            // 返回按钮
            val backImgVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorTopView_BackImg_Visible, true)
            val backImgRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_BackImg_Res, R.drawable.icon_back_black)
            ibookerEditorTopView!!.backImg!!.visibility = if (backImgVisible) View.VISIBLE else View.GONE
            ibookerEditorTopView!!.backImg!!.setImageResource(backImgRes)

            // 撤销按钮
            val undoIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorTopView_UndoIBtn_Visible, true)
            val undoIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_UndoIBtn_Res, R.drawable.draw_undo)
            ibookerEditorTopView!!.undoIBtn!!.visibility = if (undoIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorTopView!!.undoIBtn!!.setBackgroundResource(undoIBtnRes)

            // 重做按钮
            val redoIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorTopView_RedoIBtn_Visible, true)
            val redoIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_RedoIBtn_Res, R.drawable.draw_redo)
            ibookerEditorTopView!!.redoIBtn!!.visibility = if (redoIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorTopView!!.redoIBtn!!.setBackgroundResource(redoIBtnRes)

            // 编辑模式
            val editIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorTopView_EditIBtn_Visible, true)
            val editIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_EditIBtn_Res, R.drawable.draw_edit)
            ibookerEditorTopView!!.editIBtn!!.visibility = if (editIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorTopView!!.editIBtn!!.setBackgroundResource(editIBtnRes)

            // 预览模式
            val previewIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorTopView_PreviewIBtn_Visible, true)
            val previewIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_PreviewIBtn_Res, R.drawable.draw_preview)
            ibookerEditorTopView!!.previewIBtn!!.visibility = if (previewIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorTopView!!.previewIBtn!!.setBackgroundResource(previewIBtnRes)

            // 帮助
            val helpIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorTopView_HelpIBtn_Visible, true)
            val helpIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_HelpIBtn_Res, R.drawable.draw_help)
            ibookerEditorTopView!!.helpIBtn!!.visibility = if (helpIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorTopView!!.helpIBtn!!.setBackgroundResource(helpIBtnRes)

            // 关于
            val aboutImgVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorTopView_AboutImg_Visible, true)
            val aboutImgRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_AboutImg_Res, R.drawable.ibooker_editor_logo)
            ibookerEditorTopView!!.aboutImg!!.visibility = if (aboutImgVisible) View.VISIBLE else View.GONE
            ibookerEditorTopView!!.aboutImg!!.setImageResource(aboutImgRes)

            // 编辑框
            val ibookerEditorEditViewBackgroundColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorEditView_BackgroundColor, -0x1)
            ibookerEditorVpView!!.editView!!.setBackgroundColor(ibookerEditorEditViewBackgroundColor)

            // 标题
            val titleEdVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorEditView_TitleEd_Visible, true)
            val titleEdTextSize = ta.getDimension(R.styleable.IbookerEditorView_IbookerEditorEditView_TitleEd_TextSize, 18f)
            val titleEdTextColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorEditView_TitleEd_TextColor, -0xbbbbbc)
            val titleEdHint = ta.getString(R.styleable.IbookerEditorView_IbookerEditorEditView_TitleEd_Hint)
            val titleEdHintTextColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorEditView_TitleEd_HintTextColor, -0x666667)
            ibookerEditorVpView!!.editView!!.ibookerTitleEd!!.visibility = if (titleEdVisible) View.VISIBLE else View.GONE
            ibookerEditorVpView!!.editView!!.ibookerTitleEd!!.textSize = titleEdTextSize
            ibookerEditorVpView!!.editView!!.ibookerTitleEd!!.setTextColor(titleEdTextColor)
            if (!TextUtils.isEmpty(titleEdHint))
                ibookerEditorVpView!!.editView!!.ibookerTitleEd!!.hint = titleEdHint
            ibookerEditorVpView!!.editView!!.ibookerTitleEd!!.setHintTextColor(titleEdHintTextColor)

            // 分割线
            val editLineViewVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorEditView_LineView_Visible, true)
            val editLineViewBackgroundColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorEditView_LineView_BackgroundColor, -0x454546)
            ibookerEditorVpView!!.editView!!.lineView!!.visibility = if (editLineViewVisible) View.VISIBLE else View.GONE
            ibookerEditorVpView!!.editView!!.lineView!!.setBackgroundColor(editLineViewBackgroundColor)

            // 内容
            val ibookerEdBackgroundColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorEditView_IbookerEd_BackgroundColor, ibookerEditorEditViewBackgroundColor)
            val ibookerEdTextSize = ta.getDimension(R.styleable.IbookerEditorView_IbookerEditorEditView_IbookerEd_TextSize, 16f)
            val ibookerEdTextColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorEditView_IbookerEd_TextColor, -0xbbbbbc)
            val ibookerEdHint = ta.getString(R.styleable.IbookerEditorView_IbookerEditorEditView_IbookerEd_Hint)
            val ibookerEdHintTextColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorEditView_IbookerEd_HintTextColor, -0x666667)
            ibookerEditorVpView!!.editView!!.ibookerEd!!.setBackgroundColor(ibookerEdBackgroundColor)
            ibookerEditorVpView!!.editView!!.ibookerEd!!.textSize = ibookerEdTextSize
            ibookerEditorVpView!!.editView!!.ibookerEd!!.setTextColor(ibookerEdTextColor)
            if (!TextUtils.isEmpty(ibookerEdHint))
                ibookerEditorVpView!!.editView!!.ibookerEd!!.hint = ibookerEdHint
            ibookerEditorVpView!!.editView!!.ibookerEd!!.setHintTextColor(ibookerEdHintTextColor)

            // 预览框
            val ibookerEditorPreViewBackgroundColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorPreView_BackgroundColor, -0x1)
            ibookerEditorVpView!!.preView!!.setBackgroundColor(ibookerEditorPreViewBackgroundColor)

            // 标题
            val titleTvVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorPreView_TitleTv_Visible, true)
            val titleTvTextSize = ta.getDimension(R.styleable.IbookerEditorView_IbookerEditorPreView_TitleTv_TextSize, 18f)
            val titleTvTextColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorPreView_TitleTv_TextColor, -0xbbbbbc)
            val titleTvHint = ta.getString(R.styleable.IbookerEditorView_IbookerEditorPreView_TitleTv_Hint)
            val titleTvHintTextColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorPreView_TitleTv_HintTextColor, -0x666667)
            ibookerEditorVpView!!.preView!!.ibookerTitleTv!!.visibility = if (titleTvVisible) View.VISIBLE else View.GONE
            ibookerEditorVpView!!.preView!!.ibookerTitleTv!!.textSize = titleTvTextSize
            ibookerEditorVpView!!.preView!!.ibookerTitleTv!!.setTextColor(titleTvTextColor)
            if (!TextUtils.isEmpty(titleTvHint))
                ibookerEditorVpView!!.preView!!.ibookerTitleTv!!.hint = titleTvHint
            ibookerEditorVpView!!.preView!!.ibookerTitleTv!!.setHintTextColor(titleTvHintTextColor)

            // 分割线
            val preLineViewVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorPreView_LineView_Visible, true)
            val preLineViewBackgroundColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorPreView_LineView_BackgroundColor, -0x454546)
            ibookerEditorVpView!!.preView!!.lineView!!.visibility = if (preLineViewVisible) View.VISIBLE else View.GONE
            ibookerEditorVpView!!.preView!!.lineView!!.setBackgroundColor(preLineViewBackgroundColor)

            // 内容
            val ibookerWebViewBackgroundColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorPreView_IbookerWebView_BackgroundColor, ibookerEditorEditViewBackgroundColor)
            ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.setBackgroundColor(ibookerWebViewBackgroundColor)

            // 底部工具栏
            val ibookerEditorToolViewBackgroundColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorToolView_BackgroundColor, -0x1)
            ibookerEditorToolView!!.setBackgroundColor(ibookerEditorToolViewBackgroundColor)

            val boldIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_BoldIBtn_Visible, true)
            val boldIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_BoldIBtn_Res, R.drawable.draw_bold)
            ibookerEditorToolView!!.boldIBtn!!.visibility = if (boldIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.boldIBtn!!.setBackgroundResource(boldIBtnRes)

            val italicIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_ItalicIBtn_Visible, true)
            val italicIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_ItalicIBtn_Res, R.drawable.draw_italic)
            ibookerEditorToolView!!.italicIBtn!!.visibility = if (italicIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.italicIBtn!!.setBackgroundResource(italicIBtnRes)

            val strikeoutIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_StrikeoutIBtn_Visible, true)
            val strikeoutIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_StrikeoutIBtn_Res, R.drawable.draw_strikeout)
            ibookerEditorToolView!!.strikeoutIBtn!!.visibility = if (strikeoutIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.strikeoutIBtn!!.setBackgroundResource(strikeoutIBtnRes)

            val underlineIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_UnderlineIBtn_Visible, true)
            val underlineIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_UnderlineIBtn_Res, R.drawable.draw_underline)
            ibookerEditorToolView!!.underlineIBtn!!.visibility = if (underlineIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.underlineIBtn!!.setBackgroundResource(underlineIBtnRes)

            val capitalsIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_CapitalsIBtn_Visible, true)
            val capitalsIBtnVisibleRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_CapitalsIBtn_Res, R.drawable.draw_capitals)
            ibookerEditorToolView!!.capitalsIBtn!!.visibility = if (capitalsIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.capitalsIBtn!!.setBackgroundResource(capitalsIBtnVisibleRes)

            val uppercaseIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_UppercaseIBtn_Visible, true)
            val uppercaseIBtnVisibleRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_UppercaseIBtnBackImg_Res, R.drawable.draw_uppercase)
            ibookerEditorToolView!!.uppercaseIBtn!!.visibility = if (uppercaseIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.uppercaseIBtn!!.setBackgroundResource(uppercaseIBtnVisibleRes)

            val lowercaseIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_LowercaseIBtn_Visible, true)
            val lowercaseIBtnVisibleRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_LowercaseIBtn_Res, R.drawable.draw_lowercase)
            ibookerEditorToolView!!.lowercaseIBtn!!.visibility = if (lowercaseIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.lowercaseIBtn!!.setBackgroundResource(lowercaseIBtnVisibleRes)

            val h1IBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_H1IBtn_Visible, true)
            val h1IBtnVisibleRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_H1IBtn_Res, R.drawable.draw_h1)
            ibookerEditorToolView!!.h1IBtn!!.visibility = if (h1IBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.h1IBtn!!.setBackgroundResource(h1IBtnVisibleRes)

            val h2IBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_H2IBtn_Visible, true)
            val h2IBtnVisibleRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_H2IBtn_Res, R.drawable.draw_h2)
            ibookerEditorToolView!!.h2IBtn!!.visibility = if (h2IBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.h2IBtn!!.setBackgroundResource(h2IBtnVisibleRes)

            val h3IBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_H3IBtn_Visible, true)
            val h3IBtnVisibleRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_H3IBtn_Res, R.drawable.draw_h3)
            ibookerEditorToolView!!.h3IBtn!!.visibility = if (h3IBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.h3IBtn!!.setBackgroundResource(h3IBtnVisibleRes)

            val h4IBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_H4IBtn_Visible, true)
            val h4IBtnVisibleRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_H4IBtn_Res, R.drawable.draw_h4)
            ibookerEditorToolView!!.h4IBtn!!.visibility = if (h4IBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.h4IBtn!!.setBackgroundResource(h4IBtnVisibleRes)

            val h5IBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_H5IBtn_Visible, true)
            val h5IBtnVisibleRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_H5IBtn_Res, R.drawable.draw_h5)
            ibookerEditorToolView!!.h5IBtn!!.visibility = if (h5IBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.h5IBtn!!.setBackgroundResource(h5IBtnVisibleRes)

            val h6IBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_H6IBtn_Visible, true)
            val h6IBtnVisibleRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_H6IBtn_Res, R.drawable.draw_h6)
            ibookerEditorToolView!!.h6IBtn!!.visibility = if (h6IBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.h6IBtn!!.setBackgroundResource(h6IBtnVisibleRes)

            val linkIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_LinkIBtn_Visible, true)
            val linkIBtnVisibleRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_LinkIBtn_Res, R.drawable.draw_link)
            ibookerEditorToolView!!.linkIBtn!!.visibility = if (linkIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.linkIBtn!!.setBackgroundResource(linkIBtnVisibleRes)

            val quoteIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_QuoteIBtn_Visible, true)
            val quoteIBtnVisibleRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_QuoteIBtn_Res, R.drawable.draw_quote)
            ibookerEditorToolView!!.quoteIBtn!!.visibility = if (quoteIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.quoteIBtn!!.setBackgroundResource(quoteIBtnVisibleRes)

            val codeIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_CodeIBtn_Visible, true)
            val codeIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_CodeIBtn_Res, R.drawable.draw_code)
            ibookerEditorToolView!!.codeIBtn!!.visibility = if (codeIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.codeIBtn!!.setBackgroundResource(codeIBtnRes)

            val imguIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_ImguIBtn_Visible, true)
            val imguIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_ImguIBtn_Res, R.drawable.draw_img_u)
            ibookerEditorToolView!!.imguIBtn!!.visibility = if (imguIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.imguIBtn!!.setBackgroundResource(imguIBtnRes)

            val olIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_OlIBtn_Visible, true)
            val olIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_OlIBtn_Res, R.drawable.draw_ol)
            ibookerEditorToolView!!.olIBtn!!.visibility = if (olIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.olIBtn!!.setBackgroundResource(olIBtnRes)

            val ulIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_UlIBtn_Visible, true)
            val ulIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_UlIBtn_Res, R.drawable.draw_ul)
            ibookerEditorToolView!!.ulIBtn!!.visibility = if (ulIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.ulIBtn!!.setBackgroundResource(ulIBtnRes)

            val unselectedIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_UnselectedIBtn_Visible, true)
            val unselectedIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_UnselectedIBtn_Res, R.drawable.draw_unselected)
            ibookerEditorToolView!!.unselectedIBtn!!.visibility = if (unselectedIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.unselectedIBtn!!.setBackgroundResource(unselectedIBtnRes)

            val selectedIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_SelectedIBtn_Visible, true)
            val selectedIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_SelectedIBtn_Res, R.drawable.draw_selected)
            ibookerEditorToolView!!.selectedIBtn!!.visibility = if (selectedIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.selectedIBtn!!.setBackgroundResource(selectedIBtnRes)

            val tableIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_TableIBtn_Visible, true)
            val tableIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_TableIBtn_Res, R.drawable.draw_table)
            ibookerEditorToolView!!.tableIBtn!!.visibility = if (tableIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.tableIBtn!!.setBackgroundResource(tableIBtnRes)

            val htmlIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_HtmlIBtn_Visible, true)
            val htmlIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_HtmlIBtn_Res, R.drawable.draw_html)
            ibookerEditorToolView!!.htmlIBtn!!.visibility = if (htmlIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.htmlIBtn!!.setBackgroundResource(htmlIBtnRes)

            val hrIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_HrIBtn_Visible, true)
            val hrIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_HrIBtn_Res, R.drawable.draw_hr)
            ibookerEditorToolView!!.hrIBtn!!.visibility = if (hrIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.hrIBtn!!.setBackgroundResource(hrIBtnRes)

            val emojiIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorToolView_EmojiIBtn_Visible, true)
            val emojiIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorToolView_EmojiIBtng_Res, R.drawable.draw_emoji)
            ibookerEditorToolView!!.emojiIBtn!!.visibility = if (emojiIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorToolView!!.emojiIBtn!!.setBackgroundResource(emojiIBtnRes)

            ta.recycle()
        }

        changeVpUpdateIbookerEditorTopView(0)
    }

    // 设置ViewPager变化
    private fun changeVpUpdateIbookerEditorTopView(position: Int) {
        if (ibookerEditorTopView != null)
            if (position == 0) {
                ibookerEditorTopView!!.editIBtn!!.setBackgroundResource(R.drawable.icon_ibooker_editor_edit_orange)
                ibookerEditorTopView!!.previewIBtn!!.setBackgroundResource(R.drawable.icon_ibooker_editor_preview_gray)

                // 设置动画
                if (ibookerEditorToolView != null) {
                    if (inAnim != null)
                        ibookerEditorToolView!!.startAnimation(inAnim)
                    ibookerEditorToolView!!.visibility = View.VISIBLE
                }

                openInputSoft(true)
            } else if (position == 1) {
                ibookerEditorTopView!!.editIBtn!!.setBackgroundResource(R.drawable.icon_ibooker_editor_edit_gray)
                ibookerEditorTopView!!.previewIBtn!!.setBackgroundResource(R.drawable.icon_ibooker_editor_preview_orange)
                // 执行预览
                ibookerCompile()

                // 设置动画
                if (ibookerEditorToolView != null) {
                    if (outAnim != null)
                        ibookerEditorToolView!!.startAnimation(outAnim)
                    ibookerEditorToolView!!.visibility = View.GONE
                }

                openInputSoft(false)
            }
    }

    // 关闭/开启软盘
    private fun openInputSoft(isOpen: Boolean) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (isOpen)
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
        else
            imm.hideSoftInputFromWindow(applicationWindowToken, 0)
    }

    // 顶部按钮点击事件监听
    override fun onTopClick(tag: Any) {
        when (tag) {
            IbookerEditorEnum.TOOLVIEW_TAG.IMG_BACK -> // 返回
                (context as Activity).finish()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UNDO -> // 撤销
                ibookerEditorVpView!!.editView!!.undo()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_REDO -> // 重做
                ibookerEditorVpView!!.editView!!.redo()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_EDIT -> // 编辑
                ibookerEditorVpView!!.currentItem = 0
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_PREVIEW -> // 预览
                ibookerEditorVpView!!.currentItem = 1
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_HELP -> {// 帮助
                val intent = Intent()
                intent.action = "android.intent.action.VIEW"
                val contentUrl1 = Uri.parse("http://ibooker.cc/article/1/detail")
                intent.data = contentUrl1
                context.startActivity(intent)
            }
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_ABOUT -> {// 关于
                val intent = Intent()
                intent.action = "android.intent.action.VIEW"
                val contentUrl2 = Uri.parse("http://ibooker.cc/article/182/detail")
                intent.data = contentUrl2
                context.startActivity(intent)
            }
        }
    }

    // 工具栏按钮点击事件监听
    override fun onToolClick(tag: Any) {
        if (ibookerEditorUtil == null)
        // 初始化ibookerEditorUtil
            ibookerEditorUtil = IbookerEditorUtil(ibookerEditorVpView!!.editView!!)
        if (ibookerEditorVpView!!.currentItem != 0)
        // 切换到编辑器模式
            ibookerEditorVpView!!.currentItem = 0
        when (tag) {
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_BOLD -> // 加粗
                ibookerEditorUtil!!.bold()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_ITALIC -> // 斜体
                ibookerEditorUtil!!.italic()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_STRIKEOUT -> // 删除线
                ibookerEditorUtil!!.strikeout()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UNDERLINE -> // 下划线
                ibookerEditorUtil!!.underline()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_CAPITALS -> // 单词首字母大写
                ibookerEditorUtil!!.capitals()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UPPERCASE -> // 字母转大写
                ibookerEditorUtil!!.uppercase()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_LOWERCASE -> // 字母转小写
                ibookerEditorUtil!!.lowercase()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H1 -> // 一级标题
                ibookerEditorUtil!!.h1()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H2 -> // 二级标题
                ibookerEditorUtil!!.h2()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H3 -> // 三级标题
                ibookerEditorUtil!!.h3()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H4 -> // 四级标题
                ibookerEditorUtil!!.h4()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H5 -> // 五级标题
                ibookerEditorUtil!!.h5()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H6 -> // 六级标题
                ibookerEditorUtil!!.h6()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_LINK -> // 超链接
                ibookerEditorUtil!!.link()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_QUOTE -> // 引用
                ibookerEditorUtil!!.quote()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_CODE -> // 代码
                ibookerEditorUtil!!.code()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_IMG_U -> // 图片
                ibookerEditorUtil!!.imgu()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_OL -> // 数字列表
                ibookerEditorUtil!!.ol()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UL -> // 普通列表
                ibookerEditorUtil!!.ul()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UNSELECTED -> // 复选框未选中
                ibookerEditorUtil!!.tasklistsUnChecked()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_SELECTED -> // 复选框选中
                ibookerEditorUtil!!.tasklistsChecked()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_TABLE -> // 表格
                ibookerEditorUtil!!.tables()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_HTML -> // HTML
                ibookerEditorUtil!!.html()
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_HR -> // 分割线
                ibookerEditorUtil!!.hr()
        }
    }

    // 执行Text预览
    private fun ibookerCompile() {
        // 获取待转义内容
        val title = ibookerEditorVpView!!.editView!!.ibookerTitleEd!!.text.toString().trim()
        val content = ibookerEditorVpView!!.editView!!.ibookerEd!!.text.toString().trim()
        // 执行预览
        if (!TextUtils.isEmpty(title))
            ibookerEditorVpView!!.preView!!.ibookerTitleTv!!.text = title
        if (!TextUtils.isEmpty(content))
            ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.ibookerCompile(content)
    }

    // 销毁方法
    fun destoryIbookerEditor() {
        inAnim!!.cancel()
        inAnim = null
        outAnim!!.cancel()
        outAnim = null
        ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.destroy()
    }

    /**
     * 显示或者隐藏顶部工具栏
     *
     * @param visibility 状态
     */
    fun setIbookerEditorTopViewVisibility(visibility: Int): IbookerEditorView {
        if (visibility == View.VISIBLE || visibility == View.GONE || visibility == View.INVISIBLE)
            ibookerEditorTopView!!.visibility = visibility
        return this
    }

    /**
     * 设置编辑控件背景颜色
     *
     * @param color 背景颜色
     */
    fun setIbookerEditorEditViewBackgroundColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setBackgroundColor(color)
        ibookerEditorVpView!!.editView!!.ibookerTitleEd!!.setBackgroundColor(color)
        ibookerEditorVpView!!.editView!!.ibookerEd!!.setBackgroundColor(color)
        return this
    }

    /**
     * 设置预览控件背景颜色
     *
     * @param color 背景颜色
     */
    fun setIbookerEditorPreViewBackgroundColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.preView!!.setBackgroundColor(color)
        ibookerEditorVpView!!.preView!!.ibookerTitleTv!!.setBackgroundColor(color)
        ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.setBackgroundColor(color)
        return this
    }
}// 构造方法
