# Vegez - Mobile Distributed Application

**Developed by:**
1. Neville Muchalwa Masheti – 138133
2. Elijah Kyalo - 225390

## Project Description
Vegez is a mobile distributed application designed to communicate with a central vegetable service engine. The system allows clients to manage vegetable pricing data and perform transaction calculations remotely. It follows a client-server architecture where the Android application acts as the client, interacting with a server-side vegetable-price table via a RESTful API.

## Functionalities
The application provides five core functionalities as per the distributed system requirements:

1. **Add Vegetable Price**: Allows the client to add a new vegetable entity with its corresponding price per unit to the server's database.
2. **Update Vegetable Price**: Enables the client to modify the price of an existing vegetable entry on the server.
3. **Delete Vegetable Price**: Provides the ability to remove a vegetable entity from the price table.
4. **Calculate Vegetable Cost**: Clients can query the price of a specific vegetable and provide a quantity. The application communicates with the service engine to calculate and return the total cost.
5. **Print Transaction Receipt**: Generates a comprehensive receipt for a transaction. This includes:
   - Total cost of items.
   - Amount provided by the customer.
   - Change due.
   - Identity of the cashier logged into the machine.

## Technical Architecture
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Networking**: Retrofit & OkHttp for API communication.
- **Navigation**: Jetpack Navigation Compose for seamless screen transitions.
- **Architecture**: MVVM (Model-View-ViewModel) for a clean separation of concerns.
- **API Base URL**: `http://10.0.2.2:8080/vegetable-rmi` (configured for Android Emulator access to localhost).
