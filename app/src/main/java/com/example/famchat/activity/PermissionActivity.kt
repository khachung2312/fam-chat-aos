package com.example.famchat.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akexorcist.localizationactivity.ui.LocalizationActivity

open class PermissionActivity : LocalizationActivity() {

    companion object {
        private const val MY_PERMISSIONS_REQUEST = 111
    }

    var launchActivityResult: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(
            this
        )

    private var permissionComplete: ((Boolean) -> Unit)? = null

    private var imageSelectListener: ((MutableList<Uri>) -> Unit)? = null

    private val launcherMultiPickMedia = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(10)
    ) { lstUri ->
        lstUri?.let {
            handlerUriPicker(it.toMutableList())
        }
    }

    private val launcherSinglePickMedia = registerForActivityResult<PickVisualMediaRequest, Uri?>(
        ActivityResultContracts.PickVisualMedia()
    ) {
        it?.let {
            handlerUriPicker(mutableListOf(it))
        }
    }

    private fun handlerUriPicker(lstUri: MutableList<Uri>) {
        lstUri.forEach {
            try {
                this.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        imageSelectListener?.invoke(lstUri.toMutableList())
    }

    open fun requestPermission(
        complete: (Boolean) -> Unit,
        permissions: Array<String>
    ) {
        this.permissionComplete = complete
        if (checkPermissionAccept(permissions)) {
            complete.invoke(true)
        } else {
            requestRuntimePermission(permissions)
        }
    }

    fun openGalleryApi34(
        allowMultiFile: Boolean = false,
        imageSelectListener: (MutableList<Uri>) -> Unit
    ) {
        this.imageSelectListener = imageSelectListener
        if (allowMultiFile) {
            launcherMultiPickMedia.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    .build()
            )
        } else {
            launcherSinglePickMedia.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    .build()
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> if (checkPermissionAccept(permissions)
            ) {
                permissionComplete?.invoke(true)
            } else {
                permissionComplete?.invoke(false)
            }
        }
    }

    private fun requestRuntimePermission(permissions: Array<String>) {
        if (!checkPermissionAccept(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST)
        }
    }

    private fun checkPermissionAccept(permissions: Array<String>): Boolean {
        permissions.forEach { permissionName ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permissionName
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun requestWriteStoragePermission(resultPermission: (Boolean) -> Unit) {
        this.permissionComplete = resultPermission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            resultPermission(true)
        } else {
            requestPermission(complete = {
                if (it) {
                    resultPermission.invoke(true)
                }
            }, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }

    }

}