package cc.ibooker.ibookereditorklib

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import java.util.ArrayList

/**
 * 更多PopupWindow列表适配器
 */
class MoreLvAdapter(context: Context, private var mDatas: ArrayList<MoreBean>?) : BaseAdapter() {
    private val inflater: LayoutInflater

    init {
        this.inflater = LayoutInflater.from(context)
    }

    fun reflashData(list: ArrayList<MoreBean>) {
        this.mDatas = list
        this.notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mDatas!!.size
    }

    override fun getItem(position: Int): Any {
        return mDatas!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            convertView = inflater.inflate(R.layout.ibooker_editor_layout_more_item, parent, false)
            holder.imageView = convertView!!.findViewById(R.id.imageView)
            holder.textView = convertView.findViewById(R.id.textView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val moreBean = mDatas!![position]
        holder.imageView!!.setImageResource(moreBean.res)
        holder.textView!!.text = moreBean.name
        return convertView
    }

    private class ViewHolder {
        internal var imageView: ImageView? = null
        internal var textView: TextView? = null
    }
}
