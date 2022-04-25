package com.ahmadfma.intermediate_submission1.data.local

import android.content.Context
import androidx.core.content.edit
import com.ahmadfma.intermediate_submission1.data.model.UserData

class UserPreferences(context: Context) {

    companion object {
        private const val USER_PREF = "user_pref"
        private const val USER_TOKEN = "user token"
        private const val USER_NAME = "user name"
        var user = UserData()
    }

    private val preferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)

    fun setUser(data: UserData) {
        preferences.edit {
            putString(USER_TOKEN, data.token)
            putString(USER_NAME, data.name)
            apply()
        }
        user = data
    }

    fun getUser(): UserData {
        val data = UserData()
        data.name = preferences.getString(USER_NAME, "")
        data.token = preferences.getString(USER_TOKEN, "")
        user = data
        return data
    }

}