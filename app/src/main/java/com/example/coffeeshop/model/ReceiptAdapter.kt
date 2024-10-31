package com.example.coffeeshop.model
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.coffeeshop.R

class ReceiptAdapter(context: Context, items: List<Item>) : ArrayAdapter<Item>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)

        val listItemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.receipt_item_row, parent, false)

        val itemNameTextView: TextView = listItemView.findViewById(R.id.itemNameTextView)
        val itemPriceTextView: TextView = listItemView.findViewById(R.id.itemPriceTextView)

        itemNameTextView.text = item?.name
        itemPriceTextView.text = "£${String.format("%.2f", item?.price)}"

        return listItemView
    }
}
