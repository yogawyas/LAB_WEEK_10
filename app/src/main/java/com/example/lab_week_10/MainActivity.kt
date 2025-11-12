package com.example.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    // Create an instance of the TotalDatabase
    private val db by lazy { prepareDatabase() }

    // Create an instance of the TotalViewModel
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the value of the total from the database
        initializeValueFromDatabase()

        // Prepare the ViewModel
        prepareViewModel()
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        // Observe the LiveData object
        viewModel.total.observe(this) {
            updateText(it)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }
    private fun prepareDatabase(): TotalDatabase {
        return TotalDatabase.getDatabase(applicationContext)
    }


    private fun initializeValueFromDatabase() {
        val total = db.totalDao().getTotal(ID)
        if (total.isEmpty()) {
            db.totalDao().insert(
                Total(
                    id = 1,
                    total = TotalObject(value = 0, date = Date().toString())
                )
            )
        } else {
            viewModel.setTotal(total.first().total.value)
        }
    }

    override fun onStart() {
        super.onStart()
        // Show toast containing the last update date
        val total = db.totalDao().getTotal(ID)
        if (total.isNotEmpty()) {
            Toast.makeText(this, "Last Updated: ${total.first().total.date}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        super.onPause()
        // Update the value and date in the database
        db.totalDao().update(
            Total(
                ID,
                TotalObject(
                    value = viewModel.total.value!!,
                    date = Date().toString()
                )
            )
        )
    }

    companion object {
        const val ID: Long = 1
    }
}
