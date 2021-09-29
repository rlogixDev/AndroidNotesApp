package com.noteapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.noteapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteFragment : Fragment() {

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
        //val edit_note_image = view.findViewById<Button>(R.id.edit_note_image)
        val edit_note_details = view.findViewById<EditText>(R.id.edit_note_details)
        btnEditNote.setOnClickListener {

//            Toast.makeText(context, "Account successfully created", Toast.LENGTH_LONG).show()
            if (edit_note_details.text.isNullOrEmpty())
                edit_note_details.error = "Please enter text"
            else {
                view.findNavController()
                    .navigate(EditNoteFragmentDirections.editNoteToNoteDetails())
            }
        }
    }
}