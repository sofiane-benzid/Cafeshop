package com.example.coffeeshop

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.coffeeshop.model.User
import com.example.coffeeshop.model.userDataBase

class LoginActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: Button = findViewById(R.id.loginButton2)

        loginButton.setOnClickListener {
            var result = login(this)
            if (result != -1 && result != -2) {
                val email = findViewById<EditText>(R.id.email2).text.toString()
                val myDataBase = userDataBase(this)
                val user = myDataBase.getUserByEmail(email)
                var intent: Intent = Intent(this, MainPageActivity::class.java)
                intent.putExtra("USER_KEY", user)
                startActivity(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun login(context: Context): Int {
        val message = findViewById<TextView>(R.id.textViewMessage2)
        val email = findViewById<EditText>(R.id.email2).text.toString()
        val password = findViewById<EditText>(R.id.password2).text.toString()

        if (email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Please insert Username and Password", Toast.LENGTH_LONG).show()
        else {
            val myDataBase = userDataBase(this)
            val result = myDataBase.verifyUser(User(-1, " ", " ", email, password, " "))
            if (result == -1) {
                message.text = "Wrong credentials, Please try again"
                message.setTextColor(ContextCompat.getColor(context, R.color.red))
                return result
            } else if (result == -2) {
                message.text = "Error Cannot Open/Create DataBase"
                return result
            } else {
                message.text = "You logged in successfully"
                message.setTextColor(ContextCompat.getColor(context, R.color.green))
                return result
            }
        }
        return -1
    }
}