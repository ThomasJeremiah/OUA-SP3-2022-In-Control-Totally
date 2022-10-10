package com.example.cyclopedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.MapFragment


class MainActivity : AppCompatActivity() { //OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val fragment_manager = supportFragmentManager
        val fragment_transaction = fragment_manager.beginTransaction()
        val map_fragment = MapsFragment()
        fragment_transaction.replace(R.id.map, map_fragment).commit()

    }

}