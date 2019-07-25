package cc.ibooker.ibookereditorklib

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.*

/**
 * Emjio适配器
 */
class EmjioGwAdapter(context: Context, private var mDatas: ArrayList<EmjioData>?) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    fun reflushData(list: ArrayList<EmjioData>) {
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
            convertView = inflater.inflate(R.layout.ibooker_editor_layout_emjio_item, parent, false)
            holder = ViewHolder()
            holder.textView = convertView!!.findViewById(R.id.textView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val emjioData = mDatas!![position]
        holder.textView!!.text = emjioData.text
        return convertView
    }

    private class ViewHolder {
        internal var textView: TextView? = null
    }
}
