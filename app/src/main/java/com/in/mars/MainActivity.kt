package com.`in`.mars

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.FragmentPagerAdapter
import com.`in`.mars.adapters.AdapterForViewPager
import com.`in`.mars.ui.FragmentUploadedImages


class MainActivity : AppCompatActivity() {
    private var mAdapter: AdapterForViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAdapter = AdapterForViewPager(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        mAdapter!!.addFragment(FragmentUpload.newInstance(), "Upload")
        mAdapter!!.addFragment(FragmentUploadedImages.newInstance(), "Uploaded Images")
        viewPager!!.adapter = mAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)
    }
}