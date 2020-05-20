package cc.ibooker.ibookereditorklib

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils

/**
 * Uri管理类
 *
 * @author 邹峰立
 */
object UriUtil {

    /**
     * 通过Uri获取文件地址
     *
     * @param context 上下文对象
     * @param uri     待处理的uri
     * @return 文件地址
     */
    fun getFilePathByUri(context: Context, uri: Uri?): String? {
        var filePath: String? = null
        if (uri != null) {
            if ("content".equals(uri.scheme!!, ignoreCase = true)) {
                // 简单查询
                filePath = queryFilePath(context, uri, null, null)
                // 复杂查询
                if (TextUtils.isEmpty(filePath)) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null
                        when (type) {
                            "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                        val selection = MediaStore.Images.Media._ID + "=?"
                        val selectionArgs = arrayOf(split[1])

                                // 重新查找

                        // 重新查找
                        filePath = queryFilePath(context, contentUri, selection, selectionArgs)
                    }
                }
            } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                filePath = uri.path
            }
        }
        return filePath
    }

    /**
     * 根据Uri查询文件地址
     */
    private fun queryFilePath(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var filePath: String? = null
        val cursor = context.contentResolver.query(uri!!, null, selection, selectionArgs, null)
        if (cursor != null) {
            if (cursor.moveToFirst())
                try {
                    filePath = cursor.getString(cursor.getColumnIndex("_data"))
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    cursor.close()
                }
        }
        return filePath
    }
}
