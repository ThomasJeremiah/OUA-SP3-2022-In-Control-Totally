package com.example.cyclopedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings_screen.*

class SettingsScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Hides the top bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_settings_screen)

        homeBtn.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}