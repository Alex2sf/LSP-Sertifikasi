package com.example.sertifikasi

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sertifikasi.User

class DetailActivity : AppCompatActivity() {

    private lateinit var imageViewProfile: ImageView
    private lateinit var textViewNIK: TextView
    private lateinit var textViewName: TextView
    private lateinit var textViewPhone: TextView
    private lateinit var textViewGender: TextView
    private lateinit var textViewDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        imageViewProfile = findViewById(R.id.imageViewProfileDetail)
        textViewNIK = findViewById(R.id.textViewNIK)
        textViewName = findViewById(R.id.textViewName)
        textViewPhone = findViewById(R.id.textViewPhone)
        textViewGender = findViewById(R.id.textViewGender)
        textViewDate = findViewById(R.id.textViewDate)

        val user = intent.getParcelableExtra<User>("EXTRA_USER")

        if (user != null) {
            Glide.with(this)
                .load(user.imageUrl)
                .into(imageViewProfile)

            textViewNIK.text = user.nik
            textViewName.text = user.name
            textViewPhone.text = user.phone
            textViewGender.text = user.gender
            textViewDate.text = user.date
        }
    }
}
