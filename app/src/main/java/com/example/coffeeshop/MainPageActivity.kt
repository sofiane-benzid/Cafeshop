package com.example.coffeeshop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.coffeeshop.model.GridAdapter
import com.example.coffeeshop.model.User
import com.example.coffeeshop.model.itemDatabase

class MainPageActivity : AppCompatActivity() {

    val ItemDataBase = itemDatabase(this)

    private var previousButton: Button? = null
    private lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_page)
        //variables
        val hotBtn: Button = findViewById(R.id.hotBtn)
        val coldBtn: Button = findViewById(R.id.coldBtn)
        val foodBtn: Button = findViewById(R.id.foodBtn)
        val CartBtn: ImageButton = findViewById(R.id.CartBtn)
        // GRID STUFF
        gridView = findViewById(R.id.gridView)
        // SESSION + WELCOME
        val check = intent.getIntExtra("USER",0)
        val welcomeUserTxt: TextView = findViewById(R.id.CartTxt)
        if (check==0) {
            val user = intent.getSerializableExtra("USER_KEY") as User
            welcomeUserTxt.text = "Hi " + user.firstName + "!"
        } else {
            welcomeUserTxt.text = "Home"
        }

        hotBtn.setOnClickListener {
            handleButtonClick(hotBtn)
            val itemNames = handleNames("Hot")
            val itemImages = handleImages("Hot")
            val gridAdapter = GridAdapter(this, itemNames, itemImages)
            gridView.adapter = gridAdapter
            findViewById<GridView>(R.id.gridView).setOnItemClickListener { parent, view, position, id ->
                val items = ItemDataBase.getItemsByCategory("Hot")
                val intent = Intent(this, ItemActivity::class.java)
                intent.putExtra("ITEM", items[position])
                startActivity(intent)
            }
        }
        coldBtn.setOnClickListener {
            handleButtonClick(coldBtn)
            val itemNames = handleNames("Cold")
            val itemImages = handleImages("Cold")
            val gridAdapter = GridAdapter(this, itemNames, itemImages)
            gridView.adapter = gridAdapter
            findViewById<GridView>(R.id.gridView).setOnItemClickListener { parent, view, position, id ->
                val items = ItemDataBase.getItemsByCategory("Cold")
                val intent = Intent(this, ItemActivity::class.java)
                intent.putExtra("ITEM", items[position])
                startActivity(intent)
            }
        }
        foodBtn.setOnClickListener {
            handleButtonClick(foodBtn)
            val itemNames = handleNames("Food")
            val itemImages = handleImages("Food")
            val gridAdapter = GridAdapter(this, itemNames, itemImages)
            gridView.adapter = gridAdapter
            findViewById<GridView>(R.id.gridView).setOnItemClickListener { parent, view, position, id ->
                val items = ItemDataBase.getItemsByCategory("Food")
                val intent = Intent(this, ItemActivity::class.java)
                intent.putExtra("ITEM", items[position])
                startActivity(intent)
            }
        }
        CartBtn.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("ITEMS", ItemDataBase.getAllItems())
            startActivity(intent)
        }
        hotBtn.isSelected = true
        hotBtn.performClick()

    }
    private fun handleButtonClick(clickedButton: Button) {
        previousButton?.setTextColor(ContextCompat.getColor(this, R.color.gray))
        clickedButton.setTextColor(ContextCompat.getColor(this, R.color.brown))
        previousButton = clickedButton
    }
    private fun handleNames(category: String): Array<String> {
        val items = ItemDataBase.getItemsByCategory(category)
        val mutableItemNames = mutableListOf<String>()
        for (item in items) {
            mutableItemNames.add(item.name)
        }
        val itemNames = mutableItemNames.toTypedArray()
        return itemNames
    }
    private fun handleImages(category: String): IntArray {
        val items = ItemDataBase.getItemsByCategory(category)
        val mutableItemImages = mutableListOf<Int>()
        for (item in items) {
            mutableItemImages.add(item.image)
        }
        val itemImages = mutableItemImages.toIntArray()
        return itemImages
    }
}