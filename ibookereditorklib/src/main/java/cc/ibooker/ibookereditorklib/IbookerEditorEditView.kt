package cc.ibooker.ibookereditorklib

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import java.util.*

/**
 * 书客编辑器 - 编辑界面
 * Created by 邹峰立 on 2018/2/11.
 */
class IbookerEditorEditView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ScrollView(context, attrs, defStyleAttr) {
    var ibookerEd: EditText? = null

    private var textList: ArrayList<String>? = ArrayList()
    private var isSign = true// 标记是否需要记录currentPos=textList.size()和textList
    private var currentPos = 0

    init {
        isVerticalScrollBarEnabled = false
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        init(context)
    }

    // 初始化
    private fun init(context: Context) {
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        linearLayout.orientation = LinearLayout.VERTICAL
        addView(linearLayout)

        ibookerEd = EditText(context)
        ibookerEd!!.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        ibookerEd!!.gravity = Gravity.TOP or Gravity.START
        ibookerEd!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        ibookerEd!!.setSingleLine(false)
        ibookerEd!!.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        ibookerEd!!.setPadding(IbookerEditorUtil.dpToPx(context, 8F), IbookerEditorUtil.dpToPx(context, 8F), IbookerEditorUtil.dpToPx(context, 8F), IbookerEditorUtil.dpToPx(context, 8F))
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

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

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
            }
        })
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
     * 设置编辑控件背景颜色
     *
     * @param color 背景颜色
     */
    fun setIbookerBackgroundColor(@ColorInt color: Int): IbookerEditorEditView {
        this.setBackgroundColor(color)
        return this
    }

}// 三种构造方法
