
package com.example.moneymate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.moneymate.ui.theme.moneymateTheme
import com.example.moneymate.ui.theme.Green40
import com.example.moneymate.ui.theme.Red40
import com.example.moneymate.ui.theme.PrimaryBlue
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Switch
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.heightIn


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            moneymateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MoneyNotesApp()
                }
            }
        }
    }
}

// --- Model
data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: Double,
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val time: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
    val type: TransactionType,
    val category: TransactionCategory,
    val notes: String = ""
)

enum class TransactionType {
    INCOME, EXPENSE
}

val CategoryColorMap = mapOf(
    TransactionCategory.SALARY to Color(0xFF4CAF50),        // Green
    TransactionCategory.BUSINESS to Color(0xFF009688),      // Teal
    TransactionCategory.INVESTMENT to Color(0xFF3F51B5),    // Indigo
    TransactionCategory.GIFT to Color(0xFF9C27B0),          // Purple
    TransactionCategory.OTHER_INCOME to Color(0xFF03A9F4),  // Light Blue

    TransactionCategory.FOOD to Color(0xFFFF5722),          // Deep Orange
    TransactionCategory.TRANSPORT to Color(0xFFFFC107),     // Amber
    TransactionCategory.HOUSING to Color(0xFF8D6E63),       // Brown Grey
    TransactionCategory.ENTERTAINMENT to Color(0xFFFF9800), // Orange
    TransactionCategory.SHOPPING to Color(0xFFE91E63),      // Pink
    TransactionCategory.HEALTH to Color(0xFFF44336),        // Red
    TransactionCategory.EDUCATION to Color(0xFF673AB7),     // Deep Purple
    TransactionCategory.OTHER_EXPENSE to Color(0xFF795548)  // Brown
)


// Constants
val incomeCategories = listOf(
    TransactionCategory.SALARY,
    TransactionCategory.BUSINESS,
    TransactionCategory.INVESTMENT,
    TransactionCategory.GIFT,
    TransactionCategory.OTHER_INCOME
)

val expenseCategories = listOf(
    TransactionCategory.FOOD,
    TransactionCategory.TRANSPORT,
    TransactionCategory.HOUSING,
    TransactionCategory.ENTERTAINMENT,
    TransactionCategory.SHOPPING,
    TransactionCategory.HEALTH,
    TransactionCategory.EDUCATION,
    TransactionCategory.OTHER_EXPENSE
)


enum class TransactionCategory(val displayName: String) {
    SALARY("Gaji"),
    BUSINESS("Bisnis"),
    INVESTMENT("Investasi"),
    GIFT("Hadiah"),
    OTHER_INCOME("Pendapatan Lain"),
    FOOD("Makanan"),
    TRANSPORT("Transportasi"),
    HOUSING("Perumahan"),
    ENTERTAINMENT("Hiburan"),
    SHOPPING("Belanja"),
    HEALTH("Kesehatan"),
    EDUCATION("Pendidikan"),
    OTHER_EXPENSE("Pengeluaran Lain")
}

enum class FilterType {
    ALL, INCOME, EXPENSE, CATEGORY, DATE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyNotesApp() {
    val transactions = remember { mutableStateListOf<Transaction>() }
    val showDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val filterType = remember { mutableStateOf(FilterType.ALL) }
    val selectedCategoryFilter = remember { mutableStateOf<TransactionCategory?>(null) }
    val currentScreen = remember { mutableStateOf(Screen.TRANSACTIONS) }

    // Sample data
    LaunchedEffect(Unit) {
        if (transactions.isEmpty()) {
            transactions.addAll(
                listOf(
                    // Income
                    Transaction(
                        title = "Monthly Salary",
                        amount = 5000000.0,
                        type = TransactionType.INCOME,
                        category = TransactionCategory.SALARY,
                        notes = "Salary for April"
                    ),
                    Transaction(
                        title = "Business Profit",
                        amount = 3000000.0,
                        type = TransactionType.INCOME,
                        category = TransactionCategory.BUSINESS,
                        notes = "Online store sales"
                    ),
                    Transaction(
                        title = "Stock Dividends",
                        amount = 1200000.0,
                        type = TransactionType.INCOME,
                        category = TransactionCategory.INVESTMENT,
                        notes = "Dividends from BCA shares"
                    ),
                    Transaction(
                        title = "Gift Money",
                        amount = 500000.0,
                        type = TransactionType.INCOME,
                        category = TransactionCategory.GIFT,
                        notes = "Birthday gift"
                    ),
                    Transaction(
                        title = "Freelance Project",
                        amount = 750000.0,
                        type = TransactionType.INCOME,
                        category = TransactionCategory.OTHER_INCOME,
                        notes = "Graphic design freelance project"
                    ),

                    // Expenses
                    Transaction(
                        title = "Lunch at Cafe",
                        amount = 75000.0,
                        type = TransactionType.EXPENSE,
                        category = TransactionCategory.FOOD,
                        notes = "Lunch with friends"
                    ),
                    Transaction(
                        title = "Motorcycle Gas",
                        amount = 50000.0,
                        type = TransactionType.EXPENSE,
                        category = TransactionCategory.TRANSPORT,
                        notes = "Fuel for motorcycle"
                    ),
                    Transaction(
                        title = "House Rent",
                        amount = 2000000.0,
                        type = TransactionType.EXPENSE,
                        category = TransactionCategory.HOUSING,
                        notes = "April house rental payment"
                    ),
                    Transaction(
                        title = "Cinema Ticket",
                        amount = 100000.0,
                        type = TransactionType.EXPENSE,
                        category = TransactionCategory.ENTERTAINMENT,
                        notes = "Watched the latest movie"
                    ),
                    Transaction(
                        title = "Clothes Shopping",
                        amount = 600000.0,
                        type = TransactionType.EXPENSE,
                        category = TransactionCategory.SHOPPING,
                        notes = "Bought new clothes for Eid"
                    ),
                    Transaction(
                        title = "Health Check-up",
                        amount = 300000.0,
                        type = TransactionType.EXPENSE,
                        category = TransactionCategory.HEALTH,
                        notes = "Annual medical check-up"
                    ),
                    Transaction(
                        title = "Online Course Payment",
                        amount = 400000.0,
                        type = TransactionType.EXPENSE,
                        category = TransactionCategory.EDUCATION,
                        notes = "Android Development course"
                    ),
                    Transaction(
                        title = "Unexpected Expenses",
                        amount = 250000.0,
                        type = TransactionType.EXPENSE,
                        category = TransactionCategory.OTHER_EXPENSE,
                        notes = "Family support"
                    )
                )
            )
        }
    }

    Scaffold(
        containerColor = Color(0xFFF9FBFD), // light white-blue background
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "MoneyMate",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryBlue
                            ),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    },
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .shadow(4.dp, shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                        .height(80.dp),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = PrimaryBlue
                    ),
                    actions = {
                        if (currentScreen.value == Screen.TRANSACTIONS) {
                            var expanded by remember { mutableStateOf(false) }

                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Filled.FilterList,
                                    contentDescription = "Filter",
                                    tint = PrimaryBlue
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("All Transactions") },
                                    onClick = {
                                        filterType.value = FilterType.ALL
                                        selectedCategoryFilter.value = null
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Filled.Home,
                                            contentDescription = null,
                                            tint = if (filterType.value == FilterType.ALL) PrimaryBlue else Color.Gray
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Income") },
                                    onClick = {
                                        filterType.value = FilterType.INCOME
                                        selectedCategoryFilter.value = null
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Filled.ArrowUpward,
                                            contentDescription = null,
                                            tint = if (filterType.value == FilterType.INCOME) Green40 else Color.Gray
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Expense") },
                                    onClick = {
                                        filterType.value = FilterType.EXPENSE
                                        selectedCategoryFilter.value = null
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Filled.ArrowDownward,
                                            contentDescription = null,
                                            tint = if (filterType.value == FilterType.EXPENSE) Red40 else Color.Gray
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("By Category") },
                                    onClick = {
                                        filterType.value = FilterType.CATEGORY
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Filled.Category, // ← Ganti Label jadi Category
                                            contentDescription = null,
                                            tint = if (filterType.value == FilterType.CATEGORY) PrimaryBlue else Color.Gray
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
                                            Icons.Filled.CalendarToday, // ← Ganti DateRange jadi CalendarToday
                                            contentDescription = null,
                                            tint = if (filterType.value == FilterType.DATE) PrimaryBlue else Color.Gray
                                        )
                                    }
                                )
                            }
                        }
                    }
                )

                if (currentScreen.value == Screen.TRANSACTIONS) {
                    BalanceSummary(
                        transactions = transactions,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                            .shadow(2.dp, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    )
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .height(72.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                IconButton(
                    onClick = { currentScreen.value = Screen.TRANSACTIONS },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Transactions",
                            tint = if (currentScreen.value == Screen.TRANSACTIONS) PrimaryBlue else Color.Gray
                        )
                        Text(
                            text = "Transactions",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (currentScreen.value == Screen.TRANSACTIONS) PrimaryBlue else Color.Gray
                        )
                    }
                }

                IconButton(
                    onClick = { currentScreen.value = Screen.REPORTS },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.PieChart,
                            contentDescription = "Reports",
                            tint = if (currentScreen.value == Screen.REPORTS) PrimaryBlue else Color.Gray
                        )
                        Text(
                            text = "Reports",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (currentScreen.value == Screen.REPORTS) PrimaryBlue else Color.Gray
                        )
                    }
                }

                IconButton(
                    onClick = { currentScreen.value = Screen.SETTINGS },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = if (currentScreen.value == Screen.SETTINGS) PrimaryBlue else Color.Gray
                        )
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (currentScreen.value == Screen.SETTINGS) PrimaryBlue else Color.Gray
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentScreen.value == Screen.TRANSACTIONS) {
                FloatingActionButton(
                    onClick = { showDialog.value = true },
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                    modifier = Modifier.shadow(8.dp, shape = CircleShape)
                ) {
                    Icon(Icons.Filled.Add, "Add new transaction")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen.value) {
                Screen.TRANSACTIONS -> {
                    if (transactions.isEmpty()) {
                        EmptyState(onAddTransaction = { showDialog.value = true })
                    } else {
                        TransactionList(
                            transactions = transactions,
                            filterType = filterType.value,
                            selectedCategoryFilter = selectedCategoryFilter.value,
                            onCategoryFilterSelected = { selectedCategoryFilter.value = it },
                            onTransactionDeleted = { transaction ->
                                transactions.remove(transaction)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Transaksi dihapus")
                                }
                            },
                            onTransactionEdited = { updatedTransaction ->
                                transactions.replaceAll {
                                    if (it.id == updatedTransaction.id) updatedTransaction else it
                                }
                                scope.launch {
                                    snackbarHostState.showSnackbar("Transaksi diperbarui")
                                }
                            }
                        )
                    }
                }
                Screen.REPORTS -> {
                    ReportsScreen(transactions = transactions)
                }
                Screen.SETTINGS -> {
                    SettingsScreen()
                }
            }

            if (showDialog.value) {
                AddTransactionDialog(
                    onAddTransaction = { title, amount, type, category, notes ->
                        if (title.isNotBlank() && amount > 0) {
                            transactions.add(0, Transaction(
                                title = title,
                                amount = amount,
                                type = type,
                                category = category,
                                notes = notes
                            ))
                            scope.launch {
                                snackbarHostState.showSnackbar("Transaksi ditambahkan")
                            }
                        }
                        showDialog.value = false
                    },
                    onDismiss = { showDialog.value = false }
                )
            }
        }
    }
}

enum class Screen {
    TRANSACTIONS, REPORTS, SETTINGS
}

@Composable
fun BalanceSummary(
    transactions: List<Transaction>,
    modifier: Modifier = Modifier
) {
    val income = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val expense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    val balance = income - expense

    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Saldo Anda",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = currencyFormat.format(balance),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (balance >= 0) Green40 else Red40
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Pemasukan",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currencyFormat.format(income),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Green40
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Pengeluaran",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currencyFormat.format(expense),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Red40
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(onAddTransaction: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.PieChart,
            contentDescription = "No Transactions",
            tint = PrimaryBlue.copy(alpha = 0.2f),
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Belum ada transaksi!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Anda belum mencatat transaksi apapun.\nTambahkan transaksi pertama Anda untuk memulai.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddTransaction,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .height(48.dp)
                .width(200.dp)
        ) {
            Text("Tambah Transaksi", fontWeight = FontWeight.SemiBold)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionList(
    transactions: List<Transaction>,
    filterType: FilterType,
    selectedCategoryFilter: TransactionCategory?,
    onCategoryFilterSelected: (TransactionCategory) -> Unit,
    onTransactionDeleted: (Transaction) -> Unit,
    onTransactionEdited: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val showDeleteConfirmation = remember { mutableStateOf<Transaction?>(null) }
    val selectedTransactionForEdit = remember { mutableStateOf<Transaction?>(null) }
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }

    val filteredTransactions = when (filterType) {
        FilterType.ALL -> transactions
        FilterType.INCOME -> transactions.filter { it.type == TransactionType.INCOME }
        FilterType.EXPENSE -> transactions.filter { it.type == TransactionType.EXPENSE }
        FilterType.CATEGORY -> selectedCategoryFilter?.let { category ->
            transactions.filter { it.category == category }
        } ?: transactions
        FilterType.DATE -> transactions.sortedByDescending { it.date }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (filterType == FilterType.CATEGORY && selectedCategoryFilter == null) {
            item {
                CategoryFilterGrid(
                    onCategorySelected = onCategoryFilterSelected,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        items(filteredTransactions, key = { it.id }) { transaction ->
            TransactionItemCard(
                transaction = transaction,
                currencyFormat = currencyFormat,
                onDelete = { showDeleteConfirmation.value = transaction },
                onEdit = { selectedTransactionForEdit.value = transaction },
                modifier = Modifier.animateItemPlacement()
            )
        }
    }

    showDeleteConfirmation.value?.let { transaction ->
        DeleteConfirmationDialog(
            onConfirm = {
                onTransactionDeleted(transaction)
                showDeleteConfirmation.value = null
            },
            onDismiss = { showDeleteConfirmation.value = null }
        )
    }

    selectedTransactionForEdit.value?.let { transaction ->
        EditTransactionDialog(
            transaction = transaction,
            onUpdateTransaction = { updatedTransaction ->
                onTransactionEdited(updatedTransaction)
                selectedTransactionForEdit.value = null
            },
            onDismiss = { selectedTransactionForEdit.value = null }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryFilterGrid(
    onCategorySelected: (TransactionCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val incomeCategories = listOf(
        TransactionCategory.SALARY,
        TransactionCategory.BUSINESS,
        TransactionCategory.INVESTMENT,
        TransactionCategory.GIFT,
        TransactionCategory.OTHER_INCOME
    )
    val expenseCategories = listOf(
        TransactionCategory.FOOD,
        TransactionCategory.TRANSPORT,
        TransactionCategory.HOUSING,
        TransactionCategory.ENTERTAINMENT,
        TransactionCategory.SHOPPING,
        TransactionCategory.HEALTH,
        TransactionCategory.EDUCATION,
        TransactionCategory.OTHER_EXPENSE
    )

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Pilih Kategori",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Pemasukan",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Green40
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                incomeCategories.forEach { category ->
                    CategoryChip(
                        category = category,
                        onClick = { onCategorySelected(category) },
                        backgroundColor = Green40.copy(alpha = 0.1f),
                        contentColor = Green40
                    )
                }
            }

            Text(
                text = "Pengeluaran",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Red40
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                expenseCategories.forEach { category ->
                    CategoryChip(
                        category = category,
                        onClick = { onCategorySelected(category) },
                        backgroundColor = Red40.copy(alpha = 0.1f),
                        contentColor = Red40
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: TransactionCategory,
    onClick: () -> Unit,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        border = BorderStroke(1.dp, contentColor.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1
        )
    }
}

@Composable
fun TransactionItemCard(
    transaction: Transaction,
    currencyFormat: NumberFormat,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val amountColor = if (transaction.type == TransactionType.INCOME) Green40 else Red40
    val icon = if (transaction.type == TransactionType.INCOME) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(amountColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = transaction.type.name,
                    tint = amountColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = transaction.category.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (transaction.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = transaction.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = currencyFormat.format(transaction.amount),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = amountColor
                    )
                )

                Text(
                    text = "${transaction.date} • ${transaction.time}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Red40,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    onAddTransaction: (String, Double, TransactionType, TransactionCategory, String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategory by remember { mutableStateOf(TransactionCategory.FOOD) }
    var notes by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Tambah Transaksi",
                style = MaterialTheme.typography.headlineSmall,
                color = PrimaryBlue
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Transaksi") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors(PrimaryBlue)
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { text ->
                        amountText = text.filter { it.isDigit() || it == '.' }
                    },
                    label = { Text("Jumlah") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors(PrimaryBlue),
                    prefix = {
                        Text(
                            text = "Rp",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )

                TransactionTypeSelector(
                    selected = transactionType,
                    onSelect = { transactionType = it }
                )

                CategoryDropdown(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    transactionType = transactionType
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan (opsional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors(PrimaryBlue)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    keyboardController?.hide()
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    if (title.isNotBlank() && amount > 0) {
                        onAddTransaction(title, amount, transactionType, selectedCategory, notes)
                    }
                },
                enabled = title.isNotBlank() && amountText.toDoubleOrNull()?.let { it > 0 } == true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text("Tambah", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.height(48.dp)
            ) {
                Text("Batal", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionDialog(
    transaction: Transaction,
    onUpdateTransaction: (Transaction) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(transaction.title) }
    var amountText by remember { mutableStateOf(transaction.amount.toString()) }
    var transactionType by remember { mutableStateOf(transaction.type) }
    var selectedCategory by remember { mutableStateOf(transaction.category) }
    var notes by remember { mutableStateOf(transaction.notes) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Transaksi",
                style = MaterialTheme.typography.headlineSmall,
                color = PrimaryBlue
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Transaksi") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors(PrimaryBlue)
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { text ->
                        amountText = text.filter { it.isDigit() || it == '.' }
                    },
                    label = { Text("Jumlah") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors(PrimaryBlue),
                    prefix = {
                        Text(
                            text = "Rp",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )

                TransactionTypeSelector(
                    selected = transactionType,
                    onSelect = { transactionType = it }
                )

                CategoryDropdown(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    transactionType = transactionType
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan (opsional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors(PrimaryBlue)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    keyboardController?.hide()
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    if (title.isNotBlank() && amount > 0) {
                        onUpdateTransaction(transaction.copy(
                            title = title,
                            amount = amount,
                            type = transactionType,
                            category = selectedCategory,
                            notes = notes
                        ))
                    }
                },
                enabled = title.isNotBlank() && amountText.toDoubleOrNull()?.let { it > 0 } == true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text("Simpan", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.height(48.dp)
            ) {
                Text("Batal", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}

// 1. TransactionTypeSelector
@Composable
fun TransactionTypeSelector(
    selected: TransactionType,
    onSelect: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = { onSelect(TransactionType.INCOME) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (selected == TransactionType.INCOME)
                    Green40.copy(alpha = 0.1f) else Color.Transparent,
                contentColor = if (selected == TransactionType.INCOME)
                    Green40 else MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(
                1.dp,
                if (selected == TransactionType.INCOME) Green40
                else MaterialTheme.colorScheme.outline
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowUpward,
                contentDescription = "Income",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pemasukan")
        }

        OutlinedButton(
            onClick = { onSelect(TransactionType.EXPENSE) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (selected == TransactionType.EXPENSE)
                    Red40.copy(alpha = 0.1f) else Color.Transparent,
                contentColor = if (selected == TransactionType.EXPENSE)
                    Red40 else MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(
                1.dp,
                if (selected == TransactionType.EXPENSE) Red40
                else MaterialTheme.colorScheme.outline
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDownward,
                contentDescription = "Expense",
                modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pengeluaran")
        }
    }
}

// 2. CategoryDropdown
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    selectedCategory: TransactionCategory,
    onCategorySelected: (TransactionCategory) -> Unit,
    transactionType: TransactionType,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = remember(transactionType) {
        TransactionCategory.values().filter { category ->
            (transactionType == TransactionType.INCOME && category in incomeCategories) ||
                    (transactionType == TransactionType.EXPENSE && category in expenseCategories)
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedCategory.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Kategori") },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, null, modifier = Modifier.clickable {
                    expanded = !expanded
                })
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            shape = RoundedCornerShape(12.dp),
            colors = textFieldColors(PrimaryBlue)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(fraction = 0.9f)
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.displayName) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            if (transactionType == TransactionType.INCOME)
                                Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                            null,
                            tint = if (transactionType == TransactionType.INCOME) Green40 else Red40
                        )
                    }
                )
            }
        }
    }
}

// 3. DeleteConfirmationDialog
@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hapus Transaksi") },
        text = { Text("Apakah Anda yakin ingin menghapus transaksi ini?") },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Red40
                )
            ) {
                Text("Hapus", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun ReportsScreen(transactions: List<Transaction>) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }
    val income = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val expense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    val balance = income - expense

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                "Statistik Pemasukan & Pengeluaran",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

//            TotalSummary(income, expense, currencyFormat)

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                PieChartByCategory(transactions)
            }
        }

        item {
            SummaryCard(income, expense, balance, currencyFormat)
        }

        item {
            ExpandableSection(
                title = "Detail Pemasukan",
                titleColor = Green40
            ) {
                Column {
                    TransactionCategory.values()
                        .filter { it in incomeCategories }
                        .forEach { category ->
                            val sum = transactions.filter { it.category == category }.sumOf { it.amount }
                            if (sum > 0) {
                                CategoryReportItem(category, sum, income, Green40, currencyFormat)
                            }
                        }
                }
            }
        }
//        items(TransactionCategory.values().filter { it in incomeCategories }) { category ->
//            val sum = transactions.filter { it.category == category }.sumOf { it.amount }
//            if (sum > 0) {
//                CategoryReportItem(category, sum, income, Green40, currencyFormat)
//            }
//        }

        item {
            ExpandableSection(
                title = "Detail Pengeluaran",
                titleColor = Red40
            ) {
                Column {
                    TransactionCategory.values()
                        .filter { it in expenseCategories }
                        .forEach { category ->
                            val sum = transactions.filter { it.category == category }.sumOf { it.amount }
                            if (sum > 0) {
                                CategoryReportItem(category, sum, expense, Red40, currencyFormat)
                            }
                        }
                }
            }
        }
//        items(TransactionCategory.values().filter { it in expenseCategories }) { category ->
//            val sum = transactions.filter { it.category == category }.sumOf { it.amount }
//            if (sum > 0) {
//                CategoryReportItem(category, sum, expense, Red40, currencyFormat)
//            }
//        }
    }
}

@Composable
fun ExpandableSection(
    title: String,
    titleColor: Color,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.graphicsLayer {
                    rotationZ = if (expanded) 180f else 0f
                }
            )

        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}


@Composable
fun SummaryCard(income: Double, expense: Double, balance: Double, currencyFormat: NumberFormat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Ringkasan",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            ReportItem("Total Pemasukan", income, Green40, currencyFormat)
            ReportItem("Total Pengeluaran", expense, Red40, currencyFormat)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Saldo Akhir",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (balance >= 0) Green40 else Red40
                )
            )
            Text(
                text = currencyFormat.format(balance),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (balance >= 0) Green40 else Red40
                )
            )
        }
    }
}

//@Composable
//fun TotalSummary(income: Double, expense: Double, currencyFormat: NumberFormat) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceEvenly
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text("Pemasukan", style = MaterialTheme.typography.labelMedium.copy(color = Green40))
//            Text(currencyFormat.format(income), style = MaterialTheme.typography.bodyMedium)
//        }
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text("Pengeluaran", style = MaterialTheme.typography.labelMedium.copy(color = Red40))
//            Text(currencyFormat.format(expense), style = MaterialTheme.typography.bodyMedium)
//        }
//    }
//}



@Composable
fun PieChartByCategory(transactions: List<Transaction>) {
    val incomeData = transactions
        .filter { it.type == TransactionType.INCOME }
        .groupBy { it.category }
        .mapValues { it.value.sumOf { tx -> tx.amount } }

    val expenseData = transactions
        .filter { it.type == TransactionType.EXPENSE }
        .groupBy { it.category }
        .mapValues { it.value.sumOf { tx -> tx.amount } }

    val pieData = (incomeData + expenseData).map { (category, amount) ->
        PieChartData(
            label = category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
            value = amount,
            color = CategoryColorMap[category] ?: PrimaryBlue
        )
    }

    if (pieData.isNotEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Pie chart
            PieChart(pieData)

            Spacer(modifier = Modifier.height(24.dp))

            // Legends (list warna + label)
            pieData.forEach { entry ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(16.dp)
//                            .background(entry.color, shape = CircleShape)
//                    )
//
//                    Spacer(modifier = Modifier.width(8.dp))

//                    Text(
//                        text = entry.label,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
                }
            }
        }
    }
}



data class PieChartData(val label: String, val value: Double, val color: Color)


@Composable
fun PieChart(data: List<PieChartData>) {
    val total = data.sumOf { it.value }
    var startAngle = -90f

    Canvas(
        modifier = Modifier
            .size(220.dp)
            .padding(24.dp)
    ) {
        data.forEach { entry ->
            val sweep = ((entry.value / total) * 360f).toFloat()
            drawArc(
                color = entry.color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true
            )
            startAngle += sweep
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(start = 16.dp)
            .heightIn(max = 200.dp),
        userScrollEnabled = false
    ) {
        items(data) { entry ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(entry.color, CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    entry.label,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}





// 5. SettingsScreen
@Composable
fun SettingsScreen() {
    var darkThemeEnabled by remember { mutableStateOf(false) }
    var currencySymbol by remember { mutableStateOf("Rp") }
    var backupEnabled by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "Pengaturan",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            )
        }

        item {
            SettingsCategory(title = "Tampilan")
        }

        item {
            SettingsCategory(title = "Keuangan")
        }

        item {
            SettingsItem(
                title = "Mata Uang",
                subtitle = "Simbol mata uang yang digunakan",
                action = {
                    OutlinedTextField(
                        value = currencySymbol,
                        onValueChange = { currencySymbol = it },
                        modifier = Modifier.width(80.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            )
        }

        item {
            SettingsCategory(title = "Backup")
        }

        item {
            SettingsItem(
                title = "Backup Otomatis",
                subtitle = "Backup data ke cloud secara otomatis",
                action = {
                    Switch(
                        checked = backupEnabled,
                        onCheckedChange = { backupEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PrimaryBlue,
                            checkedTrackColor = PrimaryBlue.copy(alpha = 0.5f)
                        )
                    )
                }
            )
        }

        item {
            Button(
                onClick = { /* Handle backup now */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Backup Sekarang")
            }
        }
    }
}

// Helper composables for Reports and Settings screens
@Composable
fun ReportItem(label: String, amount: Double, color: Color, currencyFormat: NumberFormat) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(
            currencyFormat.format(amount),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
    }
}

@Composable
fun CategoryReportItem(
    category: TransactionCategory,
    amount: Double,
    total: Double,
    color: Color,
    currencyFormat: NumberFormat
) {
    val percentage = if (total > 0) (amount / total * 100).toInt() else 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(category.displayName, style = MaterialTheme.typography.bodyMedium)
            Text(
                "${currencyFormat.format(amount)} ($percentage%)",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
        }
        LinearProgressIndicator(
            progress = if (total > 0) (amount / total).toFloat() else 0f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color.copy(alpha = 0.5f),
            trackColor = color.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun SettingsCategory(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = PrimaryBlue
        ),
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    action: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        action()
    }
}

// Helper functions
@Composable
fun textFieldColors(focusedColor: Color): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedIndicatorColor = focusedColor,
        focusedLabelColor = focusedColor,
        cursorColor = focusedColor,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
