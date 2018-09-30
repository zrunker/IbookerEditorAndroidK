package cc.ibooker.ibookereditorklib

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.PopupWindow

import java.util.ArrayList

/**
 * 更多PopupWindow
 */
class IbookerEditorMorePopuwindow(private val context: Context, attrs: AttributeSet?, defStyleAttr: Int, list: ArrayList<MoreBean>) : PopupWindow(context, attrs, defStyleAttr) {
    private var listView: ListView? = null
    private var adapter: MoreLvAdapter? = null

    private var onMoreLvItemClickListener: OnMoreLvItemClickListener? = null

    constructor(context: Context, list: ArrayList<MoreBean>) : this(context, null, list) {}

    constructor(context: Context, attrs: AttributeSet?, list: ArrayList<MoreBean>) : this(context, attrs, 0, list) {}

    init {
        init(list)
    }

    private fun init(list: ArrayList<MoreBean>) {
        val rootView = LayoutInflater.from(context).inflate(R.layout.ibooker_editor_layout_more, null)
        listView = rootView.findViewById(R.id.listView)
        listView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (ClickUtil.isFastClick) return@OnItemClickListener
            dismiss()
            if (onMoreLvItemClickListener != null)
                onMoreLvItemClickListener!!.onItemClick(parent, view, position, id)
            else {
                if (position == 0) {// 帮助
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    val content_url = Uri.parse("http://ibooker.cc/article/1/detail")
                    intent.data = content_url
                    context.startActivity(intent)
                } else if (position == 1) {// 关于
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    val content_url = Uri.parse("http://ibooker.cc/article/182/detail")
                    intent.data = content_url
                    context.startActivity(intent)
                }
            }
        }
        setMoreLvAdapter(list)

        contentView = rootView
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        width = IbookerEditorUtil.dpToPx(context, 150f)
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(BitmapDrawable())
    }

    fun setMoreLvAdapter(list: ArrayList<MoreBean>) {
        if (adapter == null) {
            adapter = MoreLvAdapter(context, list)
            listView!!.adapter = adapter
        } else {
            adapter!!.reflashData(list)
        }
    }

    interface OnMoreLvItemClickListener {
        fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long)
    }

    fun setOnMoreLvItemClickListener(onMoreLvItemClickListener: OnMoreLvItemClickListener) {
        this.onMoreLvItemClickListener = onMoreLvItemClickListener
    }
}
