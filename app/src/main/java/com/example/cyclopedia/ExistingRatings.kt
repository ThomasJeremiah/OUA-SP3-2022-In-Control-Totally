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
    private var userDetails = ""

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
        var keys = ratingsOject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = ratingsOject.optString(key)
            Log.d("Ratings", "$key: $value")
            ratingsString = "$ratingsString$key: $value\n"
        }

        runBlocking {
            // TODO:
            ratingsResponse = apiaccess.getDistance()
        }
        var communityTotal = ratingsResponse.toDouble()
        ratingsString = "$ratingsString \n Total distance ridden by community: $communityTotal"
        ratingsString = "$ratingsString \n\n User leaderboard:"

        runBlocking {
            // TODO:
            ratingsResponse = apiaccess.getRankedDistance()
        }
        var userRank = JSONArray(ratingsResponse)
        for (i in 0 until  userRank.length()){
            val userID = userRank.getJSONObject(i).getInt("user_id")
            Log.d("UserID", "$userID")
            runBlocking {
                userDetails = apiaccess.getUserDetails(userID)
            }
            Log.d("Ratings", "$userDetails")
            val userObject = JSONObject(userDetails)
            val rank = i + 1
            val userFName = userObject.getString("f_name")
            val userLName = userObject.getString("l_name")
            val distanceTraveled = userRank.getJSONObject(i).getLong("distance_travelled")
            val formattedRanking =
                String.format("%1s %5s %5s %10s", rank, userFName, userLName, distanceTraveled)
            ratingsString = "$ratingsString \n $formattedRanking"
        }




        scrollWindow.text = ratingsString


        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}