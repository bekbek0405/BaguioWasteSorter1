package com.example.baguiowastesorter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showAnnouncementDialog()


        val wasteExchangeButton: Button = findViewById(R.id.waste_exchange_button)
        val scanWasteItemButton: Button = findViewById(R.id.scan_button)
        val mapsButton: Button = findViewById(R.id.maps_button)

        // Set click listener to launch MapsActivity
        mapsButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        wasteExchangeButton.setOnClickListener {
            val intent = Intent(this, PostItemActivity::class.java)
            startActivity(intent)
        }

        scanWasteItemButton.setOnClickListener {
            val intent = Intent(this, Camera::class.java)
            startActivity(intent)
        }
    }




    private fun showAnnouncementDialog() {
        val announcement = "Important: There will be a delay in garbage collection today due to the bad weather."

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Announcement")
        builder.setMessage(announcement)

        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun getDisposalMethod(product: String): String {
        val recyclables = listOf("plastic bottle", "paper", "aluminum can", "glass bottle", "cardboard")
        val compostables = listOf("food waste", "fruit peel", "vegetable waste", "eggshells", "coffee grounds")
        val residual = listOf("diaper", "styrofoam", "ceramics", "broken glass")
        val eWaste = listOf("phone", "laptop", "battery", "television", "printer")

        return when {
            recyclables.contains(product) -> "This item is recyclable."
            compostables.contains(product) -> "This item is compostable."
            residual.contains(product) -> "This item belongs to residual waste."
            eWaste.contains(product) -> "This item requires special handling (e-waste)."
            else -> "Item not found. Please check with local waste management policies."
        }
    }
}
