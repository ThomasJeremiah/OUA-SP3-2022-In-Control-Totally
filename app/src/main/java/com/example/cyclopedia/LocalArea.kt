package com.example.cyclopedia

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.widget.Toast
import androidx.compose.material.Icon
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import kotlinx.android.synthetic.main.activity_local_area.*
import kotlinx.android.synthetic.main.activity_main.mapView
import com.example.cyclopedia.ApiAccess
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_new_report_screen.*
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import java.util.Dictionary

private var position = LatLng(-37.808514, 144.964749)
private var location = Location(LocationManager.GPS_PROVIDER)
private var apiaccess = ApiAccess()
var markerList:ArrayList<MarkerOptions> = ArrayList()
private var poiTypes: JSONArray = JSONArray()

class LocalArea : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Hides the top bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_local_area)
        getPOItypes()
        for (i in 0 until poiTypes.length()){
            Log.d("POI Types", poiTypes.getJSONObject(i).toString())
        }

        with(mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync{
                MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
            }
        }
    }

    private fun setMapLocation(map : GoogleMap) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
            addMarker(MarkerOptions().position(position))
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
                Toast.makeText (this@LocalArea, "Clicked on map screen", Toast.LENGTH_SHORT).show()
            }
        }

        upDatePoints.setOnClickListener{
            //Call API and add markers to map
            getMarkers()
            with(mapView) {
                // Set the map ready callback to receive the GoogleMap object
                getMapAsync{
                    showMarkers(it)
                }
            }
        }
    }

    private fun getMarkers() {
        var markerString: String
        var markerJson: JSONArray
        var poiDictionary = emptyMap<String,String>()
        runBlocking {
            markerString = apiaccess.getLocalPointOfInterest(position.latitude, position.longitude)
        }
        Log.d("API Response body","$markerString")
        markerJson = JSONArray(markerString)
        Log.d("JSON response","$markerJson")
        markerList.clear()
        for (i in 0 until markerJson.length()){
            val item = markerJson.getJSONObject(i)
            Log.d("Response $i","$item")
            for (j in 0 until poiTypes.length()){
                if (item.getInt("poi_type_id") == poiTypes.getJSONObject(j).getInt("poi_type_id")){
                    poiDictionary = mapOf("type_name" to poiTypes.getJSONObject(j).getString("poi_type_name"),"typeDesc" to poiTypes.getJSONObject(j).getString("poi_type_description"))
                }
            }
            val marker = MarkerOptions()
                .position(LatLng(item.getDouble("latitude"),item.getDouble("longitude")))
                .icon(bitmapDescriptorFromVector(this,R.drawable.ic_danger))
                .title(if(poiDictionary != null) poiDictionary["type_name"] else "Undefined!")
                .snippet(item.getString("comments"))

            markerList.add(marker)
        }
    }
    private fun getPOItypes (){
        var typesString: String
        runBlocking {
            typesString  = apiaccess.getAllPointOfInterestTypes()
        }
        Log.d("typesString","$typesString")
        poiTypes = JSONArray(typesString)
    }

    private fun showMarkers(map : GoogleMap){
        for(marker in markerList){
            with(map){
                addMarker(marker)
                Log.d("Marker","${marker}")
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
    fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor? {

        // retrieve the actual drawable
        val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bm = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // draw it onto the bitmap
        val canvas = android.graphics.Canvas(bm)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }


}