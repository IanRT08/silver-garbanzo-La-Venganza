package mx.edu.utez.silvergarbanzo2.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
    onMarkerClick: (Int) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    //Recargar posts públicos cada vez que la pantalla vuelve al frente
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                postViewModel.loadPublicPosts()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    if (postViewModel.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text("No hay ubicaciones para mostrar")
                    }
                }
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

    mapView.setTileSource(TileSourceFactory.MAPNIK)
    mapView.setMultiTouchControls(true)
    mapView.controller.setZoom(15.0)

    if (posts.isNotEmpty()) {
        val firstPost = posts.first()
        mapView.controller.setCenter(GeoPoint(firstPost.latitud, firstPost.longitud))
    }

    posts.forEach { post ->
        val marker = Marker(mapView)
        marker.position = GeoPoint(post.latitud, post.longitud)
        marker.title = post.titulo
        marker.snippet = post.descripcion

        //Determinar el color según el tipo de publicación
        val color = when {
            post.usuarioId == currentUserId && post.esPrivado ->
                android.graphics.Color.RED // Mis lugares privados
            post.usuarioId == currentUserId ->
                android.graphics.Color.GREEN // Mis lugares públicos
            else ->
                android.graphics.Color.BLUE // Lugares de otros usuarios
        }

        //Crear icono personalizado con el color correcto
        marker.icon = createColoredMarkerIcon(context, color)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        marker.setOnMarkerClickListener { clickedMarker, _ ->
            onMarkerClick(post.id)
            true
        }

        mapView.overlays.add(marker)
    }

    return mapView
}

//Crear icono de marcador con color personalizado
private fun createColoredMarkerIcon(context: Context, color: Int): BitmapDrawable {
    val size = 80
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint().apply {
        this.color = color
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    // Dibujar un círculo
    canvas.drawCircle(size / 2f, size / 2f, size / 3f, paint)

    // Borde blanco
    paint.apply {
        this.color = android.graphics.Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    canvas.drawCircle(size / 2f, size / 2f, size / 3f, paint)

    return BitmapDrawable(context.resources, bitmap)
}