package com.example.coffeeshop.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


private val DataBaseName = "Items.db"
private val ver : Int = 1

class itemDataBase (context: Context) : SQLiteOpenHelper (context, DataBaseName, null, ver){

    private val itemTableName = "Item"
    private val itemColumnId = "id"
    private val itemColumnName = "name"
    private val itemColumnImage = "image"
    private val itemColumnDescription = "description"
    private val itemColumnRating = "rating"
    private val itemColumnPrice = "price"
    private val itemColumnCategory = "category"
    private val itemColumnNumberOfRatings = "numberOfRatings"


    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val sqlCreateStatement: String = "CREATE TABLE " + itemTableName + " ( " + itemColumnId +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + itemColumnName + " TEXT NOT NULL, " +
                    itemColumnImage + " INTEGER NOT NULL, " + itemColumnDescription + " TEXT , " +
                    itemColumnRating + " REAL NOT NULL, " + itemColumnPrice + " REAL NOT NULL, " +
                    itemColumnCategory + " STRING NOT NULL, " + itemColumnNumberOfRatings + " INTEGER NOT NULL )"

            db?.execSQL(sqlCreateStatement)
        }
        catch(_: SQLException) {  }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun getItemsBasedOnCategory(Category: String): ArrayList<Item> {

        val itemList = ArrayList<Item>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $itemTableName WHERE category = ?"
        val cursor: Cursor = db.rawQuery(sqlStatement, arrayOf(Category))

        if (cursor.moveToFirst())
            do {
                val i = item(cursor)
                itemList.add(i)
            } while (cursor.moveToNext())

        cursor.close()
        db.close()

        return itemList
    }
    fun getItemWithID(ID: Int): Item {

        var item: Item = Item(1, "", 1, "", 1.2, 1.2, "", 1)
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $itemTableName WHERE id = ?"
        val cursor: Cursor = db.rawQuery(sqlStatement, arrayOf(ID.toString()))

        if (cursor.moveToFirst())
            do {
                item = item(cursor)
            } while (cursor.moveToNext())

        cursor.close()
        db.close()

        return item
    }
    fun getAllItems(): ArrayList<Item> {

        val itemList = ArrayList<Item>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $itemTableName"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst())
            do {
                val i = item(cursor)
                itemList.add(i)
            } while (cursor.moveToNext())

        cursor.close()
        db.close()

        return itemList
    }

    private fun item(cursor: Cursor): Item {
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

    fun addItem(item: Item) : Int {
        val db: SQLiteDatabase
        try {
            db = this.writableDatabase
        }
        catch(e: SQLiteException) {
            return -2
        }
        val cv = ContentValues()
        cv.put(itemColumnName, item.name)
        cv.put(itemColumnImage,item.image)
        cv.put(itemColumnDescription, item.description)
        cv.put(itemColumnRating, item.rating)
        cv.put(itemColumnPrice,item.price)
        cv.put(itemColumnCategory,item.category)
        cv.put(itemColumnNumberOfRatings,item.numberOfRatings)
        val success  =  db.insert(itemTableName, null, cv)
        db.close()
        return success.toInt() //1
    }
    fun clearDatabase() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $itemTableName") // Deletes all rows from the table
        db.close()
    }

    fun updateRating(item: Item, newRating: Double){
        val add = (item.rating * item.numberOfRatings + newRating)
        val dev = item.numberOfRatings + 1
        val result = add / dev
        Log.d("RatingBar", "User rated: $result")
        val db: SQLiteDatabase = this.writableDatabase
        val cv= ContentValues()

        cv.put("rating", result)
        cv.put("numberOfRatings", dev)

        // Specify which row to update with the "id" column
        val rowsUpdated = db.update(itemTableName, cv, "id = ?", arrayOf(item.id.toString()))
        db.close()
    }
}