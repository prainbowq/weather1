package rainbow.weather.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import rainbow.weather.R
import rainbow.weather.ui.MainViewModel
import rainbow.weather.ui.WeatherImage
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var index by rememberSaveable { mutableStateOf(0) }
    val weather = viewModel.weathers[index]
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val localeIsZh = viewModel.language == "zh"
    var languageDialogShown by rememberSaveable { mutableStateOf(false) }
    if (languageDialogShown) {
        val componentActivity = LocalContext.current as ComponentActivity
        Dialog(onDismissRequest = { languageDialogShown = false }) {
            Column(Modifier.background(Color.White)) {
                ListItem(Modifier.clickable { viewModel.setLanguage(componentActivity, "en") }) {
                    Text(stringResource(R.string.english))
                }
                ListItem(Modifier.clickable { viewModel.setLanguage(componentActivity, "zh") }) {
                    Text(stringResource(R.string.chinese))
                }
            }
        }
    }
    ModalDrawer({
        ListItem(
            Modifier.clickable { viewModel.routes += "list" },
            icon = { Icon(painterResource(R.drawable.pin_drop), null) }
        ) {
            Text(stringResource(R.string.edit_locations))
        }
        viewModel.weathers.forEachIndexed { i, it ->
            ListItem(
                Modifier.clickable {
                    index = i
                    coroutineScope.launch { drawerState.close() }
                },
                icon = { Icon(painterResource(R.drawable.pin_drop), null) }
            ) {
                Text(if (localeIsZh) it.locationZhName else it.locationEnName)
            }
        }
        ListItem(
            Modifier.clickable { languageDialogShown = true },
            icon = { Icon(painterResource(R.drawable.language), null) }
        ) {
            Text(stringResource(R.string.set_language))
        }
    }, drawerState = drawerState, modifier = modifier) {
        Column {
            Box(Modifier.fillMaxWidth()) {
                IconButton(
                    { coroutineScope.launch { drawerState.open() } },
                    Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Default.Menu, null)
                }
                Text(
                    if (localeIsZh) weather.locationZhName else weather.locationEnName,
                    Modifier.align(Alignment.Center)
                )
                Row(Modifier.align(Alignment.CenterEnd)) {
                    IconButton({ viewModel.routes += "map" }) {
                        Icon(painterResource(R.drawable.map), null)
                    }
                    IconButton({ viewModel.routes += "adding" }) {
                        Icon(Icons.Default.Add, null)
                    }
                }
            }
            Column {
                Text(weather.wxList[0])
                Text(buildAnnotatedString {
                    append("${weather.tList[0]}°")
                    append('C')
                })
                Row {
                    Icon(painterResource(R.drawable.navigation), null)
                    Text("${weather.maxTList[0]}°")
                    Icon(painterResource(R.drawable.navigation), null, Modifier.rotate(180f))
                    Text("${weather.minTList[0]}°")
                    Icon(Icons.Default.Person, null)
                    Text("${((weather.maxAtList[0] + weather.minAtList[0]) / 2.0).roundToInt()}°")
                }
                Text(stringResource(R.string.hourly_forecast))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(weather.timeList.size) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${weather.tList[it]}°")
                            WeatherImage(weather.wxList[it], Modifier.size(30.dp))
                            Text(weather.timeList[it].substring(5, 10))
                            Text(weather.timeList[it].substring(11, 16))
                        }
                    }
                }
                LazyColumn {
                    items(weather.timeList.size) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            WeatherImage(weather.wxList[it], Modifier.size(30.dp))
                            Text(weather.wxList[it], Modifier.weight(1f))
                            Text("${weather.maxTList[it]}°")
                            Spacer(Modifier.width(10.dp))
                            Text("${weather.minTList[it]}°", color = Color.Blue)
                        }
                    }
                }
            }
        }
    }
}
