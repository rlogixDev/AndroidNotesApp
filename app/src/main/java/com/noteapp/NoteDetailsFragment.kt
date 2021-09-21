package com.noteapp
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController

class NoteDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Edit Note Page
        val btnNoteDetails = view.findViewById<Button>(R.id.btnNoteDetails)
        btnNoteDetails.setOnClickListener {
//            Toast.makeText(context, "Account successfully created", Toast.LENGTH_LONG).show()
            view.findNavController().navigate(NoteDetailsFragmentDirections.noteDetailsToEditNote())
        }
        //Home Page
        val logoNoteDetails = view.findViewById<ImageView>(R.id.logoNoteDetails)
        logoNoteDetails.setOnClickListener {
//            Toast.makeText(context, "Account successfully created", Toast.LENGTH_LONG).show()
            view.findNavController().navigate(NoteDetailsFragmentDirections.noteDetailsToHome())
        }
    }

}