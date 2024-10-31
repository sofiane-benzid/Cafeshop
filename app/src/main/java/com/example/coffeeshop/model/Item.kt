package com.example.coffeeshop.model

import java.io.Serializable

data class Item (val id: Int, val name: String, val image: Int, val description: String, var rating: Double, val price: Double, val category: String, var numberOfRatings: Int) : Serializable