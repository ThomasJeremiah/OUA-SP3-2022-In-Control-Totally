package com.example.cyclopedia

import android.content.Intent
import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.cyclopedia.databinding.ActivityExistingRatingsBinding
import kotlinx.android.synthetic.main.activity_existing_ratings.*
import kotlinx.android.synthetic.main.content_scrolling.*
import com.example.cyclopedia.ApiAccess
import kotlinx.coroutines.runBlocking

class ExistingRatings : AppCompatActivity() {

    private lateinit var binding: ActivityExistingRatingsBinding
    private var apiaccess = ApiAccess()
    private var ratingsResponse = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExistingRatingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scrollWindow.text = "No records available to display!"

        runBlocking {
            // TODO:
            ratingsResponse = apiaccess.getAllTrackRatings()
        }
        scrollWindow.text = ratingsResponse


        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}