package com.noteapp.usermanager

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.noteapp.User
import com.noteapp.database.NoteDBRepository
import com.noteapp.database.di.NoteDBScope
import javax.inject.Inject


interface IUserManager {
    fun addUser(employee: User)
    fun getUserList(): ArrayList<User>
}

class UserManager
@Inject constructor(
    @NoteDBScope
    val db: SQLiteDatabase,
    val contentResolver: ContentResolver
) : IUserManager
{

    override fun addUser(employee: User) {
        val values = ContentValues().apply {
            put(NoteDBRepository.UserEntry.COLUMN_USERNAME, employee.userName)
            put(NoteDBRepository.UserEntry.COLUMN_BIRTHDATE, employee.birthDate)
            put(NoteDBRepository.UserEntry.COLUMN_USERID, employee.userid)
            put(NoteDBRepository.UserEntry.COLUMN_PASSWORD, employee.password)
            put(NoteDBRepository.UserEntry.COLUMN_FIRSTNAME, employee.firstName)
            put(NoteDBRepository.UserEntry.COLUMN_MOBILENUMBER, employee.mobileNumber)
            put(NoteDBRepository.UserEntry.COLUMN_GENDER, employee.gender)
            put(NoteDBRepository.UserEntry.COLUMN_EMAILID, employee.emailID)
            put(NoteDBRepository.UserEntry.COLUMN_COUNTRY, employee.country)
            put(NoteDBRepository.UserEntry.COLUMN_ZIPCODE, employee.zipcode)
            put(NoteDBRepository.UserEntry.COLUMN_STATE, employee.state)
            put(NoteDBRepository.UserEntry.COLUMN_CITY, employee.city)
            put(NoteDBRepository.UserEntry.COLUMN_GOOGLEID, employee.googleID)
        }
        db.insert(NoteDBRepository.UserEntry.TABLE_NAME, null, values)
    }

    @SuppressLint("Range")
    override fun getUserList(): ArrayList<User> {
        val projection = arrayOf(
            BaseColumns._ID,
            NoteDBRepository.UserEntry.COLUMN_USERNAME,
            NoteDBRepository.UserEntry.COLUMN_BIRTHDATE,
            NoteDBRepository.UserEntry.COLUMN_USERID,
            NoteDBRepository.UserEntry.COLUMN_PASSWORD,
            NoteDBRepository.UserEntry.COLUMN_FIRSTNAME,
            NoteDBRepository.UserEntry.COLUMN_MOBILENUMBER,
            NoteDBRepository.UserEntry.COLUMN_GENDER,
            NoteDBRepository.UserEntry.COLUMN_EMAILID,
            NoteDBRepository.UserEntry.COLUMN_COUNTRY,
            NoteDBRepository.UserEntry.COLUMN_ZIPCODE,
            NoteDBRepository.UserEntry.COLUMN_STATE,
            NoteDBRepository.UserEntry.COLUMN_CITY,
            NoteDBRepository.UserEntry.COLUMN_GOOGLEID)
        val selection = "${BaseColumns._ID} = ?"
        val orderBY = "${BaseColumns._ID} DESC"
        val selectionArgs = arrayOf("2", "3")

        val list: ArrayList<User> = arrayListOf()

        val cursor = db.query(
            NoteDBRepository.UserEntry.TABLE_NAME,
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            orderBY               // The sort order
        )

        cursor?.let { cursor ->

            while (cursor.moveToNext()) {
                val user =
                    User(
                        cursor?.getInt(cursor.getColumnIndex(BaseColumns._ID)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_USERNAME)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_BIRTHDATE)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_USERID)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_PASSWORD)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_FIRSTNAME)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_MOBILENUMBER)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_GENDER)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_EMAILID)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_COUNTRY)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_ZIPCODE)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_STATE)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_CITY)),
                        cursor?.getString(cursor?.getColumnIndex(NoteDBRepository.UserEntry.COLUMN_GOOGLEID)))

                list.add(user)
            }
        }

        return list
    }

}