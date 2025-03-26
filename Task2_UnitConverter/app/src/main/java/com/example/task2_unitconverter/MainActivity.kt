package com.example.task2_unitconverter

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val units = listOf(
        "Inch", "Foot", "Yard", "Mile", "Centimeter", "Kilometer",
        "Pound", "Ounce", "Ton", "Gram", "Kilogram",
        "Celsius", "Fahrenheit", "Kelvin"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val inputValue = findViewById<EditText>(R.id.inputValue)
        val sourceSpinner = findViewById<Spinner>(R.id.sourceUnit)
        val targetSpinner = findViewById<Spinner>(R.id.targetUnit)
        val convertButton = findViewById<Button>(R.id.convertButton)
        val resultView = findViewById<TextView>(R.id.resultView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, units)
        sourceSpinner.adapter = adapter
        targetSpinner.adapter = adapter

        convertButton.setOnClickListener {
            val value = inputValue.text.toString().toDoubleOrNull()
            val fromUnit = sourceSpinner.selectedItem.toString()
            val toUnit = targetSpinner.selectedItem.toString()

            if (value == null) {
                Toast.makeText(this, "Please type in a valid value", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = convertUnits(value, fromUnit, toUnit)
            resultView.text = result?.let { "$it" } ?: "Unable to convert this unit. Please ensure that the two units are interchangeable."
        }
    }

    private fun convertUnits(value: Double, from: String, to: String): Double? {
        val lengthMap = mapOf(
            "Inch" to 2.54, "Foot" to 30.48, "Yard" to 91.44,
            "Mile" to 160934.0, "Centimeter" to 1.0, "Kilometer" to 100000.0
        )

        val weightMap = mapOf(
            "Pound" to 453.592, "Ounce" to 28.3495, "Ton" to 907185.0,
            "Gram" to 1.0, "Kilogram" to 1000.0
        )

        return when {
            lengthMap.containsKey(from) && lengthMap.containsKey(to) -> {
                val cm = value * lengthMap[from]!!
                cm / lengthMap[to]!!
            }
            weightMap.containsKey(from) && weightMap.containsKey(to) -> {
                val grams = value * weightMap[from]!!
                grams / weightMap[to]!!
            }
            from == "Celsius" && to == "Fahrenheit" -> (value * 1.8) + 32
            from == "Fahrenheit" && to == "Celsius" -> (value - 32) / 1.8
            from == "Celsius" && to == "Kelvin" -> value + 273.15
            from == "Kelvin" && to == "Celsius" -> value - 273.15
            else -> null
        }
    }
}
