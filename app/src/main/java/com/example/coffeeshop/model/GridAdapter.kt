package com.example.coffeeshop.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.coffeeshop.R

class GridAdapter(context: Context, private val itemName: Array<String>, private val images: IntArray) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return itemName.size
    }

    override fun getItem(position: Int): Any {
        return itemName[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflater.inflate(R.layout.grid_item, parent, false)
        val imageView: ImageView = view.findViewById(R.id.grid_image)
        val textView: TextView = view.findViewById(R.id.item_name)
        imageView.setImageResource(images[position])
        textView.text = itemName[position]

        return view
    }


}