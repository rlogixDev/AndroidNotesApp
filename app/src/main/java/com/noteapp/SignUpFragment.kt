package com.noteapp
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import android.widget.*
import androidx.core.text.isDigitsOnly
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
import java.util.*

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    @Inject
    lateinit var firebaseAuthenticationManager: IFirebaseAuthenticationManager
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

        fun CharSequence.isValidEmail() =
            isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this,).matches()

        fun CharSequence.isValidMobile() = isNotEmpty() && Patterns.PHONE.matcher(this,).matches()

        //Birth Date Input
        val birthDate = view.findViewById<EditText>(R.id.birthDate)
        val calendar = Calendar.getInstance()

        birthDate.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val month_add = month + 1
                        birthDate.text = ("$year/$month_add/$dayOfMonth") as Editable
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                ).show()

            }
        }

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
                firebaseAuthenticationManager.createAccount(
                    emailId.text.toString(),
                    password.text.toString()
                ).collect { result ->
                    when (result) {
                        Result.SUCCESS -> {
                            firebaseStorageManager.writeNewUser(
                                User("20th Mar, 1988",
                                "Awaneesh Singh",
                                "1234567890",
                                "Male",
                                "test@gmail.com",
                                "India",
                                "201301",
                                "Uttar Pradesh",
                                "Noida")
                            ).collect { dbResult->
                                view.findNavController().popBackStack()
                            }
                        }
                        Result.FAIL -> {
                            AlertDialogFragment(
                                "Alert!", "Unable to create account", "OK",
                                "", firstButtonClick, secondButtonClick
                            ).show(requireActivity().supportFragmentManager, "AlertDialogFragment")
                        }
                        /*Toast.makeText(context, "Unable to create account", Toast.LENGTH_LONG).show()*/
                    }
                }
            }


            val name = view.findViewById<EditText>(R.id.name)
            val password = view.findViewById<EditText>(R.id.password)
            val mobile = view.findViewById<EditText>(R.id.mobile)
            val firstName = view.findViewById<EditText>(R.id.firstName)
            val emailId = view.findViewById<EditText>(R.id.emailId)
            val country = view.findViewById<EditText>(R.id.country)
            val zipCode = view.findViewById<EditText>(R.id.zipCode)
            val state = view.findViewById<EditText>(R.id.state)
            val city = view.findViewById<EditText>(R.id.city)
            val googleId = view.findViewById<EditText>(R.id.googleId)
            val tvGender = view.findViewById<TextView>(R.id.tvGender)
            val rbGenderFemale = view.findViewById<RadioButton>(R.id.rbGenderFemale)
            val rbGenderMale = view.findViewById<RadioButton>(R.id.rbGenderMale)
            val rbGenderOther = view.findViewById<RadioButton>(R.id.rbGenderOther)

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
                else if (!(rbGenderFemale.isChecked || rbGenderMale.isChecked || rbGenderOther.isChecked))
                    tvGender.error = "Please select a gender"
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
                    Toast.makeText(context, "Account successfully created", Toast.LENGTH_LONG)
                        .show()
                    view.findNavController().navigate(SignUpFragmentDirections.signUpToSignIn())
                }
            }
        }
    }
}