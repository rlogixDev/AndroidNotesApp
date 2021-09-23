package com.test.notes

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.noteapp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlertDialogFragment
@Inject constructor(
    val title: String,
    val message: String,
    val firstButtonText: String,
    val secondButtonText: String,
    val firstButtonClick: () -> Unit,
    val secondButtonClick: () -> Unit
): DialogFragment() {
    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val alertView = inflater.inflate(R.layout.alert_view, container, false)
        val tvTitle = alertView.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = alertView.findViewById<TextView>(R.id.tvMessage)
        val btnDelete = alertView.findViewById<Button>(R.id.btnDelete)
        val btnCancel = alertView.findViewById<Button>(R.id.btnCancel)

        if (secondButtonText.isNullOrEmpty())
            btnCancel.visibility = View.GONE
        else
            btnCancel.visibility = View.VISIBLE

        if (firstButtonText.isNullOrEmpty())
            btnDelete.visibility = View.GONE
        else
            btnDelete.visibility = View.VISIBLE

        tvTitle.text = title
        tvMessage.text = message
        btnDelete.text = firstButtonText
        btnCancel.text = secondButtonText

        btnCancel.setOnClickListener{
            dismiss()
            secondButtonClick()
        }
        btnDelete.setOnClickListener{
            dismiss()
            firstButtonClick()
        }
        return alertView


    }
}