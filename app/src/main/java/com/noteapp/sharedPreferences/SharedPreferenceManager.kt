package com.noteapp.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.noteapp.sharedPreferences.SharedPreferenceStorageScope
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ISharedPreferenceManager {
    fun saveString(key: String, value: String)
    fun getString(key: String): String
    fun saveBool(key: String, value: Boolean)
    fun getBool(key: String): Boolean
    fun saveInt(key: String, value: Int)
    fun getInt(key: String): Int
}

class SharedPreferenceManager
@Inject constructor(
    @SharedPreferenceStorageScope
    val sharedPref: SharedPreferences
): ISharedPreferenceManager {

    override fun saveString(key: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.commit()
    }

    override fun getString(key: String): String = sharedPref.getString(key, "")?:""

    override fun saveBool(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    override fun getBool(key: String): Boolean = sharedPref.getBoolean(key, false)?:false

    override fun saveInt(key: String, value: Int) {
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    override fun getInt(key: String): Int = sharedPref.getInt(key, 0)?:0

}