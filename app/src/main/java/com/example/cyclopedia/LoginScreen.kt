package com.example.cyclopedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login_screen.*



class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Hides the top bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_login_screen)

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
        }
    }

}