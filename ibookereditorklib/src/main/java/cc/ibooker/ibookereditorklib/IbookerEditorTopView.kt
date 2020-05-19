package cc.ibooker.ibookereditorklib

import android.content.Context
import android.graphics.Color
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

/**
 * 书客编辑器 - 顶部布局
 * Created by 邹峰立 on 2018/2/11.
 */
class IbookerEditorTopView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {
    //    private ImageView backImg, aboutImg;
    // getter
    var backTv: TextView? = null
        private set
    //    public ImageView getBackImg() {
    //        return backImg;
    //    }

    //    public ImageView getAboutImg() {
    //        return aboutImg;
    //    }

    var rightLayout: LinearLayout? = null
        private set
    var undoIBtn: ImageButton? = null
        private set
    var redoIBtn: ImageButton? = null
        private set
    var editIBtn: ImageButton? = null
        private set
    var previewIBtn: ImageButton? = null
        private set
    //    public ImageButton getHelpIBtn() {
    //        return helpIBtn;
    //    }

    var shareIBtn: ImageButton? = null
        private set
    var elseIBtn: ImageButton? = null
        private set
    var setIBtn: ImageButton? = null
    private val helpIBtn: ImageButton? = null
    private val dp13: Int
    private val dp3_5: Int
    private val dp4_5: Int
    private val dp5: Int
    private val dp12: Int
    private val dp29: Int

    private var onTopClickListener: OnTopClickListener? = null

    init {
        orientation = HORIZONTAL
        setBackgroundColor(Color.parseColor("#EFEFEF"))
        gravity = Gravity.CENTER_VERTICAL
        minimumHeight = IbookerEditorUtil.dpToPx(context, 48f)

        dp13 = IbookerEditorUtil.dpToPx(context, 13f)
        dp12 = IbookerEditorUtil.dpToPx(context, 12f)
        dp3_5 = IbookerEditorUtil.dpToPx(context, 3.5f)
        dp4_5 = IbookerEditorUtil.dpToPx(context, 4.5f)
        dp5 = IbookerEditorUtil.dpToPx(context, 5f)
        dp29 = IbookerEditorUtil.dpToPx(context, 29f)

        setPadding(dp5, dp5, dp5, dp5)
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        init(context)
    }

    // 初始化
    private fun init(context: Context) {
        //        backImg = new ImageView(context);
        //        LayoutParams backParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, IbookerEditorUtil.dpToPx(context, 22F));
        //        backParams.setMargins(dp5, 0, dp5, 0);
        //        backImg.setLayoutParams(backParams);
        //        backImg.setImageResource(R.drawable.icon_back_black);
        //        backImg.setAdjustViewBounds(true);
        //        backImg.setContentDescription(getResources().getString(R.string.back));
        //        backImg.setTag(IbookerEditorEnum.TOOLVIEW_TAG.IMG_BACK);
        //        backImg.setOnClickListener(this);
        //        addView(backImg);

        backTv = TextView(context)
        val backParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, IbookerEditorUtil.dpToPx(context, 22f))
        backParams.setMargins(dp5, 0, dp5, 0)
        backTv!!.layoutParams = backParams
        backTv!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_back_black, 0, 0, 0)
        backTv!!.compoundDrawablePadding = dp5
        backTv!!.contentDescription = resources.getString(R.string.back)
        backTv!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IMG_BACK
        backTv!!.gravity = Gravity.CENTER
        backTv!!.textSize = 15f
        backTv!!.setTextColor(Color.parseColor("#777777"))
        backTv!!.setOnClickListener(this)
        addView(backTv)

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

        shareIBtn = ImageButton(context)
        shareIBtn!!.layoutParams = layoutParams
        shareIBtn!!.setBackgroundResource(R.drawable.draw_share)
        shareIBtn!!.contentDescription = resources.getString(R.string.share)
        shareIBtn!!.setPadding(dp12, dp12, dp12, dp12)
        shareIBtn!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IBTN_SHARE
        shareIBtn!!.setOnClickListener(this)
        rightLayout!!.addView(shareIBtn)

        setIBtn = ImageButton(context)
        setIBtn!!.layoutParams = layoutParams
        setIBtn!!.setBackgroundResource(R.drawable.draw_set)
        setIBtn!!.contentDescription = resources.getString(R.string.set)
        setIBtn!!.setPadding(dp12, dp12, dp12, dp12)
        setIBtn!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IBTN_SET
        setIBtn!!.setOnClickListener(this)
        rightLayout!!.addView(setIBtn)

        //        helpIBtn = new ImageButton(context);
        //        helpIBtn.setLayoutParams(layoutParams);
        //        helpIBtn.setBackgroundResource(R.drawable.draw_help);
        //        helpIBtn.setContentDescription(getResources().getString(R.string.help));
        //        helpIBtn.setPadding(dp13, dp13, dp13, dp13);
        //        helpIBtn.setTag(IbookerEditorEnum.TOOLVIEW_TAG.IBTN_HELP);
        //        helpIBtn.setOnClickListener(this);
        //        rightLayout.addView(helpIBtn);

        //        LayoutParams params = new LayoutParams(dp29, dp29);
        //        params.setMargins(dp3_5, 0, dp3_5, 0);
        //        aboutImg = new ImageView(context);
        //        aboutImg.setLayoutParams(params);
        //        aboutImg.setImageResource(R.drawable.ibooker_editor_logo);
        //        aboutImg.setContentDescription(getResources().getString(R.string.about));
        //        aboutImg.setPadding(dp4_5, dp4_5, dp4_5, dp4_5);
        //        aboutImg.setTag(IbookerEditorEnum.TOOLVIEW_TAG.IBTN_ABOUT);
        //        aboutImg.setOnClickListener(this);
        //        rightLayout.addView(aboutImg);

        elseIBtn = ImageButton(context)
        elseIBtn!!.layoutParams = layoutParams
        elseIBtn!!.setBackgroundResource(R.drawable.draw_else)
        elseIBtn!!.contentDescription = resources.getString(R.string._else)
        elseIBtn!!.setPadding(dp12, dp12, dp12, dp12)
        elseIBtn!!.tag = IbookerEditorEnum.TOOLVIEW_TAG.IBTN_ELSE
        elseIBtn!!.setOnClickListener(this)
        rightLayout!!.addView(elseIBtn)
    }

    // 点击事件监听
    override fun onClick(v: View) {
        if (onTopClickListener != null)
            onTopClickListener!!.onTopClick(v.tag)
    }

    //    // 设置返回按钮backImg
    //    public IbookerEditorTopView setBackImageResource(@DrawableRes int resId) {
    //        backImg.setImageResource(resId);
    //        return this;
    //    }
    //
    //    public IbookerEditorTopView setBackImgVisibility(int visibility) {
    //        backImg.setVisibility(visibility);
    //        return this;
    //    }

    // 设置返回按钮backTv
    fun setBackTvResource(@DrawableRes resId: Int): IbookerEditorTopView {
        backTv!!.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
        return this
    }

    fun setBackTvVisibility(visibility: Int): IbookerEditorTopView {
        backTv!!.visibility = visibility
        return this
    }

    fun setBackTvFontNum(num: Int): IbookerEditorTopView {
        if (num > 0) {
            var text = num.toString() + ""
            if (num > 100000)
                text = (num / 100000).toString() + "W"
            backTv!!.text = text
        }
        return this
    }

    // 设置撤销按钮undoIBtn
    fun setUndoImageResource(@DrawableRes resId: Int): IbookerEditorTopView {
        undoIBtn!!.setImageResource(resId)
        return this
    }

    fun setUndoImageBgResource(@DrawableRes resId: Int): IbookerEditorTopView {
        undoIBtn!!.setBackgroundResource(resId)
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

    fun setRedoImageBgResource(@DrawableRes resId: Int): IbookerEditorTopView {
        redoIBtn!!.setBackgroundResource(resId)
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

    fun setEditImageBgResource(@DrawableRes resId: Int): IbookerEditorTopView {
        editIBtn!!.setBackgroundResource(resId)
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

    fun setPreviewImageBgResource(@DrawableRes resId: Int): IbookerEditorTopView {
        previewIBtn!!.setBackgroundResource(resId)
        return this
    }

    fun setPreviewIBtnVisibility(visibility: Int): IbookerEditorTopView {
        previewIBtn!!.visibility = visibility
        return this
    }

    //    // 设置帮助按钮
    //    public IbookerEditorTopView setHelpImageResource(@DrawableRes int resId) {
    //        helpIBtn.setImageResource(resId);
    //        return this;
    //    }
    //
    //    public IbookerEditorTopView setHelpIBtnVisibility(int visibility) {
    //        helpIBtn.setVisibility(visibility);
    //        return this;
    //    }

    //    // 设置关于按钮
    //    public IbookerEditorTopView setAboutImageResource(@DrawableRes int resId) {
    //        aboutImg.setImageResource(resId);
    //        return this;
    //    }
    //
    //    public IbookerEditorTopView setAboutImgVisibility(int visibility) {
    //        aboutImg.setVisibility(visibility);
    //        return this;
    //    }

    // 设置分享按钮
    fun setShareIBtnResource(@DrawableRes resId: Int): IbookerEditorTopView {
        shareIBtn!!.setImageResource(resId)
        return this
    }

    fun setShareIBtnBgResource(@DrawableRes resId: Int): IbookerEditorTopView {
        shareIBtn!!.setBackgroundResource(resId)
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

    fun setElseIBtnBgResource(@DrawableRes resId: Int): IbookerEditorTopView {
        elseIBtn!!.setBackgroundResource(resId)
        return this
    }

    fun setElseIBtnVisibility(visibility: Int): IbookerEditorTopView {
        elseIBtn!!.visibility = visibility
        return this
    }

    // 设置按钮
    fun setSetIBtnResource(@DrawableRes resId: Int): IbookerEditorTopView {
        setIBtn!!.setImageResource(resId)
        return this
    }

    fun setSetIBtnBgResource(@DrawableRes resId: Int): IbookerEditorTopView {
        setIBtn!!.setBackgroundResource(resId)
        return this
    }

    fun setSetIBtnVisibility(visibility: Int): IbookerEditorTopView {
        setIBtn!!.visibility = visibility
        return this
    }

    /**
     * 点击事件监听
     */
    interface OnTopClickListener {
        fun onTopClick(tag: Any)
    }

    fun setOnTopClickListener(onTopClickListener: OnTopClickListener): IbookerEditorTopView {
        this.onTopClickListener = onTopClickListener
        return this
    }
}// 构造方法
