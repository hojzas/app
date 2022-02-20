package com.example.app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    object DB{
        // Jobs
        object JobTable : BaseColumns {
            const val TABLE_NAME = "jobs"
            const val NAME = "name"
            const val WAGE = "wage"
        }
        // Shifts
        object ShiftTable : BaseColumns {
            const val TABLE_NAME = "shifts"
            const val START = "start"
            const val END = "end"
            const val DATE = "date"
        }
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        private const val DATABASE_NAME = "app.db"
        private const val DATABASE_VERSION = 1

        // Jobs
        private const val SQL_CREATE_TABLE_JOBS =
            "CREATE TABLE ${DB.JobTable.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${DB.JobTable.NAME} TEXT," +
                    "${DB.JobTable.WAGE} REAL)"

        // Shifts
        private const val SQL_CREATE_TABLE_SHIFTS =
            "CREATE TABLE ${DB.ShiftTable.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${DB.ShiftTable.START} TEXT," +
                    "${DB.ShiftTable.END} TEXT," +
                    "${DB.ShiftTable.DATE} TEXT)"

        private const val SQL_DELETE_TABLE_JOBS = "DROP TABLE IF EXISTS ${DB.JobTable.TABLE_NAME}"
        private const val SQL_DELETE_TABLE_SHIFTS = "DROP TABLE IF EXISTS ${DB.ShiftTable.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_JOBS)
        db?.execSQL(SQL_CREATE_TABLE_SHIFTS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_TABLE_JOBS)
        db?.execSQL(SQL_DELETE_TABLE_SHIFTS)
        onCreate(db)
    }

    fun insertTestData(data: Job): Long {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(DB.JobTable.NAME, data.name)
            put(DB.JobTable.WAGE, data.wage)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DB.JobTable.TABLE_NAME, null, values)

        db.close()
        return newRowId
    }

    @SuppressLint("Range")
    fun getAllTestData(): ArrayList<Job> {
        // TODO to separate fun
        val db = this.readableDatabase

        val selectQuery = "SELECT * FROM " + DB.JobTable.TABLE_NAME
        val dataList: ArrayList<Job> = ArrayList()
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
        var wage: Float

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                name = cursor.getString(cursor.getColumnIndex(DB.JobTable.NAME))
                wage = cursor.getFloat(cursor.getColumnIndex(DB.JobTable.WAGE))

                val data = Job(id, name, wage)
                dataList.add(data)

            } while (cursor.moveToNext())
        }

        return dataList
    }

}