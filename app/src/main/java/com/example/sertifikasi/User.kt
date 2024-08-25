package com.example.sertifikasi
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class User(
    val nik: String = "",
    val name: String = "",
    val phone: String = "",
    val gender: String = "",
    val date: String = "",
    val imageUrl: String = ""
): Parcelable

