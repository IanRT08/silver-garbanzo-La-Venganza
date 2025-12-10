package mx.edu.utez.silvergarbanzo2.ui.screens

import android.content.Context
import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import mx.edu.utez.silvergarbanzo2.data.model.Post
import mx.edu.utez.silvergarbanzo2.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    postViewModel: PostViewModel,
    currentUserId: Int,
    onMarkerClick: (Int) -> Unit // Navegar al detalle del post
) {
    val context = LocalContext.current

    // Cargar todas las publicaciones públicas
    LaunchedEffect(Unit) {
        postViewModel.loadPublicPosts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mapa de Lugares") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (postViewModel.posts.isNotEmpty()) {
                AndroidView(
                    factory = { ctx ->
                        initializeOsmdroidConfig(ctx)
                        createMapView(ctx, postViewModel.posts, currentUserId, onMarkerClick)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Mostrar mensaje si no hay publicaciones
                Text(
                    text = "No hay ubicaciones para mostrar",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

private fun initializeOsmdroidConfig(context: Context) {
    Configuration.getInstance().load(
        context,
        context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
    )
}

private fun createMapView(
    context: Context,
    posts: List<Post>,
    currentUserId: Int,
    onMarkerClick: (Int) -> Unit
): MapView {
    val mapView = MapView(context)

    // Configuración básica del mapa
    mapView.setTileSource(TileSourceFactory.MAPNIK)
    mapView.setMultiTouchControls(true)
    mapView.controller.setZoom(15.0)

    // Centrar el mapa en la primera ubicación disponible
    if (posts.isNotEmpty()) {
        val firstPost = posts.first()
        mapView.controller.setCenter(GeoPoint(firstPost.latitud, firstPost.longitud))
    }

    // Agregar marcadores para cada publicación
    posts.forEach { post ->
        val marker = Marker(mapView)
        marker.position = GeoPoint(post.latitud, post.longitud)
        marker.title = post.titulo
        marker.snippet = post.descripcion

        // Determinar el color del marcador según el tipo
        val markerColor = when {
            post.usuarioId == currentUserId && post.esPrivado -> Color.RED // Mis lugares privados
            post.usuarioId == currentUserId -> Color.GREEN // Mis lugares públicos
            else -> Color.BLUE // Lugares de otros usuarios
        }

        // Configurar el ícono del marcador con color
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        // Evento al hacer click en el marcador
        marker.setOnMarkerClickListener { clickedMarker, _ ->
            onMarkerClick(post.id)
            true
        }

        mapView.overlays.add(marker)
    }

    return mapView
}
