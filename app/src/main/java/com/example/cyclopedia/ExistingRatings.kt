package com.example.cyclopedia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.cyclopedia.databinding.ActivityExistingRatingsBinding
import kotlinx.android.synthetic.main.activity_existing_ratings.*
import kotlinx.android.synthetic.main.content_scrolling.*
import com.example.cyclopedia.ApiAccess
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject

class ExistingRatings : AppCompatActivity() {

    private lateinit var binding: ActivityExistingRatingsBinding
    private var apiaccess = ApiAccess()
    private var ratingsResponse = ""
    private var ratingsString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExistingRatingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scrollWindow.text = "No records available to display!"

        runBlocking {
            // TODO:
            ratingsResponse = apiaccess.getAllTrackRatings()
        }

        var ratingsOject = JSONObject(ratingsResponse)
        val keys = ratingsOject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = ratingsOject.optString(key)
            Log.d("Ratings", "$key: $value")
            ratingsString = "$ratingsString$key: $value\n"
        }

        scrollWindow.text = ratingsString


        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}