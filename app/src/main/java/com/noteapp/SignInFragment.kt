package com.noteapp
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.noteapp.authentication.IFirebaseAuthenticationManager
import com.noteapp.authentication.Result
import com.test.notes.AlertDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {
    @Inject
    lateinit var firebaseAuthenticationManager : IFirebaseAuthenticationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val i = inflater.inflate(R.layout.fragment_sign_in, container, false)
        // Inflate the layout for this fragment
        return i

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email_mobile = view.findViewById<EditText>(R.id.email_mobile)
        val sign_in_password = view.findViewById<EditText>(R.id.sign_in_password)

        fun CharSequence.isValidEmail() = isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this,).matches()
        fun CharSequence.isValidMobile() = isNotEmpty() && Patterns.PHONE.matcher(this,).matches()


        //Sign Up Page
        val createNewAcc = view.findViewById<TextView>(R.id.createNewAcc)
        createNewAcc.setOnClickListener {
            view.findNavController().navigate(SignInFragmentDirections.signInToSignUp())
        }

        //Home Page
        val btnSignIn = view.findViewById<Button>(R.id.btnSignIn)
        val firstButtonClick: () -> Unit = { ->
            Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
        }

        val secondButtonClick: () -> Unit = { ->
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
        }
        btnSignIn.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                firebaseAuthenticationManager.login(email_mobile.text.toString(), sign_in_password.text.toString()).collect {result->
                    when(result){
                        Result.SUCCESS-> {

                            view.findNavController().navigate(SignInFragmentDirections.signInToHome())
                        }
                        Result.FAIL-> {
                            AlertDialogFragment(
                                "Alert!", "Invalid email or password", "OK",
                                "", firstButtonClick, secondButtonClick
                            ).show(requireActivity().supportFragmentManager, "AlertDialogFragment")
                        }
                        /*Toast.makeText(context, "Invalid username and password", Toast.LENGTH_LONG).show()*/
                    }
                }
            }

            if (!(email_mobile.text.isValidEmail() || email_mobile.text.isValidMobile()))
                sign_in_password.error = "Invalid input!"
            else if (sign_in_password.text.isNullOrEmpty())
                sign_in_password.error = "Incorrect password!"
            else
                view.findNavController().navigate(SignInFragmentDirections.signInToHome())
        }

        //Forgot Password Page
        val sign_in_forgot_password = view.findViewById<TextView>(R.id.sign_in_forgot_password)
        sign_in_forgot_password.setOnClickListener {
            view.findNavController().navigate(SignInFragmentDirections.signInToForgotPassword())
        }

        //OTP Sign In Page
        val sign_in_otp = view.findViewById<TextView>(R.id.sign_in_otp)
        sign_in_otp.setOnClickListener {
            view.findNavController().navigate(SignInFragmentDirections.signInToOtpSignIn())
        }
    }
}