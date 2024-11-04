package com.example.coffeeshop.model

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private const val DATABASE_NAME = "Reviews.db"
private const val DATABASE_VERSION = 1

class ReviewsDataBase (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    public val REVIEW_TABLE_NAME = "Reviews"
    public val REVIEW_COLUMN_ID = "ReviewID"
    public val REVIEW_COLUMN_ITEM_ID = "ItemID"
    public val REVIEW_COLUMN_TEXT = "ReviewText"
    public val ITEM_TABLE_NAME = "Item"
    public val ITEM_COLUMN_ID = "id"

    override fun onCreate(db: SQLiteDatabase?) {
        val sqlCreateStatement = """
        CREATE TABLE $REVIEW_TABLE_NAME (
            $REVIEW_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $REVIEW_COLUMN_ITEM_ID INTEGER,
            $REVIEW_COLUMN_TEXT TEXT NOT NULL,
            FOREIGN KEY($REVIEW_COLUMN_ITEM_ID) REFERENCES $ITEM_TABLE_NAME($ITEM_COLUMN_ID) 
        )
    """
        try {
            db?.execSQL(sqlCreateStatement)
        } catch (e: SQLException) {
            Log.e("ReviewsDatabase", "Error creating table", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun addReview(itemID: Int, reviewText: String): Long {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(REVIEW_COLUMN_ITEM_ID, itemID)
            put(REVIEW_COLUMN_TEXT, reviewText)
        }
        val result = db.insert(REVIEW_TABLE_NAME, null, cv)
        db.close()
        return result
    }

    fun getReviewsForItem(itemID: Int): ArrayList<String> {
        val db = this.readableDatabase
        val reviews = ArrayList<String>()
        val cursor = db.rawQuery("SELECT $REVIEW_COLUMN_TEXT FROM $REVIEW_TABLE_NAME WHERE $REVIEW_COLUMN_ITEM_ID = ?", arrayOf(itemID.toString()))

        if (cursor.moveToFirst()) {
            do {
                reviews.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return reviews
    }
    fun clearDatabase() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $REVIEW_TABLE_NAME") // Deletes all rows from the table
        db.close()
    }
}
