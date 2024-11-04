package com.example.coffeeshop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.model.Item
import com.example.coffeeshop.model.itemDatabase
import com.example.coffeeshop.model.utils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // variables
        val registerButton: Button = findViewById(R.id.registerButton)
        val loginButton: Button = findViewById(R.id.loginBtn)

        registerButton.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }


        loginButton.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}