package com.japaride.app

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment

fun<A: Activity> Activity.startNewActivity(activity: Class<A>){

    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

}

fun View.visible(isVisible: Boolean){
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enabled: Boolean){

    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

fun copyText(text: String, activity: Activity?) {
    val myClipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val myClip = ClipData.newPlainText("text", text)
    myClipboard?.setPrimaryClip(myClip)
    Toast.makeText(
        activity?.applicationContext,
        "Address Copied",
        Toast.LENGTH_SHORT
    ).show()
}

fun Fragment.shareAddress() {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}


//fun generateQR(content: String): Bitmap? {
//    return try {
//        val barcodeEncoder = BarcodeEncoder()
//        barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 300, 300)
//    } catch (e: Exception) {
//        null
//    }

    //fun Fragment.handleApiError(
    //    failure: Resource.Error,
    //    retry: (() -> Unit)? = null

    //){


   // }






