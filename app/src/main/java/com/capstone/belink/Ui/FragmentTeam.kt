package com.capstone.belink.Ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.capstone.belink.MainActivity
import com.capstone.belink.R
import com.capstone.belink.databinding.FragmentMakeTeamBinding


class FragmentTeam:Fragment() {
    private var mBinding:FragmentMakeTeamBinding?=null
    private val binding get() = mBinding!!

    var mainActivity:MainActivity?=null

    private lateinit var callback: OnBackPressedCallback
    var checkView=0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding= FragmentMakeTeamBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.fragmentTeamLayout.visibility=View.VISIBLE

        binding.btnTeamNext.setOnClickListener {
            Log.d("fragment", "${(activity as MainActivity).supportFragmentManager.backStackEntryCount}")
            checkView=1
            mainActivity!!.openFragmentOnFrameLayoutFriend(1)
        }


        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        val fm = (activity as MainActivity).supportFragmentManager
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(checkView==1){
                    checkView=0
                }else if(checkView==-1){
                    isEnabled = false
                    checkView=0
//                    (activity as MainActivity).binding.activeMain.visibility=View.VISIBLE
                    (activity as MainActivity).onBackPressed()
                }
                else{
                    checkView=-1
                    binding.fragmentTeamLayout.visibility=View.INVISIBLE
                    (activity as MainActivity).binding.activeMain.visibility=View.VISIBLE
                }

            }
        }

        (activity as MainActivity).onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}