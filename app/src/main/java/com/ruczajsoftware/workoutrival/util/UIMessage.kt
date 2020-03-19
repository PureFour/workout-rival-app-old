package com.ruczajsoftware.workoutrival.util

import android.content.DialogInterface

data class UIMessage(
    val message: String,
    val uiMessageType: UIMessageType
)

sealed class UIMessageType {

    class Toast : UIMessageType()

    class Dialog : UIMessageType()

    class AreYouSureDialog(
        val callback: DialogInterface.OnClickListener
    ) : UIMessageType()

    class None : UIMessageType()
}