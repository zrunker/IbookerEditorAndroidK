package cc.ibooker.ibookereditorklib

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * 书客编辑器界面 - ViewPager
 * Created by 邹峰立 on 2018/1/17.
 */
class IbookerEditorVpView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {
    // 编辑控件
    var editView: IbookerEditorEditView? = null
        private set
    // 预览控件
    var preView: IbookerEditorPreView? = null
        private set
    private var mDatas: ArrayList<View>? = null
    private var adapter: IbookerEditorVpAdapter? = null

    init {
        init(context)
    }

    // 初始化
    private fun init(context: Context) {
        editView = IbookerEditorEditView(context)
        preView = IbookerEditorPreView(context)

        if (mDatas == null)
            mDatas = ArrayList()
        mDatas!!.add(editView!!)
        mDatas!!.add(preView!!)

        setAdapter()
        // 设置缓存
        offscreenPageLimit = mDatas!!.size
    }

    /**
     * 自定义setAdapter
     */
    private fun setAdapter() {
        if (adapter == null) {
            adapter = IbookerEditorVpAdapter(mDatas!!)
            this.setAdapter(adapter)
        } else {
            adapter!!.reflashData(this.mDatas!!)
        }
    }
}
