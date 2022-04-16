package com.ahmadfma.intermediate_submission1.data.local

import android.content.Context
import android.util.Log
import com.ahmadfma.intermediate_submission1.data.model.UserData

class UserPreferences(context: Context) {

    companion object {
        private const val USER_PREF = "user_pref"
        private const val USER_TOKEN = "user token"
        private const val USER_ID = "user id"
        private const val USER_NAME = "user name"
        private const val TAG = "UserPreferences"
        var user = UserData()
    }

    private val preferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)

    fun setUser(data: UserData) {
        val editor = preferences.edit()
        editor.putString(USER_TOKEN, data.token)
        editor.putString(USER_ID, data.userId)
        editor.putString(USER_NAME, data.name)
        editor.apply()
        user = data
        Log.d(TAG, "setUser: $data")
    }

    fun getUser(): UserData {
        val data = UserData()
        data.name = preferences.getString(USER_NAME, "")
        data.token = preferences.getString(USER_TOKEN, "")
        data.userId = preferences.getString(USER_ID, "")
        user = data
        Log.d(TAG, "getUser: $data")
        return data
    }

}