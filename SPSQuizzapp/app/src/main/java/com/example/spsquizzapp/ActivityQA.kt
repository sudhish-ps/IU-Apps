package com.example.spsquizzapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spsquizzapp.models.QuizCatalog
import com.example.spsquizzapp.utility.JSONLoader
import com.example.spsquizzapp.utility.SessionHandler
import com.google.android.material.button.MaterialButton
import com.google.android.material.radiobutton.MaterialRadioButton
import android.database.sqlite.SQLiteDatabase

class ActivityQA : AppCompatActivity() {
    private lateinit var qaSubmitButton: MaterialButton
    private lateinit var nextButton: MaterialButton
    private lateinit var prevButton: MaterialButton
    private lateinit var txtViewSalutation: TextView
    private lateinit var txtViewTitle: TextView
    private lateinit var txtViewQuestion: TextView
    private lateinit var radioOption1: MaterialRadioButton
    private lateinit var radioOption2: MaterialRadioButton
    private lateinit var radioOption3: MaterialRadioButton
    private lateinit var radioOption4: MaterialRadioButton
    private lateinit var quizCatalog: QuizCatalog

    private var currentQuestionIndex = 0
    private var totalQuestions = 0
    private var userAnswers = Array<Int>(5){-1}
    private var bundle = Bundle()

    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qa)

        qaSubmitButton = findViewById(R.id.QAsubmit_button)
        nextButton = findViewById((R.id.next_button))
        prevButton = findViewById((R.id.prev_button))

        txtViewSalutation = findViewById(R.id.quiz_salutation)
        txtViewTitle = findViewById(R.id.quiz_title)
        txtViewQuestion = findViewById(R.id.question_text)
        radioOption1 = findViewById(R.id.radioButton1)
        radioOption2 = findViewById(R.id.radioButton2)
        radioOption3 = findViewById(R.id.radioButton3)
        radioOption4 = findViewById(R.id.radioButton4)

        val userSal = txtViewSalutation.text.toString().replace("{USER}",SessionHandler.getUsername())
        txtViewSalutation.text = userSal

        prevButton.visibility = View.GONE
        qaSubmitButton.visibility = View.GONE

        // Load quiz questions from JSON file
        quizCatalog = JSONLoader.loadQuizContentFromAssets(this,"questions.json")
        totalQuestions = quizCatalog.questions.size
        txtViewTitle.text = quizCatalog.title

        // Display the first question
        displayQuestion(currentQuestionIndex)

        nextButton.setOnClickListener {
            // Check if an answer is selected
            if(!isAnswerSelected()){
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Save the user's selection and move to next question
            keepUserSelection()
            currentQuestionIndex++
            displayQuestion(currentQuestionIndex)
            renderUserSelection()
        }

        prevButton.setOnClickListener {
            // Check if an answer is selected
            if(!isAnswerSelected()){
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Save the user's selection and move to previous question
            keepUserSelection()
            currentQuestionIndex--
            displayQuestion(currentQuestionIndex)
            renderUserSelection()
        }

        qaSubmitButton.setOnClickListener {
            // Check if an answer is selected
            if(!isAnswerSelected()){
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Save the user's selection, calculate final score and saves to local storage
            keepUserSelection()
            val userScore = persistUserResult()

            val intent = Intent(this@ActivityQA, ActivityResult::class.java)
            bundle.putString("SCORE", userScore.toString())
            intent.putExtras(bundle)
            startActivity(intent)
        }

    }

    // Function to display the current question and options
    private fun displayQuestion(index: Int) {
        val questiontext = "Question ${index + 1} : ${quizCatalog.questions[index].question}"
        txtViewQuestion.text =  questiontext

        radioOption1.text = quizCatalog.questions[index].options[0]
        radioOption2.text = quizCatalog.questions[index].options[1]
        radioOption3.text = quizCatalog.questions[index].options[2]
        radioOption4.text = quizCatalog.questions[index].options[3]

        qaSubmitButton.visibility = View.GONE
        if ((currentQuestionIndex > 0) && (currentQuestionIndex < 4)){
            prevButton.visibility = View.VISIBLE
            nextButton.visibility = View.VISIBLE
        }
        if(currentQuestionIndex == 0){
            prevButton.visibility = View.GONE
        }
        if(currentQuestionIndex == 4){
            qaSubmitButton.visibility = View.VISIBLE
            nextButton.visibility = View.GONE
        }
    }

    // Function to check if an answer is selected
    private fun isAnswerSelected(): Boolean {
        return (radioOption1.isChecked || radioOption2.isChecked || radioOption3.isChecked || radioOption4.isChecked)
    }

    //Function to identify which answer is selected by the user
    private fun keepUserSelection() {
        if(radioOption1.isChecked) {
            userAnswers[currentQuestionIndex] = 0
        }
        else if(radioOption2.isChecked) {
            userAnswers[currentQuestionIndex] = 1
        }
        else if(radioOption3.isChecked) {
            userAnswers[currentQuestionIndex] = 2
        }
        else if(radioOption4.isChecked) {
            userAnswers[currentQuestionIndex] = 3
        }
        else{
            userAnswers[currentQuestionIndex] = -1
        }
    }

    // Function to handle user navigation between questions and to track the option selected by user
    private fun renderUserSelection(){
        if(userAnswers[currentQuestionIndex] == 0){
            radioOption1.isChecked = true
        }
        else if(userAnswers[currentQuestionIndex] == 1) {
            radioOption2.isChecked = true
        }
        else if(userAnswers[currentQuestionIndex] == 2) {
            radioOption3.isChecked = true
        }
        else if(userAnswers[currentQuestionIndex] == 3) {
            radioOption4.isChecked = true
        }
        else {
            radioOption1.isChecked = false
            radioOption2.isChecked = false
            radioOption3.isChecked = false
            radioOption4.isChecked = false
        }
    }

    // Function to persist the user result to local storage
    private fun persistUserResult(): Int{
        var score = 0
        val uName = SessionHandler.getUsername()

        // get the Long type value of the current system date
        val dateValueInLong: Long = System.currentTimeMillis()

        //calculating the score value
        for(actualResult in quizCatalog.questions){
            if (actualResult.correctAnswerIndex == userAnswers[quizCatalog.questions.indexOf(actualResult)]){
                score += 1
            }
            else{
                val infotext = "Question ${quizCatalog.questions.indexOf(actualResult) + 1} : Failed, for more details refer ${actualResult.reference}"
                bundle.putString("Q${quizCatalog.questions.indexOf(actualResult) + 1}", infotext)
            }
        }
        score *= 20

        // persist the result to local storage
        db = openOrCreateDatabase("quiz_database", MODE_PRIVATE, null)
        val sql = "INSERT INTO quiz_results (userid,name,score) VALUES ('$dateValueInLong','$uName',$score)"
        db.execSQL(sql)
        db.close()

        return score
    }
}


