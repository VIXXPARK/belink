package com.capstone.belink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.capstone.belink.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var mBinding:ActivityMainBinding?=null
    private val binding get() = mBinding!!

    private var fragmentList = listOf(FragmentMain(),FragmentFriend(),FragmentMap(),FragmentEtcetra())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        var adapter = CustomFragmentStateAdapter(this)
        adapter.fragmentList=fragmentList
        binding.viewPager.adapter=adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> binding.bottomNavigationView.selectedItemId = R.id.main
                    1 -> binding.bottomNavigationView.selectedItemId = R.id.friend
                    2 -> binding.bottomNavigationView.selectedItemId = R.id.map
                    3 -> binding.bottomNavigationView.selectedItemId = R.id.etcetra
                }
            }
        })

        binding.bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.main -> binding.viewPager.setCurrentItem(0)
                R.id.friend -> binding.viewPager.setCurrentItem(1)
                R.id.map -> binding.viewPager.setCurrentItem(2)
                R.id.etcetra -> binding.viewPager.setCurrentItem(3)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}