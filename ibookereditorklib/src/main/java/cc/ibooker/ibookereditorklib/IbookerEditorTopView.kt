package cc.ibooker.ibookereditorklib

import android.content.Context
import android.graphics.Color
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout

/**
 * 书客编辑器 - 顶部布局
 * Created by 邹峰立 on 2018/2/11.
 */
class IbookerEditorTopView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {
    // getter
    var backImg: ImageView? = null
    var aboutImg: ImageView? = null
    private var rightLayout: LinearLayout? = null
    var undoIBtn: ImageButton? = null
    var redoIBtn: ImageButton? = null
    var editIBtn: ImageButton? = null
    var previewIBtn: ImageButton? = null
    var helpIBtn: ImageButton? = null
    var shareIBtn: ImageButton? = null
    var elseIBtn: ImageButton? = null

    private var onTopClickListener: OnTopClickListener? = null

    private var dp5 = 0
    private var dp13 = 0
    private var dp29 = 0

    init {
        orientation = LinearLayout.HORIZONTAL
        setBackgroundColor(Color.parseColor("#EFEFEF"))
        gravity = Gravity.CENTER_VERTICAL
        minimumHeight = IbookerEditorUtil.dpToPx(context, 48f)

        dp5 = IbookerEditorUtil.dpToPx(context, 5f)
        dp13 = IbookerEditorUtil.dpToPx(context, 13f)
        dp29 = IbookerEditorUtil.dpToPx(context, 29f)

        setPadding(dp5, dp5, dp5, dp5)
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        init(context)
    }

    // 初始化
    private fun init(context: Context) {
        backImg = ImageView(context)
        val backParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, IbookerEditorUtil.dpToPx(context, 22f))
        backParams.setMargins(dp5, 0, dp5, 0)
        backImg!!.layoutParams = backParams
        backImg!!.setImageResource(R.drawable.icon_back_black)
        backImg!!.adjustViewBounds = true
        backImg!!.contentDescription = resources.getString(R.string.back)
        backImg!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IMG_BACK
        backImg!!.setOnClickListener(this)
        addView(backImg)

        rightLayout = LinearLayout(context)
        rightLayout!!.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        rightLayout!!.gravity = Gravity.END or Gravity.CENTER_VERTICAL
        addView(rightLayout)

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(dp5, 0, dp5, 0)

        undoIBtn = ImageButton(context)
        undoIBtn!!.layoutParams = layoutParams
        undoIBtn!!.setBackgroundResource(R.drawable.draw_undo)
        undoIBtn!!.contentDescription = resources.getString(R.string.undo)
        undoIBtn!!.setPadding(dp13, dp13, dp13, dp13)
        undoIBtn!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UNDO
        undoIBtn!!.setOnClickListener(this)
        rightLayout!!.addView(undoIBtn)

        redoIBtn = ImageButton(context)
        redoIBtn!!.layoutParams = layoutParams
        redoIBtn!!.setBackgroundResource(R.drawable.draw_redo)
        redoIBtn!!.contentDescription = resources.getString(R.string.redo)
        redoIBtn!!.setPadding(dp13, dp13, dp13, dp13)
        redoIBtn!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IBTN_REDO
        redoIBtn!!.setOnClickListener(this)
        rightLayout!!.addView(redoIBtn)

        editIBtn = ImageButton(context)
        editIBtn!!.layoutParams = layoutParams
        editIBtn!!.setBackgroundResource(R.drawable.draw_edit)
        editIBtn!!.contentDescription = resources.getString(R.string.edit)
        editIBtn!!.setPadding(dp13, dp13, dp13, dp13)
        editIBtn!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IBTN_EDIT
        editIBtn!!.setOnClickListener(this)
        rightLayout!!.addView(editIBtn)

        previewIBtn = ImageButton(context)
        previewIBtn!!.layoutParams = layoutParams
        previewIBtn!!.setBackgroundResource(R.drawable.draw_preview)
        previewIBtn!!.contentDescription = resources.getString(R.string.preview)
        previewIBtn!!.setPadding(dp13, dp13, dp13, dp13)
        previewIBtn!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IBTN_PREVIEW
        previewIBtn!!.setOnClickListener(this)
        rightLayout!!.addView(previewIBtn)

        helpIBtn = ImageButton(context)
        helpIBtn!!.layoutParams = layoutParams
        helpIBtn!!.setBackgroundResource(R.drawable.draw_help)
        helpIBtn!!.contentDescription = resources.getString(R.string.help)
        helpIBtn!!.setPadding(dp13, dp13, dp13, dp13)
        helpIBtn!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IBTN_HELP
        helpIBtn!!.setOnClickListener(this)
        rightLayout!!.addView(helpIBtn)

        shareIBtn = ImageButton(context)
        shareIBtn!!.layoutParams = layoutParams
        shareIBtn!!.setBackgroundResource(R.drawable.draw_share)
        shareIBtn!!.contentDescription = resources.getString(R.string.share)
        shareIBtn!!.setPadding(dp13, dp13, dp13, dp13)
        shareIBtn!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IBTN_SHARE
        shareIBtn!!.setOnClickListener(this)
        rightLayout!!.addView(shareIBtn)

        val params = LinearLayout.LayoutParams(dp29, dp29)
        params.setMargins(dp5, 0, dp5, 0)
        aboutImg = ImageView(context)
        aboutImg!!.layoutParams = params
        aboutImg!!.setImageResource(R.drawable.ibooker_editor_logo)
        aboutImg!!.contentDescription = resources.getString(R.string.about)
        aboutImg!!.setPadding(dp5, dp5, dp5, dp5)
        aboutImg!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IBTN_ABOUT
        aboutImg!!.setOnClickListener(this)
        rightLayout!!.addView(aboutImg)

        elseIBtn = ImageButton(context)
        elseIBtn!!.layoutParams = layoutParams
        elseIBtn!!.setBackgroundResource(R.drawable.draw_else)
        elseIBtn!!.contentDescription = resources.getString(R.string._else)
        elseIBtn!!.setPadding(dp13, dp13, dp13, dp13)
        elseIBtn!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IBTN_ELSE
        elseIBtn!!.setOnClickListener(this)
        rightLayout!!.addView(elseIBtn)
    }

    // 点击事件监听
    override fun onClick(v: View) {
        if (onTopClickListener != null)
            onTopClickListener!!.onTopClick(v.tag)
    }

    // 设置返回按钮backImg
    fun setBackImageResource(@DrawableRes resId: Int): IbookerEditorTopView {
        backImg!!.setImageResource(resId)
        return this
    }

    fun setBackImgVisibility(visibility: Int): IbookerEditorTopView {
        backImg!!.visibility = visibility
        return this
    }

    // 设置撤销按钮undoIBtn
    fun setUndoImageResource(@DrawableRes resId: Int): IbookerEditorTopView {
        undoIBtn!!.setImageResource(resId)
        return this
    }

    fun setUndoIBtnVisibility(visibility: Int): IbookerEditorTopView {
        undoIBtn!!.visibility = visibility
        return this
    }

    // 设置重做按钮
    fun setRedoImageResource(@DrawableRes resId: Int): IbookerEditorTopView {
        redoIBtn!!.setImageResource(resId)
        return this
    }

    fun setRedoIBtnVisibility(visibility: Int): IbookerEditorTopView {
        redoIBtn!!.visibility = visibility
        return this
    }

    // 设置编辑按钮
    fun setEditImageResource(@DrawableRes resId: Int): IbookerEditorTopView {
        editIBtn!!.setImageResource(resId)
        return this
    }

    fun setEditIBtnVisibility(visibility: Int): IbookerEditorTopView {
        editIBtn!!.visibility = visibility
        return this
    }

    // 设置预览按钮
    fun setPreviewImageResource(@DrawableRes resId: Int): IbookerEditorTopView {
        previewIBtn!!.setImageResource(resId)
        return this
    }

    fun setPreviewIBtnVisibility(visibility: Int): IbookerEditorTopView {
        previewIBtn!!.visibility = visibility
        return this
    }

    // 设置帮助按钮
    fun setHelpImageResource(@DrawableRes resId: Int): IbookerEditorTopView {
        helpIBtn!!.setImageResource(resId)
        return this
    }

    fun setHelpIBtnVisibility(visibility: Int): IbookerEditorTopView {
        helpIBtn!!.visibility = visibility
        return this
    }

    // 设置关于按钮
    fun setAboutImageResource(@DrawableRes resId: Int): IbookerEditorTopView {
        aboutImg!!.setImageResource(resId)
        return this
    }

    fun setAboutImgVisibility(visibility: Int): IbookerEditorTopView {
        aboutImg!!.visibility = visibility
        return this
    }


    // 设置分享按钮
    fun setShareIBtnResource(@DrawableRes resId: Int): IbookerEditorTopView {
        shareIBtn!!.setImageResource(resId)
        return this
    }

    fun setShareIBtnVisibility(visibility: Int): IbookerEditorTopView {
        shareIBtn!!.visibility = visibility
        return this
    }

    // 设置更多按钮
    fun setElseIBtnResource(@DrawableRes resId: Int): IbookerEditorTopView {
        elseIBtn!!.setImageResource(resId)
        return this
    }

    fun setElseIBtnVisibility(visibility: Int): IbookerEditorTopView {
        elseIBtn!!.visibility = visibility
        return this
    }

    /**
     * 点击事件监听
     */
    interface OnTopClickListener {
        fun onTopClick(tag: Any)
    }

    fun setOnTopClickListener(onTopClickListener: OnTopClickListener) {
        this.onTopClickListener = onTopClickListener
    }
}// 构造方法
