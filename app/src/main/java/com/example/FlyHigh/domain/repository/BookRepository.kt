package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.data.remote.api.BookApiService
import com.example.FlyHigh.domain.model.Book
import com.example.FlyHigh.data.remote.mapper.toDomain
import com.example.FlyHigh.data.remote.mapper.toDto
import com.example.FlyHigh.domain.repository.BookInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val api: BookApiService
) : BookInterface {

    private val gid = "axel"                       // hard-coded group

    override suspend fun list(): List<Book> =
        api.getBooks(gid).map { it.toDomain() }

    override suspend fun add(b: Book): Book =
        api.createBook(gid, b.toDto()).book!!.toDomain()

    override suspend fun update(b: Book): Book =
        api.updateBook(gid, b.id!!, b.toDto()).book!!.toDomain()

    override suspend fun delete(id: Int) {
        api.deleteBook(gid, id)
    }
}