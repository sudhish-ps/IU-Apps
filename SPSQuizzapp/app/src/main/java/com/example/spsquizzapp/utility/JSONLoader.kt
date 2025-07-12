package com.example.spsquizzapp.utility

import android.content.Context
import org.json.JSONObject
import com.example.spsquizzapp.models.QuizCatalog
import com.example.spsquizzapp.models.QuizQuestions
import java.io.IOException


/* A class to load the quiz from the JSON file */
object JSONLoader {
    fun loadQuizContentFromAssets(context: Context, fileName: String): QuizCatalog {
        try {
            // Read the JSON file from assets
            val jsonString = context.assets.open(fileName)
                .bufferedReader()
                .use { it.readText() }

            // Parse the JSON data
            val jsonObject = JSONObject(jsonString)
            val title = jsonObject.getString("title")

            // Extract questions from the JSON
            val questionsArray = jsonObject.getJSONArray("questions")
            val questionsList = mutableListOf<QuizQuestions>()

            // Iterate through the questions and create QuizQuestions objects
            for (i in 0 until questionsArray.length()) {
                val questionObj = questionsArray.getJSONObject(i)

                val id = questionObj.getInt("id")
                val questionText = questionObj.getString("question")

                val optionsArray = questionObj.getJSONArray("options")
                val options = mutableListOf<String>()

                for (j in 0 until optionsArray.length()) {
                    options.add(optionsArray.getString(j))
                }

                val correctAnswerIndex = questionObj.getInt("correctAnswerIndex")
                val explanation = questionObj.getString("explanation")
                val reference = questionObj.getString("reference")

                questionsList.add(QuizQuestions(
                    id = id,
                    question = questionText,
                    options = options,
                    correctAnswerIndex = correctAnswerIndex,
                    explanation = explanation,
                    reference = reference
                ))
            }

            // Create and return the QuizCatalog object
            return QuizCatalog(
                title = title,
                questions = questionsList
            )

        } catch (e: IOException) {
            e.printStackTrace()
            // Return an empty quiz if there's an error
            return QuizCatalog("Error", emptyList())
        }
    }
}
