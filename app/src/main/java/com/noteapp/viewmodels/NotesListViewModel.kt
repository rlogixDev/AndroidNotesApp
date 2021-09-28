package com.noteapp.viewmodels

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noteapp.dataclass.Notes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
public class NotesListViewModel
@Inject constructor(
): ViewModel() {


    private val _mStateFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    val mStateFlow: StateFlow<Int> = _mStateFlow

    val mFlow: Flow<Int> = mStateFlow
        .filter { it % 2 == 0 }
        .map { it }

    val notesListFlow: MutableStateFlow<ArrayList<Notes>> = MutableStateFlow(arrayListOf())

    fun readNotesList():ArrayList<Notes> {
        val list: ArrayList<Notes> = ArrayList()
        list.add(
            Notes("Today",
                "A Dummy Note",
                "The official Android IDE. Android Studio will help you develop your app in a " +
                        "more productive way at scale. Android Studio provides the fastest tools for building apps on every Android device."
            )
        )
        list.add(
            Notes("Today",
                " B Dummy Note ",
                "The official Android IDE. Android Studio will help you develop your app in a " +
                        "more productive way at scale. Android Studio provides the fastest tools for building apps on every Android device."
            )
        )
        list.add(
            Notes("Today",
                "C Dummy Note ",
                "The official Android IDE. Android Studio will help you develop your app in a " +
                        "more productive way at scale. Android Studio provides the fastest tools for building apps on every Android device."
            )
        )
        list.add(
            Notes("Today",
                " D Dummy Note ",
                "The official Android IDE. Android Studio will help you develop your app in a " +
                        "more productive way at scale. Android Studio provides the fastest tools for building apps on every Android device."
            )
        )
        list.add(
            Notes("Today",
                "E Dummy Note ",
                "The official Android IDE. Android Studio will help you develop your app in a " +
                        "more productive way at scale. Android Studio provides the fastest tools for building apps on every Android device."
            )
        )
        list.add(
            Notes("Today",
                "F Dummy Note ",
                "The official Android IDE. Android Studio will help you develop your app in a " +
                        "more productive way at scale. Android Studio provides the fastest tools for building apps on every Android device."
            )
        )



        notesListFlow.value = list
        return list
    }
}

  /*  init {
        viewModelScope.launch {
            var count = 0
            while (true) {
                _mStateFlow.value = count++
                kotlinx.coroutines.delay(5000)
            }
        }
    }
}*/