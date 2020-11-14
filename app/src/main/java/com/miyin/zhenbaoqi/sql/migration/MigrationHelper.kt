package com.miyin.zhenbaoqi.sql.migration

import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import com.orhanobut.logger.Logger
import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.database.StandardDatabase
import org.greenrobot.greendao.internal.DaoConfig
import java.lang.ref.WeakReference
import java.lang.reflect.InvocationTargetException
import java.util.*

object MigrationHelper {

    private const val SQLITE_MASTER = "sqlite_master"
    private const val SQLITE_TEMP_MASTER = "sqlite_temp_master"
    private var weakListener: WeakReference<ReCreateAllTableListener>? = null

    fun migrate(db: SQLiteDatabase?, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        val database: Database = StandardDatabase(db)
        migrate(database, *daoClasses)
    }

    fun migrate(db: SQLiteDatabase?, listener: ReCreateAllTableListener, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        weakListener = WeakReference(listener)
        migrate(db, *daoClasses)
    }

    fun migrate(database: Database, listener: ReCreateAllTableListener, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        weakListener = WeakReference(listener)
        migrate(database, *daoClasses)
    }

    fun migrate(database: Database, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        generateTempTables(database, *daoClasses)
        var listener: ReCreateAllTableListener? = null
        if (weakListener != null) {
            listener = weakListener!!.get()
        }
        if (listener != null) {
            listener.onDropAllTables(database, true)
            listener.onCreateAllTables(database, false)
        } else {
            dropAllTables(database, true, *daoClasses)
            createAllTables(database, false, *daoClasses)
        }
        restoreData(database, *daoClasses)
    }

    private fun generateTempTables(db: Database, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        for (element in daoClasses) {
            var tempTableName: String? = null
            val daoConfig = DaoConfig(db, element)
            val tableName = daoConfig.tablename
            if (!isTableExists(db, false, tableName)) {
                continue
            }
            try {
                tempTableName = daoConfig.tablename + "_TEMP"
                db.execSQL("DROP TABLE IF EXISTS $tempTableName;")
                val insertTableStringBuilder = "CREATE TEMPORARY TABLE " + tempTableName +
                        " AS SELECT * FROM `" + tableName + "`;"
                db.execSQL(insertTableStringBuilder)
            } catch (e: SQLException) {
                Logger.e("【Failed to generate temp table】$tempTableName", e)
            }
        }
    }

    private fun isTableExists(db: Database?, isTemp: Boolean, tableName: String): Boolean {
        if (db == null || TextUtils.isEmpty(tableName)) {
            return false
        }
        val dbName = if (isTemp) SQLITE_TEMP_MASTER else SQLITE_MASTER
        val sql = "SELECT COUNT(*) FROM `$dbName` WHERE type = ? AND name = ?"
        var cursor: Cursor? = null
        var count = 0
        try {
            cursor = db.rawQuery(sql, arrayOf("table", tableName))
            if (cursor == null || !cursor.moveToFirst()) {
                return false
            }
            count = cursor.getInt(0)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return count > 0
    }

    private fun getColumnsStr(daoConfig: DaoConfig?): String {
        if (daoConfig == null) {
            return "no columns"
        }
        val builder = StringBuilder()
        for (i in daoConfig.allColumns.indices) {
            builder.append(daoConfig.allColumns[i])
            builder.append(",")
        }
        if (builder.isNotEmpty()) {
            builder.deleteCharAt(builder.length - 1)
        }
        return builder.toString()
    }

    @SafeVarargs
    private fun dropAllTables(db: Database, ifExists: Boolean, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        reflectMethod(db, "dropTable", ifExists, *daoClasses)
    }

    @SafeVarargs
    private fun createAllTables(db: Database, ifNotExists: Boolean, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        reflectMethod(db, "createTable", ifNotExists, *daoClasses)
    }

    @SafeVarargs
    private fun reflectMethod(db: Database, methodName: String, isExists: Boolean, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        if (daoClasses.isEmpty()) {
            return
        }
        try {
            for (cls in daoClasses) {
                val method = cls.getDeclaredMethod(methodName, Database::class.java, Boolean::class.javaPrimitiveType)
                method.invoke(null, db, isExists)
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    @SafeVarargs
    private fun restoreData(db: Database, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        for (element in daoClasses) {
            val daoConfig = DaoConfig(db, element)
            val tableName = daoConfig.tablename
            val tempTableName = daoConfig.tablename + "_TEMP"
            if (!isTableExists(db, true, tempTableName)) {
                continue
            }
            try {
                // get all columns from tempTable, take careful to use the columns list
                val newTableInfos = TableInfo.getTableInfo(db, tableName)
                val tempTableInfos = TableInfo.getTableInfo(db, tempTableName)
                val selectColumns = ArrayList<String?>(newTableInfos.size)
                val intoColumns = ArrayList<String?>(newTableInfos.size)
                for (tableInfo in tempTableInfos) {
                    if (newTableInfos.contains(tableInfo)) {
                        val column = '`'.toString() + tableInfo.name + '`'
                        intoColumns.add(column)
                        selectColumns.add(column)
                    }
                }
                // NOT NULL columns list
                for (tableInfo in newTableInfos) {
                    if (tableInfo.notnull && !tempTableInfos.contains(tableInfo)) {
                        val column = '`'.toString() + tableInfo.name + '`'
                        intoColumns.add(column)
                        val value = if (tableInfo.dfltValue != null) {
                            "'" + tableInfo.dfltValue + "' AS "
                        } else {
                            "'' AS "
                        }
                        selectColumns.add(value + column)
                    }
                }
                if (intoColumns.size != 0) {
                    val insertTableStringBuilder = "REPLACE INTO `" + tableName + "` (" +
                            TextUtils.join(",", intoColumns) +
                            ") SELECT " +
                            TextUtils.join(",", selectColumns) +
                            " FROM " + tempTableName + ";"
                    db.execSQL(insertTableStringBuilder)
                }
                db.execSQL("DROP TABLE $tempTableName")
            } catch (e: SQLException) {
                Logger.e("【Failed to restore data from temp table 】$tempTableName", e)
            }
        }
    }

    private fun getColumns(db: Database, tableName: String): List<String?> {
        var columns: List<String?>? = null
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM $tableName limit 0", null)
            if (null != cursor && cursor.columnCount > 0) {
                columns = listOf(*cursor.columnNames)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            if (null == columns) columns = ArrayList()
        }
        return columns
    }

    interface ReCreateAllTableListener {
        fun onCreateAllTables(db: Database, ifNotExists: Boolean)
        fun onDropAllTables(db: Database, ifExists: Boolean)
    }

    private class TableInfo {

        var cid = 0
        var name: String? = null
        var type: String? = null
        var notnull = false
        var dfltValue: String? = null
        var pk = false

        override fun equals(other: Any?): Boolean {
            return (this === other
                    || other != null && javaClass == other.javaClass && name == (other as TableInfo).name)
        }

        override fun toString(): String {
            return "TableInfo{" +
                    "cid=" + cid +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", notnull=" + notnull +
                    ", dfltValue='" + dfltValue + '\'' +
                    ", pk=" + pk +
                    '}'
        }

        companion object {
            fun getTableInfo(db: Database, tableName: String): List<TableInfo> {
                val sql = "PRAGMA table_info(`$tableName`)"
                val cursor = db.rawQuery(sql, null) ?: return ArrayList()
                var tableInfo: TableInfo
                val tableInfos: MutableList<TableInfo> = ArrayList()
                while (cursor.moveToNext()) {
                    tableInfo = TableInfo()
                    tableInfo.cid = cursor.getInt(0)
                    tableInfo.name = cursor.getString(1)
                    tableInfo.type = cursor.getString(2)
                    tableInfo.notnull = cursor.getInt(3) == 1
                    tableInfo.dfltValue = cursor.getString(4)
                    tableInfo.pk = cursor.getInt(5) == 1
                    tableInfos.add(tableInfo)
                }
                cursor.close()
                return tableInfos
            }
        }
    }

}