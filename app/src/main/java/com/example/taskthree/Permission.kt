package com.example.taskthree

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val READ_CONTACTS = Manifest.permission.READ_CONTACTS
const val PERMISSION_REQUEST = 200

fun checkPermission(permission: String,view: View):Boolean{
    return if (Build.VERSION.SDK_INT >= 23 &&
        ContextCompat.checkSelfPermission(view.context,permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(view.context as Activity, arrayOf(permission), PERMISSION_REQUEST)
        false
    } else true
}