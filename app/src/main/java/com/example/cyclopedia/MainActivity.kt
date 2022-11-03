package com.example.cyclopedia

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import kotlin.collections.ArrayList
import com.example.cyclopedia.ApiAccess
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {

    //Marker position on map
    private var position = LatLng(-37.808514, 144.964749)
    private var location = Location(LocationManager.GPS_PROVIDER)
    private var apiaccess = ApiAccess()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Hides the top bar
        supportActionBar?.hide()
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
            openWebPage("https://www.cyclingnews.com/")

        }
        mapButton.setOnClickListener{
            //Nav to activity (screen)
            startActivity(Intent(this,LocalArea::class.java))
        }
        reportButton.setOnClickListener{
            //Nav to activity (screen)
            startActivity(Intent(this,ReportsScreen::class.java))
        }
        settingsBtn.setOnClickListener{
            //Nav to activity (screen)
            startActivity(Intent(this,SettingsScreen::class.java))
        }
        loginBtn.setOnClickListener{
            //Nav to activity (screen)
            startActivity(Intent(this,LoginScreen::class.java))
        }

        //Write the value of 'position' variable to the screen.
        textView_GPS_coord.text = position.toString()

        //Forces the lat/long value onto the location object. The object is invalid until permission for location have been handled.
        location.latitude = position.latitude
        location.longitude = position.longitude
        upDateAddress(location)

        /**
        runBlocking {
            Log.d("CYC_API", apiaccess.helloworld())
        }**/


    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }

    private fun setMapLocation(map : GoogleMap) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
            addMarker(MarkerOptions().position(position))
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
                Toast.makeText (this@MainActivity, "Clicked on map screen", Toast.LENGTH_SHORT).show()
                updateLocation()
            }
        }
    }
    private fun updateMapLocation(map : GoogleMap) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
            addMarker(MarkerOptions().position(position))
            mapType = GoogleMap.MAP_TYPE_NORMAL
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
    private fun updateLocation() {
        if (LocationTrack(this).canGetLocation) {
            Log.d("LocationTrack.canGetLocation: ","True")
            Toast.makeText(this@MainActivity,"Got location",Toast.LENGTH_SHORT).show()
            Log.d("LocationTrack: ","Fetching lat/long")
            position = LatLng(LocationTrack(this).getLatitude(),LocationTrack(this).getLongitude())
            Log.d("LocationTrack: ","Got lat/long")
            textView_GPS_coord.text = position.toString()
            Log.d("LocationTrack: ","Fetching location object")

            location = LocationTrack(this).getLocationVar()
            Log.d("LocationTrack: ","Got location object")
            Log.d("MainActivity.updateLocation: ","Calling function")
            upDateAddress(location)
            Log.d("MainActivity.updateLocation: ","Returned from function")
            with(mapView) {

                // Set the map ready callback to receive the GoogleMap object
                getMapAsync{
                    MapsInitializer.initialize(applicationContext)
                    setMapLocation(it)
                }
            }

        }
    }
}