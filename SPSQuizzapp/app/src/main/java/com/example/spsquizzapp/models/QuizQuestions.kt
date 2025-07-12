package com.example.spsquizzapp.models

data class QuizQuestions(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val reference: String
)
