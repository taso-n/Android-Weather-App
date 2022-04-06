package ge.anikolaishvili.weatherApp

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HourlyForecastAdapter(var list: ArrayList<OneHourlyWeather>): RecyclerView.Adapter<ForecastListItemView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastListItemView {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_list_item, parent, false)
        return ForecastListItemView(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ForecastListItemView, position: Int) {
        var item = list.get(position)

        val date = item.dt.toString() + "000"
        val formatter = java.text.SimpleDateFormat("hh a dd MMM")
        val test = formatter.format(date.toLong())
        holder.date.text = test
        holder.temp.text = item.main.temp + '°'
        holder.desc.text = item.weather[0].description
        val url = "https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png"
        Glide.with(holder.itemView.context)
                .load(url)
                .circleCrop()
                .into(holder.icon)
    }

}

class ForecastListItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var date: TextView = itemView.findViewById<TextView>(R.id.date)
    var temp: TextView = itemView.findViewById<TextView>(R.id.temp)
    var desc: TextView = itemView.findViewById<TextView>(R.id.desc)
    var icon: ImageView = itemView.findViewById<ImageView>(R.id.weatherIcon2)
}

