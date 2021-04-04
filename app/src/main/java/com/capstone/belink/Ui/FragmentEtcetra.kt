package com.capstone.belink.Ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.capstone.belink.LoginActivity
import com.capstone.belink.MainActivity
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.databinding.FragmentEtcetraBinding
import com.capstone.belink.databinding.FragmentMapBinding
import retrofit2.Retrofit

class FragmentEtcetra:Fragment() {
    private var mBinding:FragmentEtcetraBinding?=null
    private val binding get() = mBinding!!

    private var mContext:Context?=null
    private val xContext get() = mContext!!



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentEtcetraBinding.inflate(inflater,container,false)
        val view = binding.root
        (activity as AppCompatActivity).supportActionBar?.title="설정"

        binding.btnLogout.setOnClickListener {
            val intent = Intent(xContext,LoginActivity::class.java)
            startActivity(intent)
            val auto = (activity as MainActivity).getSharedPreferences("auto",Activity.MODE_PRIVATE)
            val editor = auto.edit()
            editor.clear()
            editor.apply()
            Toast.makeText(xContext,"Logout",Toast.LENGTH_SHORT).show()

        }

        return view
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