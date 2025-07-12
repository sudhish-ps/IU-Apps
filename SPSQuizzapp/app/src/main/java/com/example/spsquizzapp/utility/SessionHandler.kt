package com.example.spsquizzapp.utility

/* Object too handle user session by keeping the logged in user name/email */
object SessionHandler {

    /* To keep current logged in username */
    private var _currentUsername: String = ""

    /* Get the current logged in username */
    fun getUsername(): String {
        return _currentUsername
    }

    /* Set the current username*/
    fun setUsername(username: String) {
        _currentUsername = username
    }

    /* Clear the current session */
    fun clearSession() {
        _currentUsername = ""
    }
}