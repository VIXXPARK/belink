package com.capstone.belink

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.capstone.belink.Adapter.FragmentStateAdapter
import com.capstone.belink.Interface.IOnBackPressed
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.Ui.*
import com.capstone.belink.databinding.ActivityMainBinding
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private var mBinding:ActivityMainBinding?=null
    private val binding get() = mBinding!!

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService


    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor



    var fragmentLists = listOf(FragmentMain(), FragmentFriend(), FragmentMap(), FragmentEtcetra())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        pref =getSharedPreferences("auto", Activity.MODE_PRIVATE)!!
        prefEdit=pref.edit()

        initRetrofit()
        init()
    }



    override fun onBackPressed() {
//        val fragment = this.supportFragmentManager.findFragmentById(R.id.frag_main)
//        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
//            super.onBackPressed()
//        }
        super.onBackPressed()

    }

    // 액션 바 관련 override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu,menu)
        return true
    }



    // 서버 관련 함수
    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }


    //fragment 뷰페이저에 맵핑
    private fun init() {
        var adapter = FragmentStateAdapter(this)
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
                R.id.main -> {
                    binding.viewPager.setCurrentItem(0)
                    prefEdit.putInt("nowPage",R.id.frag_main)
                }
                R.id.friend -> {
                    binding.viewPager.setCurrentItem(1)
                    prefEdit.putInt("nowPage",R.id.frag_recycler)
                }
                R.id.map -> {
                    binding.viewPager.setCurrentItem(2)
                    prefEdit.putInt("nowPage",R.id.frag_map)
                }
                R.id.etcetra -> {
                    binding.viewPager.setCurrentItem(3)
                    prefEdit.putInt("nowPage",R.id.frag_etcetra)
                }
            }
            prefEdit.apply()
            return@setOnNavigationItemSelectedListener true
        }
    }


    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}