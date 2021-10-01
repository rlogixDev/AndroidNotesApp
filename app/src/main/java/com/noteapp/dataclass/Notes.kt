package com.noteapp.dataclass


data class Notes(val id: String, val day: String, val title: String, val details: String, val date: String,
                 val imagePath: String,
                 var isSelected: Boolean = false)