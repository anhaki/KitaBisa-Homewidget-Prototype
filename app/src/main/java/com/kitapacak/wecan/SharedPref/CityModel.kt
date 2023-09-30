package com.kitapacak.wecan.SharedPref

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityModel(
    var city: String? = null,
    var country: String? = null
) : Parcelable
