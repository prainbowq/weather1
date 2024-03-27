package rainbow.weather.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database([Weather::class], version = 1)
@TypeConverters(
    WeatherDatabase.StringListTypeConverter::class,
    WeatherDatabase.IntListTypeConverter::class
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val dao: WeatherDao


    abstract class ListTypeConverter<T> {
        @TypeConverter
        fun listToString(list: List<T>) = list.toString()

        abstract fun stringToList(string: String): List<T>
    }

    class IntListTypeConverter : ListTypeConverter<Int>() {
        @TypeConverter
        override fun stringToList(string: String) =
            string.drop(1).dropLast(1).split(", ").map { it.toInt() }
    }

    class StringListTypeConverter : ListTypeConverter<String>() {
        @TypeConverter
        override fun stringToList(string: String) = string.drop(1).dropLast(1).split(", ")
    }
}
