package rainbow.weather.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import rainbow.weather.R

@Composable
fun WeatherImage(
    wx: String,
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(
            when {
                wx.contains('雨') -> R.drawable.raining
                wx.contains('雲') -> R.drawable.cloudy
                else -> R.drawable.sun
            }
        ),
        null,
        modifier
    )
}
