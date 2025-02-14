package com.example.cyclopedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_reports_screen.*

class ReportsScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Hides the top bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_reports_screen)

        homeBtn.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
        newReportNavBtn.setOnClickListener {
            startActivity(Intent(this, NewReportScreen::class.java))
        }
        newRatingNavBtn.setOnClickListener {
            startActivity(Intent(this, TrackRating::class.java))
        }
        seeRatingsNavBtn.setOnClickListener {
            startActivity(Intent(this, ExistingRatings::class.java))
        }
    }
}