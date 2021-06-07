package com.papb.eats

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.EditText
import com.papb.eats.adapter.RecyclerAdapter
import com.papb.eats.model.Reminder
import java.lang.Exception

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        val DATABASE_NAME = "Eats.db"
        val TABLE_NAME = "reminders"
        val ID = "id"
        val TITLE = "title"
        val TIME = "time"
        val COMPLETED = "isComplete"
    }


    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_REMINDERS_TABLE = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " VARCHAR(64), "
                + TIME + " VARCHAR(64), "
                + COMPLETED + " INTEGER" + ")")
//        db?.execSQL("CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $TITLE VARCHAR(64), $TIME VARCHAR(64), $COMPLETED BOOLEAN);")
        db.execSQL(CREATE_REMINDERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
//        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME;")
        onCreate(db)
    }

    //Insert data ke database
    fun insertData(title: String, time: String, isCompleted: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TITLE, title)
        contentValues.put(TIME, time)
        contentValues.put(COMPLETED, isCompleted)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    //Query list data
    fun listOfReminder(): ArrayList<Reminder> {
        val db = this.writableDatabase
        val res = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val reminderList = ArrayList<Reminder>()

        if (res.moveToFirst()) {
            do {
                val reminder = Reminder()
                reminder.id = res.getString(res.getColumnIndex("id").toInt())
                reminder.time = res.getString(res.getColumnIndex("time"))
                reminder.title = res.getString(res.getColumnIndex("title"))
                reminder.completed = res.getInt(res.getColumnIndex("isComplete"))
                reminderList.add(reminder)
            } while (res.moveToNext())
        }
        return reminderList
    }

    // Update data
    fun updateData(id: String, title: String, time: String, isCompleted: Boolean) : Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)
        contentValues.put(TITLE, title)
        contentValues.put(TIME, time)
        contentValues.put(COMPLETED, isCompleted)
        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    // Delete data
    fun deleteData(id: String) : Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "ID = ?", arrayOf(id))
    }

//    Gak jadi dipake, malah gabisa pake yang ini xixixi
//    fun getAllData(): ArrayList<Reminder> {
//        val reminderList: ArrayList<Reminder> = ArrayList()
//        val SELECT_ALL = "SELECT * FROM $TABLE_NAME"
//        val db = this.readableDatabase
//
//        val cursor: Cursor?
//
//        try {
//            cursor = db.rawQuery(SELECT_ALL, null)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            db.execSQL(SELECT_ALL)
//            return ArrayList()
//        }
//
//        var id: String
//        var title: String
//        var time: String
//
//        if (cursor.moveToFirst()) {
//            do {
//                id = cursor.getString(cursor.getColumnIndex("id"))
//                title = cursor.getString(cursor.getColumnIndex("title"))
//                time = cursor.getString(cursor.getColumnIndex("time"))
//
////                val reminder = Reminder(id, title, time)
////                reminderList.add(reminder)
//            } while (cursor.moveToNext())
//        }
//
//        return reminderList
//    }
}
