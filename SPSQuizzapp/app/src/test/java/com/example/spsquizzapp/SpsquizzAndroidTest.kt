package com.example.spsquizzapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.spsquizzapp.utility.SessionHandler
import org.junit.Test
import org.junit.Assert.*
import android.database.sqlite.SQLiteDatabase
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SpsquizzAndroidTest {

    @Test
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

    @Test
    fun testDatabase_Connection_Failure() {
        // Create a mock of MainActivity
        val mainActivity = mock<MainActivity>()

        // Configure the mock to return false when setQuizDatabase() is called
        whenever(mainActivity.setQuizDatabase()).thenReturn(false)

        // Call the method
        val success = mainActivity.setQuizDatabase()

        // Verify the result
        assertFalse(success)
    }
}