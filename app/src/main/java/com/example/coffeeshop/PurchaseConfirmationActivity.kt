package com.example.coffeeshop

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.model.CartData
import com.example.coffeeshop.model.ReceiptAdapter

class PurchaseConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_confirmation)
        // variables
        val receiptListView: ListView = findViewById(R.id.receiptListView)
        val totalPriceTextView: TextView = findViewById(R.id.totalPriceTextView)
        val itemAdapter = ReceiptAdapter(this, CartData.itemList)

        receiptListView.adapter = itemAdapter

        // Calculate total price
        var totalPrice = 0.0
        for (item in CartData.itemList) {
            totalPrice += item.price
        }

        val formattedTotal = String.format("%.2f", totalPrice)
        totalPriceTextView.text = "Total: £$formattedTotal"
    }
}
