package com.noteapp
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.noteapp.dataclass.Notes
import com.noteapp.storage.DBReadResult
import com.noteapp.storage.IFirebaseStorageManager
import com.noteapp.viewmodels.NoteDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class   HomeFragment : Fragment(R.layout.fragment_home), AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var firebaseStorageManager: IFirebaseStorageManager

    lateinit var notesListAdapter: NotesListAdapter

    private val noteDetailViewModel: NoteDetailViewModel by viewModels()

    lateinit var btnDelete: Button
    lateinit var btnEdit: Button
    lateinit var checkboxSelectAll: CheckBox
    lateinit var tvTitle: TextView
    lateinit var etSearch: EditText
    lateinit var _list: List<Notes>
    lateinit var ivDelete: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun performCheckBoxChangeAction(listData: List<Notes>) {
        val listFilter = listData.filter { it.isSelected }
        when (listFilter.size) {
            1 -> {
                btnDelete.visibility = View.VISIBLE
                btnEdit.visibility = View.VISIBLE
            }
            0 -> {
                btnDelete.visibility = View.GONE
                btnEdit.visibility = View.GONE
            }
            else -> {
                btnDelete.visibility = View.VISIBLE
                btnEdit.visibility = View.GONE
            }
        }
        checkboxSelectAll.isChecked = listFilter.size == listData.size
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemOnClick: (View, Int) -> Unit = { view, position ->
            noteDetailViewModel.setIsNotesEditable(false)
            noteDetailViewModel.setNoteDetail(notesListAdapter.getList()[position])
            findNavController().navigate(HomeFragmentDirections.homeToEditNote())
        }

        btnDelete = view.findViewById(R.id.btnDelete)
        btnEdit   = view.findViewById(R.id.btnEdit)
        checkboxSelectAll = view.findViewById(R.id.checkboxSelectAll)
        tvTitle = view.findViewById(R.id.tvTitle)
        etSearch = view.findViewById(R.id.etSearch)
        ivDelete = view.findViewById(R.id.ivDelete)

        btnDelete.setOnClickListener {
            notesListAdapter.getList().filter {
                it.isSelected
            }.forEach { note ->
                lifecycleScope.launchWhenStarted {
                    firebaseStorageManager.deleteNote(note).collect {
                        loadData()
                    }
                }
            }
            loadData()
        }

        btnEdit.setOnClickListener {
            notesListAdapter.getList().first { it.isSelected }?.let { note->
                noteDetailViewModel.setIsNotesEditable(true)
                noteDetailViewModel.setNoteDetail(note)
                findNavController().navigate(HomeFragmentDirections.homeToEditNote())
            }
        }

        tvTitle.setOnClickListener {
            if(_list.size > 0) {
                etSearch.visibility = View.VISIBLE
                ivDelete.visibility  = View.VISIBLE
                tvTitle.visibility  = View.GONE
            }
        }

        ivDelete.setOnClickListener {
            etSearch.visibility = View.GONE
            ivDelete.visibility  = View.GONE
            tvTitle.visibility  = View.VISIBLE
        }


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(::_list.isInitialized) {
                    val list = _list.filter {
                        it.title.contains(p0.toString())
                    }
                    notesListAdapter.refreshData(list)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        etSearch.addTextChangedListener(textWatcher)

        checkboxSelectAll.setOnClickListener {
            val listData = notesListAdapter.getList()
            listData.forEach {
                it.isSelected = checkboxSelectAll.isChecked
            }
            notesListAdapter.notifyDataSetChanged()
            performCheckBoxChangeAction(listData)
        }

        val checkBoxChangeListner: (View, Int) -> Unit = { view, position ->
            val listData = notesListAdapter.getList()
            listData[position].isSelected = !listData[position].isSelected
            notesListAdapter.refreshData(listData)
            performCheckBoxChangeAction(listData)
        }

        val rvUserList = view.findViewById<RecyclerView>(R.id.rvUserList)
        notesListAdapter =
            NotesListAdapter(arrayListOf(), requireContext(), itemOnClick, checkBoxChangeListner)
        rvUserList.adapter = notesListAdapter

        //Edit Note
        val createNewNote = view.findViewById<FloatingActionButton>(R.id.createNewNote)
        createNewNote.setOnClickListener {
            noteDetailViewModel.setIsNotesEditable(true)
            noteDetailViewModel.setNoteDetail(null)
            view.findNavController().navigate(HomeFragmentDirections.homeToEditNote())
        }

        val spinner: Spinner = view.findViewById(R.id.spinner)
        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Sorting_Array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        loadData()
    }

    private fun loadData() {
        lifecycleScope.launchWhenStarted {
            firebaseStorageManager.getUserNotes().collect { result ->
                when (result) {
                    is DBReadResult.Success -> {
                        val list: ArrayList<Notes> = ArrayList()
                        if (result.result != null && result.result.size > 0) {
                            result.result.forEach { key, any ->
                                val map = any as HashMap<String, String>
                                val title = map["title"] ?: ""
                                val details = map["details"] ?: ""
                                val date = map["date"] ?: ""
                                val imagePath = map["imagePath"] ?: ""
                                list.add(let { Notes(key, title, details, date, imagePath) })
                            }
                            _list = list.sortedBy {it.date}
                            notesListAdapter?.refreshData(_list)
                        }
                    }
                    else -> Log.i("Home", "No action needed")
                }
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(p2) {
            0-> {
                val list = notesListAdapter.getList()
                    .sortedBy {it.title}
                notesListAdapter.refreshData(list)
            }
            1-> {
                val list = notesListAdapter.getList()
                    .sortedByDescending {it.title}
                notesListAdapter.refreshData(list)
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}
