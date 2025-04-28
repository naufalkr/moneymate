package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.util.UUID
import com.example.todolist.ui.theme.ToDoListTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import java.util.Locale
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.ui.text.font.FontFamily

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoListApp()
                }
            }
        }
    }
}

// --- Model
data class TodoItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val date: String,
    val time: String,
    val priority: Priority,
    val category: String = "Personal",
    val isCompleted: Boolean = false,
    val notes: String = ""
)

enum class Priority {
    TRIVIAL, MODERATE, IMPORTANT
}

enum class FilterType {
    ALL, COMPLETED, UNCOMPLETED, PRIORITY, DATE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListApp() {
    val todos = remember { mutableStateListOf<TodoItem>() }
    val showDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val filterType = remember { mutableStateOf(FilterType.ALL) }
    val primaryColor = Color(0xFF00A1FF)

    LaunchedEffect(Unit) {
        if (todos.isEmpty()) {
            todos.addAll(
                listOf(
                    TodoItem(
                        title = "Complete Jetpack Compose tutorial",
                        date = "2025-04-27",
                        time = "10:00",
                        priority = Priority.IMPORTANT,
                        category = "Work",
                        isCompleted = true,
                        notes = "Finish the advanced Compose animations chapter"
                    ),
                    TodoItem(
                        title = "Design UI for To-Do App",
                        date = "2025-04-28",
                        time = "2:00",
                        priority = Priority.MODERATE,
                        category = "School",
                        notes = "Create mockups for all screens"
                    ),
                    TodoItem(
                        title = "Implement functionality",
                        date = "2025-04-29",
                        time = "4:00",
                        priority = Priority.IMPORTANT,
                        category = "Work",
                        notes = "Add database persistence and state management"
                    ),
                    TodoItem(
                        title = "Test the application",
                        date = "2025-04-30",
                        time = "11:00",
                        priority = Priority.TRIVIAL,
                        category = "Personal",
                        notes = "Write unit tests for all components"
                    ),
                    TodoItem(
                        title = "Deploy to Play Store",
                        date = "2025-05-01",
                        time = "9:00",
                        priority = Priority.IMPORTANT,
                        category = "Work",
                        notes = "Prepare release notes and screenshots"
                    )
                )
            )
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Image(
                            painter = painterResource(id = R.drawable.img),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(128.dp) // Ukuran logo TopBar, bisa disesuaikan lebih kecil/besar
                                .padding(top = 8.dp) // Biar posisi agak ke bawah, ngikutin TopBar yang lebih tinggi
                        )
                    },
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color.White
                                )
                            )
                        )
                        .height(80.dp),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    actions = {
                        var expanded by remember { mutableStateOf(false) }

                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.FilterList,
                                contentDescription = "Filter",
                                tint = primaryColor,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("All Tasks") },
                                onClick = {
                                    filterType.value = FilterType.ALL
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = if (filterType.value == FilterType.ALL) primaryColor else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Completed") },
                                onClick = {
                                    filterType.value = FilterType.COMPLETED
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = if (filterType.value == FilterType.COMPLETED) primaryColor else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Uncompleted") },
                                onClick = {
                                    filterType.value = FilterType.UNCOMPLETED
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = if (filterType.value == FilterType.UNCOMPLETED) primaryColor else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("By Priority") },
                                onClick = {
                                    filterType.value = FilterType.PRIORITY
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = if (filterType.value == FilterType.PRIORITY) primaryColor else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("By Date") },
                                onClick = {
                                    filterType.value = FilterType.DATE
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = if (filterType.value == FilterType.DATE) primaryColor else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            )
                        }

                        if (todos.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        val count = todos.count { it.isCompleted }
                                        todos.removeAll { it.isCompleted }
                                        if (count > 0) {
                                            snackbarHostState.showSnackbar("$count completed tasks removed")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = "Clear completed",
                                    tint = primaryColor,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                )

                Surface(
                    color = primaryColor,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                ) {}
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog.value = true },
                containerColor = primaryColor,
                contentColor = Color.White,
                modifier = Modifier.shadow(8.dp, shape = CircleShape)
            ) {
                Icon(Icons.Filled.Add, "Add new task", modifier = Modifier.size(24.dp))
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (todos.isEmpty()) {
                EmptyState(onAddTask = { showDialog.value = true })
            } else {
                TodoList(
                    todos = todos,
                    filterType = filterType.value,
                    onTodoChecked = { todo, isChecked ->
                        todos.replaceAll {
                            if (it.id == todo.id) it.copy(isCompleted = isChecked)
                            else it
                        }
                    },
                    onTodoDeleted = { todo ->
                        todos.remove(todo)
                        scope.launch {
                            snackbarHostState.showSnackbar("Task deleted")
                        }
                    },
                    onTodoEdited = { updatedTodo ->
                        todos.replaceAll {
                            if (it.id == updatedTodo.id) updatedTodo else it
                        }
                        scope.launch {
                            snackbarHostState.showSnackbar("Task updated")
                        }
                    },
                    primaryColor = primaryColor
                )
            }
        }

        if (showDialog.value) {
            AddTodoDialog(
                onAddTodo = { title, date, time, priority, category, notes ->
                    if (title.isNotBlank()) {
                        todos.add(0, TodoItem(
                            title = title,
                            date = date,
                            time = time,
                            priority = priority,
                            category = category,
                            notes = notes
                        ))
                        scope.launch {
                            snackbarHostState.showSnackbar("Task added")
                        }
                    }
                    showDialog.value = false
                },
                onDismiss = { showDialog.value = false },
                primaryColor = primaryColor
            )
        }
    }
}

@Composable
fun EmptyState(onAddTask: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "No Tasks",
            tint = Color(0xFF00A1FF).copy(alpha = 0.2f),
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No tasks yet!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "You don't have any tasks at the moment.\nAdd your first task to get started.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddTask,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00A1FF),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .height(48.dp)
                .width(200.dp)
        ) {
            Text("Add Task", fontWeight = FontWeight.SemiBold)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoList(
    todos: List<TodoItem>,
    filterType: FilterType,
    onTodoChecked: (TodoItem, Boolean) -> Unit,
    onTodoDeleted: (TodoItem) -> Unit,
    onTodoEdited: (TodoItem) -> Unit,
    primaryColor: Color
) {
    val showDeleteConfirmation = remember { mutableStateOf<TodoItem?>(null) }
    val selectedTodoForEdit = remember { mutableStateOf<TodoItem?>(null) }
    val expandedItems = remember { mutableStateMapOf<String, Boolean>() }

    val filteredTodos = when (filterType) {
        FilterType.ALL -> todos
        FilterType.COMPLETED -> todos.filter { it.isCompleted }
        FilterType.UNCOMPLETED -> todos.filter { !it.isCompleted }
        FilterType.PRIORITY -> todos.sortedByDescending { it.priority }
        FilterType.DATE -> todos.sortedWith(compareBy({ it.date }, { it.time }))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filteredTodos, key = { it.id }) { todo ->
            val isExpanded = expandedItems[todo.id] ?: false

            TodoItemCard(
                todo = todo,
                isExpanded = isExpanded,
                onCheckedChange = { isChecked -> onTodoChecked(todo, isChecked) },
                onDelete = { showDeleteConfirmation.value = todo },
                onEdit = { selectedTodoForEdit.value = todo },
                onToggleExpand = {
                    expandedItems[todo.id] = !isExpanded
                },
                primaryColor = primaryColor,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }

    showDeleteConfirmation.value?.let { todo ->
        DeleteConfirmationDialog(
            onConfirm = {
                onTodoDeleted(todo)
                showDeleteConfirmation.value = null
            },
            onDismiss = { showDeleteConfirmation.value = null },
            primaryColor = primaryColor
        )
    }

    selectedTodoForEdit.value?.let { todo ->
        EditTodoDialog(
            todo = todo,
            onUpdateTodo = { updatedTodo ->
                onTodoEdited(updatedTodo)
                selectedTodoForEdit.value = null
            },
            onDismiss = { selectedTodoForEdit.value = null },
            primaryColor = primaryColor
        )
    }
}

@Composable
fun TodoItemCard(
    todo: TodoItem,
    isExpanded: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onToggleExpand: () -> Unit,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    val priorityColor = when (todo.priority) {
        Priority.IMPORTANT -> Color(0xFFE53935)
        Priority.MODERATE -> Color(0xFFFFB300)
        Priority.TRIVIAL -> Color(0xFF43A047)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
            .border(
                width = 1.dp,
                color = if (todo.isCompleted) MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (todo.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpand() }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = todo.isCompleted,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = primaryColor,
                        uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        checkmarkColor = Color.White
                    ),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (todo.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        else MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    color = priorityColor,
                                    shape = CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = todo.priority.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = priorityColor,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "${todo.date} • ${todo.time}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onToggleExpand,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))

                Column {
                    if (todo.notes.isNotBlank()) {
                        Text(
                            text = todo.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(primaryColor.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = todo.category,
                                style = MaterialTheme.typography.labelSmall,
                                color = primaryColor,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row {
                            IconButton(
                                onClick = onEdit,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit",
                                    tint = primaryColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = onDelete,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFE53935),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditTodoDialog(
    todo: TodoItem,
    onUpdateTodo: (TodoItem) -> Unit,
    onDismiss: () -> Unit,
    primaryColor: Color
) {
    var title by remember { mutableStateOf(todo.title) }
    var date by remember { mutableStateOf(todo.date) }
    var time by remember { mutableStateOf(todo.time) }
    var priority by remember { mutableStateOf(todo.priority) }
    var category by remember { mutableStateOf(todo.category) }
    var notes by remember { mutableStateOf(todo.notes) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Task",
                style = MaterialTheme.typography.headlineSmall,
                color = primaryColor
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors(primaryColor)
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors(primaryColor)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = {},
                        label = { Text("Date") },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showDatePicker = true },
                        enabled = false,
                        readOnly = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors(primaryColor),
                        trailingIcon = {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Pick date",
                                tint = primaryColor
                            )
                        }
                    )

                    OutlinedTextField(
                        value = time,
                        onValueChange = {},
                        label = { Text("Time") },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showTimePicker = true },
                        enabled = false,
                        readOnly = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors(primaryColor),
                        trailingIcon = {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Pick time",
                                tint = primaryColor
                            )
                        }
                    )
                }

                CategoryDropdown(
                    selectedCategory = category,
                    onCategorySelected = { category = it },
                    primaryColor = primaryColor
                )

                PrioritySelector(
                    selected = priority,
                    onSelect = { priority = it },
                    primaryColor = primaryColor
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdateTodo(
                        todo.copy(
                            title = title,
                            date = date,
                            time = time,
                            priority = priority,
                            category = category,
                            notes = notes
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text("Save Changes", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.height(48.dp)
            ) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )

    if (showDatePicker) {
        val calendar = java.util.Calendar.getInstance()
        val datePicker = android.app.DatePickerDialog(
            context,
            { _, year, month, day ->
                date = String.format("%04d-%02d-%02d", year, month + 1, day)
                showDatePicker = false
            },
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH),
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    if (showTimePicker) {
        val calendar = java.util.Calendar.getInstance()
        val timePicker = android.app.TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                time = String.format("%02d:%02d", hourOfDay, minute)
                showTimePicker = false
            },
            calendar.get(java.util.Calendar.HOUR_OF_DAY),
            calendar.get(java.util.Calendar.MINUTE),
            true
        )
        timePicker.show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoDialog(
    onAddTodo: (String, String, String, Priority, String, String) -> Unit,
    onDismiss: () -> Unit,
    primaryColor: Color
) {
    var todoTitle by remember { mutableStateOf("") }
    var todoDate by remember { mutableStateOf("") }
    var todoTime by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MODERATE) }
    var selectedCategory by remember { mutableStateOf("Personal") }
    var todoNotes by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add New Task",
                style = MaterialTheme.typography.headlineSmall,
                color = primaryColor
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = todoTitle,
                    onValueChange = { todoTitle = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors(primaryColor)
                )

                OutlinedTextField(
                    value = todoNotes,
                    onValueChange = { todoNotes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors(primaryColor)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = todoDate,
                        onValueChange = {},
                        label = { Text("Date") },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showDatePicker = true },
                        enabled = false,
                        readOnly = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors(primaryColor),
                        trailingIcon = {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Pick date",
                                tint = primaryColor
                            )
                        }
                    )

                    OutlinedTextField(
                        value = todoTime,
                        onValueChange = {},
                        label = { Text("Time") },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showTimePicker = true },
                        enabled = false,
                        readOnly = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors(primaryColor),
                        trailingIcon = {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Pick time",
                                tint = primaryColor
                            )
                        }
                    )
                }

                // Di dalam Column di AddTodoDialog, ganti OutlinedTextField untuk category dengan:
                CategoryDropdown(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    primaryColor = primaryColor
                )

                PrioritySelector(
                    selected = selectedPriority,
                    onSelect = { selectedPriority = it },
                    primaryColor = primaryColor
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    keyboardController?.hide()
                    onAddTodo(todoTitle, todoDate, todoTime, selectedPriority, selectedCategory, todoNotes)
                },
                enabled = todoTitle.isNotBlank() && todoDate.isNotBlank() && todoTime.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text("Add Task", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.height(48.dp)
            ) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )

    if (showDatePicker) {
        val calendar = java.util.Calendar.getInstance()
        val datePicker = android.app.DatePickerDialog(
            context,
            { _, year, month, day ->
                todoDate = String.format("%04d-%02d-%02d", year, month + 1, day)
                showDatePicker = false
            },
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH),
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    if (showTimePicker) {
        val calendar = java.util.Calendar.getInstance()
        val timePicker = android.app.TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                todoTime = String.format("%02d:%02d", hourOfDay, minute)
                showTimePicker = false
            },
            calendar.get(java.util.Calendar.HOUR_OF_DAY),
            calendar.get(java.util.Calendar.MINUTE),
            true
        )
        timePicker.show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    primaryColor: Color
) {
    val categories = listOf("Work", "Personal", "School", "Shopping", "Others")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            label = { Text("Category") },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            shape = RoundedCornerShape(12.dp),
            colors = textFieldColors(primaryColor),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category,
                            color = if (category == selectedCategory) primaryColor
                            else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    },
                    leadingIcon = {
                        if (category == selectedCategory) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                tint = primaryColor
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PrioritySelector(
    selected: Priority,
    onSelect: (Priority) -> Unit,
    primaryColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Priority",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(Priority.values()) { priority ->
                val isSelected = selected == priority
                val backgroundColor = if (isSelected) {
                    when (priority) {
                        Priority.IMPORTANT -> Color(0xFFE53935)
                        Priority.MODERATE -> Color(0xFFFFB300)
                        Priority.TRIVIAL -> Color(0xFF43A047)
                    }
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }

                val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface

                OutlinedButton(
                    onClick = { onSelect(priority) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = backgroundColor,
                        contentColor = textColor
                    ),
                    border = if (isSelected) BorderStroke(0.dp, Color.Transparent)
                    else BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 100.dp) // minimum lebar supaya gak kekecilan
                        .wrapContentWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = priority.name.lowercase(Locale.getDefault()).replaceFirstChar { it.uppercaseChar() },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    primaryColor: Color
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete Task",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text("Are you sure you want to delete this task?")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor, // pakai warna custom
                    contentColor = Color.White
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun textFieldColors(primaryColor: Color): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = primaryColor,
        unfocusedBorderColor = primaryColor.copy(alpha = 0.5f),
        focusedLabelColor = primaryColor,
        cursorColor = primaryColor
    )
}


@Preview(showBackground = true)
@Composable
fun TodoListPreview() {
    ToDoListTheme {
        TodoListApp()
    }
}