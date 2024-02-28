package com.example.project1

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
class ImageSliderAdapter(
    fragmentManager: FragmentManager,
    private val imageUrls: List<String>
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_SET_USER_VISIBLE_HINT) {

    override fun getCount(): Int = imageUrls.size

    override fun getItem(position: Int): Fragment {
        return ImageSliderFragment.newInstance(imageUrls[position])
    }
}
