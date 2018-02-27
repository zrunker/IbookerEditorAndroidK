package cc.ibooker.ibookereditorklib

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * ViewPage适配器
 * Created by 邹峰立 on 2018/2/11.
 */
class IbookerEditorVpAdapter internal constructor(list: ArrayList<View>) : PagerAdapter() {
    private var mDatas = ArrayList<View>()

    init {
        this.mDatas = list
    }

    internal fun reflashData(list: ArrayList<View>) {
        this.mDatas = list
        this.notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mDatas.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = mDatas[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // 移除
        if (position >= 0 && position < mDatas.size)
            container.removeView(mDatas[position])
    }
}
