package cc.ibooker.ibookereditorklib

/**
 * Emjio相关数据
 */
class EmjioData {
    var text: String? = null
    var emjioStr: String? = null

    constructor() : super() {}

    constructor(text: String, emjioStr: String) {
        this.text = text
        this.emjioStr = emjioStr
    }

    override fun toString(): String {
        return "EmjioData{" +
                "text='" + text + '\''.toString() +
                ", emjioStr='" + emjioStr + '\''.toString() +
                '}'.toString()
    }
}
