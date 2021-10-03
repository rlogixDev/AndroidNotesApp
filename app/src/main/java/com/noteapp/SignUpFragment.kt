package com.noteapp
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.noteapp.authentication.IFirebaseAuthenticationManager
import com.noteapp.authentication.Result
import com.noteapp.dataclass.Address
import com.noteapp.dataclass.User
import com.noteapp.services.IAddressManager
import com.noteapp.storage.IFirebaseStorageManager
import com.test.notes.AlertDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject
import java.util.*
import kotlin.collections.ArrayList
import android.widget.ArrayAdapter




@AndroidEntryPoint
class SignUpFragment : Fragment() {
    @Inject
    lateinit var firebaseAuthenticationManager: IFirebaseAuthenticationManager
    @Inject
    lateinit var firebaseStorageManager: IFirebaseStorageManager
    @Inject
    lateinit var addressManager: IAddressManager

    lateinit var btnSignUp: Button
    lateinit var email_id: EditText
    lateinit var password: EditText
    lateinit var birthDate: TextView
    lateinit var mobile: EditText
    lateinit var firstName: EditText
    lateinit var country: EditText
    lateinit var state: EditText
    lateinit var zipCode: AutoCompleteTextView
    lateinit var city:EditText
    lateinit var tvGender:TextView
    lateinit var rbGenderFemale:RadioButton
    lateinit var rbGenderMale:RadioButton
    lateinit var rbGenderOther:RadioButton
    lateinit var rgGender: RadioGroup
    var addressList: ArrayList<Address>? = null
    var zipCodes: ArrayList<String>? = null

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
        password = view.findViewById<EditText>(R.id.password)
        mobile = view.findViewById<EditText>(R.id.mobile)
        firstName = view.findViewById<EditText>(R.id.firstName)
        email_id = view.findViewById<EditText>(R.id.email_id)
        country = view.findViewById<EditText>(R.id.country)
        zipCode = view.findViewById<AutoCompleteTextView>(R.id.zipCode)
        state = view.findViewById<EditText>(R.id.state)
        city = view.findViewById<EditText>(R.id.city)
        tvGender = view.findViewById<TextView>(R.id.tvGender)
        rbGenderFemale = view.findViewById<RadioButton>(R.id.rbGenderFemale)
        rbGenderMale = view.findViewById<RadioButton>(R.id.rbGenderMale)
        rbGenderOther = view.findViewById<RadioButton>(R.id.rbGenderOther)
        rgGender = view.findViewById<RadioGroup>(R.id.rgGender)

        fun CharSequence.isValidEmail() =
            isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

        fun CharSequence.isValidMobile() = isNotEmpty() && Patterns.PHONE.matcher(this).matches()

        //Birth Date Input
        birthDate = view.findViewById<TextView>(R.id.birthDate)
        val calendar = Calendar.getInstance()

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                kotlin.runCatching {
                    val mZipCode = zipCode?.text.toString()
                    val address = addressList?.filter { it.zipCode == mZipCode }?.first()
                    address?.let {
                        state.setText(it.state?:"")
                        country.setText(it.country?:"")
                        city.setText(it.city?:"")
                    }
                }
            }
        }

        zipCode.addTextChangedListener(textWatcher)

        birthDate.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        var month_add = ""
                        var strDay = ""
                        if(month < 9)
                            month_add = "0${month + 1}"
                        else
                            month_add = (month + 1).toString()

                        if(dayOfMonth < 10)
                            strDay = "0${dayOfMonth}"
                        else
                            strDay = dayOfMonth.toString()
                        val dateFormatter = SimpleDateFormat("DD MMM, YYYY")
                        val dateFormatter1 = SimpleDateFormat("yyyy-mm-dd")

                        val strDate = year.toString()+"-"+month_add+"-"+strDay
                        val date: Date = dateFormatter1.parse(strDate)
                        val formattedDate = dateFormatter.format(date)
                        birthDate.text = formattedDate
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                ).show()

            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            addressManager.getAddress().collect { data->
                data?.let { values->
                    addressList = ArrayList<Address>()
                    zipCodes = ArrayList()
                    values.forEach { map ->
                        val zipCode = map.keys.first()
                        val addressResponse = map[zipCode]
                        val address = Address(zipCode, addressResponse?.city?:"", addressResponse?.state?:"", addressResponse?.country?:"")
                        addressList?.add(address)
                        zipCodes?.add(zipCode)
                    }

                    updateZipCodeAdapter()
                }
            }
        }

        //Sign In Page
        val firstButtonClick: () -> Unit = { ->
            Toast.makeText(context, "Unable to create account", Toast.LENGTH_SHORT).show()
        }

        val secondButtonClick: () -> Unit = { ->
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
        }


        val btnSignUp = view.findViewById<Button>(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
            if (email_id.text.isNullOrEmpty())
                email_id.error = "Please enter a emailid"
            else if (!email_id.text.isValidEmail())
                email_id.error = "Please enter a valid emailid"
            else if (password.text.isNullOrEmpty())
                password.error = "Please enter a valid password"
            else if (birthDate.text.isNullOrEmpty())
                birthDate.error = "Please enter a valid birth date"
            else if (!mobile.text.isValidMobile())
                mobile.error = "Please enter a valid number"
            else if (firstName.text.isNullOrEmpty())
                firstName.error = "Please enter a valid name"
            else if (!(rbGenderFemale.isChecked || rbGenderMale.isChecked || rbGenderOther.isChecked))
                tvGender.error = "Please select a gender"
            else if (country.text.isNullOrEmpty())
                country.error = "Please enter a valid country"
            else if (zipCode.text.isNullOrEmpty() || !zipCode.text.isDigitsOnly())
                zipCode.error = "Please enter a valid zip code"
            else if (state.text.isNullOrEmpty())
                state.error = "Please enter a valid state"
            else if (city.text.isNullOrEmpty())
                city.error = "Please enter a valid city"
            else {
                lifecycleScope.launchWhenStarted {
                    firebaseAuthenticationManager.createAccount(
                        email_id.text.toString(),
                        password.text.toString(),

                        ).collect { result ->
                        when (result) {
                            Result.SUCCESS -> {
                                firebaseStorageManager.writeNewUser(
                                    User(birthDate.text.toString(),
                                        firstName.text.toString(),
                                        mobile.text.toString(),
                                        getGender(),
                                        "",
                                        country.text.toString(),
                                        zipCode.text.toString(),
                                        state.text.toString(),
                                        city.text.toString())
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
            }
        }
    }

    private fun getGender(): String {
        return when {
            rbGenderFemale.isChecked -> "Female"
            rbGenderMale.isChecked   -> "Male"
            else                     -> "Other"
        }
    }

    private fun updateZipCodeAdapter() {
        lifecycleScope.launch(Dispatchers.Main) {
            zipCodes?.let {
                val adapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(requireContext(), R.layout.auto_complete_text_row, it)
                zipCode?.setAdapter(adapter)
            }
        }
    }
}