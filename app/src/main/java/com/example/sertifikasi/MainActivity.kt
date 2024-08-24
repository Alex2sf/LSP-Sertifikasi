package com.example.sertifikasi
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Button for InformasiActivity
        val btnInformasi = findViewById<Button>(R.id.btnInformasi)
        btnInformasi.setOnClickListener {
            val intent = Intent(this, InformasiActivity::class.java)
            startActivity(intent)
        }

        // Button for FormEntryActivity
        val btnFormEntry = findViewById<Button>(R.id.btnFormEntry)
        btnFormEntry.setOnClickListener {
            val intent = Intent(this, FormEntryActivity::class.java)
            startActivity(intent)
        }

        // Button for LihatDataActivity
        val btnLihatData = findViewById<Button>(R.id.btnLihatData)
        btnLihatData.setOnClickListener {
            val intent = Intent(this, LihatDataActivity::class.java)
            startActivity(intent)
        }

        // Button for LogoutActivity
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val intent = Intent(this, LogoutActivity::class.java)
            startActivity(intent)
        }
    }
}