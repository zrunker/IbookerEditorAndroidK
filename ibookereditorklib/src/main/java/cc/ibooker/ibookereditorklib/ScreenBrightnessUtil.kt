package cc.ibooker.ibookereditorklib

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi

/**
 * 屏幕亮度管理类
 */
object ScreenBrightnessUtil {

    /**
     * 判断是否为自动亮度
     */
    fun isAutoBrightness(context: Context): Boolean {
        var isAutoBrightness = false
        try {
            val contentResolver = context.applicationContext.contentResolver
            isAutoBrightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }

        return isAutoBrightness
    }

    /**
     * 开启自动亮度
     */
    @Synchronized
    fun startAutoBrightness(context: Context) {
        Settings.System.putInt(context.applicationContext.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
    }

    /**
     * 停止自动亮度
     */
    @Synchronized
    fun stopAutoBrightness(context: Context) {
        Settings.System.putInt(context.applicationContext.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
    }

    /**
     * 设置屏幕亮度
     *
     * @param light 亮度值 1-255
     */
    @Synchronized
    fun saveBrightness(context: Context, light: Int) {
        if (light < 1 || light > 255) return
        val resolver = context.applicationContext.contentResolver
        val uri = Settings.System.getUriFor("screen_brightness")
        Settings.System.putInt(resolver, "screen_brightness", light)
        resolver.notifyChange(uri, null)

    }

    /**
     * 检测权限
     */
    @Synchronized
    fun checkPermission(context: Context, isJump: Boolean): Boolean {
        var isNeedPermission = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context.applicationContext)) {
            isNeedPermission = true
            if (isJump)
                enterSettingIntent(context)
        }
        return isNeedPermission
    }

    /**
     * 进入设置界面
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun enterSettingIntent(context: Context) {
        val selfPackageUri = Uri.parse("package:" + context.applicationContext.packageName)
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, selfPackageUri)
        context.startActivity(intent)
    }
}
