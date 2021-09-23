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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    @Inject
    lateinit var firebaseAuthenticationManager : IFirebaseAuthenticationManager

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
        val btnCheck = view.findViewById<Button>(R.id.btnCheck)
        val btnSignUp = view.findViewById<Button>(R.id.btnSignUp)
        btnCheck.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                firebaseAuthenticationManager.createAccount(emailId.text.toString(), password.text.toString()).collect {result->
                    when(result){
                        Result.SUCCESS-> {
                            Toast.makeText(context, "Account created successfully", Toast.LENGTH_LONG).show()
                            view.findNavController().popBackStack()
                        }
                        Result.FAIL->Toast.makeText(context, "Unable to create account", Toast.LENGTH_LONG).show()
                    }
                }
            }


        }
    }
}