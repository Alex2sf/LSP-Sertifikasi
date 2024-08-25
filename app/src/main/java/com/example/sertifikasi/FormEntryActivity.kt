package com.example.sertifikasi

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class FormEntryActivity : AppCompatActivity() {

    private lateinit var imageViewProfile: ImageView
    private lateinit var buttonChooseImage: Button
    private lateinit var editTextNIK: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextDate: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var buttonSubmit: Button
    private lateinit var databaseUsers: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_entry)

        // Inisialisasi Firebase
        databaseUsers = FirebaseDatabase.getInstance().getReference("users")
        storageReference = FirebaseStorage.getInstance().reference.child("profile_images")

        // Inisialisasi Views
        imageViewProfile = findViewById(R.id.imageViewProfile)
        buttonChooseImage = findViewById(R.id.buttonChooseImage)
        editTextNIK = findViewById(R.id.editTextNIK)
        editTextName = findViewById(R.id.editTextName)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextDate = findViewById(R.id.editTextDate)
        editTextAddress = findViewById(R.id.editTextAddress)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        // Pilih gambar dari galeri
        buttonChooseImage.setOnClickListener {
            openFileChooser()
        }

        // Tanggal pendataan (Pilih tanggal)
        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Tombol submit untuk menyimpan data
        buttonSubmit.setOnClickListener {
            val nik = editTextNIK.text.toString()
            val phone = editTextPhone.text.toString()

            if (validateInput() && validateNIK(nik) && validatePhone(phone)) {
                if (imageUri != null) {
                    uploadImageAndSaveUserData()
                } else {
                    val user = User(
                        nik = nik,
                        name = editTextName.text.toString(),
                        phone = phone,
                        date = editTextDate.text.toString(),
                        gender = getSelectedGender(),
                        imageUrl = "https://example.com/profile.jpg" // Gambar profil dummy
                    )

                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra("EXTRA_USER", user)
                    startActivity(intent)
                }
            }
        }
    }
    private fun getSelectedGender(): String {
        val selectedGenderId = radioGroupGender.checkedRadioButtonId
        val radioGenderButton = findViewById<RadioButton>(selectedGenderId)
        return radioGenderButton.text.toString()
    }
    private fun validateInput(): Boolean {
        val nik = editTextNIK.text.toString().trim()
        val name = editTextName.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()
        val date = editTextDate.text.toString().trim()
        val selectedGenderId = radioGroupGender.checkedRadioButtonId

        // Validasi input
        return when {
            TextUtils.isEmpty(nik) -> {
                editTextNIK.error = "NIK harus diisi"
                editTextNIK.requestFocus()
                false
            }
            TextUtils.isEmpty(name) -> {
                editTextName.error = "Nama lengkap harus diisi"
                editTextName.requestFocus()
                false
            }
            TextUtils.isEmpty(phone) -> {
                editTextPhone.error = "Nomor handphone harus diisi"
                editTextPhone.requestFocus()
                false
            }
            TextUtils.isEmpty(date) -> {
                editTextDate.error = "Tanggal pendataan harus diisi"
                editTextDate.requestFocus()
                false
            }
            selectedGenderId == -1 -> {
                Toast.makeText(this, "Harap pilih jenis kelamin", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
    private fun validateNIK(nik: String): Boolean {
        return if (nik.length == 16) {
            true
        } else {
            Toast.makeText(this, "NIK harus berisi 16 karakter", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun validatePhone(phone: String): Boolean {
        return if (phone.length == 13 && phone.startsWith("08")) {
            true
        } else {
            Toast.makeText(this, "Nomor Handphone harus berisi 13 karakter dan diawali dengan 08", Toast.LENGTH_SHORT).show()
            false
        }
    }
    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imageViewProfile.setImageURI(imageUri)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editTextDate.setText(date)
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun uploadImageAndSaveUserData() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Mengunggah Gambar...")
        progressDialog.show()

        val fileReference = storageReference.child(System.currentTimeMillis().toString() + ".jpg")
        fileReference.putFile(imageUri!!)
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    progressDialog.dismiss()
                    saveUserData(uri.toString())
                }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Gagal mengunggah gambar: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserData(imageUrl: String) {
        val nik = editTextNIK.text.toString().trim()
        val name = editTextName.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()
        val date = editTextDate.text.toString().trim()
        val address = editTextAddress.text.toString().trim()

        val selectedGenderId = radioGroupGender.checkedRadioButtonId
        val radioGenderButton = findViewById<RadioButton>(selectedGenderId)
        val gender = radioGenderButton.text.toString()

        // Validasi input
        if (TextUtils.isEmpty(nik) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(date) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Harap isi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        // Simpan data ke Firebase Database
        val id = databaseUsers.push().key
        val user = User(nik, name, phone, gender, date, imageUrl, address)
        if (id != null) {
            databaseUsers.child(id).setValue(user)
            Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
            resetForm()
        }
    }

    private fun resetForm() {
        editTextNIK.setText("")
        editTextName.setText("")
        editTextPhone.setText("")
        editTextDate.setText("")
        editTextAddress.setText("")
        radioGroupGender.clearCheck()
        imageViewProfile.setImageResource(R.drawable.ic_launcher_background)
        imageUri = null
    }
}
