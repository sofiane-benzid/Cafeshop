package com.example.coffeeshop.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

private val DataBaseName = "Users.db"
private val ver : Int = 1

class userDataBase (context: Context) : SQLiteOpenHelper (context, DataBaseName, null, ver){

    public val UserTableName = "User"
    public val UserColumn_ID = "id"
    public val UserColumn_firstName = "firstName"
    public val UserColumn_lastName = "lastName"
    public val UserColumn_email = "email"
    public val UserColumn_password = "password"
    public val UserColumn_dateOfBirth = "dateOfBirth"

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            var sqlCreateStatement: String = "CREATE TABLE " + UserTableName + " ( " + UserColumn_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + UserColumn_firstName + " TEXT NOT NULL, " +
                    UserColumn_lastName + " TEXT NOT NULL, " + UserColumn_email + " TEXT NOT NULL UNIQUE, " +
                    UserColumn_password + " TEXT NOT NULL, " + UserColumn_dateOfBirth + " TEXT NOT NULL )"

            db?.execSQL(sqlCreateStatement)
        }
        catch(e : SQLException) {  }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun getAllUsers(): ArrayList<User> {

        val userList = ArrayList<User>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $UserTableName"

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

        val cv: ContentValues = ContentValues()
        val hashedPassword = hashPassword(user.password)
        cv.put(UserColumn_firstName, user.firstName)
        cv.put(UserColumn_lastName,user.lastName)
        cv.put(UserColumn_email, user.email.lowercase())
        cv.put(UserColumn_password, hashedPassword)
        cv.put(UserColumn_dateOfBirth,user.dateOfBirth)


        val success  =  db.insert(UserTableName, null, cv)

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

        val sqlStatement = "SELECT * FROM $UserTableName WHERE $UserColumn_email = ?"
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
        val password = user.password
        val hashedPassword = hashPassword(user.password)

        val sqlStatement = "SELECT * FROM $UserTableName WHERE $UserColumn_email = ? AND $UserColumn_password = ?"
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

        val sqlStatement = "SELECT * FROM $UserTableName WHERE $UserColumn_email = ?"
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
        db.execSQL("DELETE FROM $UserTableName") // Deletes all rows from the table
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