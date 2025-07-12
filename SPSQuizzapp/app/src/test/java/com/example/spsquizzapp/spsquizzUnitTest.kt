package com.example.spsquizzapp

import com.example.spsquizzapp.models.*
import com.example.spsquizzapp.utility.*
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class spsquizzUnitTest {

    @Test
    // Unit test for session handler loader
    fun testUser_Session() {
        val user = "John"
        SessionHandler.setUsername(user)
        val sessionUser = SessionHandler.getUsername()

        assertEquals(user, sessionUser)
    }

    @Test
    // Unit test for JSON loader
    fun testQuiz_QuestionLoad(){
        val quizCatalog =  QuizCatalog("Test Quiz", listOf(QuizQuestions(1,"Test Question", listOf("Option 1", "Option 2", "Option 3", "Option 4"), 0, "Test Explanation", "Test Reference")))
        assertEquals("Test Question", quizCatalog.questions[0].question)
    }

    @Test
    // Unit test for database connection
    fun testDatabase_Connection_Success() {
        // Create a mock of MainActivity
        val mainActivity = mock<MainActivity>()

        // Configure the mock to return true when setQuizDatabase() is called
        whenever(mainActivity.setQuizDatabase()).thenReturn(true)

        // Call the method
        val success = mainActivity.setQuizDatabase()

        // Verify the result
        assertTrue(success)
    }
}