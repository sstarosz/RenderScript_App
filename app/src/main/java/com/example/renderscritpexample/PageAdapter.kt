package com.example.renderscritpexample

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapter(activity: AppCompatActivity, val itemsCount : Int) : FragmentStateAdapter(activity){

    var adusmenTab: AdjustmentsTab = AdjustmentsTab()
    var filtersTab: FiltersTab = FiltersTab()

    override fun getItemCount(): Int {
        return  itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> adusmenTab
            1 -> filtersTab
            else -> Fragment()
        }
    }
}