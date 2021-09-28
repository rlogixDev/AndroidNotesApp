package com.noteapp
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.noteapp.dataclass.Notes
import com.noteapp.storage.DBReadResult
import com.noteapp.storage.IFirebaseStorageManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class   HomeFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemOnClick: (View, Int) -> Unit = { view, position ->
            Toast.makeText(context, "Item: $position", Toast.LENGTH_SHORT).show()
        }
        val rvUserList = view.findViewById<RecyclerView>(R.id.rvUserList)
        /*val notesListAdapter = NotesListAdapter(notesListViewModel.readNotesList(), requireContext(), itemOnClick)
        rvUserList.adapter = notesListAdapter*/

        //Edit Note
        val createNewNote = view.findViewById<FloatingActionButton>(R.id.createNewNote)
        createNewNote.setOnClickListener {
//            Toast.makeText(context, "Account successfully created", Toast.LENGTH_LONG).show()
            view.findNavController().navigate(HomeFragmentDirections.homeToEditNote())
        }
        //Note Details
//        val btnSignUp = view.findViewById<Button>(R.id.btnSignUp)
//        btnSignUp.setOnClickListener {
////            Toast.makeText(context, "Account successfully created", Toast.LENGTH_LONG).show()
//            view.findNavController().navigate(HomeFragmentDirections.homeToNoteDetails())
//        }

        lifecycleScope.launchWhenStarted {
            firebaseStorageManager.getUserNotes().collect { result->
                when(result) {
                    is DBReadResult.Success -> {
                        val list: ArrayList<Notes> = ArrayList()
                        if(result.result != null && result.result.size > 0) {
                            result.result.forEach { key, any ->
                                val map = any as HashMap<String, String>
                                val title = map["title"]?: ""
                                val details = map["details"]?: ""
                                val date = map["date"]?: ""
                                val imagePath = map["imagePath"]?: ""

                                list.add(let { Notes(key, title, details, date, imagePath) })
                            }
                            val notesListAdapter = NotesListAdapter(list, requireContext(), itemOnClick)
                            rvUserList.adapter = notesListAdapter
                        }
                    }
                    else -> Log.i("Home", "No action needed")
                }
            }
        }

        
    }
}