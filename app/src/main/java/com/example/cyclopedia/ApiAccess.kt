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
 *
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
        val response: HttpResponse =
            client.request("https://api-dev.cyclopedia.goldenrivet.xyz:443/") {
                method = HttpMethod.Post
                url {
                    appendPathSegments("track", "bylocation/")
                    parameters.append("lat", lat.toString())
                    parameters.append("lon", lon.toString())
                }
                headers { append(HttpHeaders.Accept, "application/json") }

            }
        return response.body()
    }

    suspend fun getAllPointOfInterestTypes(): String {
        /**
         * return a list of all Point of Interest types in the database
         */
        val client = HttpClient(Android)
        val response: HttpResponse =
            client.get("https://api-dev.cyclopedia.goldenrivet.xyz:443/poi/show_all_types")
        return response.body()
    }

    suspend fun createPointOfInterestType(poitype: String, poidesc: String): Boolean {
        /**
         * Pass in the short and long descriptions of a new point of interest.
         * Method returns true if the record was processed OK and False if not
         */
        val client = HttpClient(Android)
        val response: HttpResponse =
            client.request("https://api-dev.cyclopedia.goldenrivet.xyz:443/") {
                method = HttpMethod.Post
                url {
                    appendPathSegments("poi", "create_def")
                }
                headers { append(HttpHeaders.Accept, "application/json") }
                setBody("{" +
                        "\"poi_type\": \"$poitype\"," +
                        "\"poi_desc\": \"$poidesc\"" +
                        "}"
                )
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

    suspend fun createPointOfInterestActual(
        latitude: Double, longitude: Double, altitude: Int,
        timestamp: Int, pointypeid: Int,
        comments: String
    ): Boolean {
        /**
         * Create the actual point of interest.  Returns true if the record has processed OK
         */
        val client = HttpClient(Android)
        val response: HttpResponse =
            client.request("https://api-dev.cyclopedia.goldenrivet.xyz:443/") {
                method = HttpMethod.Post
                contentType(ContentType.Application.Json)
                url {
                    appendPathSegments("poi", "create")
                }
                headers { append(HttpHeaders.Accept, "application/json") }
                setBody("{" +
                        "\"latitude\": $latitude,"  +
                        "\"longitude\": $longitude," +
                        "\"altitude\": $altitude," +
                        "\"timestamp\": $timestamp," +
                        "\"comments\": \"$comments\"," +
                        "\"poi_type_id\": $pointypeid" +
                        "}")
            }
        if (response.status.value == 200) {
            return true
        }
        Log.e("CYC_API", response.body())
        return false
    }

    suspend fun getLocalPointOfInterest(lat: Double, lon: Double): String {
        /**
         * Pass lat and lon from the location obj. API handles the processing and returns the data
         *
         * @return is a string that should be converted into a JSON object to extract the data
         */
        val client = HttpClient(Android)
        val response: HttpResponse =
            client.request("https://api-dev.cyclopedia.goldenrivet.xyz:443/") {
                method = HttpMethod.Post
                url {
                    appendPathSegments("poi", "bylocation")
                    parameters.append("lat", lat.toString())
                    parameters.append("lon", lon.toString())
                }
                headers { append(HttpHeaders.Accept, "application/json") }

            }
        return response.body()
    }

    suspend fun getAllTrackRatings(): String {
        /**
         * Gets all tracks and their average ratings.
         * Returns the track name and the average
         *
         * {
         *  "My new track": 3
         * }
         *
         */
        val client = HttpClient(Android)
        val response: HttpResponse =
            client.get("https://api-dev.cyclopedia.goldenrivet.xyz:443/stats/tracks/ratings/")
        return response.body()
    }

    suspend fun getDistance(user_id: Int = 0): String {
        /**
         * Get the total sum of all journey distances recorded.
         * If the user_id is 0 (or not passed) it gets all distance.  If a user_id is passed it gets
         * the specific users total distance
         */
        val client = HttpClient(Android)
        val response: HttpResponse =
            client.request("https://api-dev.cyclopedia.goldenrivet.xyz:443/") {
                method = HttpMethod.Get
                url {
                    appendPathSegments("stats", "distance/")
                    parameters.append("user_id", user_id.toString())

                }
                headers { append(HttpHeaders.Accept, "application/json") }

            }
        if (response.status.value != 200) {
            Log.e("CYC_API", response.body())
        }
        return response.body()
    }

    suspend fun getRankedDistance(): String {
        /**
         * Get the ranking of all users that have recorded a distance.
         * The returned response should be ordered by distance travelled
         * https://api-dev.cyclopedia.goldenrivet.xyz/docs#/default/user_ranking_by_distance_stats_distanceranking_get
         */
        val client = HttpClient(Android)
        val response: HttpResponse =
            client.request("https://api-dev.cyclopedia.goldenrivet.xyz:443/") {
                method = HttpMethod.Get
                url {
                    appendPathSegments("stats", "distanceranking")
                }
                headers { append(HttpHeaders.Accept, "application/json") }

            }
        if (response.status.value != 200) {
            Log.e("CYC_API", response.body())
        }
        return response.body()
    }

    suspend fun getUserDetails(user_id: Int): String {
        /**
         * Get the total sum of all journey distances recorded.
         * If the user_id is 0 (or not passed) it gets all distance.  If a user_id is passed it gets
         * the specific users total distance
         */
        val client = HttpClient(Android)
        val response: HttpResponse =
            client.request("https://api-dev.cyclopedia.goldenrivet.xyz:443/") {
                method = HttpMethod.Get
                url {
                    appendPathSegments("user",user_id.toString())

                }

                headers { append(HttpHeaders.Accept, "application/json") }

            }
        if (response.status.value != 200) {
            Log.e("CYC_API", response.body())
        }
        return response.body()
    }

}
