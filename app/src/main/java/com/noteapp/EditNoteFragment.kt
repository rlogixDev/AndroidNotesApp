package com.noteapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.noteapp.dataclass.Notes
import com.noteapp.storage.DBResult
import com.noteapp.storage.IFirebaseStorageManager
import com.noteapp.storage.UpladImageResult
import com.test.notes.AlertDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EditNoteFragment : Fragment() {

    @Inject
    lateinit var firebaseStorageManager: IFirebaseStorageManager

    lateinit var note_title: EditText
    lateinit var edit_note_details: EditText
    lateinit var addImage: Button
    lateinit var edit_note_image: ImageView
    lateinit var btnEditNote: Button

    private lateinit var photoFile: File
    val CAPTURE_IMAGE_REQUEST = 1
    private var mCurrentPhotoPath: String = ""
    private var imagePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        note_title = view.findViewById<EditText>(R.id.note_title)
        edit_note_details = view.findViewById<EditText>(R.id.edit_note_details)
        addImage = view.findViewById<Button>(R.id.addImage)
        edit_note_image = view.findViewById<ImageView>(R.id.edit_note_image)
        btnEditNote = view.findViewById<Button>(R.id.btnEditNote)

        addImage.setOnClickListener {
            checkPermissions()
        }
        //Note Details Page
        btnEditNote.setOnClickListener { mView->
            if(note_title.text.isEmpty())
                showErrorMessage("Please enter note title.")
            else if(edit_note_details.text.isEmpty())
                showErrorMessage("Please enter note description.")
            else if(imagePath.isEmpty())
                showErrorMessage("Please capture a image.")
            else {
                lifecycleScope.launch {
                    firebaseStorageManager
                        .createNote(Notes("", "", note_title.text.toString(),
                            edit_note_details.text.toString(), "2021-10-01",imagePath))
                        .collect { dbResult->
                            mView.findNavController().popBackStack()
                        }
                }
            }
        }
    }

    val firstButtonClick: () -> Unit = { ->
    }

    val secondButtonClick: () -> Unit = { ->
    }

    private fun showErrorMessage(message: String) {
        AlertDialogFragment(
            "Alert!", message, "OK",
            "", firstButtonClick, secondButtonClick
        ).show(requireActivity().supportFragmentManager, "AlertDialogFragment")
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun startCamera() {
        try {
            photoFile = createImageFile()
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    requireContext(),
                    "com.noteapp.fileprovider",
                    photoFile!!
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
            }
        } catch (ex: Exception) {
            Log.d("Error :", ex.message.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            edit_note_image.setImageBitmap(myBitmap)
            lifecycleScope.launchWhenStarted {
                val fileName = System.currentTimeMillis().toString()
                firebaseStorageManager.uploadImage(photoFile!!, fileName).collect {
                    when(it) {
                        is UpladImageResult.Success -> imagePath = it.path
                        UpladImageResult.Fail -> showErrorMessage("Unable to upload image")
                    }
                }
            }
        } else {
            // displayMessage(baseContext, "Request cancelled or something went wrong.")
        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                checkPermissions()
            } else {
                showErrorMessage("Camera permission is not allowed.")
            }
        }

    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
                    &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                startCamera()
            }
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> requestPermissionLauncher.launch(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA)
            }
        }
    }
}