package com.elijahneville.vegez

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
                val context = LocalContext.current
                val statusMessage by viewModel.statusMessage.collectAsState()

                LaunchedEffect(statusMessage) {
                    statusMessage?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        viewModel.clearStatus()
                    }
                }
                
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
            }, modifier = Modifier.fillMaxWidth()) { Text("Save") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateVegetableScreen(navController: NavController, viewModel: VegetableViewModel) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Update Price") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
            })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp).fillMaxWidth()) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Vegetable Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("New Price") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.updateVegetable(name, price.toDoubleOrNull() ?: 0.0)
            }, modifier = Modifier.fillMaxWidth()) { Text("Update") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteVegetableScreen(navController: NavController, viewModel: VegetableViewModel) {
    var name by remember { mutableStateOf("") }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Delete Vegetable") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
        })}
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp).fillMaxWidth()) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Vegetable Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.deleteVegetable(name) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) { Text("Delete") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculateCostScreen(navController: NavController, viewModel: VegetableViewModel) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    val result by viewModel.calculatedCost.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Calculate Cost") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
        })}
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp).fillMaxWidth()) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Vegetable Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Quantity") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.calculateCost(name, quantity.toDoubleOrNull() ?: 0.0)
            }, modifier = Modifier.fillMaxWidth()) { Text("Calculate") }
            
            result?.let { 
                Spacer(modifier = Modifier.height(16.dp))
                Text("Total Cost: $$it", style = MaterialTheme.typography.headlineSmall) 
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintReceiptScreen(navController: NavController, viewModel: VegetableViewModel) {
    var clerkId by remember { mutableStateOf("Admin") }
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    val receipt by viewModel.receiptData.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Print Receipt") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
        })}
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp).fillMaxWidth()) {
            OutlinedTextField(value = clerkId, onValueChange = { clerkId = it }, label = { Text("Cashier ID") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Vegetable Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Quantity") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.calculateReceipt(clerkId, name, quantity.toDoubleOrNull() ?: 0.0)
            }, modifier = Modifier.fillMaxWidth()) { Text("Generate Receipt") }

            receipt?.let {
                Spacer(modifier = Modifier.height(24.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("VEGEZ OFFICIAL RECEIPT", style = MaterialTheme.typography.titleLarge)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Cashier: ${it.cashierName}")
                        Text("Item: $name")
                        Text("Quantity: $quantity")
                        Text("Total Cost: $${it.totalCost}")
                        Text("Amount Given: $${it.amountGiven}")
                        Text("Change Due: $${it.changeDue}")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Thank you for shopping!")
                    }
                }
            }
        }
    }
}

@Composable
fun VegeButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) { Text(text) }
}
