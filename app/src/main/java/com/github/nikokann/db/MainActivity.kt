package com.github.nikokann.db

import TestOpenHelper
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

//package your.package.name;


class MainActivity : AppCompatActivity() {
    private var textView: TextView? = null
    private var editTextKey: EditText? = null
    private var editTextValue: EditText? = null
    private var helper: TestOpenHelper? = null
    private var db: SQLiteDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextKey = findViewById(R.id.edit_text_key)
        editTextValue = findViewById(R.id.edit_text_value)
        textView = findViewById(R.id.text_view)
        val insertButton = findViewById<Button>(R.id.button_insert)
        insertButton.setOnClickListener { v: View? ->
            if (helper == null) {
                helper = TestOpenHelper(applicationContext)
            }
            if (db == null) {
                db = helper!!.getWritableDatabase()
            }
            val key = editTextKey!!.getText().toString()
            val value = editTextValue!!.getText().toString()

            // 価格は整数を想定
            insertData(db!!, key, value.toInt())
        }
        val readButton = findViewById<Button>(R.id.button_read)
        readButton.setOnClickListener { v: View? -> readData() }
    }

    private fun readData() {
        if (helper == null) {
            helper = TestOpenHelper(applicationContext)
        }
        if (db == null) {
            db = helper!!.getReadableDatabase()
        }
        Log.d("debug", "**********Cursor")
        val cursor = db!!.query(
            "testdb", arrayOf("company", "stockprice"),
            null,
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        val sbuilder = StringBuilder()
        for (i in 0 until cursor.count) {
            sbuilder.append(cursor.getString(0))
            sbuilder.append(": ")
            sbuilder.append(cursor.getInt(1))
            sbuilder.append("\n")
            cursor.moveToNext()
        }

        // 忘れずに！
        cursor.close()
        Log.d("debug", "**********$sbuilder")
        textView!!.text = sbuilder.toString()
    }

    private fun insertData(db: SQLiteDatabase, com: String, price: Int) {
        val values = ContentValues()
        values.put("company", com)
        values.put("stockprice", price)
        db.insert("testdb", null, values)
    }
}