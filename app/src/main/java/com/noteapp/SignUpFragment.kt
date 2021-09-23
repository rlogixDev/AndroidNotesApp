package com.noteapp
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.navigation.findNavController

class SignUpFragment : Fragment() {

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

        fun CharSequence.isValidEmail() = isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this,).matches()
        fun CharSequence.isValidMobile() = isNotEmpty() && Patterns.PHONE.matcher(this,).matches()

        //Sign In Page
        val btnSignUp = view.findViewById<Button>(R.id.btnSignUp)
        val name = view.findViewById<EditText>(R.id.name)
        val password = view.findViewById<EditText>(R.id.password)
        val birthDate = view.findViewById<EditText>(R.id.birthDate)
        val mobile = view.findViewById<EditText>(R.id.mobile)
        val firstName = view.findViewById<EditText>(R.id.firstName)
        val emailId = view.findViewById<EditText>(R.id.emailId)
        val country = view.findViewById<EditText>(R.id.country)
        val zipCode = view.findViewById<EditText>(R.id.zipCode)
        val state = view.findViewById<EditText>(R.id.state)
        val city = view.findViewById<EditText>(R.id.city)
        val googleId = view.findViewById<EditText>(R.id.googleId)

        btnSignUp.setOnClickListener {

            if (name.text.isNullOrEmpty())
                name.error = "Please enter a valid name"
            else if (password.text.isNullOrEmpty())
                password.error = "Please enter a valid password"
            else if (birthDate.text.isNullOrEmpty())
                birthDate.error = "Please enter a valid birth date"
            else if (mobile.text.isValidMobile())
                mobile.error = "Please enter a valid number"
            else if (firstName.text.isNullOrEmpty())
                firstName.error = "Please enter a valid name"
            else if (emailId.text.isValidEmail())
                emailId.error = "Please enter a valid email"
            else if (country.text.isNullOrEmpty())
                country.error = "Please enter a valid country"
            else if (zipCode.text.isNullOrEmpty() || !zipCode.text.isDigitsOnly())
                zipCode.error = "Please enter a valid zip code"
            else if (state.text.isNullOrEmpty())
                state.error = "Please enter a valid state"
            else if (city.text.isNullOrEmpty())
                city.error = "Please enter a valid city"
            else if (googleId.text.isValidEmail())
                googleId.error = "Please enter a valid email"

            else {
                Toast.makeText(context, "Account successfully created", Toast.LENGTH_LONG).show()
                view.findNavController().navigate(SignUpFragmentDirections.signUpToSignIn())
            }

        }
    }
}