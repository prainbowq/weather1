package rainbow.weather.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert
    fun insert(weather: Weather)

    @Query("SELECT * FROM weather")
    fun query(): List<Weather>
}
