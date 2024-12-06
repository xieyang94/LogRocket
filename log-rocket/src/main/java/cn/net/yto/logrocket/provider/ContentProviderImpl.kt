package cn.net.yto.logrocket.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri

/**
 * @Author xiey
 * @Date 2024/4/12-14:21
 * @Email 02724892@yto.net.cn
 * @Description
 */
abstract class ContentProviderImpl : ContentProvider() {

    abstract fun doWork(context: Context)

    override fun onCreate(): Boolean {
        if (context != null && context?.applicationContext != null) {
            context?.applicationContext?.let { doWork(it) }
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        return null
    }

    override fun getType(uri: Uri): String? {

        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {

        return -1
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {

        return -1
    }
}