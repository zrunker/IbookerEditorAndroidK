package cc.ibooker.ibookereditorklib

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView

/**
 * 自定义可以缩放的ImageView
 *
 * @author 邹峰立
 */
class IbookerEditorScaleImageView
/**
 * 三种构造方法
 */
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : AppCompatImageView(context, attrs, defStyle), OnGlobalLayoutListener, OnScaleGestureListener, OnTouchListener {
    private var mOnce = false // 是否为第一次加载
    /**
     * 初始化时缩放值，也是最小缩放值
     */
    private var mInitScale: Float = 0.toFloat()
    /**
     * 双击时，放大达到的值
     */
    private var mMidScale: Float = 0.toFloat()
    /**
     * 放大的最大值
     */
    private var mMaxScale: Float = 0.toFloat()

    private val mScaleMatrix: Matrix? // 缩放或平移图片
    private val mScaleGestureDetector: ScaleGestureDetector // 捕获多指触碰比例
    // 自由移动
    /**
     * 记录上一次多点触碰的数量
     */
    private var mLastPointerCount: Int = 0
    private var mLastX: Float = 0.toFloat()
    private var mLastY: Float = 0.toFloat()
    private var isCanDrag: Boolean = false // 判断是否移动
    private var isCheckLeftAndRight: Boolean = false
    private var isCheckTopAndBottom: Boolean = false
    // 双击放大和缩小
    private val mGestureDetector: GestureDetector
    private var isAutoScale: Boolean = false
    // 是否限制大小
    private var isLimitSize: Boolean = false

    /**
     * 获取当前缩放值
     */
    val scale: Float
        get() {
            val values = FloatArray(9)
            mScaleMatrix!!.getValues(values)
            return values[Matrix.MSCALE_X]
        }

    /**
     * 获得图片放大或缩小以后的宽和高，以及l，t,r,b
     */
    private val matrixRectF: RectF
        get() {
            val matrix = mScaleMatrix
            val rectF = RectF()

            val d = drawable
            if (d != null) {
                rectF.set(0f, 0f, d.intrinsicWidth.toFloat(), d.intrinsicHeight.toFloat())
                matrix!!.mapRect(rectF)
            }
            return rectF
        }

    /**
     * ImageView单击事件，对外接口
     */
    private var onMyClickListener: OnMyClickListener? = null

    /**
     * ImageView长按事件监听
     */
    private var onMyLongClickListener: OnMyLongClickListener? = null

    init {
        if (attrs != null) {
            // 获取自定义属性，并设置
            val ta = getContext().obtainStyledAttributes(attrs, R.styleable.IbookerEditorScaleImageView)
            isLimitSize = ta.getBoolean(R.styleable.IbookerEditorScaleImageView_isLimitSize, false)
            ta.recycle()
        }
        // 初始化
        mScaleMatrix = Matrix()
        scaleType = ImageView.ScaleType.MATRIX
        mScaleGestureDetector = ScaleGestureDetector(context, this)
        setOnTouchListener(this)
        mGestureDetector = GestureDetector(context,
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onLongPress(e: MotionEvent) {// 长按事件监听
                        super.onLongPress(e)
                        if (onMyLongClickListener != null)
                            onMyLongClickListener!!.onMyLongClick(this@IbookerEditorScaleImageView)
                    }

                    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {// 单击事件监听
                        if (onMyClickListener != null)
                            onMyClickListener!!.onMyClick(this@IbookerEditorScaleImageView)
                        return true
                    }

                    override fun onDoubleTap(e: MotionEvent): Boolean {// 双击事件监听
                        if (isAutoScale) {
                            return true
                        }

                        val x = e.x
                        val y = e.y

                        if (scale < mMidScale) {
                            // mScaleMatrix.postScale(mMidScale / getScale(),
                            // mMidScale / getScale(), x, y);
                            // setImageMatrix(mScaleMatrix);
                            postDelayed(AutoScaleRunnable(mMidScale, x, y), 16)
                            isAutoScale = true
                        } else {
                            // mScaleMatrix.postScale(mInitScale / getScale(),
                            // mInitScale / getScale(), x, y);
                            // setImageMatrix(mScaleMatrix);
                            postDelayed(AutoScaleRunnable(mInitScale, x, y), 16)
                            isAutoScale = true
                        }
                        return true
                    }
                })
    }

    /**
     * 自动放大与缩小
     *
     * @author 邹峰立
     */
    private inner class AutoScaleRunnable internal constructor(private val mTargetScale: Float // 缩放的目标值
                                                               ,
                                                               /**
                                                                * 缩放的中心点
                                                                */
                                                               private val x: Float, private val y: Float) : Runnable {
        // 梯度值
        private val BIGGER = 1.04f
        private val SMALL = 0.93f
        private var tempScale: Float = 0.toFloat()

        init {

            if (scale < mTargetScale) {
                tempScale = BIGGER
            }
            if (scale > mTargetScale) {
                tempScale = SMALL
            }
        }

        override fun run() {
            // 进行过缩放
            mScaleMatrix!!.postScale(tempScale, tempScale, x, y)
            checkBorderAndCenterWhenScale()
            imageMatrix = mScaleMatrix

            val currentScale = scale
            if (tempScale > 1.0f && currentScale < mTargetScale || tempScale < 1.0f && currentScale > mTargetScale) {
                postDelayed(this, 16)
            } else { // 设置成目标值
                val scale = mTargetScale / currentScale
                mScaleMatrix.postScale(scale, scale, x, y)
                checkBorderAndCenterWhenScale()
                imageMatrix = mScaleMatrix
                isAutoScale = false
            }
        }

    }

    /**
     * 初始化图片，获取imageView加载完成之后的图片，OnGlobalLayoutListener
     */
    override fun onGlobalLayout() {
        // 只有当全局布局完成之后进行调用
        if (!mOnce) {
            // 得到控件的宽和高
            val width = width.toFloat()
            val height = height.toFloat()

            // 得到我们的图片，以及图片的宽和高
            val d = drawable ?: return
            val dw = d.intrinsicWidth.toFloat()
            val dh = d.intrinsicHeight.toFloat()

            // 对比图片的高度和宽度和控件的宽度和高度
            var scale = 1.0f // 缩放比例
            if (dw > width && dh < height) { // 图片宽度大于控件的宽度，高度小于控件的高度，缩小
                scale = width * 1.0f / dw
            }
            if (dh > height && dw < width) { // 图片高度大于控件的宽度，宽度小于控件的高度，缩小
                scale = height * 1.0f / dh
            }
            //            if ((dw > width && dh > height) || (dw < width && dh < height)) {
            //                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            //            }
            if (isLimitSize) {
                if (dw > width && dh > height) {
                    scale = Math.min(width * 1.0f / dw, height * 1.0f / dh)
                }
            } else if (dw > width && dh > height || dw < width && dh < height) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh)
            }

            /**
             * 得到一些縮放比例
             */
            mInitScale = scale
            mMidScale = 2.0f * scale
            mMaxScale = 4.0f * scale
            /**
             * 将图片移动到当前控件中心位置
             */
            val dx = getWidth() / 2 - dw / 2 // x轴上偏移量
            val dy = getHeight() / 2 - dh / 2 // y轴上偏移量
            // 之后进行缩放和移动
            if (mScaleMatrix != null) {
                mScaleMatrix.postTranslate(dx, dy) // 平移
                mScaleMatrix.postScale(mInitScale, mInitScale, (getWidth() / 2).toFloat(), (getHeight() / 2).toFloat()) // 缩放
                imageMatrix = mScaleMatrix
            }

            mOnce = true
        }
    }

    override fun onAttachedToWindow() {
        // 当View从window上显示的时候调用
        super.onAttachedToWindow()
        // 注册onGlobalLayout
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onDetachedFromWindow() {
        // 当View从window上消费的时候调用
        super.onDetachedFromWindow()
        // 消费onGlobalLayout
        viewTreeObserver.removeGlobalOnLayoutListener(this)
    }

    /**
     * 下面三个方法实现多点触碰 缩放区间 mInitScale maxScale
     */
    override fun onScale(detector: ScaleGestureDetector): Boolean {
        // 缩放进行中
        val scale = scale
        var scaleFactor = detector.scaleFactor // 想要缩放的比例，<1.0f代表想要缩小，>1.0f代表想要放大

        if (drawable == null)
            return true
        // 缩放范围控制
        if (scale < mMaxScale && scaleFactor > 1.0f || scale > mInitScale && scaleFactor < 1.0f) {
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale
            }
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale
            }
        }
        // 实现缩放
        mScaleMatrix!!.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)

        checkBorderAndCenterWhenScale()

        imageMatrix = mScaleMatrix
        return true
    }

    /**
     * 在缩放的时候进行边界控制和位置控制
     */
    private fun checkBorderAndCenterWhenScale() {
        val rectF = matrixRectF
        // X和Y的偏移量，水平和竖直
        var deltaX = 0f
        var deltaY = 0f

        val width = width
        val height = height
        // 缩放时，进行边界检测，防止出现白边
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                deltaX = -rectF.left
            }
            if (rectF.right < width) {
                deltaX = width - rectF.right
            }
        }
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top
            }
            if (rectF.bottom < height) {
                deltaY = height - rectF.bottom
            }
        }
        // 如果图片的宽度和高度小于控件的宽和高，居中
        if (rectF.width() < width) {
            deltaX = width / 2f - rectF.right + rectF.width() / 2f
        }
        if (rectF.height() < height) {
            deltaY = height / 2f - rectF.bottom + rectF.height() / 2f
        }
        mScaleMatrix!!.postTranslate(deltaX, deltaY)

    }

    override fun onScaleBegin(arg0: ScaleGestureDetector): Boolean {
        // 缩放开始，一定要return true
        return true
    }

    override fun onScaleEnd(arg0: ScaleGestureDetector) {
        // 缩放结束

    }

    /**
     * 触摸事件监听
     */
    override fun onTouch(arg0: View, event: MotionEvent): Boolean {
        if (mGestureDetector.onTouchEvent(event)) {
            return true
        }
        mScaleGestureDetector.onTouchEvent(event) // 将event事件交给mScaleGestureDetector处理
        /**
         * 实现图片移动
         */
        // 获取中心点位置
        var x = 0f
        var y = 0f
        // 多触点数量
        val pointerCount = event.pointerCount
        for (i in 0 until pointerCount) {
            x += event.getX(i)
            y += event.getY(i)
        }
        x /= pointerCount.toFloat()
        y /= pointerCount.toFloat()

        if (mLastPointerCount != pointerCount) {
            // 只有一个触点时才能移动
            isCanDrag = pointerCount == 1
            mLastX = x
            mLastY = y
        }
        mLastPointerCount = pointerCount
        val rectF = matrixRectF // 获取rectF
        when (event.action) {
            MotionEvent.ACTION_DOWN// 按下
            ->
                // 处理按下事件
                if (rectF.height() > height + 0.01 || rectF.width() > width + 0.01) {
                    if (parent is ViewPager)
                        parent.requestDisallowInterceptTouchEvent(true) // 取消父控件拦截
                }
            MotionEvent.ACTION_MOVE -> {
                if (rectF.height() > height + 0.01 || rectF.width() > width + 0.01) {
                    if (parent is ViewPager)
                        parent.requestDisallowInterceptTouchEvent(true) // 取消父控件拦截
                }
                if (isCanDrag) {
                    // 如果宽度小于控件宽度，不允许横向移动，如果高度小于控件高度，不允许竖向移动
                    if (drawable != null) {
                        // 记录移动距离
                        var dx = x - mLastX
                        var dy = y - mLastY

                        isCheckTopAndBottom = true
                        isCheckLeftAndRight = isCheckTopAndBottom
                        if (rectF.width() < width) {
                            dx = 0f
                            isCheckLeftAndRight = false
                        }
                        if (rectF.height() < height) {
                            dy = 0f
                            isCheckTopAndBottom = false
                        }
                        mScaleMatrix!!.postTranslate(dx, dy)
                        checkBorderWhenTranslate()
                        imageMatrix = mScaleMatrix
                    }
                }
                mLastX = x
                mLastY = y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mLastPointerCount = 0
            else -> {
            }
        }
        return true
    }

    /**
     * 当移动时，进行边界检查
     */
    private fun checkBorderWhenTranslate() {
        val rectF = matrixRectF
        var deltaX = 0f
        var deltaY = 0f
        val width = width.toFloat()
        val height = height.toFloat()

        if (rectF.top > 0 && isCheckTopAndBottom) {
            deltaY = -rectF.top
        }
        if (rectF.bottom < height && isCheckTopAndBottom) {
            deltaY = height - rectF.bottom
        }
        if (rectF.left > 0 && isCheckLeftAndRight) {
            deltaX = -rectF.left
        }
        if (rectF.right < width && isCheckLeftAndRight) {
            deltaX = width - rectF.right
        }

        mScaleMatrix!!.postTranslate(deltaX, deltaY)
    }

    interface OnMyClickListener {
        fun onMyClick(v: View)
    }

    fun setOnMyClickListener(onMyClickListener: OnMyClickListener) {
        this.onMyClickListener = onMyClickListener
    }

    interface OnMyLongClickListener {
        fun onMyLongClick(v: View)
    }

    fun setOnMyLongClickListener(onMyLongClickListener: OnMyLongClickListener) {
        this.onMyLongClickListener = onMyLongClickListener
    }
}
