package com.example.baguiowastesorter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ItemAdapter(context: Context, private val items: List<Item>) :
    ArrayAdapter<Item>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Use convertView to improve performance
        val listItemView = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.card_item, // Inflate the CardView layout
            parent,
            false
        )

        // Get the current item
        val currentItem = getItem(position)

        // Find the TextViews in the card_item layout
        val itemNameTextView: TextView = listItemView.findViewById(R.id.item_name)
        val itemDescriptionTextView: TextView = listItemView.findViewById(R.id.item_description)
        val itemCategoryTextView: TextView = listItemView.findViewById(R.id.item_category)

        // Set the data into the TextViews
        itemNameTextView.text = currentItem?.name
        itemDescriptionTextView.text = currentItem?.description
        itemCategoryTextView.text = currentItem?.category

        // Return the completed view to render on screen
        return listItemView
    }
}
