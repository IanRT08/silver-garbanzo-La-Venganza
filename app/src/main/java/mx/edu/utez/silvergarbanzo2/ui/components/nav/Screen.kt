package mx.edu.utez.silvergarbanzo2.ui.components.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Map : Screen("mapa", "Mapa", Icons.Default.LocationOn)
    object Profile : Screen("perfil", "Perfil", Icons.Default.AccountBox)
    object MyLocations : Screen("misLugares", "Mis lugares", Icons.Default.Star)
    object Tarjetas : Screen("tarjetas", "Ver publicaciones", Icons.Default.Menu)

    object Login : Screen("login", "Login", Icons.Default.Lock)
    object Register : Screen("register", "Registro", Icons.Default.Person)
}
val navBarItems = listOf(
    Screen.Map,
    Screen.Profile,
    Screen.MyLocations,
    Screen.Tarjetas,
)
