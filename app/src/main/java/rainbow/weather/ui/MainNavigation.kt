package rainbow.weather.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import rainbow.weather.ui.adding.AddingScreen
import rainbow.weather.ui.home.HomeScreen
import rainbow.weather.ui.list.ListScreen
import rainbow.weather.ui.map.MapScreen

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val componentActivity = LocalContext.current as ComponentActivity
    val viewModel by componentActivity.viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                MainViewModel(componentActivity) as T
        }
    }
    BackHandler { viewModel.routes.removeLast() }
    Box(modifier) {
        when (viewModel.routes.lastOrNull()) {
            "home" -> HomeScreen(viewModel)
            "map" -> MapScreen(viewModel)
            "adding" -> AddingScreen(viewModel)
            "list" -> ListScreen(viewModel)
            null -> componentActivity.finish()
        }
    }
}
