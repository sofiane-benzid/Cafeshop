package com.example.coffeeshop.model

import java.io.Serializable

data class User (val id: Int, val firstName: String, val lastName: String, val email: String, val password: String, val dateOfBirth: String) : Serializable{
}