package ge.anikolaishvili.weatherApp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat


const val API_KEY: String = "74b44df7e0803bb238419529ce17ee99"

class MainActivity : AppCompatActivity() {

    private lateinit var todayIcon: ImageView
    private lateinit var hourlyIcon: ImageView

    private lateinit var georgia: ImageView
    private lateinit var england: ImageView
    private lateinit var jamaica: ImageView

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val shared: SharedPreferences =  getSharedPreferences("currentCity", Context.MODE_PRIVATE)

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(this)

        todayIcon = findViewById(R.id.todayIcon)
        hourlyIcon = findViewById(R.id.hourlyIcon)

        georgia = findViewById(R.id.georgia)
        england = findViewById(R.id.england)
        jamaica = findViewById(R.id.jamaica)

        todayIcon.setOnClickListener {
            Log.d("click", "Curr icon")
            viewPager.setCurrentItem(0, true)
            val city = shared.getString("currentCity", "Tbilisi")
            if (city != null) {
                getCurrentWeather(city)
            }
        }

        hourlyIcon.setOnClickListener {
            Log.d("click","Hourly Weather")
            viewPager.setCurrentItem(1, true)
            val city = shared.getString("currentCity", "Tbilisi")
            if (city != null) {
                getHourlyWeather(city)
            }
        }

        georgia.setOnClickListener {
            Log.d("click","Georgia Weather")

            val edit: SharedPreferences.Editor = shared.edit()
            edit.putString("currentCity", getString(R.string.tbilisi))
            edit.apply()
            val currentCity = shared.getString("currentCity", "Tbilisi")

            findViewById<TextView>(R.id.currentCity).text = currentCity
            handleClick(currentCity)
        }
        england.setOnClickListener {
            Log.d("click","England Weather")

            val edit: SharedPreferences.Editor = shared.edit()
            edit.putString("currentCity", getString(R.string.london))
            edit.apply()
            val currentCity = shared.getString("currentCity", "Tbilisi")
            findViewById<TextView>(R.id.currentCity).text = currentCity
            handleClick(currentCity)

        }
        jamaica.setOnClickListener {
            Log.d("click","Jamaica Weather")

            val edit: SharedPreferences.Editor = shared.edit()
            edit.putString("currentCity", getString(R.string.kingston))
            edit.apply()

            val currentCity = shared.getString("currentCity", "Tbilisi")
            findViewById<TextView>(R.id.currentCity).text = currentCity

            handleClick(currentCity)
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val city = shared.getString("currentCity", "Tbilisi")
                handleClick(city)
                super.onPageSelected(position)
            }
        })


        getCurrentWeather(getString(R.string.tbilisi))

    }

    private fun handleClick(city: String?) {
        if(city == null) {
            handleError()
        } else {
            if (viewPager.currentItem == 1) {
                getHourlyWeather(city)
            } else {
                getCurrentWeather(city)
            }
        }
    }


   private fun getCurrentWeather(city: String) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Log.d("String", "here 111111111111111111111111 $city")
        val api = retrofit.create(MyApi::class.java)
        val getCurrentWeather = api.getCurrentWeather(city)

       getCurrentWeather.enqueue(object : Callback<CurrentWeatherModel>{
            override fun onFailure(call: Call<CurrentWeatherModel>, t: Throwable) {
                handleError()
                Log.d("data", "Failed 77777777777777")
            }

            override fun onResponse(call: Call<CurrentWeatherModel>, response: Response<CurrentWeatherModel>) {
                if(response.isSuccessful){
                    displayDayWeather(response)
                }
            }
        })
   }

    private fun displayDayWeather(response: Response<CurrentWeatherModel>) {
        val weather = response.body()?.weather?.get(0)
        val main = response.body()?.main
        val date1 = response.body()?.dt
        val date = date1.toString() + "000"
        val formatter = SimpleDateFormat("HHmm")
        val hours = formatter.format(date.toLong())
        println(response.body())

        if (weather != null && main != null ) {
            val weatherIcon: ImageView = findViewById(R.id.weatherIcon)
            val url = "https://openweathermap.org/img/wn/${weather.icon}@2x.png"
            findViewById<TextView>(R.id.description).text = weather.description
            Glide.with(this)
                .load(url)
                .circleCrop()
                .into(weatherIcon)
            findViewById<TextView>(R.id.description).text = weather.description
            findViewById<TextView>(R.id.temperature).text = main.temp + '°'
            findViewById<TextView>(R.id.tempValue).text = main.temp + '°'
            findViewById<TextView>(R.id.feelsLike).text = main.feels_like + '°'
            findViewById<TextView>(R.id.humidityVal).text = main.humidity.toString()
            findViewById<TextView>(R.id.pressureVal).text = main.pressure.toString()
            if(hours.toInt() in 600..1800) {
                findViewById<LinearLayout>(R.id.main).setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.dayBackground))
            } else {
                findViewById<LinearLayout>(R.id.main).setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.night))
            }
        }
    }

    private fun getHourlyWeather(city: String) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val api = retrofit.create(MyApi::class.java)
        println(city)
        val getHourlyWeather = api.getHourlyWeather(city)

        getHourlyWeather.enqueue(object : Callback<HourlyWeatherModel> {
            override fun onFailure(call: Call<HourlyWeatherModel>, t: Throwable) {
                handleError()
                Log.d("data", "Failed 44444444444 ${t.message}")
            }

            override fun onResponse(call: Call<HourlyWeatherModel>, response: Response<HourlyWeatherModel>) {
                if(response.isSuccessful){
                    println("successs")
                    displayHourlyWeather(response)
                }
            }

        })
    }

    fun handleError() {
        Toast.makeText(this,  getString(R.string.error), Toast.LENGTH_SHORT).show()
    }

    private fun displayHourlyWeather(response: Response<HourlyWeatherModel>) {
        val list = response.body()?.list ?: return

        val forecastList = findViewById<RecyclerView>(R.id.hourlyList)
        val adapter = HourlyForecastAdapter(list)

        if (forecastList != null) {
            forecastList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            forecastList.adapter = adapter
        }
    }

}