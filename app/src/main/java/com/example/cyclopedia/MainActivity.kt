package com.example.cyclopedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : AppCompatActivity() {

    //Marker position on map
    val position = LatLng(-37.808514, 144.964749)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        with(mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync{
                MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
            }
        }
        //Listeners for all the interactive objects on the screen.
        newsButton.setOnClickListener {
            Toast.makeText (this@MainActivity, "Clicked on news", Toast.LENGTH_SHORT).show()
        }
        mapButton.setOnClickListener{
            Toast.makeText (this@MainActivity, "Clicked on map button", Toast.LENGTH_SHORT).show()
        }
        reportButton.setOnClickListener{
            Toast.makeText (this@MainActivity, "Clicked on report", Toast.LENGTH_SHORT).show()
        }

    }
    private fun setMapLocation(map : GoogleMap) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
            addMarker(MarkerOptions().position(position))
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
                Toast.makeText (this@MainActivity, "Clicked on map screen", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //Below methods are required by MapView component.
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}