// TaskManagerApp.kt
package com.example.taskapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskapp.model.SortOrder
import com.example.taskapp.model.Task
import com.example.taskapp.ui.components.DatePickerComponent
import com.example.taskapp.ui.components.TaskItem
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagerApp() {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var taskName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Date()) }
    var sortOrder by remember { mutableStateOf(SortOrder.BY_DATE) }

    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Manager") },
                actions = {
                    // Sort Menu
                    var expanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Sort"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sort by name") },
                            onClick = {
                                sortOrder = SortOrder.BY_NAME
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Sort by date") },
                            onClick = {
                                sortOrder = SortOrder.BY_DATE
                                expanded = false
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Input section
            OutlinedTextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Task Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // DatePicker
            DatePickerComponent(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            // Add Button
            Button(
                onClick = {
                    if (taskName.isNotBlank()) {
                        val newTask = Task(name = taskName, dueDate = selectedDate)
                        tasks = tasks + newTask
                        taskName = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Task")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tasks List
            Text(
                text = "Tasks",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            val sortedTasks = when (sortOrder) {
                SortOrder.BY_NAME -> tasks.sortedBy { it.name }
                SortOrder.BY_DATE -> tasks.sortedBy { it.dueDate }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sortedTasks) { task ->
                    TaskItem(
                        task = task,
                        dateFormatter = dateFormatter,
                        onDelete = { taskToDelete ->
                            tasks = tasks.filter { it.id != taskToDelete.id }
                        }
                    )
                }
            }
        }
    }
}