package com.example.spsquizzapp

import android.R.attr
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.spsquizzapp.utility.SessionHandler
import com.google.android.material.button.MaterialButton


class ActivityResult : AppCompatActivity() {
    private lateinit var closeButton: MaterialButton
    private lateinit var txtViewSalutation: TextView
    private lateinit var txtViewScore: TextView
    private lateinit var txtResult: TextView
    private lateinit var layoutResultTable: TableLayout
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        closeButton = findViewById((R.id.close_button))
        txtViewSalutation = findViewById(R.id.quiz_salutation)
        txtViewScore = findViewById(R.id.results_score)
        txtResult = findViewById(R.id.results_title)
        layoutResultTable = findViewById(R.id.results_list)

        val userSal = txtViewSalutation.text.toString().replace("{USER}", SessionHandler.getUsername())
        txtViewSalutation.text = userSal

        // Get the result details from the intent
        val resultDetails = StringBuilder()
        val bundle = intent.extras
        for(keys in bundle!!.keySet()){
            if(keys == "SCORE") {
                val str = txtResult.text.toString().replace("{SCORE}",bundle.getString(keys).toString()).replace("{USER}", SessionHandler.getUsername())
                txtResult.text = str
                continue
            }
            val keyValue = bundle.getString(keys)
            resultDetails.append(keyValue)
            resultDetails.append("\n")
        }
        // Display the result details
        txtViewScore.text = resultDetails.toString()

        // Fill the high score list in the table
        fillHighScoreList()

        closeButton.setOnClickListener {
            val intent = Intent( applicationContext,  MainActivity::class.java   )
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("EXIT", true)
            startActivity(intent)
        }

    }
    // Function to fill the high score list in the table view from the local storage
    private fun fillHighScoreList() {
        // get the list of users those having the highest score
        db = openOrCreateDatabase("quiz_database", MODE_PRIVATE, null)
        val sql = "SELECT * FROM quiz_results ORDER BY score DESC"
        val cursor = db.rawQuery(sql, null)

        // display the result in the table view by binding each column values
        for(i in 0 until cursor.count){
            cursor.moveToNext()
            val rank = i+1
            val name = cursor.getString(1)
            val score = cursor.getInt(2)

            val row = TableRow(this)
            row.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
            row.setPadding(16   , 16, 16, 16)
            row.setBackgroundColor(ContextCompat.getColor(this,R.color.secondary))

            val rankView = TextView(this)
            rankView.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f)
            rankView.text = rank.toString()
            rankView.gravity = 1
            rankView.setPadding(5,5,5,5)
            row.addView(rankView)

            val nameView = TextView(this)
            nameView.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f)
            nameView.text = name
            nameView.gravity=1
            nameView.setPadding(5,5,5,5)
            row.addView(nameView)

            val scoreView = TextView(this)
            scoreView.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f)
            scoreView.text = score.toString()
            scoreView.gravity=1
            scoreView.setPadding(5,5,5,5)
            row.addView(scoreView)

            layoutResultTable.addView(row)

        }
        cursor.close()
        db.close()
    }


}
