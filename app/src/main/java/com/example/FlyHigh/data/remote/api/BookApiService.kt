package com.example.FlyHigh.data.remote.api

import com.example.FlyHigh.data.remote.dto.BookDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {

    @GET("{group_id}/book")
    suspend fun getBookById(@Path("group_id") groupId: String, @Query("id") id: Int): BookDto

    @POST("{group_id}/book/create")
    suspend fun createBook( @Path("group_id") groupId: String, @Body book: BookDto): ResponseBodyDto

    @PUT("{group_id}/book/update")
    suspend fun updateBook(
        @Path("group_id") groupId: String,
        @Query("id") id: Int,
        @Body book: BookDto
    ): ResponseBodyDto

    @DELETE("{group_id}/book/delete")
    suspend fun deleteBook(@Path("group_id") groupId: String,@Query("id") id: Int): ResponseBodyDto

    @GET("{group_id}/books")
    suspend fun getBooks(@Path("group_id") groupId: String): List<BookDto>

    @GET("hotels/{group_id}/availability")
    suspend fun getHotelAvailability(@Path("group_id") groupId: String): ResponseBodyDto

}

/* DTO for message wrapper */
data class ResponseBodyDto(
    val ok: Boolean,
    val message: String,
    val book: BookDto?
)