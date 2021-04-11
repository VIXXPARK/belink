package com.capstone.belink.UIActivity

import android.app.Activity
import android.content.Intent

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import androidx.viewpager2.widget.ViewPager2
import com.capstone.belink.Adapter.FragmentStateAdapter
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.R
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


    var fragmentLists = listOf(FragmentMain(), FragmentGroup(), FragmentMap(), FragmentEtcetra())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref =getSharedPreferences("auto", Activity.MODE_PRIVATE)!!
        prefEdit=pref.edit()

        initRetrofit()
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK){
            when(requestCode){
                0 ->{
                    Toast.makeText(this,"방이 만들어졌습니다.",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }



    // 액션 바 관련 override
    override fun onOptionsItemSelected(item: MenuItem): Boolean= when(item.itemId){
            R.id.action_plus ->{
                val intent = Intent(this, TeamActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_alert ->{
                val intent = Intent(this, AlarmActivity::class.java)
//                startActivity(intent)
                startActivityForResult(intent,0)
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
                        supportActionBar?.title="그룹"
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

