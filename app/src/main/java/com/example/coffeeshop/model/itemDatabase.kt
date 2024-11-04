package com.example.coffeeshop.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


private const val DATABASE_NAME = "Items.db"
private const val DATABASE_VERSION = 1

class itemDatabase (context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION){

    private val ITEM_TABLE_NAME = "Item"
    private val ITEM_COLUMN_ID = "id"
    private val ITEM_COLUMN_NAME = "name"
    private val ITEM_COLUMN_IMAGE = "image"
    private val ITEM_COLUMN_DESCRIPTION = "description"
    private val ITEM_COLUMN_RATING = "rating"
    private val ITEM_COLUMN_PRICE = "price"
    private val ITEM_COLUMN_CATEGORY = "category"
    private val ITEM_COLUMN_NUMBER_OF_RATINGS = "numberOfRatings"


    override fun onCreate(db: SQLiteDatabase?) {
        val sqlCreateStatement = """
            CREATE TABLE $ITEM_TABLE_NAME (
                $ITEM_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $ITEM_COLUMN_NAME TEXT NOT NULL,
                $ITEM_COLUMN_IMAGE INTEGER NOT NULL,
                $ITEM_COLUMN_DESCRIPTION TEXT,
                $ITEM_COLUMN_RATING REAL NOT NULL,
                $ITEM_COLUMN_PRICE REAL NOT NULL,
                $ITEM_COLUMN_CATEGORY TEXT NOT NULL,
                $ITEM_COLUMN_NUMBER_OF_RATINGS INTEGER NOT NULL
            )
        """.trimIndent()

        try {
            db?.execSQL(sqlCreateStatement)
        } catch (e: SQLException) {
            Log.e("ItemDatabase", "Error creating table", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun getItemsByCategory(category: String): List<Item> {
        val itemList = mutableListOf<Item>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $ITEM_TABLE_NAME WHERE $ITEM_COLUMN_CATEGORY = ?", arrayOf(category))

        if (cursor.moveToFirst())
            do {
                itemList.add(cursorToItem(cursor))
            } while (cursor.moveToNext())

        cursor.close()
        db.close()
        return itemList
    }
    fun getItemById(id: Int): Item {

        var item: Item = Item(1, "", 1, "", 1.2, 1.2, "", 1)
        val db: SQLiteDatabase = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $ITEM_TABLE_NAME WHERE $ITEM_COLUMN_ID = ?", arrayOf(id.toString()))

        if (cursor.moveToFirst())
            do {
                item = cursorToItem(cursor)
            } while (cursor.moveToNext())

        cursor.close()
        db.close()
        return item
    }
    fun getAllItems(): ArrayList<Item> {

        val itemList = ArrayList<Item>()
        val db: SQLiteDatabase = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $ITEM_TABLE_NAME", null)

        if (cursor.moveToFirst())
            do {
                itemList.add(cursorToItem(cursor))
            } while (cursor.moveToNext())

        cursor.close()
        db.close()
        return itemList
    }

    fun addItem(item: Item) : Int {
        val db: SQLiteDatabase
        try {
            db = this.writableDatabase
        }
        catch(e: SQLiteException) {
            return -2
        }
        val cv = ContentValues()
        cv.put(ITEM_COLUMN_NAME, item.name)
        cv.put(ITEM_COLUMN_IMAGE,item.image)
        cv.put(ITEM_COLUMN_DESCRIPTION, item.description)
        cv.put(ITEM_COLUMN_RATING, item.rating)
        cv.put(ITEM_COLUMN_PRICE,item.price)
        cv.put(ITEM_COLUMN_CATEGORY,item.category)
        cv.put(ITEM_COLUMN_NUMBER_OF_RATINGS,item.numberOfRatings)
        val success  =  db.insert(ITEM_TABLE_NAME, null, cv)
        db.close()
        return success.toInt() //1
    }
    fun clearDatabase() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $ITEM_TABLE_NAME") // Deletes all rows from the table
        db.close()
    }

    fun updateRating(item: Item, newRating: Double){
        val add = (item.rating * item.numberOfRatings + newRating)
        val dev = item.numberOfRatings + 1
        val result = add / dev

        val db: SQLiteDatabase = this.writableDatabase
        val cv= ContentValues()

        cv.put("rating", result)
        cv.put("numberOfRatings", dev)

        // Specify which row to update with the "id" column
        db.update(ITEM_TABLE_NAME, cv, "id = ?", arrayOf(item.id.toString()))
        db.close()
    }
    private fun cursorToItem(cursor: Cursor): Item {
        val id: Int = cursor.getInt(0)
        val name: String = cursor.getString(1)
        val image: Int = cursor.getInt(2)
        val description: String = cursor.getString(3)
        val rating: Double = cursor.getDouble(4)
        val price: Double = cursor.getDouble(5)
        val category: String = cursor.getString(6)
        val numberOfRatings: Int = cursor.getInt(7)

        val i = Item(id, name, image, description, rating, price, category, numberOfRatings)
        return i
    }
}