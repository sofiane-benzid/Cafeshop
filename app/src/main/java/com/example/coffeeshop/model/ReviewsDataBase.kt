package com.example.coffeeshop.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ReviewsDataBase (context: Context) : SQLiteOpenHelper(context, "Reviews.db", null, 1){

    public val ReviewTableName = "Reviews"
    public val ReviewColumn_ID = "ReviewID"
    public val ReviewColumn_ItemID = "ItemID"
    public val ReviewColumn_Text = "ReviewText"
    public val ItemTableName = "Item"
    public val ItemColumn_ID = "id"

    override fun onCreate(db: SQLiteDatabase?) {
        val createReviewTable = """
        CREATE TABLE $ReviewTableName (
            $ReviewColumn_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $ReviewColumn_ItemID INTEGER,
            $ReviewColumn_Text TEXT NOT NULL,
            FOREIGN KEY($ReviewColumn_ItemID) REFERENCES $ItemTableName($ItemColumn_ID) 
        )
    """
        db?.execSQL(createReviewTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $ReviewTableName")
        onCreate(db)
    }

    fun addReview(itemID: Int, reviewText: String): Long {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(ReviewColumn_ItemID, itemID)
            put(ReviewColumn_Text, reviewText)
        }
        val result = db.insert(ReviewTableName, null, cv)
        db.close()
        return result
    }

    fun getReviewsForItem(itemID: Int): ArrayList<String> {
        val db = this.readableDatabase
        val reviews = ArrayList<String>()
        val cursor = db.rawQuery("SELECT $ReviewColumn_Text FROM $ReviewTableName WHERE $ReviewColumn_ItemID = ?", arrayOf(itemID.toString()))

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
        db.execSQL("DELETE FROM $ReviewTableName") // Deletes all rows from the table
        db.close()
    }
}
