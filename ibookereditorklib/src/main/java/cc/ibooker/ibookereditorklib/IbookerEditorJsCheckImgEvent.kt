package cc.ibooker.ibookereditorklib

import android.webkit.JavascriptInterface
import java.util.*

/**
 * 书客编辑器 - 查看图片js事件
 * Created by 邹峰立 on 2018/3/13.
 */
class IbookerEditorJsCheckImgEvent {
    private var mImgPathList: ArrayList<String>? = null
    private var mIbookerEditorImgPreviewListener: IbookerEditorWebView.IbookerEditorImgPreviewListener? = null

    fun getmImgPathList(): ArrayList<String>? {
        return mImgPathList
    }

    fun setmImgPathList(mImgPathList: ArrayList<String>) {
        this.mImgPathList = mImgPathList
    }

    fun getmIbookerEditorImgPreviewListener(): IbookerEditorWebView.IbookerEditorImgPreviewListener? {
        return mIbookerEditorImgPreviewListener
    }

    fun setmIbookerEditorImgPreviewListener(mIbookerEditorImgPreviewListener: IbookerEditorWebView.IbookerEditorImgPreviewListener) {
        this.mIbookerEditorImgPreviewListener = mIbookerEditorImgPreviewListener
    }

    @JavascriptInterface
    fun onCheckImg(src: String) {
        // 执行相应的逻辑操作-查看图片
        if (mIbookerEditorImgPreviewListener != null) {
            var position = 0
            if (mImgPathList != null) {
                for (i in mImgPathList!!.indices) {
                    val imgPath = mImgPathList!![i]
                    if (imgPath == src) {
                        position = i
                        break
                    }
                }
            }
            mIbookerEditorImgPreviewListener!!.onIbookerEditorImgPreview(src, position, this.mImgPathList!!)
        }
    }
}
