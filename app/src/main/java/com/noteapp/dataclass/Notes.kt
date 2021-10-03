package com.noteapp.dataclass


data class Notes(public val id: String, public val title: String, val details: String, val date: String,
                 val imagePath: String,
                 var isSelected: Boolean = false)