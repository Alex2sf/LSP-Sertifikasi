package com.example.sertifikasi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LihatDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Untuk mengaktifkan tampilan edge-to-edge
        setContentView(R.layout.activity_lihat_data) // Mengatur layout dari XML


    }
}
