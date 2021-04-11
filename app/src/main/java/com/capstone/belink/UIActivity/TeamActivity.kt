package com.capstone.belink.UIActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.capstone.belink.Adapter.FriendAdapter
import com.capstone.belink.Model.*
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.R
import com.capstone.belink.Ui.FragmentFriend
import com.capstone.belink.Ui.FragmentTeam
import com.capstone.belink.Utils.getMemberPref
import com.capstone.belink.databinding.ActivityTeamBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class TeamActivity : AppCompatActivity() {
    private var mBinding:ActivityTeamBinding?=null
    private val binding get() = mBinding!!

    private lateinit var retrofit:Retrofit
    private lateinit var supplementService:RetrofitService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRetrofit()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.team_frame_layout,FragmentFriend()).commit()

    }

    private fun initRetrofit() {
        retrofit=RetrofitClient.getInstance()
        supplementService=retrofit.create(RetrofitService::class.java)
    }

    fun replaceFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.team_frame_layout,fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.team_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_check -> {
                val member = getMemberPref(this, "team")
                var teamMember: MutableList<String> = ArrayList()
                for ((k, v) in member) {
                    println("$k  $v")
                    if (v) {
                        teamMember.add(k)
                    }
                }
                var teamName: String = ""
                println(teamMember.toString())
                supplementService.idContactUser(teamMember).enqueue(object : Callback<ContactInfo> {
                    override fun onResponse(call: Call<ContactInfo>, response: Response<ContactInfo>) {
                        val data = response.body()?.data
                        println(data.toString())
                        if (data != null) {
                            for (i in data.indices) {
                                teamName += if(i != data.size - 1) {
                                    data[i].username + " , "
                                } else {
                                    data[i].username
                                }
                            }
                        }
                        supplementService.makeTeam(teamName).enqueue(object : Callback<Team> {
                            override fun onResponse(call: Call<Team>, response: Response<Team>) {
                                if (response.message() == "OK") {
                                    val id = response.body()?.id
                                    if (id!!.isNotEmpty()) {
                                        var teamList: MutableList<Member> = ArrayList()
                                        for (i in 0 until teamMember.size) {
                                            teamList.add(Member(id, teamMember[i]))
                                        }
                                        supplementService.makeMember(teamList).enqueue(object : Callback<Success> {
                                            override fun onResponse(call: Call<Success>, response: Response<Success>) {
                                                if (response.message() == "OK") {
                                                    Toast.makeText(this@TeamActivity, "만들어졌습니다", Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                            override fun onFailure(call: Call<Success>, t: Throwable) {
                                                Log.d("memberFail", "$t")
                                            }

                                        })
                                    }
                                }
                            }

                            override fun onFailure(call: Call<Team>, t: Throwable) {
                                Log.d("makeTeamFail", "$t")
                            }

                        })
                    }

                    override fun onFailure(call: Call<ContactInfo>, t: Throwable) {
                        Log.d("contactUserFail", "$t")
                    }

                })
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}