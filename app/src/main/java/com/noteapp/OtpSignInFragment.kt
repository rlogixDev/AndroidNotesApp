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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpSignInFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Show OTP Input
        val btnSendOtp = view.findViewById<Button>(R.id.btnSendOtp)
        val tvOtp = view.findViewById<TextView>(R.id.tvOtp)
        val etOtp = view.findViewById<EditText>(R.id.etOtp)
        val btnOtpSignIn = view.findViewById<Button>(R.id.btnOtpSignIn)
        val etForgotPassword = view.findViewById<EditText>(R.id.etForgotPassword)

        fun CharSequence.isValidEmail() = isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this,).matches()

        btnSendOtp.setOnClickListener {
            if (etForgotPassword.text.isValidEmail()) {

                tvOtp.visibility = View.VISIBLE
                etOtp.visibility = View.VISIBLE
                btnOtpSignIn.visibility = View.VISIBLE
                btnSendOtp.text = "RESEND OTP"

                Toast.makeText(context, "OTP Sent!", Toast.LENGTH_LONG).show()

            } else {
                etForgotPassword.error = "Invalid email!"
            }
        }

        //Home Page
        btnOtpSignIn.setOnClickListener {
            if (etOtp.text.isNotEmpty()) {
                view.findNavController().navigate(OtpSignInFragmentDirections.otpSignInToHome())

            } else {
                etOtp.error = "Invalid OTP!"
            }
        }
    }
}