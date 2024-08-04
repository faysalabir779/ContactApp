package com.example.contactapp.data.sharedPreference

import android.content.Context
import android.content.SharedPreferences

class Permission(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    companion object{
        private const val FIRST_LAUNCH = "first launch"
    }

    fun isFirstLaunch():Boolean{
        return preferences.getBoolean(FIRST_LAUNCH, true)
    }

    fun setFirstLaunch(isFirstLaunch: Boolean){
        preferences.edit().putBoolean(FIRST_LAUNCH, isFirstLaunch).apply()
    }
}