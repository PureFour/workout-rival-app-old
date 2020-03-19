package com.ruczajsoftware.workoutrival.util


import android.app.Activity
import android.content.DialogInterface
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ruczajsoftware.workoutrival.R


fun Activity.displayToast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Activity.displayToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Activity.displaySuccessDialog(message: String?) {
    MaterialAlertDialogBuilder(this)
        .setTitle(R.string.text_success)
        .setMessage(message)
        .setPositiveButton(R.string.text_ok, null)
        .show()

}

fun Activity.displayErrorDialog(errorMessage: String?) {
    MaterialAlertDialogBuilder(this, R.style.MainDialogStyle)
        .setTitle(R.string.text_error)
        .setMessage(errorMessage)
        .setPositiveButton(R.string.text_ok, null)
        .show()

}

fun Activity.displayInfoDialog(message: String?) {
    MaterialAlertDialogBuilder(this)
        .setTitle(R.string.text_info)
        .setMessage(message)
        .setPositiveButton(R.string.text_ok, null)
        .show()
}

fun Activity.areYouSureDialog(message: String, callback: DialogInterface.OnClickListener) {
    MaterialAlertDialogBuilder(this, R.style.MainDialogStyle)
        .setTitle(R.string.are_you_sure)
        .setMessage(message)
        .setNegativeButton(R.string.text_cancel, null)
        .setPositiveButton(R.string.text_yes, callback)
        .show()
}