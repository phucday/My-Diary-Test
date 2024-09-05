package com.example.mydiarytest

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object PermissionGallery {
    fun checkReadImagePermission(context: Context) : Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        else context.checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
    }


}