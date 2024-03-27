package rainbow.weather.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import rainbow.weather.R
import rainbow.weather.ui.MainViewModel
import rainbow.weather.ui.WeatherImage

@Composable
fun ListScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton({ viewModel.routes.removeLast() }) {
                Icon(Icons.Default.ArrowBack, null)
            }
            Text(stringResource(R.string.edit_locations), Modifier.weight(1f))
            TextButton(onClick = { /*TODO*/ }) {
                Text(stringResource(R.string.edit))
            }
        }
        Text(
            stringResource(R.string.enter_city_name),
            Modifier
                .clickable { viewModel.routes += "adding" }
                .background(Color.LightGray)
                .padding(10.dp)
                .fillMaxWidth()
        )
        LazyColumn {
            items(viewModel.weathers) {
                Box(
                    Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                ) {
                    Text(if (viewModel.language == "zh") it.locationZhName else it.locationEnName)
                    Text("03/27", Modifier.align(Alignment.BottomStart))
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            WeatherImage(it.wxList[0], Modifier.size(30.dp))
                            Text("${it.tList[0]}°C")
                        }
                        Text(it.wxList[0])
                    }
                }
            }
        }
    }
}
