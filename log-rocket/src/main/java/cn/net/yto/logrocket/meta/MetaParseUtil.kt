package cn.net.yto.logcat.meta

import android.content.Context
import android.content.pm.PackageManager

/**
 * @Author xiey
 * @Date 2024/4/12-09:47
 * @Email 02724892@yto.net.cn
 * @Description
 */
object MetaParseUtil {

    private const val TAG_KEY = "uniquePort"

    @JvmStatic
    fun parseMeta(context: Context): Boolean {
        try {
            val info = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            val uniquePort = info.metaData.getBoolean(TAG_KEY)
            return uniquePort
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false;
    }


}