package com.example.FlyHigh

import com.example.FlyHigh.data.remote.api.BookApiService
import com.example.FlyHigh.data.remote.api.ResponseBodyDto
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: BookApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getHotelAvailability returns expected ResponseBodyDto`() = runBlocking {
        // Arrange: JSON simulating backend response
        val mockJson = """
            {
              "ok": true,
              "message": "Rooms available",
              "book": null
            }
        """.trimIndent()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(mockJson)

        mockWebServer.enqueue(mockResponse)

        // Act
        val result: ResponseBodyDto = api.getHotelAvailability("G09")

        // Assert
        assertNotNull(result)
        assertEquals(true, result.ok)
        assertEquals("Rooms available", result.message)
        assertEquals(null, result.book)
    }
}
