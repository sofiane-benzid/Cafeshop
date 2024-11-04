package com.example.coffeeshop.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

private const val DATABASE_NAME = "Users.db"
private const val DATABASE_VERSION = 1

class userDataBase (context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION){

    private val USER_TABLE_NAME = "User"
    private val USER_COLUMN_ID = "id"
    private val USER_COLUMN_FIRST_NAME = "firstName"
    private val USER_COLUMN_LAST_NAME = "lastName"
    private val USER_COLUMN_EMAIL = "email"
    private val USER_COLUMN_PASSWORD = "password"
    private val USER_COLUMN_DATE_OF_BIRTH = "dateOfBirth"

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            var sqlCreateStatement: String = "CREATE TABLE " + USER_TABLE_NAME + " ( " + USER_COLUMN_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                    USER_COLUMN_LAST_NAME + " TEXT NOT NULL, " + USER_COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    USER_COLUMN_PASSWORD + " TEXT NOT NULL, " + USER_COLUMN_DATE_OF_BIRTH + " TEXT NOT NULL )"

            db?.execSQL(sqlCreateStatement)
        }
        catch(e : SQLException) {
            Log.e("UserDatabase", "Error creating table", e)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun getAllUsers(): ArrayList<User> {

        val userList = ArrayList<User>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $USER_TABLE_NAME"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst())
            do {
                val id: Int = cursor.getInt(0)
                val firstName: String = cursor.getString(1)
                val lastName: String = cursor.getString(2)
                val email: String = cursor.getString(3)
                val password: String = cursor.getString(4)
                val dateOfBirth: String = cursor.getString(5)

                val u = User(id, firstName, lastName, email, password, dateOfBirth)
                userList.add(u)
            } while (cursor.moveToNext())

        cursor.close()
        db.close()

        return userList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addUser(user: User) : Int {

        val db: SQLiteDatabase
        val isUserExist = checkUserExists(user)
        if(isUserExist < 0)
            return isUserExist
        try {
            db = this.writableDatabase
        }
        catch(e: SQLiteException) {
            return -2
        }

        val cv = ContentValues()
        val hashedPassword = hashPassword(user.password)
        cv.put(USER_COLUMN_FIRST_NAME, user.firstName)
        cv.put(USER_COLUMN_LAST_NAME,user.lastName)
        cv.put(USER_COLUMN_EMAIL, user.email.lowercase())
        cv.put(USER_COLUMN_PASSWORD, hashedPassword)
        cv.put(USER_COLUMN_DATE_OF_BIRTH,user.dateOfBirth)

        val success  =  db.insert(USER_TABLE_NAME, null, cv)

        db.close()
        return success.toInt() //1

    }
    private fun checkUserExists(user: User): Int {

        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        }
        catch(e: SQLiteException) {
            return -2
        }

        val email = user.email.lowercase()

        val sqlStatement = "SELECT * FROM $USER_TABLE_NAME WHERE $USER_COLUMN_EMAIL = ?"
        val param = arrayOf(email)
        val cursor: Cursor =  db.rawQuery(sqlStatement,param)

        if(cursor.moveToFirst()){
            // The user is found
            cursor.close()
            db.close()
            return -3 // error the user name is already exist
        }

        cursor.close()
        db.close()
        return 0 //User not found

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun verifyUser(user: User) : Int {

        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        }
        catch(e: SQLiteException) {
            return -2
        }

        val email = user.email.lowercase()
        val hashedPassword = hashPassword(user.password)

        val sqlStatement = "SELECT * FROM $USER_TABLE_NAME WHERE $USER_COLUMN_EMAIL = ? AND $USER_COLUMN_PASSWORD = ?"
        val param = arrayOf(email,hashedPassword)
        val cursor: Cursor =  db.rawQuery(sqlStatement,param)
        if(cursor.moveToFirst()){
            val n = cursor.getInt(0)
            cursor.close()
            db.close()
            return n
        }

        cursor.close()
        db.close()
        return -1 //User not found
    }

    fun getUserByEmail(email: String): User {
        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        }
        catch(e: SQLiteException) {
            return User(-2,"","","","","")
        }

        val sqlStatement = "SELECT * FROM $USER_TABLE_NAME WHERE $USER_COLUMN_EMAIL = ?"
        val param = arrayOf(email.lowercase())
        val cursor: Cursor =  db.rawQuery(sqlStatement,param)
        if(cursor.moveToFirst()){
            // The user is found
            val id: Int = cursor.getInt(0)
            val firstName: String = cursor.getString(1)
            val lastName: String = cursor.getString(2)
            val password: String = cursor.getString(4)
            val dateOfBirth: String = cursor.getString(5)
            val u = User(id, firstName, lastName, email, password, dateOfBirth)
            cursor.close()
            db.close()
            return u
        }

        cursor.close()
        db.close()
        return User(-1,"","","","","")

    }
    fun clearDatabase() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $USER_TABLE_NAME") // Deletes all rows from the table
        db.close()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun hashPassword(password: String): String {
        val salt = "asaltnoonecanfind".toByteArray()
        val iterations = 10000
        val keyLength = 256
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, keyLength)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val hash = factory.generateSecret(spec).encoded
        return Base64.getEncoder().encodeToString(hash)
    }
}