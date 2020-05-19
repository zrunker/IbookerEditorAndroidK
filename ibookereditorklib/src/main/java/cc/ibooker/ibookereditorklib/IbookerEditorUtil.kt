package cc.ibooker.ibookereditorklib

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.widget.EditText
import java.util.regex.Pattern

/**
 * 书客编辑器 - 工具类
 *
 * Created by 邹峰立 on 2018/2/12.
 */
class IbookerEditorUtil// 构造方法
internal constructor(ibookerEditorEditView: IbookerEditorEditView) {

    // 操作的EditText
    private val ibookerEd: EditText = ibookerEditorEditView.ibookerEd!!

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
        set(data) =
            ibookerEd.setSelection(if (data.start >= 0) data.start else 0, if (data.end >= 0) data.end else 0)

    // 内部类 - 保存光标相关信息
    private inner class RangeData {
        var start: Int = 0
        var end: Int = 0
        var text: String? = null

        constructor() : super()

        internal constructor(start: Int, end: Int, text: String) {
            this.start = start
            this.end = end
            this.text = text
        }

        override fun toString(): String {
            return "RangeData{" +
                    "start=" + start +
                    ", end=" + end +
                    ", text='" + text + '\''.toString() +
                    '}'.toString()
        }
    }

    /**
     * 在尾部添加text
     */
    fun addEnd(addStr: String) {
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
    fun bold() {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 斜体
     */
    fun italic() {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 删除线
     */
    fun strikeout() {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 下划线
     */
    fun underline() {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 单词首字母大写
     */
    @SuppressLint("SetTextI18n")
    fun capitals() {
        try {
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
            //        String finalTxt = text.substring(0, start) + selectTxt.toLowerCase().replaceAll("\\b[a-z]/g", "$1".toUpperCase()) + text.substring(end, text.length());

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
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 字母转大写
     */
    fun uppercase() {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 字母转小写
     */
    fun lowercase() {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 一级标题
     */
    fun h1() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val finalTxt = StringBuilder()
            val text = ibookerEd.text.toString()
            if (TextUtils.isEmpty(text)) {
                finalTxt.append("# ")
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                var thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]

                // 处理数据
                val pattern = Pattern.compile("^#\\s.*$")
                thisLine = if (pattern.matcher(thisLine).matches()) {
                    thisLine!!.replace("^#\\s(.*)$".toRegex(), "$1")
                } else {
                    "# $thisLine"
                }

                // 重新组织待显示数据
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }

            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            rangeData.end = end + finalTxt.length - text.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 二级标题
     */
    fun h2() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val finalTxt = StringBuilder()
            val text = ibookerEd.text.toString()
            if (TextUtils.isEmpty(text)) {
                finalTxt.append("## ")
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                var thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]

                // 处理数据
                val pattern = Pattern.compile("^##\\s.*$")
                thisLine = if (pattern.matcher(thisLine).matches()) {
                    thisLine!!.replace("^##\\s(.*)$".toRegex(), "$1")
                } else {
                    "## $thisLine"
                }

                // 重新组织待显示数据
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }

            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            rangeData.end = end + finalTxt.length - text.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 三级标题
     */
    fun h3() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val finalTxt = StringBuilder()
            val text = ibookerEd.text.toString()
            if (TextUtils.isEmpty(text)) {
                finalTxt.append("### ")
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                var thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]

                // 处理数据
                val pattern = Pattern.compile("^###\\s.*$")
                thisLine = if (pattern.matcher(thisLine).matches()) {
                    thisLine!!.replace("^###\\s(.*)$".toRegex(), "$1")
                } else {
                    "### $thisLine"
                }

                // 重新组织待显示数据
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }

            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            rangeData.end = end + finalTxt.length - text.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 四级标题
     */
    fun h4() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val finalTxt = StringBuilder()
            val text = ibookerEd.text.toString()
            if (TextUtils.isEmpty(text)) {
                finalTxt.append("#### ")
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                var thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]

                // 处理数据
                val pattern = Pattern.compile("^####\\s.*$")
                thisLine = if (pattern.matcher(thisLine).matches()) {
                    thisLine!!.replace("^####\\s(.*)$".toRegex(), "$1")
                } else {
                    "#### $thisLine"
                }

                // 重新组织待显示数据
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }

            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            rangeData.end = end + finalTxt.length - text.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 五级标题
     */
    fun h5() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val finalTxt = StringBuilder()
            val text = ibookerEd.text.toString()
            if (TextUtils.isEmpty(text)) {
                finalTxt.append("##### ")
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                var thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]

                // 处理数据
                val pattern = Pattern.compile("^#####\\s.*$")
                thisLine = if (pattern.matcher(thisLine).matches()) {
                    thisLine!!.replace("^#####\\s(.*)$".toRegex(), "$1")
                } else {
                    "##### $thisLine"
                }

                // 重新组织待显示数据
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }

            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            rangeData.end = end + finalTxt.length - text.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 六级标题
     */
    fun h6() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val finalTxt = StringBuilder()
            val text = ibookerEd.text.toString()
            if (TextUtils.isEmpty(text)) {
                finalTxt.append("###### ")
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                var thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]

                // 处理数据
                val pattern = Pattern.compile("^######\\s.*$")
                thisLine = if (pattern.matcher(thisLine).matches()) {
                    thisLine!!.replace("^######\\s(.*)$".toRegex(), "$1")
                } else {
                    "###### $thisLine"
                }

                // 重新组织待显示数据
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }

            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            rangeData.end = end + finalTxt.length - text.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 超链接
     */
    fun link(link: String) {
        link(link, "链接描述")
    }

    /**
     * 超链接
     */
    fun link(link: String, des: String) {
        var link = link
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val text = ibookerEd.text.toString()
            val finalTxt = StringBuilder()
            if (TextUtils.isEmpty(link))
                link = "链接地址"
            val tagStr = "\n[${des}]($link)\n"
            var thisLine: String
            if (TextUtils.isEmpty(text)) {
                thisLine = tagStr
                finalTxt.append(thisLine)
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]!!

                // 重新组织待显示数据
                thisLine += tagStr
                allLine[line - 1] = thisLine
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = end + tagStr.length
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
    fun quote() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val finalTxt = StringBuilder()
            val text = ibookerEd.text.toString()
            if (TextUtils.isEmpty(text)) {
                finalTxt.append(">")
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                var thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]

                // 处理数据
                val pattern = Pattern.compile("^>.*$")
                thisLine = if (pattern.matcher(thisLine).matches()) {
                    thisLine!!.replace("^>(.*)$".toRegex(), "$1")
                } else {
                    ">$thisLine"
                }

                // 重新组织待显示数据
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }

            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            rangeData.end = end + finalTxt.length - text.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 代码
     */
    fun code() {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 图片
     */
    fun imgu(imgPath: String) {
        imgu(imgPath, "图片描述")
    }

    /**
     * 图片
     */
    fun imgu(imgPath: String, des: String) {
        var imgPath = imgPath
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val text = ibookerEd.text.toString()
            val finalTxt = StringBuilder()
            if (TextUtils.isEmpty(imgPath))
                imgPath = "图片地址"
            val tagStr = "\n![$des]($imgPath)\n"
            var thisLine: String
            if (TextUtils.isEmpty(text)) {
                thisLine = tagStr
                finalTxt.append(thisLine)
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]!!

                // 重新组织待显示数据
                thisLine += tagStr
                allLine[line - 1] = thisLine
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = end + tagStr.length
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
    fun ol() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val finalTxt = StringBuilder()
            val text = ibookerEd.text.toString()
            if (TextUtils.isEmpty(text)) {
                finalTxt.append("1. ")
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                val thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]

                // 处理数据
                val newLine = line - 1
                var j = 1
                val pattern = Pattern.compile("^\\d+\\.\\s([^\\s]*)$")
                for (i in 0..newLine) {
                    if (i == newLine && pattern.matcher(allLine[i]).matches()) {
                        allLine[i] = allLine[i]!!.replace("^\\d+\\.\\s([^\\s]*)$".toRegex(), "$1")
                        continue
                    }
                    if (pattern.matcher(allLine[i]).matches()) {
                        allLine[i] = allLine[i]?.replace("^\\d+\\.\\s([^\\s]*)$".toRegex(), j++.toString() + ". " + "$1")
                        continue
                    }
                    if (i == newLine) {
                        allLine[i] = j++.toString() + ". " + thisLine
                        continue
                    }
                    if (i - 1 >= 0 && TextUtils.isEmpty(allLine[i - 1]) && !TextUtils.isEmpty(allLine[i]) && !pattern.matcher(allLine[i]).matches()) {
                        j = 1
                    }
                }

                // 重新组织待显示数据
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }

            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            rangeData.end = end + finalTxt.length - text.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 普通列表
     */
    fun ul() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val finalTxt = StringBuilder()
            val text = ibookerEd.text.toString()
            if (TextUtils.isEmpty(text)) {
                finalTxt.append("- ")
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                var thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]

                // 处理数据
                val pattern = Pattern.compile("^-\\s.*$")
                thisLine = if (pattern.matcher(thisLine).matches()) {
                    thisLine!!.replace("^-\\s(.*)$".toRegex(), "$1")
                } else {
                    "- $thisLine"
                }

                // 重新组织待显示数据
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }

            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            rangeData.end = end + finalTxt.length - text.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 复选框未选中
     */
    fun tasklistsUnChecked() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val finalTxt = StringBuilder()
            val text = ibookerEd.text.toString()
            if (TextUtils.isEmpty(text)) {
                finalTxt.append("- [ ] ")
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                var thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]

                // 处理数据
                val pattern = Pattern.compile("^-\\s+\\[\\s?]\\s+.*$")
                thisLine = if (pattern.matcher(thisLine).matches()) {
                    thisLine!!.replace("^-\\s+\\[\\s?]\\s+(.*)$".toRegex(), "$1")
                } else {
                    "- [ ] $thisLine"
                }

                // 重新组织待显示数据
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }

            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            rangeData.end = end + finalTxt.length - text.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 复选框选中
     */
    fun tasklistsChecked() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val finalTxt = StringBuilder()
            val text = ibookerEd.text.toString()
            if (TextUtils.isEmpty(text)) {
                finalTxt.append("- [x] ")
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                var thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]

                // 处理数据
                val pattern = Pattern.compile("^-\\s+\\[x]\\s+.*$")
                thisLine = if (pattern.matcher(thisLine).matches()) {
                    thisLine!!.replace("^-\\s+\\[x]\\s+(.*)$".toRegex(), "$1")
                } else {
                    "- [x] $thisLine"
                }

                // 重新组织待显示数据
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }

            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            rangeData.end = end + finalTxt.length - text.length
            rangeData.start = rangeData.end
            selectionInfo = rangeData
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 表格
     */
    fun tables() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val text = ibookerEd.text.toString()
            val finalTxt = StringBuilder()
            val tagStr = ("\n|  h1   |    h2   |    h3   |"
                    + "\n|:------|:-------:|--------:|"
                    + "\n| 100   | [a][1]  | ![b][2] |"
                    + "\n| *foo* | **bar** | ~~baz~~ |\n")
            var thisLine: String
            if (TextUtils.isEmpty(text)) {
                thisLine = tagStr
                finalTxt.append(thisLine)
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]!!

                // 重新组织待显示数据
                thisLine += tagStr
                allLine[line - 1] = thisLine
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = end + tagStr.length
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
    fun html() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val text = ibookerEd.text.toString()
            val finalTxt = StringBuilder()
            val tagStr = "\n<html>\n<!--在这里插入内容-->\n</html>\n"
            var thisLine: String
            if (TextUtils.isEmpty(text)) {
                thisLine = tagStr
                finalTxt.append(thisLine)
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]!!

                // 重新组织待显示数据
                thisLine += tagStr
                allLine[line - 1] = thisLine
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = end + tagStr.length - 9
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
    fun hr() {
        try {
            val rangeData = selectionInfo
            val start = rangeData.start
            val end = rangeData.end
            val text = ibookerEd.text.toString()
            val finalTxt = StringBuilder()
            val tagStr = "\n***\n"
            var thisLine: String
            if (TextUtils.isEmpty(text)) {
                thisLine = tagStr
                finalTxt.append(thisLine)
            } else {
                // 标记每行内容
                val allLineCount = IbookerEditorStrUtil().countStr(text, "\n") + 1
                val allLine = arrayOfNulls<String>(allLineCount)
                val allLineTemp = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0 until allLineCount) {
                    if (i < allLineTemp.size)
                        allLine[i] = allLineTemp[i]
                    else
                        allLine[i] = ""
                }

                // 计算当前游标的精准行数-即换行符的个数+1
                val temp = text.substring(0, start)
                var line = IbookerEditorStrUtil().countStr(temp, "\n") + 1

                // 排除异常情况
                if (line > allLineCount) line = allLineCount
                if (line < 1) line = 1

                // 计算当前行的数据
                thisLine = if (allLine.isEmpty()) "" else allLine[line - 1]!!

                // 重新组织待显示数据
                thisLine += tagStr
                allLine[line - 1] = thisLine
                if (allLine.isEmpty())
                    finalTxt.append(thisLine)
                else {
                    allLine[line - 1] = thisLine
                    for (i in allLine.indices) {
                        val str = allLine[i]
                        finalTxt.append(str)
                        if (i != allLine.size - 1) {
                            finalTxt.append("\n")
                        }
                    }
                }
            }
            ibookerEd.setText(finalTxt.toString())
            // 设置光标位置
            val position = end + tagStr.length
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
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        fun dpToPx(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        fun pxToDp(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }
    }
}
