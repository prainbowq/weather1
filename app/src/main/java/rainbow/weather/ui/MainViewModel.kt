package rainbow.weather.ui

import android.content.Intent
import android.util.Xml
import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.room.Room
import org.json.JSONArray
import org.xmlpull.v1.XmlPullParser
import rainbow.weather.MainActivity
import rainbow.weather.data.Weather
import rainbow.weather.data.WeatherDatabase
import java.util.Locale

class MainViewModel(componentActivity: ComponentActivity) : ViewModel() {
    val sharedPreferences = componentActivity.getSharedPreferences("weather", 0)
    val language = componentActivity.resources.configuration.locale.language.also {
        val savedLanguage = sharedPreferences.getString("language", null)
        if (savedLanguage != null && it != savedLanguage) {
            setLanguage(componentActivity, savedLanguage)
        }
    }
    private val database = Room.databaseBuilder(
        componentActivity,
        WeatherDatabase::class.java,
        "weather.db"
    ).allowMainThreadQueries().build()
    val allWeathers = Xml.newPullParser().run {
        val cityNames = JSONArray(
            componentActivity.assets.open("city.json").readBytes().toString(Charsets.UTF_8)
        ).run {
            List(length()) { getJSONObject(it).run { getString("E-city ") to getString("City") } }
        }
        setInput(componentActivity.assets.open("F-D0047-091.xml"), "UTF-8")
        val result = mutableListOf<Weather>()
        var weather = Weather(
            "",
            "",
            0.0,
            0.0,
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
        val timeList = mutableListOf<String>()
        var elementName = ""
        val elementValues = mutableListOf<String>()
        while (true) {
            when (eventType) {
                XmlPullParser.START_TAG -> when (name) {
                    "locationName" -> {
                        val locationZhName = nextText()
                        weather = weather.copy(
                            locationEnName = cityNames.first { it.second == locationZhName }.first,
                            locationZhName = locationZhName
                        )
                    }
                    "lat" -> weather = weather.copy(lat = nextText().toDouble())
                    "lon" -> weather = weather.copy(lon = nextText().toDouble())
                    "weatherElement" -> {
                        timeList.clear()
                        next()
                    }
                    "startTime" -> timeList += nextText()
                    "elementName" -> elementName = nextText()
                    "value" -> elementValues += nextText()
                    else -> next()
                }
                XmlPullParser.END_TAG -> {
                    when (name) {
                        "location" -> result += weather
                        "weatherElement" -> {
                            weather = weather.copy(timeList = timeList.toList())
                            when (elementName) {
                                "T" -> weather =
                                    weather.copy(tList = elementValues.map { it.toInt() })
                                "MaxT" -> weather =
                                    weather.copy(maxTList = elementValues.map { it.toInt() })
                                "MinT" -> weather =
                                    weather.copy(minTList = elementValues.map { it.toInt() })
                                "MaxAT" -> weather =
                                    weather.copy(maxAtList = elementValues.map { it.toInt() })
                                "MinAT" -> weather =
                                    weather.copy(minAtList = elementValues.map { it.toInt() })
                                "Wx" -> weather =
                                    weather.copy(wxList = elementValues.chunked(2) { it[0] })
                            }
                            elementValues.clear()
                        }
                    }
                    next()
                }
                XmlPullParser.END_DOCUMENT -> break
                else -> next()
            }
        }
        result.toList()
    }
    var weathers by mutableStateOf(database.dao.query().ifEmpty {
        val taipeiWeather = allWeathers.first { it.locationZhName == "臺北市" }
        database.dao.insert(taipeiWeather)
        listOf(taipeiWeather)
    })
    val routes = mutableStateListOf("home")

    fun setLanguage(componentActivity: ComponentActivity, lang: String) {
        sharedPreferences.edit().putString("language", lang).apply()
        val newLocale = when (lang) {
            "zh" -> Locale("zh", "tw")
            else -> Locale("en", "us")
        }
        componentActivity.run {
            resources.updateConfiguration(
                resources.configuration.apply { locale = newLocale },
                resources.displayMetrics
            )
            finish()
            startActivity(Intent(componentActivity, MainActivity::class.java))
        }
    }

    fun addWeather(weather: Weather) {
        weathers = weathers + weather
        database.dao.insert(weather)
    }
}
