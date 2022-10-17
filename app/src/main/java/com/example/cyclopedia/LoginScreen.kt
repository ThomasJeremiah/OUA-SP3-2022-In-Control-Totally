package com.example.cyclopedia

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login_screen.*

var loginCredential:String = ""

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Hides the top bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_login_screen)

        editTextEmail.visibility = View.INVISIBLE
        editTextTextPassword.visibility = View.INVISIBLE


        homeBtn.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
        googleLoginBtn.setOnClickListener{
            /* Start OAuth using Google. */
        }
        fbLoginBtn.setOnClickListener{
            /* Start OAuth using Facebook. */
        }
        textViewForgotPassword.setOnClickListener{
            /*Start account recovery process. */
        }
        emailLoginBtn.setOnClickListener{
            editTextEmail.visibility = View.VISIBLE
            emailLoginBtn.visibility = View.INVISIBLE
            passwordLoginBtn.visibility = View.INVISIBLE
        }
        passwordLoginBtn.setOnClickListener{
            editTextTextPassword.visibility = View.VISIBLE
            emailLoginBtn.visibility = View.INVISIBLE
            passwordLoginBtn.visibility = View.INVISIBLE
        }
        submitBtn.setOnClickListener{
            if(editTextEmail.visibility == View.VISIBLE){
                loginCredential = editTextEmail.text.toString()
                hideKeyboard()
                Toast.makeText(this@LoginScreen,"$loginCredential",Toast.LENGTH_SHORT).show()
            } else {
                if (editTextTextPassword.visibility == View.VISIBLE){
                    loginCredential = editTextTextPassword.text.toString()
                    hideKeyboard()
                    Toast.makeText(this@LoginScreen,"$loginCredential",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginScreen,"Please select a login option",Toast.LENGTH_SHORT).show()
                }
            }
            editTextEmail.visibility = View.INVISIBLE
            editTextTextPassword.visibility = View.INVISIBLE
            emailLoginBtn.visibility = View.VISIBLE
            passwordLoginBtn.visibility = View.VISIBLE
        }

        /**Button behaviour changed to independent. Toggle logic left for reference.
        /*Listens for changes in the toggle button group.
        This is independent of the Google and Facebook login options,
        might need to add some logic to have only a single selection between the 4 options, not just the two. */
        this.toggleButton.addOnButtonCheckedListener{ toggleButton,checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.emailBtn -> {
                        //Email or Phone number option selected.
                    }
                    R.id.passwordBtn -> {
                        //Password option selected.
                    }
                }
            } else {
                if (toggleButton.checkedButtonId == View.NO_ID) {
                    //None selected - enforce selection here...
                }
            }
        }*/
    }
    fun Activity.hideKeyboard(view:View){
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
    }
    fun Activity.hideKeyboard(){
        hideKeyboard(currentFocus?:View(this))
    }
}