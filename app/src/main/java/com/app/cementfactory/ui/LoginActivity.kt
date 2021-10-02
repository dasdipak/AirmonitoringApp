package com.app.cementfactory.ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.core.app.ActivityCompat
import com.app.cementfactory.R
import com.app.cementfactory.api.ServiceBuilder
import com.app.cementfactory.repository.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var signupLink: TextView
    private lateinit var invalid: TextView
    private lateinit var constraintLayout: LinearLayout
    private lateinit var showPassowrd: CheckBox

    companion object {
        public var PREF_NAME: String = "loginSharedPref";
    }

    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        btnLogin = findViewById(R.id.btnLogin)
        showPassowrd = findViewById(R.id.showPassword)
        etUsername = findViewById(R.id.etFullname)
        etPassword = findViewById(R.id.etPassword)
        signupLink = findViewById(R.id.signupLink)
        invalid = findViewById(R.id.invalid)
        constraintLayout = findViewById(R.id.constraintLayout)

        checkRunTimePermission()
        showPassowrd.text = "Show Password"
        showPassowrd.setOnClickListener {
            if (showPassowrd.isChecked) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showPassowrd.text = "Hide Password"
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                showPassowrd.text = "Show Password"
            }
        }

        signupLink.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            login()
        }

        getSharedPref();

    }

    private fun getSharedPref() {
        val spf = getSharedPreferences(LoginActivity.PREF_NAME, MODE_PRIVATE);
        val username = spf.getString("username", "");
        val password = spf.getString("password", "");
        etUsername.setText(username.toString());
        etPassword.setText(password.toString());
    }

    private fun setSharedPref() {
        val username = etUsername.text.toString();
        val password = etPassword.text.toString();
        val sharedPref = getSharedPreferences("loginSharedPref", MODE_PRIVATE);
        val editor = sharedPref.edit();
        editor.putBoolean("hasLoggedIn", true)
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    private fun login() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepo()
                val response = repository.letMeLogin(username, password)
                if (response.success == true) {
                    ServiceBuilder.token = "Bearer " + response.token
                    ServiceBuilder.USER_ID = response.data?._id.toString()
                    withContext(Dispatchers.Main) {
                        setSharedPref()
                        Toast.makeText(this@LoginActivity, "Welcome", Toast.LENGTH_SHORT).show()
                    }
                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Invalid Credentials",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Invalid Credentials",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



    private fun checkRunTimePermission() {
        if (!hasPermission()) {
            requestPermission()
        }
    }

    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
                break
            }
        }
        return hasPermission
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this@LoginActivity, permissions, 1)
    }

}