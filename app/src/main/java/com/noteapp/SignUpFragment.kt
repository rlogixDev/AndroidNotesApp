package com.noteapp
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.noteapp.authentication.FirebaseAuthenticationManager
import com.noteapp.authentication.IFirebaseAuthenticationManager
import com.noteapp.authentication.Result
import com.noteapp.dataclass.User
import com.noteapp.storage.IFirebaseStorageManager
import com.test.notes.AlertDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    @Inject
    lateinit var firebaseAuthenticationManager : IFirebaseAuthenticationManager
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emailId = view.findViewById<EditText>(R.id.emailId)
        val password = view.findViewById<EditText>(R.id.password)

        //Sign In Page
        val firstButtonClick: () -> Unit = { ->
            Toast.makeText(context, "Unable to create account", Toast.LENGTH_SHORT).show()
        }

        val secondButtonClick: () -> Unit = { ->
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
        }
        val btnCheck = view.findViewById<Button>(R.id.btnCheck)
        val btnSignUp = view.findViewById<Button>(R.id.btnSignUp)
        btnCheck.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                firebaseAuthenticationManager.createAccount(emailId.text.toString(), password.text.toString()).collect {result->
                    when(result){
                        Result.SUCCESS-> {
                            firebaseStorageManager.writeNewUser(User("20th Mar, 1988",
                            "Awaneesh Singh",
                            "1234567890",
                            "Male",
                            "test@gmail.com",
                            "India",
                            "201301",
                            "Uttar Pradesh",
                            "Noida")).collect { dbResult->
                                view.findNavController().popBackStack()
                            }
                        }
                        Result.FAIL->{
                            AlertDialogFragment(
                                "Alert!", "Unable to create account", "OK",
                                "", firstButtonClick, secondButtonClick
                            ).show(requireActivity().supportFragmentManager, "AlertDialogFragment")
                        }
                    /*Toast.makeText(context, "Unable to create account", Toast.LENGTH_LONG).show()*/
                    }
                }
            }


        }
    }
}