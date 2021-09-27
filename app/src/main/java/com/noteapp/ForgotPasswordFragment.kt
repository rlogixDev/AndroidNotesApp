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
import androidx.navigation.findNavController

class ForgotPasswordFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSendEmail = view.findViewById<Button>(R.id.btnSendEmail)
        val etForgotPassword = view.findViewById<EditText>(R.id.etForgotPassword)

        fun CharSequence.isValidEmail() = isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this,).matches()

        if (etForgotPassword.text.isValidEmail()) {

            //Sign In Page
            btnSendEmail.setOnClickListener {
                Toast.makeText(context, "Password Sent!", Toast.LENGTH_LONG).show()
                view.findNavController().navigate(ForgotPasswordFragmentDirections.forgotPasswordToSignIn())
            }
        } else {
            etForgotPassword.error = "Invalid email!"
        }

    }
}