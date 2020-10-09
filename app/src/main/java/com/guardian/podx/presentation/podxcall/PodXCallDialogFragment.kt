package com.guardian.podx.presentation.podxcall

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.guardian.podx.R

class PodXCallDialogFragment constructor(private val phoneNumberString: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = AlertDialog.Builder(fragmentActivity)
            builder.setMessage(
                fragmentActivity.getString(R.string.alertmessage_call_event) + phoneNumberString
            )
                .setPositiveButton(
                    R.string.alertpositive_call_event
                ) { _, _ ->
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${phoneNumberString.replace(Regex("[^0-9.]"), "")}")
                    }

                    if (intent.resolveActivity(fragmentActivity.packageManager) != null) {
                        startActivity(intent)
                    }
                }
                .setNegativeButton(
                    R.string.alertnegative_call_event
                ) { dialog, _ ->
                    dialog?.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
