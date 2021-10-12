package com.noteapp
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.noteapp.authentication.IFirebaseAuthenticationManager
import com.noteapp.authentication.Result
import com.test.notes.AlertDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {
    @Inject
    lateinit var firebaseAuthenticationManager : IFirebaseAuthenticationManager

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private companion object {
        private const val TAG = "SignInFragment"
        private const val RC_GOOGLE_SIGN_IN= 4926
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    private fun updateUI() {
        if(firebaseAuthenticationManager.getUserId().isNullOrEmpty()) {
            Log.w(TAG, "User is null, not going to navigate")
            return
        }
        else {
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .setConfirmationRequired(false)
                .build()
            biometricPrompt.authenticate(promptInfo)
        }
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

        executor = ContextCompat.getMainExecutor(requireContext())

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(requireContext(),
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    findNavController()?.navigate(SignInFragmentDirections.signInToHome())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(requireContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        updateUI()

        //Sign Up Page
        val createNewAcc = view.findViewById<TextView>(R.id.createNewAcc)
        createNewAcc.setOnClickListener {
            view.findNavController().navigate(SignInFragmentDirections.signInToSignUp())
        }

        //Home Page
        val btnSignIn = view.findViewById<Button>(R.id.btnSignIn)
        btnSignIn.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                firebaseAuthenticationManager.login(email_mobile.text.toString(), sign_in_password.text.toString()).collect {result->
                    Log.e("SIGN IN ERROR RESULT"+ result,"")
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

        }

        //Forgot Password Page
        val sign_in_forgot_password = view.findViewById<TextView>(R.id.sign_in_forgot_password)
        sign_in_forgot_password.setOnClickListener {
            view.findNavController().navigate(SignInFragmentDirections.signInToForgotPassword())
        }

        // Configure Google Sign In
        val sign_in_google = view.findViewById<SignInButton>(R.id.sign_in_google)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        sign_in_google.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    val firstButtonClick: () -> Unit = { ->
    }

    val secondButtonClick: () -> Unit = { ->
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                Log.w(TAG, "TRY 1")
                val account = task.getResult(ApiException::class.java)!!
                Log.w(TAG, "TRY 2")
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                lifecycleScope.launchWhenStarted {
                    firebaseAuthenticationManager.firebaseAuthWithGoogle(account.idToken!!).collect {
                        when(it) {
                            Result.SUCCESS -> findNavController().navigate(SignInFragmentDirections.signInToHome())
                            Result.FAIL-> {
                                AlertDialogFragment(
                                    "Alert!", "Unable to login, please try again", "OK",
                                    "", firstButtonClick, secondButtonClick
                                ).show(requireActivity().supportFragmentManager, "AlertDialogFragment")
                            }
                        }
                    }
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
}