package cc.ibooker.ibookereditorandroidk

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import cc.ibooker.ibookereditorklib.IbookerEditorScaleImageView
import java.util.*

/**
 * 自定义图片缩放Adapter
 *
 * Created by 邹峰立 on 2018/3/14.
 */
class ImgVPagerAdapter(private var mDatas: ArrayList<IbookerEditorScaleImageView>?) : PagerAdapter() {

    // 刷新
    fun reflashData(list: ArrayList<IbookerEditorScaleImageView>) {
        this.mDatas = list
        this.notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mDatas!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val scaleImageView = mDatas!![position]
        container.addView(scaleImageView)
        return scaleImageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        container.removeView(mDatas!![position])
    }
}
