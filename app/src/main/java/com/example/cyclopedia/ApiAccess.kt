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
}