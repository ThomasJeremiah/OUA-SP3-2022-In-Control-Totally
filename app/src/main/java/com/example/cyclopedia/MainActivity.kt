package com.example.cyclopedia

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    //Marker position on map
    private val position = LatLng(-37.808514, 144.964749)
    private val location = Location(LocationManager.NETWORK_PROVIDER)

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
        settingsBtn.setOnClickListener{
            Toast.makeText (this@MainActivity, "Clicked on settings", Toast.LENGTH_SHORT).show()
        }
        loginBtn.setOnClickListener{
            Toast.makeText (this@MainActivity, "Clicked on login", Toast.LENGTH_SHORT).show()
        }

        //Write the value of 'position' variable to the screen.
        textView_GPS_coord.text = position.toString()

        //Forces the lat/long value onto the location object. The object is invalid until permission for location have been handled.
        location.latitude = position.latitude
        location.longitude = position.longitude
        upDateAddress(location)



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

    //Pulls address value based on lat/long coordinates.
    //Visual element 'textView_address' is updated from within this function directly. Need to return the array of addresses and handle elsewhere.
    private fun upDateAddress(location: Location){
        var addressList = ArrayList<Address>()
        var addressString = String()
        var geocoder:Geocoder = Geocoder(this, Locale.getDefault())
        addressList = geocoder.getFromLocation(location.latitude,location.longitude,1) as ArrayList<Address>
        textView_address.text =  addressList.get(0).getAddressLine(0)
    }
}