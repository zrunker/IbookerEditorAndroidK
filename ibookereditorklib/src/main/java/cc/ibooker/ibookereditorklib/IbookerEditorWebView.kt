package cc.ibooker.ibookereditorklib

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.webkit.*
import java.util.*

/**
 * 书客编辑器 - 预览界面
 * Created by 邹峰立 on 2018/2/11.
 */
class IbookerEditorWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : WebView(context, attrs, defStyleAttr) {
    /**
     * 是否完成本地文件加载
     */
    var isLoadFinished = false // 本地文件是否加载完成
    private var isExecuteCompile = false// 是否执行预览
    private var isExecuteHtmlCompile = false// 是否执行HTML预览
    private var ibookerEditorText: String? = null
    private var ibookerEditorHtml: String? = null

    private var imgPathList: ArrayList<String>? = null// WebView所有图片地址
    private var ibookerEditorJsCheckImgEvent: IbookerEditorJsCheckImgEvent? = null

    private var webSettings: WebSettings? = null
    private var currentFontSize: Int = 0

    fun getCurrentFontSize(): Int {
        return currentFontSize
    }

    init {
        isVerticalScrollBarEnabled = false
        setVerticalScrollbarOverlay(false)
        isHorizontalScrollBarEnabled = false
        setHorizontalScrollbarOverlay(false)
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }

    // 初始化
    @SuppressLint("AddJavascriptInterface", "SetJavaScriptEnabled", "JavascriptInterface")
    private fun init() {
        webSettings = this.settings
        // 支持内容重新布局
        webSettings!!.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        // 允许JS
        webSettings!!.javaScriptEnabled = true
        // 支持插件
        webSettings!!.pluginState = WebSettings.PluginState.ON
        // 设置允许JS弹窗
        webSettings!!.javaScriptCanOpenWindowsAutomatically = true
        // access Assets and resources
        webSettings!!.allowFileAccess = true
        webSettings!!.setAppCacheEnabled(false)
        // 提高渲染优先级
        webSettings!!.setRenderPriority(WebSettings.RenderPriority.HIGH)
        // 设置编码格式
        webSettings!!.defaultTextEncodingName = "utf-8"
        // 支持自动加载图片
        webSettings!!.loadsImagesAutomatically = true
        // 关闭webview中缓存
        webSettings!!.cacheMode = WebSettings.LOAD_NO_CACHE
        // 将图片调整到适合webview的大小
        webSettings!!.useWideViewPort = true
        // 缩放至屏幕的大小
        webSettings!!.loadWithOverviewMode = true
        // 支持缩放，默认为true。
        webSettings!!.setSupportZoom(true)
        // 设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings!!.builtInZoomControls = true
        // 隐藏原生的缩放控件
        webSettings!!.displayZoomControls = false

        // 获取当前字体大小
        if (webSettings!!.textSize == WebSettings.TextSize.SMALLEST) {
            currentFontSize = 1
        } else if (webSettings!!.textSize == WebSettings.TextSize.SMALLER) {
            currentFontSize = 2
        } else if (webSettings!!.textSize == WebSettings.TextSize.NORMAL) {
            currentFontSize = 3
        } else if (webSettings!!.textSize == WebSettings.TextSize.LARGER) {
            currentFontSize = 4
        } else if (webSettings!!.textSize == WebSettings.TextSize.LARGEST) {
            currentFontSize = 5
        }

        // 隐藏滚动条
        this.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        // 使页面获取焦点，防止点击无响应
        requestFocus()
        // 设置WebViewClient
        this.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return if (ibookerEditorWebViewUrlLoadingListener != null)
                    ibookerEditorWebViewUrlLoadingListener!!.shouldOverrideUrlLoading(view, url)
                else {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(url)
                    context.startActivity(intent)
                    true
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return if (ibookerEditorWebViewUrlLoadingListener != null)
                    ibookerEditorWebViewUrlLoadingListener!!.shouldOverrideUrlLoading(view, request)
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.loadUrl(request.url.toString())
                    }
                    true
                }
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                if (ibookerEditorWebViewUrlLoadingListener != null)
                    ibookerEditorWebViewUrlLoadingListener!!.onReceivedError(view, request, error)
                else
                // 当网页加载出错时，加载本地错误文件
                    this@IbookerEditorWebView.loadUrl("file:///android_asset/error.html")
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                if (error.primaryError == SslError.SSL_DATE_INVALID
                        || error.primaryError == SslError.SSL_EXPIRED
                        || error.primaryError == SslError.SSL_INVALID
                        || error.primaryError == SslError.SSL_UNTRUSTED) {
                    handler.proceed()
                } else {
                    handler.cancel()
                }
                if (ibookerEditorWebViewUrlLoadingListener != null)
                    ibookerEditorWebViewUrlLoadingListener!!.onReceivedSslError(view, handler, error)
                else
                // 当网页加载出错时，加载本地错误文件
                    this@IbookerEditorWebView.loadUrl("file:///android_asset/error.html")
            }

//            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
//                if (ibookerEditorWebViewUrlLoadingListener != null)
//                    ibookerEditorWebViewUrlLoadingListener!!.onPageStarted(view, url, favicon)
//            }

            override fun onPageFinished(view: WebView, url: String) {
                isLoadFinished = true
                when {
                    isExecuteCompile -> ibookerCompile(ibookerEditorText)
                    isExecuteHtmlCompile -> ibookerHtmlCompile(ibookerEditorHtml)
                    else -> addWebViewListener()
                }

                if (ibookerEditorWebViewUrlLoadingListener != null)
                    ibookerEditorWebViewUrlLoadingListener!!.onPageFinished(view, url)
            }
        }
        // 添加js
        ibookerEditorJsCheckImgEvent = IbookerEditorJsCheckImgEvent()
        addJavascriptInterface(ibookerEditorJsCheckImgEvent, "ibookerEditorJsCheckImgEvent")
        // 加载本地HTML
        loadUrl("file:///android_asset/ibooker_editor_index.html")
    }

    // 给WebView添加相关监听
    private fun addWebViewListener() {
        // 获取WebView中全部图片地址
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.evaluateJavascript("javascript:getImgAllPaths()") { value ->
                var value1 = value
                // value即为所有图片地址
                if (!TextUtils.isEmpty(value1)) {
                    value1 = value1.replace("\"", "").replace("\"", "")
                    if (!TextUtils.isEmpty(value1)) {
                        if (imgPathList == null)
                            imgPathList = ArrayList()
                        imgPathList!!.clear()
                        val imgPaths = value1.split(";ibookerEditor;".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
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
     * 执行预览
     *
     * @param ibookerEditorText 待预览内容 非HTML
     */
    fun ibookerCompile(ibookerEditorText: String?) {
        var ibookerEditorText1 = ibookerEditorText
        if (isLoadFinished) {
            ibookerEditorText1 = ibookerEditorText1!!.replace("\\n".toRegex(), "\\\\n")
            val js = "javascript:ibookerCompile('$ibookerEditorText1')"
            this.loadUrl(js)

            // 重新WebView添加监听
            addWebViewListener()

            this.isExecuteCompile = false
            this.ibookerEditorText = null
            this.isExecuteHtmlCompile = false
            this.ibookerEditorHtml = null
        } else {
            this.isExecuteCompile = true
            this.ibookerEditorText = ibookerEditorText1
        }

    }

    /**
     * 执行Html预览
     *
     * @param ibookerEditorHtml 待预览内容 HTML
     */
    fun ibookerHtmlCompile(ibookerEditorHtml: String?) {
        if (isLoadFinished) {
            val js = "javascript:ibookerHtmlCompile('$ibookerEditorHtml')"
            this.loadUrl(js)

            // 重新WebView添加监听
            addWebViewListener()

            this.isExecuteHtmlCompile = false
            this.ibookerEditorHtml = null
            this.isExecuteCompile = false
            this.ibookerEditorText = null
        } else {
            this.isExecuteHtmlCompile = true
            this.ibookerEditorHtml = ibookerEditorHtml
        }
    }

    /**
     * 获取整个WebView截图
     */
    fun getWebViewBitmap(): Bitmap {
        val picture = this.capturePicture()
        val bitmap = Bitmap.createBitmap(picture.width, picture.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        picture.draw(canvas)
        return bitmap
    }

    /**
     * 设置当前字体大小
     */
    fun setIbookerEditorWebViewFontSize(fontSize: Int) {
        if (fontSize in 1..5) {
            currentFontSize = fontSize
            when (fontSize) {
                1 -> webSettings!!.textSize = WebSettings.TextSize.SMALLEST
                2 -> webSettings!!.textSize = WebSettings.TextSize.SMALLER
                3 -> webSettings!!.textSize = WebSettings.TextSize.NORMAL
                4 -> webSettings!!.textSize = WebSettings.TextSize.LARGER
                5 -> webSettings!!.textSize = WebSettings.TextSize.LARGEST
            }
        }
    }

    // 图片预览接口
    interface IbookerEditorImgPreviewListener {
        fun onIbookerEditorImgPreview(currentPath: String, position: Int, imgAllPathList: ArrayList<String>)
    }

    fun setIbookerEditorImgPreviewListener(ibookerEditorImgPreviewListener: IbookerEditorImgPreviewListener) {
        ibookerEditorJsCheckImgEvent!!.setmIbookerEditorImgPreviewListener(ibookerEditorImgPreviewListener)
    }

    // Url加载状态监听
    interface IbookerEditorWebViewUrlLoadingListener {
        fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean

        fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean

        fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError)

        fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError)

        fun onPageStarted(view: WebView, url: String, favicon: Bitmap)

        fun onPageFinished(view: WebView, url: String)
    }

    private var ibookerEditorWebViewUrlLoadingListener: IbookerEditorWebViewUrlLoadingListener? = null

    fun setIbookerEditorWebViewUrlLoadingListener(ibookerEditorWebViewUrlLoadingListener: IbookerEditorWebViewUrlLoadingListener) {
        this.ibookerEditorWebViewUrlLoadingListener = ibookerEditorWebViewUrlLoadingListener
    }
}
