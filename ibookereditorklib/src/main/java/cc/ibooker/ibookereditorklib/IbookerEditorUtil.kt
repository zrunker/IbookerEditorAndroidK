package cc.ibooker.ibookereditorklib

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.widget.EditText
import java.util.regex.Pattern

/**
 * 书客编辑器 - 工具类
 * Created by 邹峰立 on 2018/2/12.
 */
class IbookerEditorUtil// 构造方法
(ibookerEditorEditView: IbookerEditorEditView) {

    // 操作的EditText
    private val ibookerEd: EditText = ibookerEditorEditView.ibookerEd!!

    /**
     * 获取EditText光标相关信息
     */
    /**
     * 设置EditText光标相关信息
     */
    private var selectionInfo: RangeData
        get() {
            val start = ibookerEd.selectionStart
            val end = ibookerEd.selectionEnd
            val text = if (start == end) "" else ibookerEd.text.toString().substring(start, end)
            return RangeData(start, end, text)
        }
        set(data) = ibookerEd.setSelection(if (data.start >= 0) data.start else 0, if (data.end >= 0) data.end else 0)

    // 内部类 - 保存光标相关信息
    private inner class RangeData(var start: Int, var end: Int, text: String) {
        var text: String? = text

        override fun toString(): String {
            return "RangeData{" +
                    "start=" + start +
                    ", end=" + end +
                    ", text='" + text + '\'' +
                    '}'
        }
    }


    /**
     * 在尾部添加text
     */
    internal fun addEnd(addStr: String) {
        try {
            val rangeData = selectionInfo
            val text = ibookerEd.text.toString()
            val start = rangeData.start
            val end = rangeData.end
            val finalTxt = text.substring(0, start) + addStr + text.substring(end, text.length)
            ibookerEd.setText(finalTxt)
            // 设置光标位置
            rangeData.end = end + finalTxt.length - text.length
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 加粗
     */
    internal fun bold() {
        // 初始化
        val finalTxt: String
        val rangeData = selectionInfo
        var selectTxt = rangeData.text
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        // 赋值
        val pattern = Pattern.compile("^[*]+.*[*]+$")
        if (pattern.matcher(selectTxt!!).matches()) {
            // 如果已加粗，去掉
            selectTxt = selectTxt.replace("^[*]+([^*]*)[*]+$".toRegex(), "$1")
            finalTxt = text.substring(0, start) + selectTxt + text.substring(end, text.length)
        } else {
            finalTxt = text.substring(0, start) + "**" + (if (TextUtils.isEmpty(selectTxt)) "加粗" else text.substring(start, end)) + "**" + text.substring(end, text.length)
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = end + finalTxt.length - text.length
        selectionInfo = rangeData
    }

    /**
     * 斜体
     */
    internal fun italic() {
        // 初始化
        val finalTxt: String
        val rangeData = selectionInfo
        var selectTxt = rangeData.text
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        // 赋值
        val pattern = Pattern.compile("^[*]+.*[*]+$")
        if (pattern.matcher(selectTxt!!).matches()) {
            // 如果已加粗，去掉
            selectTxt = selectTxt.replace("^[*]+([^*]*)[*]+$".toRegex(), "$1")
            finalTxt = text.substring(0, start) + selectTxt + text.substring(end, text.length)
        } else {
            finalTxt = text.substring(0, start) + "*" + (if (TextUtils.isEmpty(selectTxt)) "斜体" else text.substring(start, end)) + "*" + text.substring(end, text.length)
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = end + finalTxt.length - text.length
        selectionInfo = rangeData
    }

    /**
     * 删除线
     */
    internal fun strikeout() {
        // 初始化
        val finalTxt: String
        val rangeData = selectionInfo
        var selectTxt = rangeData.text
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        // 赋值
        val pattern = Pattern.compile("^[~]+.*[~]+$")
        if (pattern.matcher(selectTxt!!).matches()) {
            // 如果已删除线，去掉
            selectTxt = selectTxt.replace("^[~]+([^~]*)[~]+$".toRegex(), "$1")
            finalTxt = text.substring(0, start) + selectTxt + text.substring(end, text.length)
        } else {
            finalTxt = text.substring(0, start) + "~~" + (if (TextUtils.isEmpty(selectTxt)) "删除线" else text.substring(start, end)) + "~~" + text.substring(end, text.length)
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = end + finalTxt.length - text.length
        selectionInfo = rangeData
    }

    /**
     * 下划线
     */
    internal fun underline() {
        // 初始化
        val finalTxt: String
        val rangeData = selectionInfo
        var selectTxt = rangeData.text
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        // 赋值
        val pattern = Pattern.compile("^[_]+.*[_]+$")
        if (pattern.matcher(selectTxt!!).matches()) {
            // 如果已下划线，去掉
            selectTxt = selectTxt.replace("^[_]+([^_]*)[_]+$".toRegex(), "$1")
            finalTxt = text.substring(0, start) + selectTxt + text.substring(end, text.length)
        } else {
            finalTxt = text.substring(0, start) + "__" + (if (TextUtils.isEmpty(selectTxt)) "下划线" else text.substring(start, end)) + "__" + text.substring(end, text.length)
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = end + finalTxt.length - text.length
        selectionInfo = rangeData
    }

    /**
     * 单词首字母大写
     */
    @SuppressLint("SetTextI18n")
    internal fun capitals() {
        // 初始化
        val rangeData = selectionInfo
        val selectTxt = rangeData.text
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()

        // 赋值
        //        String finalTxt = text;
        //        if (selectTxt.length() == 1)
        //            finalTxt = text.substring(0, start) + selectTxt.substring(0, 1).toUpperCase() + text.substring(end, text.length());
        //        else if (selectTxt.length() > 1)
        //            finalTxt = text.substring(0, start) + selectTxt.substring(0, 1).toUpperCase() + selectTxt.substring(1) + text.substring(end, text.length());

        // 赋值 - 无效
        //      String finalTxt = text.substring(0, start) + selectTxt.toLowerCase().replaceAll("\\b[a-z]/g", "$1".toUpperCase()) + text.substring(end, text.length());

        // 赋值
        val finalTxt = StringBuffer()
        val p = Pattern.compile("\\b\\w")
        val m = p.matcher(selectTxt!!.toLowerCase())
        while (m.find()) {
            m.appendReplacement(finalTxt, m.group().toUpperCase())
        }
        m.appendTail(finalTxt)
        ibookerEd.setText(text.substring(0, start) + finalTxt + text.substring(end, text.length))
        // 设置光标位置
        selectionInfo = rangeData
    }

    /**
     * 字母转大写
     */
    internal fun uppercase() {
        // 初始化
        val finalTxt: String
        val rangeData = selectionInfo
        val selectTxt = rangeData.text
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        // 赋值
        finalTxt = text.substring(0, start) + selectTxt!!.toUpperCase() + text.substring(end, text.length)
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        selectionInfo = rangeData
    }

    /**
     * 字母转小写
     */
    internal fun lowercase() {
        // 初始化
        val finalTxt: String
        val rangeData = selectionInfo
        val selectTxt = rangeData.text
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        // 赋值
        finalTxt = text.substring(0, start) + selectTxt!!.toLowerCase() + text.substring(end, text.length)
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        selectionInfo = rangeData
    }

    /**
     * 一级标题
     */
    internal fun h1() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val text = ibookerEd.text.toString()
            val temp = text.substring(0, start)
            var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
            val texts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (line >= texts.size) line = texts.size - 1
            if (line < 0) line = 0
            var thisline = if (texts.isEmpty()) "" else texts[line]
            val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            // 赋值
            val pattern = Pattern.compile("^#\\s.*$")
            thisline = if (pattern.matcher(thisline).matches()) {
                thisline.replace("^#\\s(.*)$".toRegex(), "$1")
            } else {
                "# $thisline"
            }
            val finalTxt = StringBuilder()
            if (allLine.isNotEmpty()) {
                if (line < allLine.size)
                    allLine[line] = thisline
                for (str in allLine) {
                    finalTxt.append(str).append("\n")
                }
            } else
                finalTxt.append(thisline).append("\n")
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = end + finalTxt.length - text.length - 1
            rangeData.end = if (position > 0 && position > thisline.length) position else thisline.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 二级标题
     */
    internal fun h2() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val text = ibookerEd.text.toString()
            val temp = text.substring(0, start)
            var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
            val texts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (line >= texts.size) line = texts.size - 1
            if (line < 0) line = 0
            var thisline = if (texts.isEmpty()) "" else texts[line]
            val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            // 赋值
            val pattern = Pattern.compile("^##\\s.*$")
            thisline = if (pattern.matcher(thisline).matches()) {
                thisline.replace("^##\\s(.*)$".toRegex(), "$1")
            } else {
                "## $thisline"
            }
            val finalTxt = StringBuilder()
            if (allLine.isNotEmpty()) {
                if (line < allLine.size)
                    allLine[line] = thisline
                for (str in allLine) {
                    finalTxt.append(str).append("\n")
                }
            } else
                finalTxt.append(thisline).append("\n")
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = end + finalTxt.length - text.length - 1
            rangeData.end = if (position > 0 && position > thisline.length) position else thisline.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 三级标题
     */
    internal fun h3() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val text = ibookerEd.text.toString()
            val temp = text.substring(0, start)
            var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
            val texts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (line >= texts.size) line = texts.size - 1
            if (line < 0) line = 0
            var thisline = if (texts.isEmpty()) "" else texts[line]
            val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            // 赋值
            val pattern = Pattern.compile("^###\\s.*$")
            thisline = if (pattern.matcher(thisline).matches()) {
                thisline.replace("^###\\s(.*)$".toRegex(), "$1")
            } else {
                "### $thisline"
            }
            val finalTxt = StringBuilder()
            if (allLine.isNotEmpty()) {
                if (line < allLine.size)
                    allLine[line] = thisline
                for (str in allLine) {
                    finalTxt.append(str).append("\n")
                }
            } else
                finalTxt.append(thisline).append("\n")
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = end + finalTxt.length - text.length - 1
            rangeData.end = if (position > 0 && position > thisline.length) position else thisline.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 四级标题
     */
    internal fun h4() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val text = ibookerEd.text.toString()
            val temp = text.substring(0, start)
            var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
            val texts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (line >= texts.size) line = texts.size - 1
            if (line < 0) line = 0
            var thisline = if (texts.isEmpty()) "" else texts[line]
            val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            // 赋值
            val pattern = Pattern.compile("^####\\s.*$")
            thisline = if (pattern.matcher(thisline).matches()) {
                thisline.replace("^####\\s(.*)$".toRegex(), "$1")
            } else {
                "#### $thisline"
            }
            val finalTxt = StringBuilder()
            if (allLine.isNotEmpty()) {
                if (line < allLine.size)
                    allLine[line] = thisline
                for (str in allLine) {
                    finalTxt.append(str).append("\n")
                }
            } else
                finalTxt.append(thisline).append("\n")
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = end + finalTxt.length - text.length - 1
            rangeData.end = if (position > 0 && position > thisline.length) position else thisline.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 五级标题
     */
    internal fun h5() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val text = ibookerEd.text.toString()
            val temp = text.substring(0, start)
            var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
            val texts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (line >= texts.size) line = texts.size - 1
            if (line < 0) line = 0
            var thisline = if (texts.isEmpty()) "" else texts[line]
            val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            // 赋值
            val pattern = Pattern.compile("^#####\\s.*$")
            thisline = if (pattern.matcher(thisline).matches()) {
                thisline.replace("^#####\\s(.*)$".toRegex(), "$1")
            } else {
                "##### $thisline"
            }
            val finalTxt = StringBuilder()
            if (allLine.isNotEmpty()) {
                if (line < allLine.size)
                    allLine[line] = thisline
                for (str in allLine) {
                    finalTxt.append(str).append("\n")
                }
            } else
                finalTxt.append(thisline).append("\n")
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = end + finalTxt.length - text.length - 1
            rangeData.end = if (position > 0 && position > thisline.length) position else thisline.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 六级标题
     */
    internal fun h6() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val text = ibookerEd.text.toString()
            val temp = text.substring(0, start)
            var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
            val texts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (line >= texts.size) line = texts.size - 1
            if (line < 0) line = 0
            var thisline = if (texts.isEmpty()) "" else texts[line]
            val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            // 赋值
            val pattern = Pattern.compile("^######\\s.*$")
            thisline = if (pattern.matcher(thisline).matches()) {
                thisline.replace("^######\\s(.*)$".toRegex(), "$1")
            } else {
                "###### $thisline"
            }
            val finalTxt = StringBuilder()
            if (allLine.isNotEmpty()) {
                if (line < allLine.size)
                    allLine[line] = thisline
                for (str in allLine) {
                    finalTxt.append(str).append("\n")
                }
            } else
                finalTxt.append(thisline).append("\n")
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = end + finalTxt.length - text.length - 1
            rangeData.end = if (position > 0 && position > thisline.length) position else thisline.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 超链接
     */
    internal fun link(link: String) {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val text = ibookerEd.text.toString()
            val temp = text.substring(0, start)
            var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
            val texts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (line >= texts.size) line = texts.size - 1
            if (line < 0) line = 0
            var thisline = texts[line]
            val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            thisline += "\n" + link
            allLine[line] = thisline
            val finalTxt = StringBuilder()
            for (str in allLine) {
                finalTxt.append(str).append("\n")
            }
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = finalTxt.indexOf(thisline) + thisline.length + 1
            rangeData.end = if (position >= finalTxt.length) finalTxt.length else position
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 引用
     */
    internal fun quote() {
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        if (line < 0) line = 0
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val pattern = Pattern.compile("^>.*$")
        thisline = if (pattern.matcher(thisline).matches()) {
            thisline.replace("^>(.*)$".toRegex(), "$1")
        } else {
            ">$thisline"
        }
        allLine[line] = thisline
        var finalTxt = ""
        for (str in allLine) {
            finalTxt = finalTxt + str + "\n"
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = end + finalTxt.length - text.length
        rangeData.start = rangeData.end
        selectionInfo = rangeData
    }

    /**
     * 代码
     */
    internal fun code() {
        val finalTxt: String
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val selectTxt = rangeData.text

        val pattern = Pattern.compile("^`{3}[\\s\\S]*`{3}$")
        finalTxt = if (pattern.matcher(selectTxt!!).matches()) {
            text.substring(0, start) + selectTxt.replace("^`{3}[\\n]([\\s\\S]*)[\\n]`{3}$".toRegex(), "$1") + text.substring(end, text.length)
        } else {
            text.substring(0, start) + "\n```\n" + selectTxt + "\n```\n" + text.substring(end, text.length)
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = end + finalTxt.length - text.length - 5
        rangeData.start = rangeData.end
        selectionInfo = rangeData
    }

    /**
     * 图片
     */
    internal fun imgu(imgPath: String) {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val text = ibookerEd.text.toString()
            val temp = text.substring(0, start)
            var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
            val texts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (line >= texts.size) line = texts.size - 1
            if (line < 0) line = 0
            var thisline = texts[line]
            val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            thisline += "\n" + imgPath
            allLine[line] = thisline
            val finalTxt = StringBuilder()
            for (str in allLine) {
                finalTxt.append(str).append("\n")
            }
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = finalTxt.indexOf(thisline) + thisline.length + 1
            rangeData.end = if (position >= finalTxt.length) finalTxt.length else position
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 数字列表
     */
    internal fun ol() {
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        if (line < 0) line = 0
        val thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var j = 1
        val pattern = Pattern.compile("^\\d+\\.\\s([^\\s]*)$")
        for (i in 0..line) {
            if (i == line && pattern.matcher(allLine[i]).matches()) {
                allLine[i] = allLine[i].replace("^\\d+\\.\\s([^\\s]*)$".toRegex(), "$1")
                continue
            }
            if (pattern.matcher(allLine[i].trim { it <= ' ' }).matches()) {
                allLine[i] = allLine[i].replace("^\\d+\\.\\s([^\\s]*)$".toRegex(), j++.toString() + ". " + "$1")
                continue
            }
            if (i == line) {
                allLine[i] = j++.toString() + ". " + thisline
                continue
            }
            if (i - 1 >= 0 && TextUtils.isEmpty(allLine[i - 1]) && !TextUtils.isEmpty(allLine[i]) && !pattern.matcher(allLine[i]).matches()) {
                j = 1
            }
        }
        var finalTxt = ""
        for (str in allLine) {
            finalTxt = finalTxt + str + "\n"
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = end + finalTxt.length - text.length
        rangeData.start = rangeData.end
        selectionInfo = rangeData
    }

    /**
     * 普通列表
     */
    internal fun ul() {
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        if (line < 0) line = 0
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val pattern = Pattern.compile("^-\\s.*$")
        thisline = if (pattern.matcher(thisline).matches()) {
            thisline.replace("^-\\s(.*)$".toRegex(), "$1")
        } else {
            "- $thisline"
        }
        allLine[line] = thisline
        var finalTxt = ""
        for (str in allLine) {
            finalTxt = finalTxt + str + "\n"
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = end + finalTxt.length - text.length
        rangeData.start = rangeData.end
        selectionInfo = rangeData
    }

    /**
     * 复选框未选中
     */
    internal fun tasklistsUnChecked() {
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        if (line < 0) line = 0
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val pattern = Pattern.compile("^-\\s+\\[\\s?]\\s+.*$")
        thisline = if (pattern.matcher(thisline).matches()) {
            thisline.replace("^-\\s+\\[\\s?]\\s+(.*)$".toRegex(), "$1")
        } else {
            "- [ ] $thisline"
        }
        allLine[line] = thisline
        var finalTxt = ""
        for (str in allLine) {
            finalTxt = finalTxt + str + "\n"
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = end + finalTxt.length - text.length
        rangeData.start = rangeData.end
        selectionInfo = rangeData
    }

    /**
     * 复选框选中
     */
    internal fun tasklistsChecked() {
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        if (line < 0) line = 0
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val pattern = Pattern.compile("^-\\s+\\[x]\\s+.*$")
        thisline = if (pattern.matcher(thisline).matches()) thisline.replace("^-\\s+\\[x]\\s+(.*)$".toRegex(), "$1") else {
            "- [x] $thisline"
        }
        allLine[line] = thisline
        var finalTxt = ""
        for (str in allLine) {
            finalTxt = finalTxt + str + "\n"
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = end + finalTxt.length - text.length
        rangeData.start = rangeData.end
        selectionInfo = rangeData
    }

    /**
     * 表格
     */
    internal fun tables() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val text = ibookerEd.text.toString()
            val temp = text.substring(0, start)
            var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
            val texts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (line >= texts.size) line = texts.size - 1
            if (line < 0) line = 0
            var thisline = texts[line]
            val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            thisline += ("\n|  h1   |    h2   |    h3   |"
                    + "\n|:------|:-------:|--------:|"
                    + "\n| 100   | [a][1]  | ![b][2] |"
                    + "\n| *foo* | **bar** | ~~baz~~ |")
            allLine[line] = thisline
            val finalTxt = StringBuilder()
            for (str in allLine) {
                finalTxt.append(str).append("\n")
            }
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = finalTxt.indexOf(thisline) + thisline.length + 1
            rangeData.end = if (position >= finalTxt.length) finalTxt.length else position
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * HTML
     */
    internal fun html() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val text = ibookerEd.text.toString()
            val temp = text.substring(0, start)
            var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
            val texts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (line >= texts.size) line = texts.size - 1
            if (line < 0) line = 0
            var thisline = texts[line]
            val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            thisline += "\n<html>\n<!--在这里插入内容-->\n</html>"
            allLine[line] = thisline
            val finalTxt = StringBuilder()
            for (str in allLine) {
                finalTxt.append(str).append("\n")
            }
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = finalTxt.indexOf(thisline) + thisline.length - 8
            rangeData.end = if (position >= finalTxt.length) finalTxt.length else position
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 分割线
     */
    internal fun hr() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val text = ibookerEd.text.toString()
            val temp = text.substring(0, start)
            var line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
            val texts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (line >= texts.size) line = texts.size - 1
            if (line < 0) line = 0
            var thisline = texts[line]
            val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            thisline = "$thisline\n***"
            allLine[line] = thisline
            val finalTxt = StringBuilder()
            for (str in allLine) {
                finalTxt.append(str).append("\n")
            }
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = finalTxt.indexOf(thisline) + thisline.length + 1
            rangeData.end = if (position >= finalTxt.length) finalTxt.length else position
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 获取EditText光标所在的位置
     */
    private fun getEditTextCursorIndex(mEditText: EditText): Int {
        return mEditText.selectionStart
    }

    /**
     * 向EditText指定光标位置插入字符串
     */
    private fun insertText(mEditText: EditText, mText: String) {
        mEditText.text.insert(getEditTextCursorIndex(mEditText), mText)
    }

    /**
     * 向EditText指定光标位置删除字符串
     */
    private fun deleteText(mEditText: EditText) {
        if (!TextUtils.isEmpty(mEditText.text.toString())) {
            mEditText.text.delete(getEditTextCursorIndex(mEditText) - 1, getEditTextCursorIndex(mEditText))
        }
    }

    companion object {
        /**
         * dp转换成px
         *
         * @param value 待转换值
         */
        internal fun dpToPx(context: Context, value: Float): Int {
            val dm = context.resources.displayMetrics
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm).toInt()
        }
    }
}
