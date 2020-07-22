package com.harry.example.assignment.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.harry.example.assignment.R
import com.harry.example.assignment.adapter.ViewPagerAdapter
import com.harry.example.assignment.utility.EMAIL
import com.harry.example.assignment.utility.USER_LOGOUT
import com.harry.example.assignment.utility.Utility
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header.view.*
import org.koin.android.ext.android.inject


class HomeActivity : AppCompatActivity() {
    private var useremail: String? = null
    private val utility: Utility by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val intent = getIntent()
        if (intent.hasExtra(EMAIL)) {
            useremail = intent.getStringExtra(EMAIL)
        }
        val viewPagerAdapter = ViewPagerAdapter(useremail, supportFragmentManager, lifecycle)
        view_pager.adapter = viewPagerAdapter
        TabLayoutMediator(tab_layout, view_pager, { tab, position ->
            run {
                when (position) {
                    0 -> tab.text = getTabTitle(R.string.contact_us)
                    1 -> tab.text = getTabTitle(R.string.images)
                    else -> tab.text = getTabTitle(R.string.view_images)
                }

            }
        }).attach()
        show_menu.setOnClickListener {
            if (!drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        navigation_view.setCheckedItem(R.id.logout)
        navigation_view.setNavigationItemSelectedListener {
            observeLogout()
            if (it.itemId == R.id.logout) {
                utility.logoutUser(this)
            }
            return@setNavigationItemSelectedListener true
        }
        navigation_view.getHeaderView(0).user_email.text = useremail
    }

    private fun getTabTitle(id: Int): String {
        return applicationContext.resources.getString(id)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun observeLogout() {
        utility.logoutResposne.observe(this, Observer {
            Log.i("TAG", "observeLogout: ")
            if (it == USER_LOGOUT) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        })
    }

}