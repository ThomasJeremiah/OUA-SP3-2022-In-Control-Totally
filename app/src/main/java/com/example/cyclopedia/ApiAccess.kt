package com.example.cyclopedia


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
//import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.coroutines.*

/**
 * How to use this:
 *
 * # Instantiate the class
 * private var apiaccess = ApiAccess()
 *
 * # Call all class methods within a runBlocking block like shown below
 *
 *         runBlocking {
 *              apiaccess.helloworld()
 *              }
 */


class ApiAccess {
    suspend fun helloworld(): String {
        /**
         * Basic test for connectivity
         */
        val client = HttpClient(Android)
        val response: HttpResponse = client.get("https://api-dev.cyclopedia.goldenrivet.xyz:443/")
        return response.body()
    }

    suspend fun getLocalTracks(lat: Double, lon: Double): String {
        /**
         * Pass lat and lon from the location obj. API handles the processing and returns the data
         *
         * @return is a string that should be converted into a JSON object to extract the data
         */
        val client = HttpClient(Android)
        val response: HttpResponse = client.request("https://api-dev.cyclopedia.goldenrivet.xyz:443/") {
            method = HttpMethod.Post
            url { appendPathSegments("track", "bylocation/")
                parameters.append("lat", lat.toString())
                parameters.append("lon", lon.toString())
            }
            headers {  append(HttpHeaders.Accept, "application/json") }

        }
        return response.body()
    }

    suspend fun getAllPointOfInterestTypes(): String {
        /**
         * return a list of all Point of Interest types in the database
         */
        val client = HttpClient(Android)
        val response: HttpResponse = client.get("https://api-dev.cyclopedia.goldenrivet.xyz:443/poi/show_all_types")
        return response.body()
    }

    suspend fun createPointOfInterestType(poitype: String, poidesc: String): Boolean {
        /**
         * Pass in the short and long descriptions of a new point of interest.
         * Method returns true if the record was processed OK and False if not
         */
        val client = HttpClient(Android)
        val response: HttpResponse = client.request("https://api-dev.cyclopedia.goldenrivet.xyz:443/") {
            method = HttpMethod.Post
            url { appendPathSegments("poi", "create_def")
                parameters.append("poi_type", poitype)
                parameters.append("poi_desc", poidesc)
            }
            headers {  append(HttpHeaders.Accept, "application/json") }

        }
        // Return True if the record was successfully processed.  False on all other conditions
        if (response.status.value == 200) {
            return true
        }
        // This looks a bit funny without an else statement, but the IDE complains about redundancy
        // when you include it.
        // In the case of a failure, log the response as an error
        Log.e("CYC_API", response.body())
            return false
        }
    }
    suspend fun createPointOfInterestActual(latitude: Double, longitude: Double, altitude: Int,
                                            timestamp: Int, poitypeid: Int,
                                            comments: String): Boolean {
        /**
         * Create the actual point of interest.  Returns true if the record has processed OK
         */
        val client = HttpClient(Android)
        val response: HttpResponse = client.request("https://api-dev.cyclopedia.goldenrivet.xyz:443/") {
            method = HttpMethod.Post
            url {
                appendPathSegments("poi", "create")
                parameters.append("latitude", latitude.toString())
                parameters.append("longitude", longitude.toString())
                parameters.append("altitude", altitude.toString())
                parameters.append("timestamp", timestamp.toString())
                parameters.append("comments", comments)
                parameters.append("poi_type_id", poitypeid.toString())
            }
            headers { append(HttpHeaders.Accept, "application/json") }
        }
        if (response.status.value == 200) {
            return true
        }
        Log.e("CYC_API", response.body())
        return false
    }


}