package com.pawlowski.stuboard.data.remote.image_storage

import android.net.Uri
import com.pawlowski.stuboard.domain.models.Resource

interface IImageUploadManager {
    suspend fun uploadNewImage(uri: Uri, userUid: String): Resource<String>
}