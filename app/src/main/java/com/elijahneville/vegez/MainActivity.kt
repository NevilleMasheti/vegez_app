package com.elijahneville.vegez

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elijahneville.vegez.ui.VegetableViewModel
import com.elijahneville.vegez.ui.theme.VegezTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VegezTheme {
                val navController = rememberNavController()
                val viewModel: VegetableViewModel = viewModel()
                
                NavHost(navController = navController, startDestination = "landing") {
                    composable("landing") { VegetableLandingScreen(navController) }
                    composable("add") { AddVegetableScreen(navController, viewModel) }
                    composable("update") { UpdateVegetableScreen(navController, viewModel) }
                    composable("delete") { DeleteVegetableScreen(navController, viewModel) }
                    composable("calculate") { CalculateCostScreen(navController, viewModel) }
                    composable("receipt") { PrintReceiptScreen(navController, viewModel) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VegetableLandingScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Vegez Engine") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            VegeButton("1. Add A Vegetable Price") { navController.navigate("add") }
            VegeButton("2. Update A vegetable price") { navController.navigate("update") }
            VegeButton("3. Delete A vegetable price") { navController.navigate("delete") }
            VegeButton("4. Calculate Cost") { navController.navigate("calculate") }
            VegeButton("5. Print Receipt") { navController.navigate("receipt") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVegetableScreen(navController: NavController, viewModel: VegetableViewModel) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Vegetable") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp).fillMaxWidth()) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Vegetable Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price per unit") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.addVegetable(name, price.toDoubleOrNull() ?: 0.0)
                navController.popBackStack()
            }, modifier = Modifier.fillMaxWidth()) { Text("Save") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateVegetableScreen(navController: NavController, viewModel: VegetableViewModel) {
    val vegetables by viewModel.vegetables.collectAsState()
    var selectedVeg by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Update Price") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
            })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            Text("Select Vegetable to Update:")
            LazyColumn {
                items(vegetables.keys.toList()) { name ->
                    Button(onClick = { selectedVeg = name; price = vegetables[name].toString() }) { Text(name) }
                }
            }
            if (selectedVeg.isNotEmpty()) {
                Text("Updating: $selectedVeg")
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("New Price") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                Button(onClick = {
                    viewModel.updateVegetable(selectedVeg, price.toDoubleOrNull() ?: 0.0)
                    navController.popBackStack()
                }) { Text("Update") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteVegetableScreen(navController: NavController, viewModel: VegetableViewModel) {
    val vegetables by viewModel.vegetables.collectAsState()
    Scaffold(
        topBar = { TopAppBar(title = { Text("Delete Vegetable") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
        })}
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(vegetables.keys.toList()) { name ->
                ListItem(
                    headlineContent = { Text(name) },
                    trailingContent = {
                        IconButton(onClick = { viewModel.deleteVegetable(name) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculateCostScreen(navController: NavController, viewModel: VegetableViewModel) {
    val vegetables by viewModel.vegetables.collectAsState()
    var selectedVeg by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Double?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Calculate Cost") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
        })}
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            Text("Select Vegetable:")
            LazyColumn(modifier = Modifier.height(200.dp)) {
                items(vegetables.keys.toList()) { name ->
                    Button(onClick = { selectedVeg = name }) { Text(name) }
                }
            }
            if (selectedVeg.isNotEmpty()) {
                Text("Selected: $selectedVeg")
                OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Quantity") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                Button(onClick = {
                    result = viewModel.calculateCost(selectedVeg, quantity.toDoubleOrNull() ?: 0.0)
                }) { Text("Calculate") }
                result?.let { Text("Total Cost: $it", style = MaterialTheme.typography.headlineSmall) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintReceiptScreen(navController: NavController, viewModel: VegetableViewModel) {
    // For simplicity, we'll simulate a transaction receipt
    Scaffold(
        topBar = { TopAppBar(title = { Text("Print Receipt") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
        })}
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("VEGEZ RECEIPT", style = MaterialTheme.typography.titleLarge)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Cashier: Admin (Logged In)")
                    Text("Total Cost: $150.00")
                    Text("Amount Given: $200.00")
                    Text("Change Due: $50.00")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Thank you for shopping!")
                }
            }
        }
    }
}

@Composable
fun VegeButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) { Text(text) }
}
