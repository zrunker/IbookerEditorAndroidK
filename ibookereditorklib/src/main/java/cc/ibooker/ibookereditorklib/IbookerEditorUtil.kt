package cc.ibooker.ibookereditorklib

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
    private val ibookerEd: EditText

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
        set(data) = ibookerEd.setSelection(data.start, data.end)

    init {
        ibookerEd = ibookerEditorEditView.ibookerEd!!
    }

    // 内部类 - 保存光标相关信息
    private inner class RangeData {
        var start: Int = 0
        var end: Int = 0
        var text: String? = null

        constructor() : super() {}

        constructor(start: Int, end: Int, text: String) {
            this.start = start
            this.end = end
            this.text = text
        }

        override fun toString(): String {
            return "RangeData{" +
                    "start=" + start +
                    ", end=" + end +
                    ", text='" + text + '\'' +
                    '}'
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
        val pattern = Pattern.compile("^[\\*]+.*[\\*]+$")
        if (pattern.matcher(selectTxt!!).matches()) {
            // 如果已加粗，去掉
            selectTxt = selectTxt.replace("^[\\*]+([^\\*]*)[\\*]+$".toRegex(), "$1")
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
        val pattern = Pattern.compile("^[\\*]+.*[\\*]+$")
        if (pattern.matcher(selectTxt!!).matches()) {
            // 如果已加粗，去掉
            selectTxt = selectTxt.replace("^[\\*]+([^\\*]*)[\\*]+$".toRegex(), "$1")
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
        val pattern = Pattern.compile("^[\\~]+.*[\\~]+$")
        if (pattern.matcher(selectTxt!!).matches()) {
            // 如果已删除线，去掉
            selectTxt = selectTxt.replace("^[\\~]+([^\\~]*)[\\~]+$".toRegex(), "$1")
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
        val pattern = Pattern.compile("^[\\_]+.*[\\_]+$")
        if (pattern.matcher(selectTxt!!).matches()) {
            // 如果已下划线，去掉
            selectTxt = selectTxt.replace("^[\\_]+([^\\_]*)[\\_]+$".toRegex(), "$1")
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
        // 初始化
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        // 赋值
        val pattern = Pattern.compile("^#\\s.*$")
        if (pattern.matcher(thisline).matches()) {
            thisline = thisline.replace("^#\\s(.*)$".toRegex(), "$1")
        } else {
            thisline = "# " + thisline
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
     * 二级标题
     */
    internal fun h2() {
        // 初始化
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        // 赋值
        val pattern = Pattern.compile("^##\\s.*$")
        if (pattern.matcher(thisline).matches()) {
            thisline = thisline.replace("^##\\s(.*)$".toRegex(), "$1")
        } else {
            thisline = "## " + thisline
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
     * 三级标题
     */
    internal fun h3() {
        // 初始化
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        // 赋值
        val pattern = Pattern.compile("^###\\s.*$")
        if (pattern.matcher(thisline).matches()) {
            thisline = thisline.replace("^###\\s(.*)$".toRegex(), "$1")
        } else {
            thisline = "### " + thisline
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
     * 四级标题
     */
    internal fun h4() {
        // 初始化
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        // 赋值
        val pattern = Pattern.compile("^####\\s.*$")
        if (pattern.matcher(thisline).matches()) {
            thisline = thisline.replace("^####\\s(.*)$".toRegex(), "$1")
        } else {
            thisline = "#### " + thisline
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
     * 五级标题
     */
    internal fun h5() {
        // 初始化
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        // 赋值
        val pattern = Pattern.compile("^#####\\s.*$")
        if (pattern.matcher(thisline).matches()) {
            thisline = thisline.replace("^#####\\s(.*)$".toRegex(), "$1")
        } else {
            thisline = "##### " + thisline
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
     * 六级标题
     */
    internal fun h6() {
        // 初始化
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        // 赋值
        val pattern = Pattern.compile("^######\\s.*$")
        if (pattern.matcher(thisline).matches()) {
            thisline = thisline.replace("^######\\s(.*)$".toRegex(), "$1")
        } else {
            thisline = "###### " + thisline
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
     * 超链接
     */
    internal fun link() {
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        thisline += "\n[链接描述](http://www.ibooker.cc)"
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
     * 引用
     */
    internal fun quote() {
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val pattern = Pattern.compile("^>.*$")
        if (pattern.matcher(thisline).matches()) {
            thisline = thisline.replace("^>(.*)$".toRegex(), "$1")
        } else {
            thisline = '>' + thisline
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
        if (pattern.matcher(selectTxt!!).matches()) {
            finalTxt = text.substring(0, start) + selectTxt.replace("^`{3}[\\n]([\\s\\S]*)[\\n]`{3}$".toRegex(), "$1") + text.substring(end, text.length)
        } else {
            finalTxt = text.substring(0, start) + "\n```\n" + selectTxt + "\n```\n" + text.substring(end, text.length)
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
    internal fun imgu() {
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        thisline += "\n![图片描述](http://ibooker.cc/favicon.ico/)"
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
     * 数字列表
     */
    internal fun ol() {
        val rangeData = selectionInfo
        val start = rangeData.start
        val end = rangeData.end
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
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
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val pattern = Pattern.compile("^-\\s.*$")
        if (pattern.matcher(thisline).matches()) {
            thisline = thisline.replace("^-\\s(.*)$".toRegex(), "$1")
        } else {
            thisline = "- " + thisline
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
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val pattern = Pattern.compile("^-\\s+\\[\\s{0,1}\\]\\s+.*$")
        if (pattern.matcher(thisline).matches()) {
            thisline = thisline.replace("^-\\s+\\[\\s{0,1}\\]\\s+(.*)$".toRegex(), "$1")
        } else {
            thisline = "- [ ] " + thisline
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
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val pattern = Pattern.compile("^-\\s+\\[x\\]\\s+.*$")
        if (pattern.matcher(thisline).matches()) {
            thisline = thisline.replace("^-\\s+\\[x\\]\\s+(.*)$".toRegex(), "$1")
        } else {
            thisline = "- [x] " + thisline
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
        val rangeData = selectionInfo
        val start = rangeData.start
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        thisline += ("\n|  h1   |    h2   |    h3   |"
                + "\n|:------|:-------:|--------:|"
                + "\n| 100   | [a][1]  | ![b][2] |"
                + "\n| *foo* | **bar** | ~~baz~~ |")
        allLine[line] = thisline
        var finalTxt = ""
        for (str in allLine) {
            finalTxt = finalTxt + str + "\n"
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = rangeData.end + finalTxt.length - text.length
        rangeData.start = rangeData.end
        selectionInfo = rangeData
    }

    /**
     * HTML
     */
    internal fun html() {
        val rangeData = selectionInfo
        val start = rangeData.start
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        thisline += "\n<html>\n<!--在这里插入内容-->\n</html>"
        allLine[line] = thisline
        var finalTxt = ""
        for (str in allLine) {
            finalTxt = finalTxt + str + "\n"
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = rangeData.end + finalTxt.length - text.length - 9
        rangeData.start = rangeData.end
        selectionInfo = rangeData
    }

    /**
     * 分割线
     */
    internal fun hr() {
        val rangeData = selectionInfo
        val start = rangeData.start
        val text = ibookerEd.text.toString()
        val temp = text.substring(0, start)
        val line = temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        var thisline = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line]
        val allLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        thisline = thisline + "\n***"
        allLine[line] = thisline
        var finalTxt = ""
        for (str in allLine) {
            finalTxt = finalTxt + str + "\n"
        }
        ibookerEd.setText(finalTxt)
        // 设置光标位置
        rangeData.end = rangeData.end + finalTxt.length - text.length
        rangeData.start = rangeData.end
        selectionInfo = rangeData
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
