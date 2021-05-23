package com.capstone.belink.UIActivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.capstone.belink.Model.*
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.R
import com.capstone.belink.Ui.FragmentFriend
import com.capstone.belink.Utils.getGroupPref
import com.capstone.belink.Utils.getMemberPref
import com.capstone.belink.Utils.setGroupPref
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

    private var teamName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRetrofit()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.team_frame_layout,FragmentFriend()).commit()
    }

    private fun initRetrofit() {
        retrofit=RetrofitClient.getInstance(this)
        supplementService=retrofit.create(RetrofitService::class.java)
    }

    /**
     * 프라그먼트에서 직접적으로 프라그먼트를 교체할 수 없기에 액티비티에서 프라그먼트를
     * 교체하는 함수를 미리 정의한다. 그러면 프라그먼트의 교체를 용이하게 할 수 있다.*/

    fun replaceFragment(fragment:Fragment){ //액티비티에서 프라그먼토 교체 함수
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.team_frame_layout,fragment).commit()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean { //팀 액티비티에 관한 액션바 아이콘 수정
        menuInflater.inflate(R.menu.team_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            /**
             * 클릭된 유저중에 true 값을 가진 유저만을 따로 저장한다.*/
            R.id.action_check -> { //확인 클릭시 그룹방 만들기
                val member = getMemberPref(this, "team") // 그룹에 관한 멤버 가져오기, 이에 관한 값들은 adapter에서 사용중
                val teamMember: MutableList<String> = ArrayList()
                for ((k, v) in member) {
                    if (v) {// 아이디 중 체크박스 중 체크된 것들만 가져오기
                        teamMember.add(k)
                    }
                }
                teamName= getSharedPreferences("auto", MODE_PRIVATE).getString("username","")!!
                retrofitIdContactUser(teamMember,teamName)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    /**
     * 연락처 조회를 하여 해당 전화번호가 데이터베이스에 존재하는 지 판단
     * */
    private fun retrofitIdContactUser(teamMember: MutableList<String>, teamName: String) {
        teamMember.add(getSharedPreferences("auto", MODE_PRIVATE).getString("userId","")!!)
        supplementService.idContactUser(teamMember).enqueue(object : Callback<ContactInfo> {// id값 기준으로 연락처 조회
        override fun onResponse(call: Call<ContactInfo>, response: Response<ContactInfo>) {
            val data = response.body()?.data
            setTeamName(data)
            retrofitMakeTeam(teamName,teamMember)
        }
            override fun onFailure(call: Call<ContactInfo>, t: Throwable) {
                Log.d("contactUserFail", "$t")
            }
        })
    }

    private fun setTeamName(data: List<User>?) {
        if (data != null) {
            for (i in data.indices) {
                this@TeamActivity.teamName += if(i != data.size - 1) {// 그룹 이름을 유저 이름 제외한 유저이름들로 구성
                    data[i].username + " , "
                } else {
                    data[i].username
                }
            }
        }
    }

    private fun retrofitMakeTeam(teamName: String, teamMember: MutableList<String>) {
        supplementService.makeTeam(teamName).enqueue(object : Callback<Team> {// 그룹 생성
        override fun onResponse(call: Call<Team>, response: Response<Team>) {
            if (response.message() == "Created") {
                val id = response.body()?.id
                val teamList: MutableList<Member> = ArrayList()
                if (id!!.isNotEmpty()) {
                    retrofitMakeMember(teamList,teamMember,id)
                }
            }
        }
            override fun onFailure(call: Call<Team>, t: Throwable) {
                Log.d("makeTeamFail", "$t")
            }
        })
    }

    private fun retrofitMakeMember(
        teamList: MutableList<Member>,
        teamMember: MutableList<String>,
        id: String
    ) {
        for (i in 0 until teamMember.size) {
            teamList.add(Member(id, teamMember[i]))
        }
        supplementService.makeMember(teamList).enqueue(object : Callback<Map<String,Boolean>> { // 유저와 그룹 맵핑
            override fun onResponse(call: Call<Map<String,Boolean>>, response: Response<Map<String,Boolean>>) {
                if (response.message() == "OK") {
                    setRetrofitMember(id,teamMember)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
            override fun onFailure(call: Call<Map<String,Boolean>>, t: Throwable) {
                Log.d("memberFail", "$t")
            }
        })
    }

    private fun setRetrofitMember(id: String, teamMember: MutableList<String>) {
        val teamList= getGroupPref(this@TeamActivity,"groupContext")
        val obj = TeamRoom(id =id!!, teamName = teamName, data =teamMember)
        teamList.add(obj)
        setGroupPref(this@TeamActivity,"groupContext",teamList)
        getSharedPreferences("team", MODE_PRIVATE).edit().clear().commit()
    }



    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}