package rainbow.weather.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(
    @PrimaryKey val locationEnName: String,
    val locationZhName: String,
    val lat: Double,
    val lon: Double,
    val timeList: List<String>,
    val tList: List<Int>,
    val maxTList: List<Int>,
    val minTList: List<Int>,
    val maxAtList: List<Int>,
    val minAtList: List<Int>,
    val wxList: List<String>
)
