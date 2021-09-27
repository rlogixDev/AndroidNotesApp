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
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SignInFragment : Fragment() {

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
        btnSignIn.setOnClickListener {
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