package com.noteapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.noteapp.R
import com.noteapp.dataclass.Notes
import com.noteapp.storage.IFirebaseStorageManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditNoteFragment : Fragment() {

    @Inject
    lateinit var firebaseStorageManager: IFirebaseStorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Note Details Page
        val btnEditNote = view.findViewById<Button>(R.id.btnEditNote)
        btnEditNote.setOnClickListener { mView->
            lifecycleScope.launch {
                firebaseStorageManager
                    .createNote(Notes("Awa Test11", "Awa Test Desc", "27th Sep, 2021", ""))
                    .collect { dbResult->
                        mView.findNavController().popBackStack()
                    }
            }
        }
    }
}