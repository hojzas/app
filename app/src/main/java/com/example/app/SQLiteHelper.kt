package com.example.app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.app.SQLiteHelper

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    object DB{
        // Table contents are grouped together in an anonymous object.
        object TestTable : BaseColumns {
            const val TABLE_NAME = "test"
            const val NAME = "name"
            const val BOOL = "bool"
        }
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        private const val DATABASE_NAME = "app.db"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE =
            "CREATE TABLE ${DB.TestTable.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${DB.TestTable.NAME} TEXT," +
                    "${DB.TestTable.BOOL} INTEGER)"

        private const val SQL_DELETE = "DROP TABLE IF EXISTS ${DB.TestTable.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE)
        onCreate(db)
    }

    fun insertTestData(data: TestData): Long {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(DB.TestTable.NAME, data.name)
            put(DB.TestTable.BOOL, data.bool)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DB.TestTable.TABLE_NAME, null, values)

        db.close()
        return newRowId
    }

    @SuppressLint("Range")
    fun getAllTestData(): ArrayList<TestData> {
        // TODO to separate fun
        val db = this.readableDatabase

        val selectQuery = "SELECT * FROM " + DB.TestTable.TABLE_NAME
        val dataList: ArrayList<TestData> = ArrayList()
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return dataList
        }

        var id: Int
        var name: String
        var bool: Boolean

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                name = cursor.getString(cursor.getColumnIndex(DB.TestTable.NAME))
                bool = cursor.getInt(cursor.getColumnIndex(DB.TestTable.BOOL)) > 0

                val data = TestData(id, name, bool)
                dataList.add(data)

            } while (cursor.moveToNext())
        }

        return dataList
    }

}