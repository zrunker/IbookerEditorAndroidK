package cc.ibooker.ibookereditorklib

/**
 * 字符串的管理类
 */
class IbookerEditorStrUtil {
    private var count = 0

    /**
     * 判断content中包含text的个数
     *
     * @param content 内容字符串
     * @param text    待判断字符串
     */
    fun countStr(content: String, text: String): Int {
        if (content.contains(text)) {
            count++
            countStr(content.substring(content.indexOf(text) + text.length), text)
            return count
        }
        return 0
    }

}
