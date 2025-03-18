package com.example.FlyHigh.domain.model

import java.util.Date

class Image(
    var imageId: String,
    var url: String,
    var uploadDate: Date
) {
    fun deleteImage() {}
}