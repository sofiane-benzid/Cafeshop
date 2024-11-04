package com.example.coffeeshop

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.model.Item
import com.example.coffeeshop.model.ReviewAdapter
import com.example.coffeeshop.model.ReviewsDataBase
import com.example.coffeeshop.model.itemDatabase

class ReviewsActivity : AppCompatActivity() {

    val ItemDataBase = itemDatabase(this)
    val ReviewsDataBase = ReviewsDataBase(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)
        // variables
        val itemID = intent.getIntExtra("ITEM_ID", 0)
        val item: Item = ItemDataBase.getItemById(itemID)
        val reviewItemTxt = findViewById<TextView>(R.id.reviewItemTxt)
        val ratingBar2 = findViewById<RatingBar>(R.id.ratingBar2)
        val reviewListView = findViewById<ListView>(R.id.reviewListView)
        val addReviewButton = findViewById<Button>(R.id.addReviewButton)
        val reviewInput = findViewById<EditText>(R.id.reviewInput)
        val reviews = ReviewsDataBase.getReviewsForItem(itemID)
        val reviewAdapter = ReviewAdapter(this, reviews)

        reviewItemTxt.text = "${item.name} Reviews"
        ratingBar2.rating = item.rating.toFloat()
        reviewListView.adapter = reviewAdapter
        addReviewButton.setOnClickListener {
            val newReviewText = reviewInput.text.toString().trim()
            if (newReviewText.isNotEmpty()) {
                // Add the new review to the database and update the list
                ReviewsDataBase.addReview(itemID, newReviewText)
                reviews.add(newReviewText)
                reviewAdapter.notifyDataSetChanged()
                reviewInput.text.clear()  // Clear the input box
            } else {
                Toast.makeText(this, "Please enter a review.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}