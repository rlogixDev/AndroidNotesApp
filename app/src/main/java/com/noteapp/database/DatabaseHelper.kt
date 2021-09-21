package com.noteapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


object NoteDBRepository {
    // Table contents are grouped together in an anonymous object.
    object UserEntry : BaseColumns {
        const val TABLE_NAME = "tblUser"
        const val COLUMN_USERNAME = "UserName"
        const val COLUMN_BIRTHDATE = "BirthDate"
        const val COLUMN_USERID = "UserID"
        const val COLUMN_PASSWORD = "Password"
        const val COLUMN_FIRSTNAME = "FirstName"
        const val COLUMN_MOBILENUMBER = "MobileNumber"
        const val COLUMN_GENDER = "Gender"
        const val COLUMN_EMAILID = "EmailID"
        const val COLUMN_COUNTRY = "Country"
        const val COLUMN_ZIPCODE = "ZipCode"
        const val COLUMN_STATE = "State"
        const val COLUMN_CITY = "City"
        const val COLUMN_GOOGLEID = "GoogleID"
    }
}

val DATABASE_NAME = "Note.db"
val DATABASE_VERSION = 1


class DatabaseHelper
@Inject constructor(
    @ApplicationContext val context: Context,
): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){



    private val SQL_CREATE_ENTRIES_USER =
        "CREATE TABLE ${NoteDBRepository.UserEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${NoteDBRepository.UserEntry.COLUMN_USERNAME} TEXT," +
                "${NoteDBRepository.UserEntry.COLUMN_BIRTHDATE} TEXT," +
                "${NoteDBRepository.UserEntry.COLUMN_USERID} TEXT," +
                "${NoteDBRepository.UserEntry.COLUMN_PASSWORD} TEXT," +
                "${NoteDBRepository.UserEntry.COLUMN_FIRSTNAME} TEXT," +
                "${NoteDBRepository.UserEntry.COLUMN_MOBILENUMBER} TEXT," +
                "${NoteDBRepository.UserEntry.COLUMN_GENDER} TEXT," +
                "${NoteDBRepository.UserEntry.COLUMN_EMAILID} TEXT," +
                "${NoteDBRepository.UserEntry.COLUMN_COUNTRY} TEXT," +
                "${NoteDBRepository.UserEntry.COLUMN_ZIPCODE} TEXT," +
                "${NoteDBRepository.UserEntry.COLUMN_STATE} TEXT," +
                "${NoteDBRepository.UserEntry.COLUMN_CITY} TEXT," +
                "${NoteDBRepository.UserEntry.COLUMN_GOOGLEID} TEXT)"


    private val SQL_DELETE_ENTRIES_USER = "DROP TABLE IF EXISTS ${NoteDBRepository.UserEntry.TABLE_NAME}"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES_USER)
        onCreate(db)
    }
}