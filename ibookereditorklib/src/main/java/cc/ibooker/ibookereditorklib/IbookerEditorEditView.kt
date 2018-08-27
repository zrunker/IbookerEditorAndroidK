package cc.ibooker.ibookereditorklib

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.RequiresApi
import android.support.v4.widget.NestedScrollView
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*

/**
 * 书客编辑器 - 编辑界面
 * Created by 邹峰立 on 2018/2/11.
 */
open class IbookerEditorEditView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : NestedScrollView(context, attrs, defStyleAttr) {
    var ibookerTitleEd: EditText? = null
    var lineView: View? = null
    var ibookerEd: EditText? = null

    private var textList: ArrayList<String>? = ArrayList()
    private var isSign = true// 标记是否需要记录currentPos=textList.size()和textList
    private var currentPos = 0

    private var dp10 = 0

    private var onIbookerTitleEdTextChangedListener: OnIbookerTitleEdTextChangedListener? = null

    private var onIbookerEdTextChangedListener: OnIbookerEdTextChangedListener? = null

    init {
        isVerticalScrollBarEnabled = false
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        isFillViewport = true

        dp10 = IbookerEditorUtil.dpToPx(context, 10f)

        init(context)
    }

    // 初始化
    private fun init(context: Context) {
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        linearLayout.orientation = LinearLayout.VERTICAL
        addView(linearLayout)

        ibookerTitleEd = EditText(context)
        val titleParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, IbookerEditorUtil.dpToPx(context, 50f))
        titleParams.leftMargin = dp10
        titleParams.rightMargin = dp10
        ibookerTitleEd!!.layoutParams = titleParams
        ibookerTitleEd!!.setPadding(0, 0, 0, 0)
        ibookerTitleEd!!.setBackgroundColor(resources.getColor(android.R.color.transparent))
        ibookerTitleEd!!.setSingleLine(true)
        ibookerTitleEd!!.setLines(1)
        ibookerTitleEd!!.setTextColor(Color.parseColor("#444444"))
        ibookerTitleEd!!.textSize = 18f
        ibookerTitleEd!!.setLineSpacing(4f, 1.3f)
        ibookerTitleEd!!.hint = "标题"
        ibookerTitleEd!!.gravity = Gravity.CENTER_VERTICAL or Gravity.START
        ibookerTitleEd!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (onIbookerTitleEdTextChangedListener != null)
                    onIbookerTitleEdTextChangedListener!!.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (onIbookerTitleEdTextChangedListener != null)
                    onIbookerTitleEdTextChangedListener!!.onTextChanged(s, start, before, count)
            }

            override fun afterTextChanged(s: Editable) {
                if (onIbookerTitleEdTextChangedListener != null)
                    onIbookerTitleEdTextChangedListener!!.afterTextChanged(s)
            }
        })
        linearLayout.addView(ibookerTitleEd)

        lineView = View(context)
        lineView!!.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1)
        lineView!!.setBackgroundColor(Color.parseColor("#BABABA"))
        linearLayout.addView(lineView)

        ibookerEd = EditText(context)
        ibookerEd!!.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        ibookerEd!!.gravity = Gravity.TOP or Gravity.START
        ibookerEd!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        ibookerEd!!.setSingleLine(false)
        ibookerEd!!.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        ibookerEd!!.hint = "书客编辑器，从这里开始"
        ibookerEd!!.setPadding(dp10, dp10, dp10, dp10)
        ibookerEd!!.setBackgroundResource(android.R.color.transparent)
        ibookerEd!!.setTextColor(Color.parseColor("#444444"))
        ibookerEd!!.textSize = 16f
        ibookerEd!!.setLineSpacing(4f, 1.3f)
        ibookerEd!!.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                try {
                    val mEditor = TextView::class.java.getDeclaredField("mEditor")//找到 TextView中的成员变量mEditor
                    mEditor.isAccessible = true
                    val `object` = mEditor.get(ibookerEd)//根具持有对象拿到mEditor变量里的值 （android.widget.Editor类的实例）

                    //--------------------显示选择控制工具------------------------------//
                    @SuppressLint("PrivateApi")
                    val mClass = Class.forName("android.widget.Editor")// 拿到隐藏类Editor；
                    val method = mClass.getDeclaredMethod("getSelectionController")// 取得方法  getSelectionController
                    method.isAccessible = true// 取消访问私有方法的合法性检查
                    val resultobject = method.invoke(`object`)// 调用方法，返回SelectionModifierCursorController类的实例

                    val show = resultobject.javaClass.getDeclaredMethod("show")// 查找 SelectionModifierCursorController类中的show方法
                    show.invoke(resultobject)// 执行SelectionModifierCursorController类的实例的show方法
                    ibookerEd!!.setHasTransientState(true)

                    //--------------------忽略最后一次TouchUP事件------------------------------//
                    val mSelectionActionMode = mClass.getDeclaredField("mDiscardNextActionUp")// 查找变量Editor类中mDiscardNextActionUp
                    mSelectionActionMode.isAccessible = true
                    mSelectionActionMode.set(`object`, true)//赋值为true
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return false// 返回false 就是屏蔽ActionMode菜单
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }
        }
        ibookerEd!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (onIbookerEdTextChangedListener != null)
                    onIbookerEdTextChangedListener!!.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (onIbookerEdTextChangedListener != null)
                    onIbookerEdTextChangedListener!!.onTextChanged(s, start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                if (textList == null)
                    textList = ArrayList()
                if (s != null) {
                    if (isSign) {
                        textList!!.add(s.toString())
                        currentPos = textList!!.size
                    }
                }
                if (onIbookerEdTextChangedListener != null)
                    onIbookerEdTextChangedListener!!.afterTextChanged(s)
            }
        })
        ibookerEd!!.setHorizontallyScrolling(false)
        linearLayout.addView(ibookerEd)

        (ibookerEd!!.context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    // 重做 - redo
    fun redo() {
        if (textList != null && textList!!.size > 0 && currentPos + 1 >= 0 && currentPos + 1 < textList!!.size) {
            isSign = false
            currentPos++
            ibookerEd!!.setText(textList!![currentPos])
            ibookerEd!!.setSelection(textList!![currentPos].length)
            isSign = true
        }
    }

    // 撤销 - undo
    fun undo() {
        if (textList != null && textList!!.size > 0) {
            isSign = false
            if (currentPos == 0)
                ibookerEd!!.setText("")
            if (currentPos - 1 >= 0 && currentPos - 1 < textList!!.size) {
                currentPos--
                ibookerEd!!.setText(textList!![currentPos])
                ibookerEd!!.setSelection(textList!![currentPos].length)
            }
            isSign = true
        }

    }

    /**
     * 设置输入框字体大小
     *
     * @param size 字体大小
     */
    fun setIbookerEdTextSize(size: Float): IbookerEditorEditView {
        ibookerEd!!.textSize = size
        return this
    }

    /**
     * 设置输入框字体颜色
     *
     * @param color 字体颜色
     */
    fun setIbookerEdTextColor(@ColorInt color: Int): IbookerEditorEditView {
        ibookerEd!!.setTextColor(color)
        return this
    }

    /**
     * 设置输入框hint内容
     *
     * @param hint hint内容
     */
    fun setIbookerEdHint(hint: CharSequence): IbookerEditorEditView {
        ibookerEd!!.hint = hint
        return this
    }

    /**
     * 设置输入框hint颜色
     *
     * @param color hint颜色
     */
    fun setIbookerEdHintTextColor(@ColorInt color: Int): IbookerEditorEditView {
        ibookerEd!!.setHintTextColor(color)
        return this
    }

    /**
     * 设置输入框背景颜色
     *
     * @param color 背景颜色
     */
    fun setIbookerEdBackgroundColor(@ColorInt color: Int): IbookerEditorEditView {
        ibookerEd!!.setBackgroundColor(color)
        return this
    }

    /**
     * 设置标题显示或者隐藏
     *
     * @param visibility View.GONE,View.VISIBLE,View.INVISIBLE
     */
    fun setIbookerTitleEdVisibility(visibility: Int): IbookerEditorEditView {
        if (visibility == View.GONE || visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            if (ibookerTitleEd != null)
                ibookerTitleEd!!.visibility = visibility
            if (lineView != null)
                lineView!!.visibility = visibility
        }
        return this
    }

    /**
     * 设置标题输入框字体大小
     *
     * @param size 字体大小
     */
    fun setIbookerTitleEdTextSize(size: Float): IbookerEditorEditView {
        ibookerTitleEd!!.textSize = size
        return this
    }

    /**
     * 设置标题输入框字体颜色
     *
     * @param color 字体颜色
     */
    fun setIbookerTitleEdTextColor(@ColorInt color: Int): IbookerEditorEditView {
        ibookerTitleEd!!.setTextColor(color)
        return this
    }

    /**
     * 设置标题输入框hint内容
     *
     * @param hint hint内容
     */
    fun setIbookerTitleEdHint(hint: CharSequence): IbookerEditorEditView {
        ibookerTitleEd!!.hint = hint
        return this
    }

    /**
     * 设置标题输入框hint颜色
     *
     * @param color hint颜色
     */
    fun setIbookerTitleEdHintTextColor(@ColorInt color: Int): IbookerEditorEditView {
        ibookerTitleEd!!.setHintTextColor(color)
        return this
    }

    /**
     * 设置线条的背景颜色
     *
     * @param color 颜色
     */
    fun setLineViewBackgroundColor(@ColorInt color: Int): IbookerEditorEditView {
        lineView!!.setBackgroundColor(color)
        return this
    }

    /**
     * 设置线条显示或者隐藏
     *
     * @param visibility View.GONE,View.VISIBLE,View.INVISIBLE
     */
    fun setLineViewVisibility(visibility: Int): IbookerEditorEditView {
        if (visibility == View.GONE || visibility == View.VISIBLE || visibility == View.INVISIBLE)
            lineView!!.visibility = visibility
        return this
    }

    /**
     * 标题输入框输入监听
     */
    interface OnIbookerTitleEdTextChangedListener {
        fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)

        fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)

        fun afterTextChanged(s: Editable)
    }

    fun setOnIbookerTitleEdTextChangedListener(onIbookerTitleEdTextChangedListener: OnIbookerTitleEdTextChangedListener) {
        this.onIbookerTitleEdTextChangedListener = onIbookerTitleEdTextChangedListener
    }

    /**
     * 内容输入框监听
     */
    interface OnIbookerEdTextChangedListener {
        fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)

        fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)

        fun afterTextChanged(s: Editable?)
    }

    fun setOnIbookerEdTextChangedListener(onIbookerEdTextChangedListener: OnIbookerEdTextChangedListener) {
        this.onIbookerEdTextChangedListener = onIbookerEdTextChangedListener
    }
}// 三种构造方法
