package com.capstone.belink

import android.app.Activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible

import androidx.viewpager2.widget.ViewPager2
import com.capstone.belink.Adapter.FragmentStateAdapter
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.Ui.*
import com.capstone.belink.databinding.ActivityMainBinding
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private var mBinding:ActivityMainBinding?=null
    val binding get() = mBinding!!

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService


    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor

    private var fragmentFreind = FragmentFriend()
    private val fragmentTeam = FragmentTeam()

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
        super.onBackPressed()
    }

    fun openFragmentOnFrameLayoutFriend(int: Int){
        val transaction = supportFragmentManager.beginTransaction()
        when(int){
            1 -> {
                transaction.replace(R.id.main_frame, fragmentFreind)
//                transaction.addToBackStack(null)

            }

        }
        transaction.commit()
    }


    // 액션 바 관련 override
    override fun onOptionsItemSelected(item: MenuItem): Boolean= when(item.itemId){
            R.id.action_plus ->{

                binding.activeMain.visibility= View.INVISIBLE

                val transaction = supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame,fragmentTeam)
                transaction.commit()
                true
            }
            R.id.action_alert ->{
                //activtiy or dialog
                println("alert")

                true
            }
            else -> super.onOptionsItemSelected(item)
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

                }
                R.id.friend -> {
                    binding.viewPager.setCurrentItem(1)

                }
                R.id.map -> {
                    binding.viewPager.setCurrentItem(2)

                }
                R.id.etcetra -> {
                    binding.viewPager.setCurrentItem(3)

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

