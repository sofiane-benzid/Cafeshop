// ItemAdapter.kt
package com.example.coffeeshop.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.coffeeshop.R

class ItemAdapter(
    context: Context, private val items: MutableList<Item>, private val updateTotalPrice: () -> Unit
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: inflater.inflate(R.layout.item_list_row, parent, false)
        val itemNameTextView: TextView = view.findViewById(R.id.itemNameTextView)
        val itemPriceTextView: TextView = view.findViewById(R.id.itemPriceTextView)
        val itemImageView: ImageView = view.findViewById(R.id.itemImageView)

        val item = items[position]
        itemNameTextView.text = item.name
        itemPriceTextView.text = String.format("$%.2f", item.price)
        itemImageView.setImageResource(item.image)

        val deleteButton = view.findViewById<ImageButton>(R.id.deleteBtn)
        deleteButton.setOnClickListener {
            // Remove item from cartItems
            items.removeAt(position)
            // Notify the adapter that the data set has changed
            notifyDataSetChanged()
            updateTotalPrice()
        }

        return view
    }
}
