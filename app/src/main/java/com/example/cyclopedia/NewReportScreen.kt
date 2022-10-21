package com.example.cyclopedia

import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_new_report_screen.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_report_screen.mapView
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


//Marker position on map
private var position = LatLng(-37.808514, 144.964749)
private var location = Location(LocationManager.GPS_PROVIDER)
private var markerPos: Marker? = null
private var hazardType: String? = null
private var apiaccess = ApiAccess()

class NewReportScreen : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var hazChoices:MutableList<String> = ArrayList()
    private val NEW_SPINNER_ID = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Hides the top bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_new_report_screen)
        with(mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync{
                MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
            }
        }

        //Janky way of setting default text to spinner
        hazChoices.add(0,"Type of hazard")
        //Add rest of options - move this to a function...
        hazChoices.add("Tree")
        hazChoices.add("Rock")
        hazChoices.add("Stick")
        hazChoices.add("Crash")

        homeBtn.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
        submitReportBtn.setOnClickListener{
            Toast.makeText(this@NewReportScreen,"Submit button clicked",Toast.LENGTH_SHORT).show()
            var lat = markerPos!!.position.latitude
            Log.e("Lat","$lat")
            var lon = markerPos!!.position.longitude
            Log.e("Lat","$lon")
            var alt = 0
            var ts = 12345
            var pointTypeID = 1
            var comments = "None supplied"
            try {
                runBlocking {
                    apiaccess.createPointOfInterestActual(lat, lon, alt, ts, pointTypeID, comments)
                }
            } catch (e: Exception) {
                Log.e("API Post ","Failed")
                e.printStackTrace()
            }
        }


        val aa = ArrayAdapter(this,R.layout.spinner_layout_as_button,hazChoices)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(hazTypeSpin) {
            adapter = aa
            !aa.isEnabled(0)
            setSelection(0,false)
            onItemSelectedListener = this@NewReportScreen
            prompt = "Select hazard type"
            gravity = Gravity.CENTER
        }

        val spinner = Spinner(this)
        spinner.id = NEW_SPINNER_ID
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this@NewReportScreen,"Nothing selected",Toast.LENGTH_SHORT).show()
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (view?.id) {
            1 -> Toast.makeText(this@NewReportScreen,"Spinner 2 Position: ${position} amd hazard: ${hazChoices[position]}",Toast.LENGTH_SHORT).show()
            else -> {
                Toast.makeText(this@NewReportScreen,"Spinner 1 Position: ${position} amd hazard: ${hazChoices[position]}",Toast.LENGTH_SHORT).show()
                hazardType = hazChoices[position]
                Log.d("Hazard choice: ","$hazardType")
            }
        }
    }
    private fun setMapLocation(map : GoogleMap) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
            markerPos = addMarker(MarkerOptions().position(position))
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
                Toast.makeText (this@NewReportScreen, "Clicked on map screen", Toast.LENGTH_SHORT).show()
                updateLocation()
            }
            setOnMapLongClickListener {
                Toast.makeText(this@NewReportScreen,"This is a long click",Toast.LENGTH_SHORT).show();
                if (markerPos != null){
                    markerPos?.remove()
                }
                markerPos = addMarker(MarkerOptions()
                    .position(it)
                    .title("Hazard")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                )
                Log.d("Marker","${markerPos?.position}")
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


    private fun updateLocation() {
        if (LocationTrack(this).canGetLocation) {
            position = LatLng(LocationTrack(this).getLatitude(),LocationTrack(this).getLongitude())
            location = LocationTrack(this).getLocationVar()
            with(mapView) {

                // Set the map ready callback to receive the GoogleMap object
                getMapAsync{
                    MapsInitializer.initialize(applicationContext)
                    setMapLocation(it)
                }
            }

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun dateFormatter(): Long {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyMMddHHmmss")
        return current.format(formatter).toLong()
    }
}