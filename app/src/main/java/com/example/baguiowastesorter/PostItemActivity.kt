package com.example.baguiowastesorter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast

class PostItemActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()  // Firestore instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_item)

        createNotificationChannel(this)

        val postButton: Button = findViewById(R.id.post_button)
        val itemNameInput: EditText = findViewById(R.id.item_name)
        val itemDescriptionSpinner: Spinner = findViewById(R.id.item_description_spinner)
        val itemCategorySpinner: Spinner = findViewById(R.id.item_category_spinner)

        // Set up the spinners
        val descriptionAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.item_descriptions,
            android.R.layout.simple_spinner_item
        )
        descriptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemDescriptionSpinner.adapter = descriptionAdapter

        val categoryAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.item_categories,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemCategorySpinner.adapter = categoryAdapter

        postButton.setOnClickListener {
            val itemName = itemNameInput.text.toString()
            val itemDescription = itemDescriptionSpinner.selectedItem.toString()  // Get selected item
            val itemCategory = itemCategorySpinner.selectedItem.toString()  // Get selected item

            val newItem = Item(itemName, itemDescription, itemCategory)

            // Save the item to Firestore
            saveItemToFirestore(newItem)

            // Start the ItemListActivity to show the posted item
            val intent = Intent(this, ItemListActivity::class.java)
            startActivity(intent)

            showToast("Item posted successfully!")

            // Show a notification after posting the item
            showNotification(this, itemName)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveItemToFirestore(newItem: Item) {
        // Reference to the Firestore collection 'items'
        val itemsCollection = db.collection("items")

        // Create a new document for the item
        val itemMap = hashMapOf(
            "name" to newItem.name,
            "description" to newItem.description,
            "category" to newItem.category,
            "timestamp" to System.currentTimeMillis()
        )

        // Add the item to Firestore
        itemsCollection.add(itemMap)
            .addOnSuccessListener { documentReference ->
                showToast("Item posted to Firestore!")
            }
            .addOnFailureListener { e ->
                showToast("Error posting item: ${e.message}")
            }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "waste_sorter_channel"
            val channelName = "Item Exchange Notifications"
            val descriptionText = "Notifications for new item postings"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(context: Context, itemName: String) {
        val intent = Intent(context, ItemListActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, "waste_sorter_channel")
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("New Item Posted")
            .setContentText("A new item '$itemName' has been posted for exchange.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(1001, builder.build())
        }
    }
}
