package com.example.FlyHigh.data.remote.mapper

import com.example.FlyHigh.data.remote.dto.BookDto
import com.example.FlyHigh.domain.model.Book


// mapper
fun BookDto.toDomain() = Book(id, tittle, description, author)
fun Book.toDto()       = BookDto(id, title, description, author)
