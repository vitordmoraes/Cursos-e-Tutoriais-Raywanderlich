package com.example.travelblog


import android.os.Bundle
import android.os.Handler
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val preferences: BlogPreferences by lazy {
        BlogPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (preferences.isLoggedIn()) {
            startMainActivity()
            finish()
            return
        }


        setContentView(R.layout.activity_login)


        loginButton.setOnClickListener { onLoginClicked ()}

        textUsarnameLayout.editText
            ?.addTextChangedListener(createTextWatcher(textUsarnameLayout))

        textPasswordInput.editText
            ?.addTextChangedListener(createTextWatcher(textPasswordInput))

    }

   private fun onLoginClicked () {
    val usarname: String = textUsarnameLayout.editText?.text.toString()
    val password: String = textPasswordInput.editText?.text.toString()
       if (usarname.isEmpty()) {
           textUsarnameLayout.error = "Usarname must not be empty"
       } else if (password.isEmpty()) {
           passwordInput.error = "Password must not be empty"
       } else if (usarname != "admin" && password != "admin") {
           showErrorDialog()
       } else {
           performLogin()
       }
   }

    private fun performLogin() {
        preferences.setLogginIn(true)
        textUsarnameLayout.isEnabled = false
        textPasswordInput.isEnabled = false
        loginButton.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE

        Handler().postDelayed({
            startMainActivity()
            finish()
        }, 2000)

    }

        private fun startMainActivity() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    private fun showErrorDialog () {
        AlertDialog.Builder(this)
            .setTitle("Login Failed")
            .setMessage("Usarname or Password is not correct. Please Try Again")
            .setPositiveButton("Ok!") {dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun createTextWatcher(textPasswordInput: TextInputLayout): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence,
                                           start: Int, count: Int, after: Int) {
            // not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textPasswordInput.error = null
            }
            

            override fun afterTextChanged(s: Editable) {
                // not needed
            }
        }
    }

}