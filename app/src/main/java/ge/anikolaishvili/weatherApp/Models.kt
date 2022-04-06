package ge.anikolaishvili.weatherApp

import com.google.gson.annotations.SerializedName

data class CurrentWeatherModel (val weather: List<OneWeather>,
                                val main: Details,
                                val dt: Int)

data class Details ( val temp: String,
                     val feels_like: String,
                     val pressure: Int,
                     val humidity: Int)

data class HourlyWeatherModel (
    val list: ArrayList<OneHourlyWeather>
)

data class OneHourlyWeather (val dt: Int, val weather: List<OneWeather>, val main: Details)

data class OneWeather (val id: Int, val main: String, val description: String, val icon: String)
