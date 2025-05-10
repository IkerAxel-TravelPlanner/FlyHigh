package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.domain.model.Book

interface BookInterface {

    /** List all books of the current group. */
    suspend fun list(): List<Book>

    /** Add a new book (the backend generates the id). */
    suspend fun add(book: Book): Book

    /** Replace the book whose id matches `book.id`. */
    suspend fun update(book: Book): Book

    /** Delete the book by id. */
    suspend fun delete(id: Int)
}