package com.example.spsquizzapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spsquizzapp.utility.SessionHandler
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import android.database.sqlite.SQLiteDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var usernameInput: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        usernameInput = findViewById(R.id.username_input)
        loginButton = findViewById(R.id.login_button)

        /* Check if user is already logged in
        * then create user session as authenticated else show login screen with message */
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            if (username.isNotBlank()) {
                //create user session
                SessionHandler.setUsername(username)

                // create local database to save the quiz results
                setQuizDatabase()

                // Navigate to quiz screen
                val intent = Intent(this@MainActivity, ActivityQA::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to create local database to table if not available  to save the quiz results
    fun setQuizDatabase(): Boolean{
        try{
            db = openOrCreateDatabase("quiz_database", MODE_PRIVATE, null)
            db.execSQL("CREATE TABLE IF NOT EXISTS quiz_results (userid varchar(20),name VARCHAR(200), score INT )")
            db.close()
            return true
        }
        catch (e: Exception){
            return false
        }
    }

}