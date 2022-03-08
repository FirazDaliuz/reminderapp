package com.example.alert.reminderapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.ArrayList as ArrayList1

val DATABASENAME = "ALARM"
val TABLENAME1 = "PERSIST"
val TABLENAME2 = "SCHEDULE"
val TABLENAME3 = "SETTING"
val COL_PERSIST_ID = "PERSIST_ID"
val COL_PERSIST_NAME = "PERSIST_NAME"
val COL_PERSIST_URI = "PERSIST_URI"
val COL_SCHEDULE_ID = "SCHEDULE_ID"
val COL_SCHEDULE_NAME = "SCHEDULE_NAME"
val COL_SCHEDULE_DATE = "SCHEDULE_DATE"
val COL_SCHEDULE_TIME = "SCHEDULE_TIME"
val COL_SCHEDULE_URI = "SCHEDULE_URI"
val COL_SETTING_ID = "SETTING_ID"
val COL_SETTING_URI = "SETTING_URI"

class DBHelper(var context: Context) : SQLiteOpenHelper(context, DATABASENAME, null,
    1) {

    var dat = ""

    override fun onCreate(db: SQLiteDatabase?) {
        createDBandTables(db)
    }

    fun createDBandTables(db: SQLiteDatabase?){
        val createTable = "CREATE TABLE " + TABLENAME1 + " (" + COL_PERSIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_PERSIST_NAME + " VARCHAR(256)," + COL_PERSIST_URI + " VARCHAR(256))"
        db?.execSQL(createTable)
        val createTable2 = "CREATE TABLE " + TABLENAME2 + " (" + COL_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_SCHEDULE_NAME + " VARCHAR(256)," + COL_SCHEDULE_DATE + " VARCHAR(256)," + COL_SCHEDULE_TIME + " VARCHAR(256)," + COL_SCHEDULE_URI + " VARCHAR(256))"
        db?.execSQL(createTable2)
        val createTable3 = "CREATE TABLE " + TABLENAME3+ " (" + COL_SETTING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_SETTING_URI + " VARCHAR(256))"
        db?.execSQL(createTable3)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    open fun insertTable1(a: String, b: String) {
        print(a)
        print(b)
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_PERSIST_NAME, a)
        contentValues.put(COL_PERSIST_URI, b)
        val result = database.insert(TABLENAME1, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    open fun insertTable2(a: String, b: String, c: String, d:String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_SCHEDULE_NAME, a)
        contentValues.put(COL_SCHEDULE_DATE, b)
        contentValues.put(COL_SCHEDULE_TIME, c)
        contentValues.put(COL_SCHEDULE_URI, d)
        val result = database.insert(TABLENAME2, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    open fun insertTable3(a: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_SETTING_URI, a)
        val result = database.insert(TABLENAME3, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("Range")
    fun GetPersist(): ArrayList1<HashMap<String, String>>? {
        val db = this.writableDatabase
        val persistList: ArrayList1<HashMap<String, String>> = ArrayList1()
        val query = "SELECT * FROM $TABLENAME1"
        val cursor: Cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val persist: HashMap<String, String> = HashMap()
            persist["PERSIST_ID"] = cursor.getString(cursor.getColumnIndex(COL_PERSIST_ID))
            persist["PERSIST_NAME"] = cursor.getString(cursor.getColumnIndex(COL_PERSIST_NAME))
            persist["PERSIST_URI"] = cursor.getString(cursor.getColumnIndex(COL_PERSIST_URI))
            persistList.add(persist)
        }
        return persistList
    }

    @SuppressLint("Range")
    open fun GetSchedule(a:String): ArrayList1<HashMap<String, String>>? {
        val db = this.writableDatabase
        val scheduleList: ArrayList1<HashMap<String, String>> = ArrayList1()
        val query = "SELECT * FROM $TABLENAME2 where SCHEDULE_DATE = '$a'"
        val cursor: Cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val schedule: HashMap<String, String> = HashMap()
            schedule["SCHEDULE_NAME"] = cursor.getString(cursor.getColumnIndex(COL_SCHEDULE_NAME))
            schedule["SCHEDULE_DATE"] = cursor.getString(cursor.getColumnIndex(COL_SCHEDULE_DATE))
            schedule["SCHEDULE_TIME"] = cursor.getString(cursor.getColumnIndex(COL_SCHEDULE_TIME))
            schedule["SCHEDULE_URI"] = cursor.getString(cursor.getColumnIndex(COL_SCHEDULE_URI))
            scheduleList.add(schedule)
        }
        return scheduleList
    }

    @SuppressLint("Range")
    fun GetSetting(): ArrayList1<HashMap<String, String>>? {
        val db = this.writableDatabase
        val settingList: ArrayList1<HashMap<String, String>> = ArrayList1()
        val query = "SELECT * FROM $TABLENAME3"
        val cursor: Cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val setting: HashMap<String, String> = HashMap()
            setting["PERSIST_URI"] = cursor.getString(cursor.getColumnIndex(COL_SETTING_URI))
            settingList.add(setting)
        }
        return settingList
    }

    fun deletePersist(userid: Int) {
        val db = this.writableDatabase
        db.delete(TABLENAME1, COL_PERSIST_ID.toString() + " = ?", arrayOf(userid.toString()))
        db.close()
    }

    fun checkROWS(){
        val db = this.writableDatabase
        val rowst = DatabaseUtils.longForQuery(db,"select count(*) from SCHEDULE",null)
        Toast.makeText(context, rowst.toString(), Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("Range")
    fun getPreciseIDPersist(): ArrayList<String> {
        val db = this.writableDatabase
        val rowst : Cursor = db.rawQuery(
            "select PERSIST_ID from PERSIST",null)
        val arrayList = ArrayList<String>()
        while (rowst.moveToNext()){
            arrayList.add(rowst.getString(rowst.getColumnIndex(COL_PERSIST_ID)))
        }
        return arrayList
    }

    @SuppressLint("Range")
    fun getPreciseURIPersist(a: Int?): String {
        val db = this.writableDatabase
        val rowst : Cursor = db.rawQuery(
            "select PERSIST_URI from PERSIST where PERSIST_ID =$a",null)
        while (rowst.moveToNext()){
            dat = rowst.getString(rowst.getColumnIndex(COL_PERSIST_URI))
        }
        return dat
    }

    @SuppressLint("Range")
    fun getPreciseNamePersist(a: Int): String {
        val db = this.writableDatabase
        val rowst : Cursor = db.rawQuery(
            "select PERSIST_NAME from PERSIST where PERSIST_ID =$a",null)
        while (rowst.moveToNext()){
            dat = rowst.getString(rowst.getColumnIndex(COL_PERSIST_NAME))
        }
        return dat
    }

    @SuppressLint("Range")
    fun getPreciseNameV(a: String): String {
        val db = this.writableDatabase
        val rowst : Cursor = db.rawQuery(
            "select SCHEDULE_ID from SCHEDULE where SCHEDULE_DATE ='$a' ",null)
        while (rowst.moveToNext()){
            dat = rowst.getString(rowst.getColumnIndex(COL_SCHEDULE_ID))
        }
        Toast.makeText(context, dat, Toast.LENGTH_SHORT).show()
        return dat
    }
}