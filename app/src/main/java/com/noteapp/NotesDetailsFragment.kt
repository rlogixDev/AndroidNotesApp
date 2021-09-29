package com.noteapp
import android.app.Activity
import android.content.Intent
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
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.noteapp.dataclass.Notes
import com.noteapp.storage.FirebaseStorageManager
import com.noteapp.storage.IFirebaseStorageManager
import com.noteapp.viewmodels.NoteDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class NotesDetailsFragment : Fragment() {

    val noteDetailViewModel: NoteDetailViewModel by viewModels()
    private lateinit var photoFile: File
    val CAPTURE_IMAGE_REQUEST = 1
    private lateinit var note_details_image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Edit Note Page
        val btnNoteDetails = view.findViewById<Button>(R.id.btnNoteDetails)
        note_details_image = view.findViewById<ImageView>(R.id.note_details_image)

        note_details_image.setOnClickListener {
            startCamera()
        }

        btnNoteDetails.setOnClickListener {
//            Toast.makeText(context, "Account successfully created", Toast.LENGTH_LONG).show()
            //view.findNavController().navigate(NoteDetailsFragmentDirections.noteDetailsToEditNote())
        }
        //Home Page
        val logoNoteDetails = view.findViewById<ImageView>(R.id.logoNoteDetails)
        logoNoteDetails.setOnClickListener {
//            Toast.makeText(context, "Account successfully created", Toast.LENGTH_LONG).show()
            //view.findNavController().navigate(NoteDetailsFragmentDirections.noteDetailsToHome())
        }
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
        noteDetailViewModel.setmCurrentPhotoPath(image.absolutePath)
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
            // Error occurred while creating the File
            //displayMessage(baseContext, ex.message.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            note_details_image.setImageBitmap(myBitmap)
        } else {
            // displayMessage(baseContext, "Request cancelled or something went wrong.")
        }
    }

}