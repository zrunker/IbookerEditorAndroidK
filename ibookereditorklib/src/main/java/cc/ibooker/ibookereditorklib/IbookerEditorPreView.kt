package cc.ibooker.ibookereditorklib

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.*

/**
 * 书客编辑器 - 预览界面
 * Created by 邹峰立 on 2018/2/11.
 */
class IbookerEditorPreView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : WebView(context, attrs, defStyleAttr) {

    init {

        init()
    }

    // 初始化
    @SuppressLint("SetJavaScriptEnabled")
    private fun init() {
        val webSettings = this.settings
        // 允许JS
        webSettings.javaScriptEnabled = true
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
        }
        // 加载本地HTML
        this.loadUrl("file:///android_asset/ibooker_editor_index.html")
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
    }
}
