package com.example.dailytasks

import android.widget.RatingBar
import androidx.annotation.DrawableRes

data class Results(
    @DrawableRes
    var smileImage: Int,
    var fullDate: String,
    var shortDate: String,
    var invisibleDate: String,
    var ratingBar: RatingBar,
    var rating: Float
)