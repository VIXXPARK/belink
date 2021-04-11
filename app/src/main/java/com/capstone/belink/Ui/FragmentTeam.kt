package com.capstone.belink.Ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.capstone.belink.Model.Sign
import com.capstone.belink.Model.Team
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.UIActivity.TeamActivity
import com.capstone.belink.databinding.FragmentMakeTeamBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class FragmentTeam:Fragment() {
    private var mBinding:FragmentMakeTeamBinding?=null
    private val binding get() = mBinding!!

    private var mContext:Context?=null
    private val _context get() = mContext!!

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding= FragmentMakeTeamBinding.inflate(inflater, container, false)
        val view = binding.root

        initRetrofit()

        binding.fragmentTeamLayout.visibility=View.VISIBLE

            binding.btnTeamNext.setOnClickListener {

                if(binding.etTeamMakeName.text.isEmpty()){
                    Toast.makeText(_context,"이름을 적어주세요",Toast.LENGTH_SHORT).show()
                }
                else{
                        supplementService.makeTeam(binding.etTeamMakeName.text.toString()).enqueue(object :Callback<Team>{
                            override fun onResponse(call: Call<Team>, response: Response<Team>) {
                                if(response.message()=="OK"){
                                    (activity as TeamActivity).replaceFragment(FragmentFriend())
                                }
                            }
                            override fun onFailure(call: Call<Team>, t: Throwable) {
                                Log.d("그룹생성 실패","fail")
                            }
                        })
                }

            }
        return view
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    override fun onAttach(context: Context) {
        mContext=context
        super.onAttach(context)
    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}