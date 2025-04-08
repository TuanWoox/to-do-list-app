package com.example.taskapp.model

import java.util.Date
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val dueDate: Date
)

enum class SortOrder {
    BY_NAME, BY_DATE
}