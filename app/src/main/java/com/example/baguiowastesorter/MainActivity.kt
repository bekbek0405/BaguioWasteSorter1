package com.example.baguiowastesorter

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showAnnouncementDialog()

        val searchButton: Button = findViewById(R.id.search_button)
        val productInput: EditText = findViewById(R.id.product_input)
        val resultText: TextView = findViewById(R.id.result_text)
        val wasteExchangeButton: Button = findViewById(R.id.waste_exchange_button)

        wasteExchangeButton.setOnClickListener {
            val intent = Intent(this, PostItemActivity::class.java)
            startActivity(intent)
        }

        searchButton.setOnClickListener {
            val product = productInput.text.toString().trim().lowercase()
            val disposalMethod = getDisposalMethod(product)
            resultText.text = disposalMethod
        }
    }

    private fun showAnnouncementDialog() {
        val announcement = "Important: There will be a delay in garbage collection today due to the bad weather."

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Announcement")
        builder.setMessage(announcement)

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }


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
