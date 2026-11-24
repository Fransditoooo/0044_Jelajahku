package com.example.jelajahku.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jelajahku.data.model.Destination
import com.example.jelajahku.data.model.Booking
import com.example.jelajahku.ui.admin.AddDestinationScreen
import com.example.jelajahku.ui.components.SearchField
import com.example.jelajahku.ui.home.HomeScreen
import com.example.jelajahku.ui.auth.LoginScreen
import com.example.jelajahku.ui.auth.RegisterScreen
import com.example.jelajahku.ui.auth.AdminLoginScreen
import com.example.jelajahku.ui.destination.DestinationScreen
import com.example.jelajahku.ui.destination.DetailDestinationScreen
import com.example.jelajahku.ui.favorite.FavoriteScreen
import com.example.jelajahku.ui.trip.TripScreen
import com.example.jelajahku.ui.booking.BookingScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.jelajahku.ui.profile.ProfileScreen
import com.example.jelajahku.ui.admin.AdminDashboardScreen

private enum class Route { HOME, EXPLORE, FAVORITES, PROFILE, DETAIL, TRIPS, BOOKINGS, LOGIN, REGISTER, ADMIN_LOGIN, ADMIN, ADD_DESTINATION }
private val destinations = listOf(
    Destination(1, "Pantai Kelingking", "Nusa Penida", "Pantai", "Tebing ikonik dengan pemandangan laut.", 250000),
    Destination(2, "Gunung Bromo", "Probolinggo", "Gunung", "Nikmati matahari terbit yang menakjubkan.", 35000),
    Destination(3, "Lawang Sewu", "Semarang", "Budaya", "Bangunan bersejarah dengan arsitektur khas.", 20000),
    Destination(4, "Pantai Parangtritis", "Yogyakarta", "Pantai", "Pantai dengan gumuk pasir dan sunset indah.", 15000),
    Destination(5, "Pantai Pink", "Lombok", "Pantai", "Pasir pantai berwarna merah muda.", 100000),
    Destination(6, "Pantai Tanjung Tinggi", "Belitung", "Pantai", "Batu granit besar dan air jernih.", 10000),
    Destination(7, "Gunung Rinjani", "Lombok", "Gunung", "Pendakian dengan pemandangan Danau Segara Anak.", 150000),
    Destination(8, "Gunung Prau", "Wonosobo", "Gunung", "Jalur pendakian dengan golden sunrise.", 25000),
    Destination(9, "Gunung Semeru", "Lumajang", "Gunung", "Gunung tertinggi di Pulau Jawa.", 150000),
    Destination(10, "Candi Borobudur", "Magelang", "Budaya", "Warisan budaya dunia dengan relief bersejarah.", 50000),
    Destination(11, "Keraton Yogyakarta", "Yogyakarta", "Budaya", "Pusat budaya dan sejarah Kesultanan Yogyakarta.", 15000),
    Destination(12, "Tanah Lot", "Tabanan", "Budaya", "Pura di atas batu karang dengan sunset ikonik.", 60000),
    Destination(13, "Pempek", "Palembang", "Kuliner", "Kuliner khas Palembang dengan kuah cuko.", 25000),
    Destination(14, "Gudeg Yu Djum", "Yogyakarta", "Kuliner", "Gudeg khas Yogyakarta dengan cita rasa manis.", 30000),
    Destination(15, "Sate Maranggi", "Purwakarta", "Kuliner", "Sate sapi khas Sunda dengan bumbu tradisional.", 35000),
    Destination(16, "Rendang Padang", "Padang", "Kuliner", "Masakan daging khas Minangkabau.", 40000)
)

@Composable
fun AppNavigation() {
    var route by remember { mutableStateOf(Route.HOME) }
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(destinations.first()) }
    var userBookings by remember { mutableStateOf(emptyList<Booking>()) }
    var adminBookings by remember { mutableStateOf(emptyList<Booking>()) }
    val bottomRoutes = setOf(Route.HOME, Route.EXPLORE, Route.FAVORITES, Route.PROFILE)
    Scaffold(bottomBar = { if (route in bottomRoutes) NavigationBar { listOf(Route.HOME to "Beranda", Route.EXPLORE to "Jelajah", Route.FAVORITES to "Booking", Route.PROFILE to "Profil").forEach { (target, label) -> NavigationBarItem(route == target, { route = target }, icon = { Icon(if (target == Route.HOME) Icons.Default.Home else if (target == Route.EXPLORE) Icons.Default.TravelExplore else if (target == Route.FAVORITES) Icons.Default.FavoriteBorder else Icons.Default.Person, label) }, label = { Text(label) }) } } }) { padding ->
        when (route) {
            Route.HOME -> HomeScreen(
                            destinations = destinations,
                            query = query,
                            onQueryChange = { query = it; selectedCategory = "" },
                            onExploreClick = { selectedCategory = ""; query = ""; route = Route.EXPLORE },
                            onCategoryClick = { category -> selectedCategory = category; query = ""; route = Route.EXPLORE },
                            onFavoriteClick = { selected = it; route = Route.DETAIL },
                            modifier = Modifier.padding(padding)
                        )
                        Route.EXPLORE -> ExploreScreen(
                            query = query,
                            onQueryChange = { query = it; selectedCategory = "" },
                            category = selectedCategory,
                            data = destinations
                        ) { selected = it; route = Route.DETAIL }
            Route.FAVORITES -> BookingScreen(
                bookings = userBookings,
                onBookingClick = { booking -> selected = destinations.firstOrNull { destination -> destination.name == booking.destination } ?: selected; route = Route.DETAIL },
                onDelete = { booking -> userBookings = userBookings.filterNot { it.id == booking.id } },
                onConfirm = { booking ->
                    val bookedAt = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
                    val confirmed = booking.copy(status = "Menunggu persetujuan admin", bookedAt = bookedAt)
                    userBookings = userBookings.map { if (it.id == booking.id) confirmed else it }
                    adminBookings = (adminBookings + confirmed).distinctBy { it.id }
                }
            )
            Route.PROFILE -> ProfileScreen({ route = Route.TRIPS }, { route = Route.BOOKINGS }, { route = Route.LOGIN }, { route = Route.ADMIN_LOGIN })
            Route.ADMIN_LOGIN -> AdminLoginScreen(onLoginSuccess = { route = Route.ADMIN }, onBack = { route = Route.PROFILE })
            Route.DETAIL -> DetailScreen(selected, { route = Route.EXPLORE }, {
                if (userBookings.none { it.destination == selected.name }) {
                    userBookings = userBookings + Booking(
                        id = (userBookings.maxOfOrNull { it.id } ?: 0) + 1,
                        destination = selected.name,
                        date = "Pilih tanggal",
                        status = "Draft",
                        totalPrice = selected.price
                    )
                }
                route = Route.FAVORITES
            })
            Route.TRIPS -> SimpleScreen("Trip saya", "Liburan Semarang", "Kota Lama • 12–14 Juni 2026", { route = Route.HOME })
            Route.BOOKINGS -> BookingScreen(
                bookings = userBookings,
                onBookingClick = { booking -> selected = destinations.firstOrNull { destination -> destination.name == booking.destination } ?: selected; route = Route.DETAIL },
                onDelete = { booking -> userBookings = userBookings.filterNot { it.id == booking.id } },
                onConfirm = { booking ->
                    val bookedAt = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
                    val confirmed = booking.copy(status = "Menunggu persetujuan admin", bookedAt = bookedAt)
                    userBookings = userBookings.map { if (it.id == booking.id) confirmed else it }
                    adminBookings = (adminBookings + confirmed).distinctBy { it.id }
                }
            )
            Route.LOGIN -> AuthScreen("Masuk ke JelajahKu", "Masuk", { route = Route.HOME }, { route = Route.REGISTER })
            Route.REGISTER -> AuthScreen("Buat akun baru", "Daftar", { route = Route.LOGIN }, { route = Route.LOGIN })
            Route.ADMIN -> AdminDashboardScreen(
                bookings = adminBookings,
                destinationCount = destinations.size,
                onApproveBooking = { booking ->
                                    val updated = booking.copy(status = "Disetujui")
                                    adminBookings = adminBookings.map { if (it.id == booking.id) updated else it }
                                    userBookings = userBookings.map { if (it.id == booking.id) updated else it }
                                },
                                onRejectBooking = { booking ->
                                    val updated = booking.copy(status = "Ditolak")
                                    adminBookings = adminBookings.map { if (it.id == booking.id) updated else it }
                                    userBookings = userBookings.map { if (it.id == booking.id) updated else it }
                                },
                onAddDestination = { route = Route.ADD_DESTINATION },
                onBack = { route = Route.PROFILE }
            )
            Route.ADD_DESTINATION -> AddDestinationScreen(
                onSave = { name, city, category -> route = Route.ADMIN },
                onBack = { route = Route.ADMIN }
            )
        }
    }
}

@Composable private fun ExploreScreen(query: String, onQueryChange: (String) -> Unit, category: String = "", data: List<Destination> = destinations, onSelect: (Destination) -> Unit) { val result = data.filter { (category.isBlank() || it.category.equals(category, true)) && (query.isBlank() || it.name.contains(query, true) || it.city.contains(query, true)) }; LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { item { Text("Jelajah destinasi", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Spacer(Modifier.height(12.dp)); SearchField(query, onQueryChange) }; items(result) { DestinationItem(it) { onSelect(it) } } } }
@Composable private fun DestinationItem(item: Destination, onClick: () -> Unit) { Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) { ListItem(headlineContent = { Text(item.name, fontWeight = FontWeight.Bold) }, supportingContent = { Text("${item.city} • ${item.category}\nMulai Rp${item.price}") }, leadingContent = { Icon(Icons.Default.TravelExplore, null) }, trailingContent = { Icon(Icons.Default.ChevronRight, null) }) } }
@Composable private fun DetailScreen(item: Destination, onBack: () -> Unit, onTrip: () -> Unit) { Column(Modifier.fillMaxSize().padding(20.dp)) { IconButton(onBack) { Icon(Icons.Default.ArrowBack, "Kembali") }; Text(item.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); Text("${item.city} • ${item.category}", color = MaterialTheme.colorScheme.primary); Spacer(Modifier.height(20.dp)); Card(Modifier.fillMaxWidth().height(180.dp)) { Box(Modifier.fillMaxSize(), Alignment.Center) { Icon(Icons.Default.Landscape, null, Modifier.size(70.dp)) } }; Spacer(Modifier.height(20.dp)); Text(item.description); Spacer(Modifier.height(12.dp)); Text("Harga tiket mulai Rp${item.price}", fontWeight = FontWeight.Bold); Spacer(Modifier.height(20.dp)); Button(onTrip, Modifier.fillMaxWidth()) { Text("Tambah ke Booking") } } }
@Composable private fun ProfileScreen(onTrip: () -> Unit, onBooking: () -> Unit, onLogin: () -> Unit, onAdmin: () -> Unit) { Column(Modifier.fillMaxSize().padding(20.dp)) { Text("Profil", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("Pengguna JelajahKu", style = MaterialTheme.typography.titleLarge); Spacer(Modifier.height(20.dp)); listOf("Trip saya" to onTrip, "Riwayat booking" to onBooking, "Masuk / ganti akun" to onLogin, "Dashboard Admin" to onAdmin).forEach { (label, action) -> Card(onClick = action, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)) { ListItem(headlineContent = { Text(label) }, trailingContent = { Icon(Icons.Default.ChevronRight, null) }) } } } }
@Composable private fun SimpleScreen(title: String, headline: String, detail: String, onBack: () -> Unit) { Column(Modifier.fillMaxSize().padding(20.dp)) { Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Spacer(Modifier.height(18.dp)); Card(Modifier.fillMaxWidth()) { ListItem(headlineContent = { Text(headline, fontWeight = FontWeight.Bold) }, supportingContent = { Text(detail) }, leadingContent = { Icon(Icons.Default.Map, null) }) }; Spacer(Modifier.height(20.dp)); OutlinedButton(onBack, Modifier.fillMaxWidth()) { Text("Kembali") } } }
@Composable private fun AuthScreen(title: String, action: String, onSubmit: () -> Unit, onSecondary: () -> Unit) { var email by remember { mutableStateOf("") }; var password by remember { mutableStateOf("") }; Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) { Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Spacer(Modifier.height(20.dp)); OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("Email") }); Spacer(Modifier.height(12.dp)); OutlinedTextField(password, { password = it }, Modifier.fillMaxWidth(), label = { Text("Password") }); Spacer(Modifier.height(20.dp)); Button(onSubmit, Modifier.fillMaxWidth()) { Text(action) }; TextButton(onSecondary, Modifier.align(Alignment.CenterHorizontally)) { Text("Belum punya akun? Daftar") } } }

