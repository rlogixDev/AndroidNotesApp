package com.noteapp
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.noteapp.viewmodels.NotesListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class   HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemOnClick: (View, Int) -> Unit = { view, position ->
            Toast.makeText(context, "Item: $position", Toast.LENGTH_SHORT).show()
        }
        val notesListViewModel: NotesListViewModel by viewModels<NotesListViewModel>()
        val rvUserList = view.findViewById<RecyclerView>(R.id.rvUserList)
        val notesListAdapter = NotesListAdapter(notesListViewModel.readNotesList(), requireContext(), itemOnClick)
        rvUserList.adapter = notesListAdapter

        //Edit Note
        val createNewNote = view.findViewById<FloatingActionButton>(R.id.createNewNote)
        createNewNote.setOnClickListener {
//            Toast.makeText(context, "Account successfully created", Toast.LENGTH_LONG).show()
            view.findNavController().navigate(HomeFragmentDirections.homeToNoteDetails())
        }
        //Note Details
//        val btnSignUp = view.findViewById<Button>(R.id.btnSignUp)
//        btnSignUp.setOnClickListener {
////            Toast.makeText(context, "Account successfully created", Toast.LENGTH_LONG).show()
//            view.findNavController().navigate(HomeFragmentDirections.homeToNoteDetails())
//        }

        
    }
}