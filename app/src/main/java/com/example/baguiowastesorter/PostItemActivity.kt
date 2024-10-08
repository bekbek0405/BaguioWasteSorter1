package com.example.baguiowastesorter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class PostItemActivity : AppCompatActivity() {

    private val itemList = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_item)

        val itemName: EditText = findViewById(R.id.item_name)
        val itemDescription: EditText = findViewById(R.id.item_description)
        val itemCategory: EditText = findViewById(R.id.item_category)
        val postButton: Button = findViewById(R.id.post_button)

        postButton.setOnClickListener {
            val name = itemName.text.toString().trim()
            val description = itemDescription.text.toString().trim()
            val category = itemCategory.text.toString().trim()

            if (name.isNotEmpty() && description.isNotEmpty() && category.isNotEmpty()) {
                val item = Item(name, description, category)
                itemList.add(item)

                val intent = Intent(this, ItemListActivity::class.java)
                intent.putParcelableArrayListExtra("items", ArrayList(itemList)) // Use ArrayList to store items
                startActivity(intent)
            }
        }
    }
}
