package rainbow.weather.ui.adding

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import rainbow.weather.R
import rainbow.weather.ui.MainViewModel

@Composable
fun AddingScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var text by rememberSaveable { mutableStateOf("") }
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton({ viewModel.routes.removeLast() }) {
                Icon(Icons.Default.ArrowBack, null)
            }
            TextField(text, { text = it })
        }
        LazyColumn {
            val localeIsZh = viewModel.language == "zh"
            items(viewModel.allWeathers.filter {
                text.isEmpty() || (if (localeIsZh) it.locationZhName else it.locationEnName).contains(
                    text
                )
            }) {
                val context = LocalContext.current
                Text(
                    if (localeIsZh) it.locationZhName else it.locationEnName,
                    Modifier.clickable {
                        if (viewModel.weathers.contains(it)) {
                            Toast.makeText(
                                context,
                                context.getString(
                                    R.string.duplicated_error,
                                    if (localeIsZh) it.locationZhName else it.locationEnName
                                ),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@clickable
                        }
                        viewModel.addWeather(it)
                    }
                )
            }
        }
    }
}
