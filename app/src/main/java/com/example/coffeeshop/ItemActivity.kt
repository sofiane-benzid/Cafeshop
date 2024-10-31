package com.example.coffeeshop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.model.CartData
import com.example.coffeeshop.model.Item
import com.example.coffeeshop.model.itemDataBase

class ItemActivity : AppCompatActivity() {

    val ItemDataBase = itemDataBase(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val itemTxt: TextView = findViewById(R.id.itemTxt)
        var item: Item = intent.getSerializableExtra("ITEM") as Item
        val itmImg : ImageView = findViewById(R.id.itemImg)
        val descriptionTxt: TextView = findViewById(R.id.descriptionTxt)
        val priceTxt: TextView = findViewById(R.id.priceTxt)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val addToOrderBtn = findViewById<Button>(R.id.addToOrderBtn)
        val reviewsBtn = findViewById<Button>(R.id.reviewsBtn)

        itemTxt.text = item.name
        itmImg.setImageResource(item.image)
        descriptionTxt.text = item.description
        priceTxt.text = "£" + String.format("%.2f", item.price)
        print("hello")
        ratingBar.rating = item.rating.toFloat()

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // Save rating in database or use as needed
            ItemDataBase.updateRating(item, rating.toDouble())
            item.rating = (item.rating * item.numberOfRatings + rating) / (item.numberOfRatings + 1)
            item.numberOfRatings += 1
        }
        addToOrderBtn.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            CartData.itemList.add(item)
            startActivity(intent)
        }
        reviewsBtn.setOnClickListener {
            val intent = Intent(this, ReviewsActivity::class.java)
            intent.putExtra("ITEM_ID",item.id)
            startActivity(intent)
        }
    }
}