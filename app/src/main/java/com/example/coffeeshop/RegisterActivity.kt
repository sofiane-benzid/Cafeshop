package com.example.coffeeshop

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.coffeeshop.model.User
import com.example.coffeeshop.model.userDataBase
import com.example.coffeeshop.model.utils

public class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // variables
        val registerButton: Button = findViewById(R.id.registerButton2)

        registerButton.setOnClickListener {
            var result = saveNewUserButton(this)
            if (result != -1 && result != -2 && result != -3){
                val email = findViewById<EditText>(R.id.email).text.toString()
                val myDataBase = userDataBase(this)
                val user = myDataBase.getUserByEmail(email)
                var intent: Intent = Intent(this, MainPageActivity::class.java)
                intent.putExtra("USER_KEY", user)
                startActivity(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveNewUserButton(context: Context): Int {

        val firstName = findViewById<EditText>(R.id.firstName).text.toString()
        val lastName  = findViewById<EditText>(R.id.lastName).text.toString()
        val email  = findViewById<EditText>(R.id.email).text.toString()
        val password  = findViewById<EditText>(R.id.password).text.toString()
        val dateOfBirth  = findViewById<EditText>(R.id.dob).text.toString()

        val message = findViewById<TextView>(R.id.textViewMessage)

        if(firstName.isEmpty()) {
            message.text = "First name required!"
            message.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        else if(lastName.isEmpty()) {
            message.text = "Last name required!"
            message.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        else if(email.isEmpty()) {
            message.text = "Email required!"
            message.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        else if(password.isEmpty()) {
            message.text = "Password required!"
            message.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        else if (utils.isStrongPassword(password) == false){
            message.text = "Make sure the password has atleast one lowercase character, one uppercase letter, one digit, one special character and is atleast 8 characters long"
            message.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        else if(dateOfBirth.isEmpty()) {
            message.text = "Date of Birth required!"
            message.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        else {
            val newUser = User(-1,firstName, lastName, email, password, dateOfBirth)
            val myDataBase = userDataBase(this)
            val result = myDataBase.addUser(newUser)

            when(result) {

                -1 -> message.text = "Error on creating new account"
                -2 -> message.text = "Error can not open/create database"
                -3 -> {
                    message.text = "There is already a registered user with the email you have provided"
                    message.setTextColor(ContextCompat.getColor(context, R.color.red))
                }

                else ->  {
                    message.text = "You have been registered successfully"
                    message.setTextColor(ContextCompat.getColor(context, R.color.green))
                }
            }
            return result
        }
        return -1
    }
}