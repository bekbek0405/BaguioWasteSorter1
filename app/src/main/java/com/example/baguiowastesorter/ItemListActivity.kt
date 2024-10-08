package com.example.baguiowastesorter

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ItemListActivity : AppCompatActivity() {

    private lateinit var itemListView: ListView
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        itemListView = findViewById(R.id.item_list_view)

        // Retrieve the Parcelable list from the intent
        val itemList = intent.getParcelableArrayListExtra<Item>("items") ?: arrayListOf()

        // Check if the item list is empty
        if (itemList.isEmpty()) {
            findViewById<TextView>(R.id.empty_view).visibility = View.VISIBLE // Show empty view
            itemListView.visibility = View.GONE // Hide the ListView if empty
        } else {
            itemAdapter = ItemAdapter(this, itemList)
            itemListView.adapter = itemAdapter
            findViewById<TextView>(R.id.empty_view).visibility = View.GONE // Hide empty view
        }
    }
}
