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
var loginEmail:Boolean = false
var loginPhone:Boolean = false
var loginPassword:Boolean = false

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Hides the top bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_login_screen)

        editTextEmail.visibility = View.INVISIBLE
        editTextTextPassword.visibility = View.INVISIBLE
        cancelLoginBtn.visibility = View.INVISIBLE


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
            Toast.makeText(this@LoginScreen,"You might be in trouble...",Toast.LENGTH_LONG).show()
        }
        emailLoginBtn.setOnClickListener{
            editTextEmail.visibility = View.VISIBLE
            emailLoginBtn.visibility = View.INVISIBLE
            passwordLoginBtn.visibility = View.INVISIBLE
            cancelLoginBtn.visibility = View.VISIBLE
        }
        passwordLoginBtn.setOnClickListener{
            editTextTextPassword.visibility = View.VISIBLE
            emailLoginBtn.visibility = View.INVISIBLE
            passwordLoginBtn.visibility = View.INVISIBLE
            cancelLoginBtn.visibility = View.VISIBLE
        }
        submitBtn.setOnClickListener{
            if(editTextEmail.visibility == View.VISIBLE){
                loginCredential = editTextEmail.text.toString()
                hideKeyboard()
            } else {
                if (editTextTextPassword.visibility == View.VISIBLE){
                    loginCredential = editTextTextPassword.text.toString()
                    hideKeyboard()
                    Toast.makeText(this@LoginScreen,"$loginCredential",Toast.LENGTH_SHORT).show()
                    loginPassword = true
                } else {
                    Toast.makeText(this@LoginScreen,"Please select a login option",Toast.LENGTH_SHORT).show()
                }
            }
            editTextEmail.visibility = View.INVISIBLE
            editTextTextPassword.visibility = View.INVISIBLE
            emailLoginBtn.visibility = View.VISIBLE
            passwordLoginBtn.visibility = View.VISIBLE
            cancelLoginBtn.visibility = View.INVISIBLE

            //Try and pull out the format (number or email address). Refactor to move this to a discreete function.
            if (loginCredential == "Enter Email address or phone number.") {
                loginCredential = null.toString()
                Toast.makeText(this@LoginScreen,"Please select a login option",Toast.LENGTH_SHORT).show()
                loginEmail = false
                loginPhone = false
                loginPassword = false
            } else {
                if (loginCredential.containsAnyOfIgnoreCase(listOf("@",".com",".net"))){
                    //user entered string contains sub-strings that might be part of an email address. Should refactor this with regex.
                    Toast.makeText(this@LoginScreen,"$loginCredential | Looks like an email" ,Toast.LENGTH_SHORT).show()
                    loginEmail = true
                    loginPhone = false
                    loginPassword = false
                } else {
                    val tempNum = loginCredential.filter { !it.isWhitespace() }
                    try {
                        val tryNum = tempNum.toInt()
                        loginCredential = tryNum.toString()
                        Toast.makeText(this@LoginScreen,"$loginCredential | Looks like a phone number" ,Toast.LENGTH_SHORT).show()
                        loginEmail = false
                        loginPhone = true
                        loginPassword = false
                    } catch (e:Exception) {
                        //Cannot convert value to an integer.
                        Toast.makeText(this@LoginScreen,"Please enter a valid email or phone number" ,Toast.LENGTH_SHORT).show()
                        loginEmail = false
                        loginPhone = false
                        loginPassword = false
                    }
                }
            }

        }
        cancelLoginBtn.setOnClickListener{
            loginCredential = null.toString()
            editTextEmail.visibility = View.INVISIBLE
            editTextTextPassword.visibility = View.INVISIBLE
            emailLoginBtn.visibility = View.VISIBLE
            passwordLoginBtn.visibility = View.VISIBLE
            cancelLoginBtn.visibility = View.INVISIBLE
            hideKeyboard()
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
    private fun Activity.hideKeyboard(view:View){
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
    }
    private fun Activity.hideKeyboard(){
        hideKeyboard(currentFocus?:View(this))
    }
    fun String.containsAnyOfIgnoreCase(keywords: List<String>): Boolean {
        for (keyword in keywords) {
            if (this.contains(keyword, true)) return true
        }
        return false
    }
}