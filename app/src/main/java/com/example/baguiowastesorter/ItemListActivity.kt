package com.example.baguiowastesorter

import android.widget.Toast
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class ItemListActivity : AppCompatActivity() {

    private lateinit var itemListView: ListView
    private lateinit var itemAdapter: ItemAdapter
    private val itemList: ArrayList<Item> = ArrayList()  // Store posted items
    private val db = FirebaseFirestore.getInstance()  // Firestore instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        itemListView = findViewById(R.id.item_list_view)

        // Load items from Firestore
        loadItemListFromFirestore()

        // Set up the custom adapter for the ListView to display items in CardView format
        itemAdapter = ItemAdapter(this, itemList)
        itemListView.adapter = itemAdapter
    }

    private fun loadItemListFromFirestore() {
        val itemsCollection = db.collection("items")  // Reference to the Firestore collection

        // Fetch items from Firestore
        itemsCollection.get()
            .addOnSuccessListener { result ->
                itemList.clear()  // Clear any existing items in the list

                for (document: QueryDocumentSnapshot in result) {
                    // Create an Item object from the document data
                    val item = Item(
                        document.getString("name") ?: "",
                        document.getString("description") ?: "",
                        document.getString("category") ?: ""
                    )

                    // Add the item to the list
                    itemList.add(item)
                }

                // Notify the adapter that data has changed
                itemAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                showToast("Error getting items: ${exception.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

