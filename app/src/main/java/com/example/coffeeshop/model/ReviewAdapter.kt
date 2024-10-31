package com.example.coffeeshop.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.coffeeshop.R

class ReviewAdapter (context: Context, private val reviews: List<String>) : ArrayAdapter<String>(context, 0, reviews){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.review_item, parent, false)
        val reviewText: TextView = view.findViewById(R.id.reviewText)
        reviewText.text = reviews[position]
        return view
    }

}