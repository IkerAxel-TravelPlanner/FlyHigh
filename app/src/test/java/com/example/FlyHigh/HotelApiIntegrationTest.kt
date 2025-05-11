package com.example.FlyHigh.data.remote.api

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Integration test that tests the real connection to the API.
 * Note: This requires a running API server and internet connection.
 * You may want to run these tests separately from unit tests.
 */
class HotelApiIntegrationTest {

    private lateinit var apiService: HotelApiService
    private val baseUrl = "http://13.39.162.212/"
    private val groupId = "G09"

    @Before
    fun setup() {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(HotelApiService::class.java)
    }

    @Test
    fun `should return available hotels for given date range`() = runBlocking {
        val startDate = "2025-06-01"
        val endDate = "2025-06-05"

        try {
            val response = apiService.getHotelAvailability(
                groupId = groupId,
                startDate = startDate,
                endDate = endDate
            )

            // Log the full response for debugging
            println("API response: hotels count=${response.hotels.size}")

            // Corrected assertions based on actual API response
            assertNotNull("Hotels list should not be null", response.hotels)
            assertTrue("Hotels list should not be empty", response.hotels.isNotEmpty())

            if (response.hotels.isNotEmpty()) {
                println("First hotel: ${response.hotels.first().name}")
            }

        } catch (e: Exception) {
            fail("API call threw an exception: ${e.message}")
            e.printStackTrace()
        }
    }
}