package com.example.coffeeshop.model

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import com.example.coffeeshop.R


object utils {

    fun isStrongPassword(password: String): Boolean {
        return password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$"))
    }
    fun defaultDbItems() : Array<Item> {
        val itemsArray: Array<Item> = arrayOf(
            Item(1, "Cappuccino", R.drawable.cappuccino, "Perfectly extracted espresso capped with luxurious frothy milk, finished with a chocolatey dusting.", 4.5, 3.80, "Hot",1),
            Item(2, "Flat Black", R.drawable.flat_black, "A short but intense espresso extracted over water, providing a smooth silky bold coffee.", 4.0, 3.30, "Hot",1),
            Item(3, "Mocha", R.drawable.mocha, "Expertly steamed chocolate milk blended with espresso for a caffeinated chocolate treat.", 4.5, 4.00, "Hot",1),
            Item(4, "Iced Mocha", R.drawable.iced_mocha, "A smooth and indulgent iced mocha, chilled milk blended with hot chocolate powder and espresso poured over ice for a delicious caffeinated chocolate treat.", 4.5, 4.00, "Cold",1),
            Item(5, "Iced Black Americano", R.drawable.iced_black_americano, "Classic black coffee served over ice.", 4.0, 3.20, "Cold",1),
            Item(6, "Coffe Frappe", R.drawable.coffee_frappe, "Have your coffee fix in the form of a creamy, milky, ice-cold frappe, with cream and a sprinkle of chocolate dusting.", 4.5, 4.80, "Cold",1),
            Item(7, "Lemonade", R.drawable.lemonade, "A refreshing still lemonade over ice", 4.5, 3.60, "Cold",1),
            Item(8, "Chicken, Bacon & Cheese Sandwich", R.drawable.cbcs, "Roast British Chicken & smoked bacon with cheese on malted bread.", 3.5, 3.95, "Food",1),
            Item(9, "Egg Mayo Sandwich", R.drawable.eggmayo, "Free range egg with seasoned mayonnaise on soft oatmeal bread.", 4.5, 3.40, "Food",1),
            Item(10, "Chilli Beef & Cheddar Toast", R.drawable.cbc, "British pulled beef & Korean BBQ sauce with mature Cheddar in a toast.", 5.0, 5.45, "Food",1)
            )
        return itemsArray
    }

}