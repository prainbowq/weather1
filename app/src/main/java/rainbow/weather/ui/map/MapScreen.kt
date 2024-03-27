package rainbow.weather.ui.map

import android.content.Context
import android.icu.text.IDNA.Info
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.scale
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
import rainbow.weather.R
import rainbow.weather.ui.MainViewModel
import rainbow.weather.ui.WeatherImage

@Composable
fun MapScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    AndroidView(
        { context ->
            Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
            MapView(context).apply {
                viewModel.weathers.forEach {
                    val marker = Marker(this)
                    marker.icon = ContextCompat.getDrawable(
                        context,
                        when {
                            it.wxList[0].contains('雨') -> R.drawable.raining
                            it.wxList[0].contains('雲') -> R.drawable.cloudy
                            else -> R.drawable.sun
                        }
                    )!!.toBitmap().scale(50, 50).toDrawable(context.resources)
                    marker.infoWindow = object : InfoWindow(ComposeView(context).apply {
                        setContent {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.background(Color.White.copy(alpha = 0.5f))
                            ) {
                                WeatherImage(it.wxList[0], Modifier.size(30.dp))
                                Text("${it.minTList[0]}-${it.maxTList[0]}°C")
                                Text(if (viewModel.language == "zh") it.locationZhName else it.locationEnName)
                            }
                        }
                    }, this) {
                        override fun onOpen(item: Any?) = closeAllInfoWindowsOn(this@apply)
                        override fun onClose() {}
                    }
                    marker.position = GeoPoint(it.lat, it.lon)
                    overlayManager.add(marker)
                }
                controller.setZoom(9.0)
                controller.setCenter(GeoPoint(23.973875, 120.982025))
            }
        },
        modifier
    )
}
