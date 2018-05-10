package cc.ibooker.ibookereditorklib

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.util.AttributeSet
import android.view.ViewGroup
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
    var ibookerEditorUtil: IbookerEditorUtil? = null

    init {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        setBackgroundColor(Color.parseColor("#FFFFFF"))

        init(context)
    }

    // 初始化
    private fun init(context: Context) {
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
        changeVpUpdateIbookerEditorTopView(0)
        addView(ibookerEditorVpView)
        // 底部工具栏
        ibookerEditorToolView = IbookerEditorToolView(context)
        ibookerEditorToolView!!.onToolClickListener = this
        addView(ibookerEditorToolView)
        // 底部工具栏 - 管理类
        ibookerEditorUtil = IbookerEditorUtil(ibookerEditorVpView!!.editView!!)
    }

    // 设置ViewPager变化
    private fun changeVpUpdateIbookerEditorTopView(position: Int) {
        if (ibookerEditorTopView != null)
            if (position == 0) {
                ibookerEditorTopView!!.editIBtn?.setBackgroundResource(R.drawable.icon_ibooker_editor_edit_orange)
                ibookerEditorTopView!!.previewIBtn?.setBackgroundResource(R.drawable.icon_ibooker_editor_preview_gray)
                openInputSoft(true)
            } else if (position == 1) {
                ibookerEditorTopView!!.editIBtn?.setBackgroundResource(R.drawable.icon_ibooker_editor_edit_gray)
                ibookerEditorTopView!!.previewIBtn?.setBackgroundResource(R.drawable.icon_ibooker_editor_preview_orange)
                openInputSoft(false)
                // 执行预览
                htmlCompile()
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
        if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IMG_BACK) {// 返回
            (context as Activity).finish()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UNDO) {// 撤销
            ibookerEditorVpView!!.editView!!.undo()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_REDO) {// 重做
            ibookerEditorVpView!!.editView!!.redo()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_EDIT) {// 编辑
            ibookerEditorVpView!!.currentItem = 0
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_PREVIEW) {// 预览
            ibookerEditorVpView!!.currentItem = 1
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_HELP) {// 帮助
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val content_url = Uri.parse("http://ibooker.cc/article/1/detail")
            intent.data = content_url
            context.startActivity(intent)
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_ABOUT) {// 关于
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val content_url = Uri.parse("http://ibooker.cc/article/182/detail")
            intent.data = content_url
            context.startActivity(intent)
        }
    }

    // 工具栏按钮点击事件监听
    override fun onToolClick(tag: Any) {
        if (ibookerEditorUtil == null)
            ibookerEditorUtil = IbookerEditorUtil(ibookerEditorVpView!!.editView!!)
        if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_BOLD) {// 加粗
            ibookerEditorUtil!!.bold()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_ITALIC) {// 斜体
            ibookerEditorUtil!!.italic()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_STRIKEOUT) {// 删除线
            ibookerEditorUtil!!.strikeout()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UNDERLINE) {// 下划线
            ibookerEditorUtil!!.underline()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_CAPITALS) {// 单词首字母大写
            ibookerEditorUtil!!.capitals()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UPPERCASE) {// 字母转大写
            ibookerEditorUtil!!.uppercase()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_LOWERCASE) {// 字母转小写
            ibookerEditorUtil!!.lowercase()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H1) {// 一级标题
            ibookerEditorUtil!!.h1()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H2) {// 二级标题
            ibookerEditorUtil!!.h2()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H3) {// 三级标题
            ibookerEditorUtil!!.h3()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H4) {// 四级标题
            ibookerEditorUtil!!.h4()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H5) {// 五级标题
            ibookerEditorUtil!!.h5()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_H6) {// 六级标题
            ibookerEditorUtil!!.h6()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_LINK) {// 超链接
            ibookerEditorUtil!!.link()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_QUOTE) {// 引用
            ibookerEditorUtil!!.quote()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_CODE) {// 代码
            ibookerEditorUtil!!.code()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_IMG_U) {// 图片
            ibookerEditorUtil!!.imgu()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_OL) {// 数字列表
            ibookerEditorUtil!!.ol()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UL) {// 普通列表
            ibookerEditorUtil!!.ul()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_UNSELECTED) {// 复选框未选中
            ibookerEditorUtil!!.tasklistsUnChecked()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_SELECTED) {// 复选框选中
            ibookerEditorUtil!!.tasklistsChecked()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_TABLE) {// 表格
            ibookerEditorUtil!!.tables()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_HTML) {// HTML
            ibookerEditorUtil!!.html()
        } else if (tag == IbookerEditorEnum.TOOLVIEW_TAG.IBTN_HR) {// 分割线
            ibookerEditorUtil!!.hr()
        }
    }

    // 执行HTML预览
    private fun htmlCompile() {
        // 获取待转义内容
        val text = ibookerEditorVpView!!.editView?.ibookerEd?.text.toString()
        // 执行预览
        if (!TextUtils.isEmpty(text)) {
            ibookerEditorVpView!!.preView?.ibookerCompile(text)
        }
    }
}// 构造方法
