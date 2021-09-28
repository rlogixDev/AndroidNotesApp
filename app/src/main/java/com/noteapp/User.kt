package com.noteapp

import dagger.hilt.android.AndroidEntryPoint


data class User(val id: Int,
                val userName: String,
                val birthDate: String,
                val userid: String,
                val password: String,
                val firstName: String,
                val mobileNumber: String,
                val gender: String,
                val emailID: String,
                val country: String,
                val zipcode: String,
                val state: String,
                val city: String,
                val googleID: String )
