package com.example.twicetwo.data.local

import android.content.Context
import com.example.twicetwo.data.model.UserDto

object SessionManager {

    private const val PREF_NAME = "twice_session"
    private const val KEY_USER_ID = "user_id"

    var currentUser: UserDto? = null

    fun login(context: Context, user: UserDto) {
        currentUser = user

        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_USER_ID, user.id)
            .apply()
    }

    fun logout(context: Context) {
        currentUser = null

        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    fun restore(context: Context, users: List<UserDto>) {
        val id = getSavedUserId(context)
        println("CURRENT USER = ${SessionManager.currentUser}")
        println("CURRENT ID = ${SessionManager.currentUser?.id}")
        currentUser = users.find { it.id == id }
    }

    private fun getSavedUserId(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_ID, null)
    }
}