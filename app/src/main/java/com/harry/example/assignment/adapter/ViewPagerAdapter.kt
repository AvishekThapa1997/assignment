package com.harry.example.assignment.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.harry.example.assignment.R
import com.harry.example.assignment.ui.fragments.ContactUs
import com.harry.example.assignment.ui.fragments.Images
import com.harry.example.assignment.ui.fragments.ViewImages
import com.harry.example.assignment.utility.EMAIL

class ViewPagerAdapter(
    private val useremail : String?,
    private val fragmentManager: FragmentManager,
    private val lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 ->{
                val contactUs = ContactUs()
                val bundle = Bundle()
                bundle.putString(EMAIL,useremail)
                contactUs.arguments = bundle
                return contactUs
            }
            1 -> return Images()
            else -> return ViewImages()
        }
    }
}