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
import com.example.coffeeshop.model.itemDataBase

class MainPageActivity : AppCompatActivity() {

    val ItemDataBase = itemDataBase(this)

    private var previousButton: Button? = null
    private lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_page)
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
        //BUTTONS
        val hotBtn: Button = findViewById(R.id.hotBtn)
        val coldBtn: Button = findViewById(R.id.coldBtn)
        val foodBtn: Button = findViewById(R.id.foodBtn)
        val CartBtn: ImageButton = findViewById(R.id.CartBtn)

//        val itemsArray: Array<Item> = arrayOf(
//            Item(1, "Cappuccino", R.drawable.cappuccino, "Perfectly extracted espresso capped with luxurious frothy milk, finished with a chocolatey dusting.", 4.5, 3.80, "Hot",1),
//            Item(2, "Flat Black", R.drawable.flat_black, "A short but intense espresso extracted over water, providing a smooth silky bold coffee.", 4.0, 3.30, "Hot",1),
//            Item(3, "Mocha", R.drawable.mocha, "Expertly steamed chocolate milk blended with espresso for a caffeinated chocolate treat.", 4.5, 4.00, "Hot",1),
//            Item(4, "Iced Mocha", R.drawable.iced_mocha, "A smooth and indulgent iced mocha, chilled milk blended with hot chocolate powder and espresso poured over ice for a delicious caffeinated chocolate treat.", 4.5, 4.00, "Cold",1),
//            Item(5, "Iced Black Americano", R.drawable.iced_black_americano, "Classic black coffee served over ice.", 4.0, 3.20, "Cold",1),
//            Item(6, "Coffe Frappe", R.drawable.coffee_frappe, "Have your coffee fix in the form of a creamy, milky, ice-cold frappe, with cream and a sprinkle of chocolate dusting.", 4.5, 4.80, "Cold",1),
//            Item(7, "Lemonade", R.drawable.lemonade, "A refreshing still lemonade over ice", 4.5, 3.60, "Cold",1),
//            Item(8, "Chicken, Bacon & Cheese Sandwich", R.drawable.cbcs, "Roast British Chicken & smoked bacon with cheese on malted bread.", 3.5, 3.95, "Food",1),
//            Item(9, "Egg Mayo Sandwich", R.drawable.eggmayo, "Free range egg with seasoned mayonnaise on soft oatmeal bread.", 4.5, 3.40, "Food",1),
//            Item(10, "Chilli Beef & Cheddar Toast", R.drawable.cbc, "British pulled beef & Korean BBQ sauce with mature Cheddar in a toast.", 5.0, 5.45, "Food",1)
//            )
//        for (item in itemsArray){
//            ItemDataBase.addItem(item)
//        }

        hotBtn.setOnClickListener {
            handleButtonClick(hotBtn)
            val itemNames = handleNames("Hot")
            val itemImages = handleImages("Hot")
            val gridAdapter = GridAdapter(this, itemNames, itemImages)
            gridView.adapter = gridAdapter

            findViewById<GridView>(R.id.gridView).setOnItemClickListener { parent, view, position, id ->
                val items = ItemDataBase.getItemsBasedOnCategory("Hot")
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
                val items = ItemDataBase.getItemsBasedOnCategory("Cold")
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
                val items = ItemDataBase.getItemsBasedOnCategory("Food")
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
        val items = ItemDataBase.getItemsBasedOnCategory(category)
        val mutableItemNames = mutableListOf<String>()
        for (item in items) {
            mutableItemNames.add(item.name)
        }
        val itemNames = mutableItemNames.toTypedArray()
        return itemNames
    }
    private fun handleImages(category: String): IntArray {
        val items = ItemDataBase.getItemsBasedOnCategory(category)
        val mutableItemImages = mutableListOf<Int>()
        for (item in items) {
            mutableItemImages.add(item.image)
        }
        val itemImages = mutableItemImages.toIntArray()
        return itemImages
    }
}