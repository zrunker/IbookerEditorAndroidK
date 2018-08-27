package cc.ibooker.ibookereditorklib

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream


/**
 * 书客编辑器布局
 * Created by 邹峰立 on 2018/2/11.
 */
class IbookerEditorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), IbookerEditorTopView.OnTopClickListener, IbookerEditorToolView.OnToolClickListener {
    // 顶部控件
    // getter/setter
    private var ibookerEditorTopView: IbookerEditorTopView? = null
    // 中间区域ViewPager
    var ibookerEditorVpView: IbookerEditorVpView? = null
    // 底部工具栏
    private var ibookerEditorToolView: IbookerEditorToolView? = null
    // 底部工具栏-操作类
    private var ibookerEditorUtil: IbookerEditorUtil? = null

    // 权限申请模块
    private val needPermissions = arrayOf(
            // SDK在Android 6.0+需要进行运行检测的权限如下：
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE)

    // 工具栏进入和退出动画
    private var inAnim: Animation? = null
    private var outAnim: Animation? = null
    private var editIBtnDefaultRes = R.drawable.icon_ibooker_editor_edit_gray
    private var editIBtnSelectedRes = R.drawable.icon_ibooker_editor_edit_orange
    private var previewIBtnDefaultRes = R.drawable.icon_ibooker_editor_preview_gray
    private var previewIBtnSelectedRes = R.drawable.icon_ibooker_editor_preview_orange

    /**
     * 是否完成本地文件加载
     */
    val isLoadFinished: Boolean
        get() = ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.isLoadFinished

    /**
     * 获取整个WebView截图
     */
    val webViewBitmap: Bitmap
        get() = ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.getWebViewBitmap()

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
        ibookerEditorVpView!!.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
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
            editIBtnDefaultRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_EditIBtn_Default_Res, editIBtnDefaultRes)
            editIBtnSelectedRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_EditIBtn_Selected_Res, editIBtnSelectedRes)
            ibookerEditorTopView!!.editIBtn!!.visibility = if (editIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorTopView!!.editIBtn!!.setBackgroundResource(editIBtnDefaultRes)

            // 预览模式
            val previewIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorTopView_PreviewIBtn_Visible, true)
            previewIBtnDefaultRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_PreviewIBtn_Default_Res, previewIBtnDefaultRes)
            previewIBtnSelectedRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_PreviewIBtn_Selected_Res, previewIBtnSelectedRes)
            ibookerEditorTopView!!.previewIBtn!!.visibility = if (previewIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorTopView!!.previewIBtn!!.setBackgroundResource(previewIBtnDefaultRes)

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

            // 分享
            val shareIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorTopView_ShareIBtn_Visible, true)
            val shareIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_ShareIBtn_Res, R.drawable.draw_share)
            ibookerEditorTopView!!.shareIBtn!!.visibility = if (shareIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorTopView!!.shareIBtn!!.setBackgroundResource(shareIBtnRes)

            // 更多
            val elseIBtnVisible = ta.getBoolean(R.styleable.IbookerEditorView_IbookerEditorTopView_ElseIBtn_Visible, true)
            val elseIBtnRes = ta.getResourceId(R.styleable.IbookerEditorView_IbookerEditorTopView_ElseIBtn_Res, R.drawable.draw_else)
            ibookerEditorTopView!!.elseIBtn!!.visibility = if (elseIBtnVisible) View.VISIBLE else View.GONE
            ibookerEditorTopView!!.elseIBtn!!.setBackgroundResource(elseIBtnRes)

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
            val ibookerEditorToolViewBackgroundColor = ta.getColor(R.styleable.IbookerEditorView_IbookerEditorToolView_BackgroundColor, 0xff)
            if (ibookerEditorToolViewBackgroundColor == 0xff)
                ibookerEditorToolView!!.setBackgroundResource(R.drawable.bg_ibooker_editor_tool)
            else
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

        ibookerEditorTopView!!.editIBtn!!.setBackgroundResource(editIBtnSelectedRes)
        ibookerEditorTopView!!.previewIBtn!!.setBackgroundResource(previewIBtnDefaultRes)
    }

    // 设置ViewPager变化
    public fun changeVpUpdateIbookerEditorTopView(position: Int) {
        if (ibookerEditorTopView != null)
            if (position == 0) {
                ibookerEditorTopView!!.editIBtn!!.setBackgroundResource(editIBtnSelectedRes)
                ibookerEditorTopView!!.previewIBtn!!.setBackgroundResource(previewIBtnDefaultRes)

                // 设置动画
                if (ibookerEditorToolView != null) {
                    if (inAnim != null)
                        ibookerEditorToolView!!.startAnimation(inAnim)
                    ibookerEditorToolView!!.visibility = View.VISIBLE
                }

                openInputSoft(true)
            } else if (position == 1) {
                ibookerEditorTopView!!.editIBtn!!.setBackgroundResource(editIBtnDefaultRes)
                ibookerEditorTopView!!.previewIBtn!!.setBackgroundResource(previewIBtnSelectedRes)
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
                val contentUrl = Uri.parse("http://ibooker.cc/article/1/detail")
                intent.data = contentUrl
                context.startActivity(intent)
            }
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_ABOUT -> {// 关于
                val intent = Intent()
                intent.action = "android.intent.action.VIEW"
                val contentUrl = Uri.parse("http://ibooker.cc/article/182/detail")
                intent.data = contentUrl
                context.startActivity(intent)
            }
            IbookerEditorEnum.TOOLVIEW_TAG.IBTN_SHARE -> {// 分享
                ibookerEditorVpView!!.currentItem = 1
                if (!hasPermission(*needPermissions)) {
                    requestPermission(12112, *needPermissions)
                } else {
                    generateBitmap()
                }
            }
        }
    }

    // 工具栏按钮点击事件监听
    override fun onToolClick(tag: Any) {
        ibookerEditorVpView!!.editView!!.ibookerTitleEd!!.clearFocus()
        ibookerEditorVpView!!.editView!!.ibookerEd!!.requestFocus()
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
    fun setIETopViewVisibility(visibility: Int): IbookerEditorView {
        if (visibility == View.VISIBLE || visibility == View.GONE || visibility == View.INVISIBLE)
            ibookerEditorTopView!!.visibility = visibility
        return this
    }

    /**
     * 设置编辑控件背景颜色
     *
     * @param color 背景颜色
     */
    fun setIEEditViewBackgroundColor(@ColorInt color: Int): IbookerEditorView {
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
    fun setIEPreViewBackgroundColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.preView!!.setBackgroundColor(color)
        ibookerEditorVpView!!.preView!!.ibookerTitleTv!!.setBackgroundColor(color)
        ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.setBackgroundColor(color)
        return this
    }

    // 设置返回按钮backImg
    fun setIETopViewBackImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorTopView!!.setBackImageResource(resId)
        return this
    }

    fun setIETopViewBackImgVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorTopView!!.setBackImgVisibility(visibility)
        return this
    }

    // 设置撤销按钮undoIBtn
    fun setIETopViewUndoImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorTopView!!.setUndoImageResource(resId)
        return this
    }

    fun setIETopViewUndoIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorTopView!!.setUndoIBtnVisibility(visibility)
        return this
    }

    // 设置重做按钮
    fun setIETopViewRedoImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorTopView!!.setRedoImageResource(resId)
        return this
    }

    fun setIETopViewRedoIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorTopView!!.setRedoIBtnVisibility(visibility)
        return this
    }

    // 设置编辑按钮
    fun setIETopViewEditImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorTopView!!.setEditImageResource(resId)
        return this
    }

    fun setIETopViewEditIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorTopView!!.setEditIBtnVisibility(visibility)
        return this
    }

    // 设置预览按钮
    fun setIETopViewPreviewImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorTopView!!.setPreviewImageResource(resId)
        return this
    }

    fun setIETopViewPreviewIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorTopView!!.setPreviewIBtnVisibility(visibility)
        return this
    }

    // 设置帮助按钮
    fun setIETopViewHelpImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorTopView!!.setHelpImageResource(resId)
        return this
    }

    fun setIETopViewHelpIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorTopView!!.setHelpIBtnVisibility(visibility)
        return this
    }

    // 设置关于按钮
    fun setIETopViewAboutImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorTopView!!.setAboutImageResource(resId)
        return this
    }

    fun setIETopViewAboutImgVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorTopView!!.setAboutImgVisibility(visibility)
        return this
    }


    // 设置分享按钮
    fun setIETopViewShareIBtnResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorTopView!!.setShareIBtnResource(resId)
        return this
    }

    fun setIETopViewShareIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorTopView!!.setShareIBtnVisibility(visibility)
        return this
    }

    // 设置更多按钮
    fun setIETopViewElseIBtnResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorTopView!!.setElseIBtnResource(resId)
        return this
    }

    fun setIETopViewElseIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorTopView!!.setElseIBtnVisibility(visibility)
        return this
    }

    /**
     * 设置输入框字体大小
     *
     * @param size 字体大小
     */
    fun setIEEditViewIbookerEdTextSize(size: Float): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setIbookerEdTextSize(size)
        return this
    }

    /**
     * 设置输入框字体颜色
     *
     * @param color 字体颜色
     */
    fun setIEEditViewIbookerEdTextColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setIbookerEdTextColor(color)
        return this
    }

    /**
     * 设置输入框hint内容
     *
     * @param hint hint内容
     */
    fun setIEEditViewIbookerEdHint(hint: CharSequence): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setIbookerEdHint(hint)
        return this
    }

    /**
     * 设置输入框text内容
     *
     * @param text text内容
     */
    fun setIEEditViewIbookerEdText(text: CharSequence): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.ibookerEd!!.setText(text)
        return this
    }

    /**
     * 设置输入框hint颜色
     *
     * @param color hint颜色
     */
    fun setIEEditViewIbookerEdHintTextColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setIbookerEdHintTextColor(color)
        return this
    }

    /**
     * 设置输入框背景颜色
     *
     * @param color 背景颜色
     */
    fun setIEEditViewIbookerEdBackgroundColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setIbookerEdBackgroundColor(color)
        return this
    }

    /**
     * 设置标题显示或者隐藏
     *
     * @param visibility View.GONE,View.VISIBLE,View.INVISIBLE
     */
    fun setIEEditViewIbookerTitleEdVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setIbookerTitleEdVisibility(visibility)
        return this
    }

    /**
     * 设置标题输入框字体大小
     *
     * @param size 字体大小
     */
    fun setIEEditViewIbookerTitleEdTextSize(size: Float): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setIbookerTitleEdTextSize(size)
        return this
    }

    /**
     * 设置标题输入框字体颜色
     *
     * @param color 字体颜色
     */
    fun setIEEditViewIbookerTitleEdTextColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setIbookerTitleEdTextColor(color)
        return this
    }

    /**
     * 设置标题输入框hint内容
     *
     * @param hint hint内容
     */
    fun setIEEditViewIbookerTitleEdHint(hint: CharSequence): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setIbookerTitleEdHint(hint)
        return this
    }

    /**
     * 设置标题输入框text内容
     *
     * @param text text内容
     */
    fun setIEEditViewIbookerTitleEdText(text: CharSequence): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.ibookerTitleEd!!.setText(text)
        return this
    }

    /**
     * 设置标题输入框hint颜色
     *
     * @param color hint颜色
     */
    fun setIEEditViewIbookerTitleEdHintTextColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setIbookerTitleEdHintTextColor(color)
        return this
    }

    /**
     * 设置线条的背景颜色
     *
     * @param color 颜色
     */
    fun setIEEditViewLineViewBackgroundColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setLineViewBackgroundColor(color)
        return this
    }

    /**
     * 设置线条显示或者隐藏
     *
     * @param visibility View.GONE,View.VISIBLE,View.INVISIBLE
     */
    fun setIEEditViewLineViewVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorVpView!!.editView!!.setLineViewVisibility(visibility)
        return this
    }

    // 粗体
    fun setIEToolViewBoldIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setBoldIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewBoldIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setBoldIBtnVisibility(visibility)
        return this
    }

    // 斜体
    fun setIEToolViewItalicIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setItalicIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewItalicIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setItalicIBtnVisibility(visibility)
        return this
    }

    // 删除线
    fun setIEToolViewStrikeoutIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setStrikeoutIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewStrikeoutIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setStrikeoutIBtnVisibility(visibility)
        return this
    }

    // 下划线
    fun setIEToolViewUnderlineIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setUnderlineIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewUnderlineIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setUnderlineIBtnVisibility(visibility)
        return this
    }

    // 单词首字母大写
    fun setIEToolViewCapitalsIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setCapitalsIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewCapitalsIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setCapitalsIBtnVisibility(visibility)
        return this
    }

    // 单词转大写
    fun setIEToolViewUppercaseIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setUppercaseIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewUppercaseIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setUppercaseIBtnVisibility(visibility)
        return this
    }

    // 单词转小写
    fun setIEToolViewLowercaseIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setLowercaseIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewLowercaseIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setLowercaseIBtnVisibility(visibility)
        return this
    }

    // 一级标题
    fun setIEToolViewH1IBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setH1IBtnImageResource(resId)
        return this
    }

    fun setIEToolViewH1IBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setH1IBtnVisibility(visibility)
        return this
    }

    // 二级标题
    fun setIEToolViewH2IBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setH2IBtnImageResource(resId)
        return this
    }

    fun setIEToolViewH2IBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setH2IBtnVisibility(visibility)
        return this
    }

    // 三级标题
    fun setIEToolViewH3IBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setH3IBtnImageResource(resId)
        return this
    }

    fun setIEToolViewH3IBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setH3IBtnVisibility(visibility)
        return this
    }

    // 四级标题
    fun setIEToolViewH4IBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setH4IBtnImageResource(resId)
        return this
    }

    fun setIEToolViewH4IBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setH4IBtnVisibility(visibility)
        return this
    }

    // 五级标题
    fun setIEToolViewH5IBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setH5IBtnImageResource(resId)
        return this
    }

    fun setIEToolViewH5IBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setH5IBtnVisibility(visibility)
        return this
    }

    // 六级标题
    fun setIEToolViewH6IBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setH6IBtnImageResource(resId)
        return this
    }

    fun setIEToolViewH6IBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setH6IBtnVisibility(visibility)
        return this
    }

    // 链接
    fun setIEToolViewLinkIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setLinkIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewLinkIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setLinkIBtnVisibility(visibility)
        return this
    }

    // 引用
    fun setIEToolViewQuoteIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setQuoteIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewQuoteIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setQuoteIBtnVisibility(visibility)
        return this
    }

    // 代码
    fun setIEToolViewCodeIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setCodeIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewCodeIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setCodeIBtnVisibility(visibility)
        return this
    }

    // 图片
    fun setIEToolViewImguIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setImguIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewImguIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setImguIBtnVisibility(visibility)
        return this
    }

    // 数字列表
    fun setIEToolViewOlIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setOlIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewOlIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setOlIBtnVisibility(visibility)
        return this
    }

    // 普通列表
    fun setIEToolViewUlIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setUlIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewUlIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setUlIBtnVisibility(visibility)
        return this
    }

    // 列表未选中
    fun setIEToolViewUnselectedIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setUnselectedIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewUnselectedIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setUnselectedIBtnVisibility(visibility)
        return this
    }

    // 列表选中
    fun setIEToolViewSelectedIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setSelectedIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewSelectedIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setSelectedIBtnVisibility(visibility)
        return this
    }

    // 表格
    fun setIEToolViewTableIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setTableIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewTableIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setTableIBtnVisibility(visibility)
        return this
    }

    // HTML
    fun setIEToolViewHtmlIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setHtmlIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewHtmlIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setHtmlIBtnVisibility(visibility)
        return this
    }

    // 分割线
    fun setIEToolViewHrIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setHrIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewHrIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setHrIBtnVisibility(visibility)
        return this
    }

    // 表情
    fun setIEToolViewEmojiIBtnImageResource(@DrawableRes resId: Int): IbookerEditorView {
        ibookerEditorToolView!!.setEmojiIBtnImageResource(resId)
        return this
    }

    fun setIEToolViewEmojiIBtnVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorToolView!!.setEmojiIBtnVisibility(visibility)
        return this
    }

    /**
     * 设置WebView控件背景颜色
     *
     * @param color 背景颜色
     */
    fun setIEPreViewIbookerEditorWebViewBackgroundColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.preView!!.setIbookerEditorWebViewBackgroundColor(color)
        return this
    }

    /**
     * 设置标题显示 或者隐藏
     *
     * @param visibility View.GONE,View.VISIBLE,View.INVISIBLE
     */
    fun setIEPreViewIbookerTitleTvVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorVpView!!.preView!!.setIbookerTitleTvVisibility(visibility)
        return this
    }

    /**
     * 设置标题字体大小
     *
     * @param size 字体大小
     */
    fun setIEPreViewIbookerTitleTvTextSize(size: Float): IbookerEditorView {
        ibookerEditorVpView!!.preView!!.setIbookerTitleTvTextSize(size)
        return this
    }

    /**
     * 设置标题字体颜色
     *
     * @param color 字体颜色
     */
    fun setIEPreViewIbookerTitleTvTextColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.preView!!.setIbookerTitleTvTextColor(color)
        return this
    }

    /**
     * 设置标题hint内容
     *
     * @param hint hint内容
     */
    fun setIEPreViewIbookerTitleTvHint(hint: CharSequence): IbookerEditorView {
        ibookerEditorVpView!!.preView!!.setIbookerTitleTvHint(hint)
        return this
    }

    /**
     * 设置标题hint颜色
     *
     * @param color hint颜色
     */
    fun setIEPreViewIbookerTitleTvHintTextColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.preView!!.setIbookerTitleTvHintTextColor(color)
        return this
    }

    /**
     * 设置线条的背景颜色
     *
     * @param color 颜色
     */
    fun setIEPreViewLineViewBackgroundColor(@ColorInt color: Int): IbookerEditorView {
        ibookerEditorVpView!!.preView!!.setLineViewBackgroundColor(color)
        return this
    }

    /**
     * 设置线条显示或者隐藏
     *
     * @param visibility View.GONE,View.VISIBLE,View.INVISIBLE
     */
    fun setIEPreViewLineViewVisibility(visibility: Int): IbookerEditorView {
        ibookerEditorVpView!!.preView!!.setLineViewVisibility(visibility)
        return this
    }

    /**
     * 执行预览
     *
     * @param ibookerEditorText 待预览内容 非HTML
     */
    fun ibookerCompile(ibookerEditorText: String) {
        ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.ibookerCompile(ibookerEditorText)
    }

    /**
     * 执行Html预览
     *
     * @param ibookerEditorHtml 待预览内容 HTML
     */
    fun ibookerHtmlCompile(ibookerEditorHtml: String) {
        ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.ibookerHtmlCompile(ibookerEditorHtml)
    }

    /**
     * 编辑框顶部按钮点击监听
     */
    fun setOnTopClickListener(onTopClickListener: IbookerEditorTopView.OnTopClickListener) {
        ibookerEditorTopView!!.setOnTopClickListener(onTopClickListener)
    }

    /**
     * 编辑区输入标题监听
     */
    fun setOnIbookerTitleEdTextChangedListener(onIbookerTitleEdTextChangedListener: IbookerEditorEditView.OnIbookerTitleEdTextChangedListener) {
        ibookerEditorVpView!!.editView!!.setOnIbookerTitleEdTextChangedListener(onIbookerTitleEdTextChangedListener)
    }

    /**
     * 编辑区输入内容监听
     */
    fun setOnIbookerEdTextChangedListener(onIbookerEdTextChangedListener: IbookerEditorEditView.OnIbookerEdTextChangedListener) {
        ibookerEditorVpView!!.editView!!.setOnIbookerEdTextChangedListener(onIbookerEdTextChangedListener)
    }

    /**
     * 底部工具栏监听
     */
    fun setOnToolClickListener(onToolClickListener: IbookerEditorToolView.OnToolClickListener) {
        ibookerEditorToolView!!.setOnToolClickListener(onToolClickListener)
    }

    /**
     * 图片预览接口
     */
    fun setIbookerEditorImgPreviewListener(ibookerEditorImgPreviewListener: IbookerEditorWebView.IbookerEditorImgPreviewListener) {
        ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.setIbookerEditorImgPreviewListener(ibookerEditorImgPreviewListener)
    }

    /**
     * Url加载状态接口
     */
    fun setIbookerEditorWebViewUrlLoadingListener(ibookerEditorWebViewUrlLoadingListener: IbookerEditorWebView.IbookerEditorWebViewUrlLoadingListener) {
        ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.setIbookerEditorWebViewUrlLoadingListener(ibookerEditorWebViewUrlLoadingListener)
    }

    /**
     * 生成File文件
     */
    fun createSDDirs(path: String): File? {
        if (Environment.getExternalStorageState() == "mounted") {
            val dir = File(path)
            var bool = true
            if (!dir.exists()) {
                bool = dir.mkdirs()
            }
            return if (!bool) null else dir
        } else {
            return null
        }
    }

    /**
     * 生成图片
     */
    fun generateBitmap() {
        Handler().postDelayed({
            if (ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.isLoadFinished) {
                Toast.makeText(this@IbookerEditorView.context, "图片生成中...", Toast.LENGTH_SHORT).show()
                val bitmap = ibookerEditorVpView!!.preView!!.ibookerEditorWebView!!.getWebViewBitmap()
                try {
                    val filePath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "ibookerEditor" + File.separator + "shares" + File.separator
                    val fileName = System.currentTimeMillis().toString() + ".jpg"
                    val dir = File(filePath)
                    val bool = dir.exists()
                    if (!bool)
                        createSDDirs(filePath)
                    val file = File(filePath, fileName)

                    val fOut = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut)
                    fOut.flush()
                    fOut.close()

                    // 进入图片预览
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.fromFile(file), "image/*")
                    this@IbookerEditorView.context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    bitmap.recycle()
                    System.gc()
                }
            } else {
                generateBitmap()
            }
        }, 500)
    }

    /**
     * 权限检查方法，false代表没有该权限，ture代表有该权限
     */
    fun hasPermission(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this.context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 权限请求方法
     */
    fun requestPermission(code: Int, vararg permissions: String) {
        ActivityCompat.requestPermissions(this.context as Activity, permissions, code)
    }
}// 构造方法
