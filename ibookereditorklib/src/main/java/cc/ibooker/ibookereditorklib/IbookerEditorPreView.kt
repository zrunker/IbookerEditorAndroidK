package cc.ibooker.ibookereditorklib

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.webkit.*
import java.util.*

/**
 * 书客编辑器 - 预览界面
 * Created by 邹峰立 on 2018/2/11.
 */
class IbookerEditorPreView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : WebView(context, attrs, defStyleAttr) {
    private var imgPathList: ArrayList<String>? = null// WebView所有图片地址
    private var ibookerEditorJsCheckImgEvent: IbookerEditorJsCheckImgEvent? = null

    init {

        init()
    }

    // 初始化
    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface")
    private fun init() {
        val webSettings = this.settings
        // 允许JS
        webSettings.javaScriptEnabled = true
        // 支持插件
        webSettings.pluginState = WebSettings.PluginState.ON;
        // 设置允许JS弹窗
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        // access Assets and resources
        webSettings.allowFileAccess = true
        webSettings.setAppCacheEnabled(false)
        // 提高渲染优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        // 设置编码格式
        webSettings.defaultTextEncodingName = "utf-8"
        // 支持自动加载图片
        webSettings.loadsImagesAutomatically = true
        // 关闭webview中缓存
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        // 将图片调整到适合webview的大小
        webSettings.useWideViewPort = true
        // 缩放至屏幕的大小
        webSettings.loadWithOverviewMode = true
        // 支持缩放，默认为true。
        webSettings.setSupportZoom(true)
        // 设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.builtInZoomControls = true
        // 隐藏原生的缩放控件
        webSettings.displayZoomControls = false


        // 隐藏滚动条
        this.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        // 使页面获取焦点，防止点击无响应
        this.requestFocus()
        // 设置WebViewClient
        this.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                // 当网页加载出错时，加载本地错误文件
                this@IbookerEditorPreView.loadUrl("file:///android_asset/error.html")
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                addWebViewListener()
            }
        }
        // 添加js
        ibookerEditorJsCheckImgEvent = IbookerEditorJsCheckImgEvent()
        this.addJavascriptInterface(ibookerEditorJsCheckImgEvent, "ibookerEditorJsCheckImgEvent")
        // 加载本地HTML
        this.loadUrl("file:///android_asset/ibooker_editor_index.html")
    }

    // 给WebView添加相关监听
    private fun addWebViewListener() {
        // 获取WebView中全部图片地址
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.evaluateJavascript("javascript:getImgAllPaths()") { value ->
                var value = value
                // value即为所有图片地址
                if (!TextUtils.isEmpty(value)) {
                    value = value.replace("\"", "").replace("\"", "")
                    if (!TextUtils.isEmpty(value)) {
                        if (imgPathList == null)
                            imgPathList = ArrayList()
                        imgPathList!!.clear()
                        val imgPaths = value.split(";ibookerEditor;".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (imgPath in imgPaths) {
                            if (!TextUtils.isEmpty(imgPath))
                                imgPathList!!.add(imgPath)
                        }
                        ibookerEditorJsCheckImgEvent!!.setmImgPathList(imgPathList!!)
                    }
                }
            }
        }

        // 动态添加图片点击事件
        this.loadUrl("javascript:(function() {"
                + "  var objs = document.getElementsByTagName(\"img\"); "
                + "  for(var i = 0; i < objs.length; i++) {"
                + "     objs[i].onclick = function() {"
                + "          window.ibookerEditorJsCheckImgEvent.onCheckImg(this.src);"
                + "     }"
                + "  }"
                + "})()")
    }

    /**
     * 执行Html预览
     *
     * @param ibookerEditorText 待预览内容 非HTML
     */
    fun ibookerHtmlCompile(ibookerEditorText: String) {
        var ibookerEditorText = ibookerEditorText
        ibookerEditorText = ibookerEditorText.replace("\\n".toRegex(), "\\\\n")
        val js = "javascript:ibookerHtmlCompile('$ibookerEditorText')"
        this.loadUrl(js)

        // 重新添加Webview相关监听
        addWebViewListener()
    }

    // 图片预览接口
    interface IbookerEditorImgPreviewListener {
        fun onIbookerEditorImgPreview(currentPath: String, position: Int, imgAllPathList: ArrayList<String>)
    }

    fun setIbookerEditorImgPreviewListener(ibookerEditorImgPreviewListener: IbookerEditorImgPreviewListener) {
        ibookerEditorJsCheckImgEvent!!.setmIbookerEditorImgPreviewListener(ibookerEditorImgPreviewListener)

    }
}
