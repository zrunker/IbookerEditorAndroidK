package cc.ibooker.ibookereditorklib

/**
 * 点击事件处理
 */
object ClickUtil {
    private var lastClickTime: Long = 0

    // 判断是否为快速点击事件
    val isFastClick: Boolean
        @Synchronized get() {
            val time = System.currentTimeMillis()
            if (time - lastClickTime < 500)
                return true
            lastClickTime = time
            return false
        }
}