package com.app.cementfactory.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import com.app.cementfactory.R
import com.app.cementfactory.entity.User
import com.app.cementfactory.repository.UserRepo
import com.mikhaellopez.circularimageview.CircularImageView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var btnRegister: Button
    private lateinit var etFname: EditText
    private lateinit var etPnumber: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText
    private lateinit var photo: CircularImageView
    private var uid: String? = null

    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        btnRegister = findViewById(R.id.btnRegister)
        etFname = findViewById(R.id.etFullname)
        etPnumber = findViewById(R.id.etPhone)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)
        photo = findViewById(R.id.photo)

        btnRegister.setOnClickListener {
            if (validate()) {
                register()
            }

        }

        photo.setOnClickListener {
            loadPopUpMenu()
        }


    }

    fun validate(): Boolean {

        if (etFname.text.toString() == "") {
            return false
            etFname.error = "this field is empty"
        }
        if (etPnumber.text.toString() == "") {
            return false
            etPnumber.error = "this field is empty"
        }
        if (etUsername.text.toString() == "") {
            return false
            etUsername.error = "Username cant be emptys"
        }

        if (etEmail.text.toString() == "") {
            return false
            etEmail.error = "cant be empty"
        }

        if (etPassword.text.toString() == "") {
            return false
            etPassword.error = "Password cant be empty"
        }


        return true
    }

    private fun register() {
        val fullname = etFname.text.toString();
        val phoneNumber = etPnumber.text.toString();
        val username = etUsername.text.toString();
        val email = etEmail.text.toString();
        val password = etPassword.text.toString();


        val user = User(
            fullname = fullname,
            phoneNumber = phoneNumber,
            username = username,
            email = email,
            password = password
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepo()
                val response = userRepository.register(
                    user
                );
                if (response.success == true) {
                    uid = response.data?._id.toString();
                    println(imageUrl);
                    if (imageUrl != null) {
                        uploadImage(uid!!)
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "User Registered",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(this@RegisterActivity, LoginActivity::class.java)
                        )
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error : ${e.toString()}", Toast.LENGTH_SHORT
                    ).show()
                    println("sign up error$e")

                }

            }
        }

    }

    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(this@RegisterActivity, photo)
        popupMenu.menuInflater.inflate(R.menu.images, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuGallery ->
                    openGallery()
                R.id.menuCamera ->
                    openCamera()
            }
            true
        }
        popupMenu.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageUrl = cursor.getString(columnIndex)

                photo.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                photo.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
            }
        }
    }

    private fun uploadImage(id: String) {
        if (imageUrl != null) {
            val file = File(imageUrl!!)
            val reqFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)

            val body =
                MultipartBody.Part.createFormData("photo", file.name, reqFile)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepo = UserRepo()
                    val response = userRepo.uploadImage(id, body)
                    if (response.success == true) {
                        withContext(Main) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Image Uploaded",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                } catch (ex: IOException) {
                    withContext(Main) {
                        Log.d("Error Uploading Image ", ex.message.toString())
                        Toast.makeText(
                            this@RegisterActivity,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

}