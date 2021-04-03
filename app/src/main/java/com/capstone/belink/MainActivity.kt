package com.capstone.belink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.viewpager2.widget.ViewPager2
import com.capstone.belink.Adapter.CustomFragmentStateAdapter
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.Ui.FragmentEtcetra
import com.capstone.belink.Ui.FragmentFriend
import com.capstone.belink.Ui.FragmentMain
import com.capstone.belink.Ui.FragmentMap
import com.capstone.belink.databinding.ActivityMainBinding
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private var mBinding:ActivityMainBinding?=null
    private val binding get() = mBinding!!

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService



    var fragmentLists = listOf(FragmentMain(), FragmentFriend(), FragmentMap(), FragmentEtcetra())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRetrofit()
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu,menu)
        return true
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    private fun init() {
        var adapter = CustomFragmentStateAdapter(this)
        adapter.fragmentList=fragmentLists

        binding.viewPager.adapter=adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> {
                        binding.bottomNavigationView.selectedItemId = R.id.main
                        supportActionBar?.title="개인"
                    }
                    1 -> {
                        binding.bottomNavigationView.selectedItemId = R.id.friend
                        supportActionBar?.title="친구"
                    }
                    2 -> {
                        binding.bottomNavigationView.selectedItemId = R.id.map
                        supportActionBar?.title="방문장소"
                    }
                    3 -> {
                        binding.bottomNavigationView.selectedItemId = R.id.etcetra
                        supportActionBar?.title="설정"
                    }
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