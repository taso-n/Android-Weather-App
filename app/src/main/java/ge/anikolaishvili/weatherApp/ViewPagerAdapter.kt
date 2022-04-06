package ge.anikolaishvili.weatherApp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2

class ViewPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    var fragmentsList = arrayListOf<Fragment>(CurrentWeather(), HourlyFragment())

    override fun getItemCount(): Int {
        return fragmentsList.size
    }

    override fun createFragment(position: Int): Fragment {
        println("create fragment $position")
        return fragmentsList[position]
    }
}
