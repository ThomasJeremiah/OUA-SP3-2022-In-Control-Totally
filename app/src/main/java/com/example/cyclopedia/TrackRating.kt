package com.example.cyclopedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_track_rating.*

class TrackRating : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var rateableTracks:MutableList<String> = ArrayList()
    private val newSpinnerID = 1
    private var selectedTrack:String = ""
    private var ratingPick = 0.0
    private var ratingComment:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_track_rating)

        homeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        submitReportBtn.setOnClickListener{
            ratingPick = ratingBar.rating.toDouble()
            if(textView5.text.toString() != "Comments"){
                ratingComment = textView5.text.toString()
            }
            if(selectedTrack == "" || selectedTrack == "Select a track"){
                textView6.text = "No track selected to rate!!"
                textView6.visibility = View.VISIBLE
            }else{
                textView6.text = """
                    Send to endpoint
                    Track name: $selectedTrack
                    Track rating: ${ratingPick.toString()}
                    Track comments: $ratingComment
                """.trimIndent()
                textView6.visibility = View.VISIBLE
            }

        }



        rateableTracks.add(0,"Select a track")
        for (i in 0 until 5){
            rateableTracks.add("Track $i")
        }

        val aa = ArrayAdapter(this,R.layout.spinner_layout_as_button,rateableTracks)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(trackToRateSpin) {
            adapter = aa
            !aa.isEnabled(0)
            setSelection(0,false)
            onItemSelectedListener = this@TrackRating
            prompt = "Select hazard type"
            gravity = Gravity.CENTER
        }
        val spinner = Spinner(this)
        spinner.id = newSpinnerID



    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (view?.id) {
            1 -> Toast.makeText(this@TrackRating,"Spinner 2 Position: $position amd hazard: ${rateableTracks[position]}",Toast.LENGTH_SHORT).show()
            else -> {
                if (rateableTracks[position] == "Select a track"){
                    Toast.makeText(this@TrackRating,"Please choose a track from the list.",Toast.LENGTH_SHORT).show()
                } else {
                    selectedTrack = rateableTracks[position]
                    textView6.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this@TrackRating,"Nothing selected", Toast.LENGTH_SHORT).show()
    }
}