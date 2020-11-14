package com.miyin.zhenbaoqi.sql.migration

import android.content.Context
import android.database.sqlite.SQLiteDatabase.CursorFactory
import com.miyin.greendao.DaoMaster
import com.miyin.greendao.DaoMaster.OpenHelper
import com.miyin.zhenbaoqi.sql.migration.MigrationHelper.ReCreateAllTableListener
import org.greenrobot.greendao.database.Database

class MySQLiteOpenHelper(context: Context, name: String, factory: CursorFactory?) : OpenHelper(context, name, factory) {

    override fun onUpgrade(db: Database, oldVersion: Int, newVersion: Int) {
        MigrationHelper.migrate(db, object : ReCreateAllTableListener {
            override fun onCreateAllTables(db: Database, ifNotExists: Boolean) {
                DaoMaster.createAllTables(db, ifNotExists)
            }

            override fun onDropAllTables(db: Database, ifExists: Boolean) {
                DaoMaster.dropAllTables(db, ifExists)
            }
        })
    }

}