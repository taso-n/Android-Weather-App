package ge.anikolaishvili.weatherApp

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface MyApi {

    @GET("weather?units=metric&appid=74b44df7e0803bb238419529ce17ee99")
    fun getCurrentWeather(@Query("q") city: String): Call<CurrentWeatherModel>

    @GET("forecast?units=metric&appid=74b44df7e0803bb238419529ce17ee99")
    fun getHourlyWeather(@Query("q") city: String): Call<HourlyWeatherModel>
}