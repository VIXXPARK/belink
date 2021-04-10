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
import com.capstone.belink.TeamActivity
import com.capstone.belink.databinding.FragmentMakeTeamBinding


class FragmentTeam:Fragment() {
    private var mBinding:FragmentMakeTeamBinding?=null
    private val binding get() = mBinding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding= FragmentMakeTeamBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.fragmentTeamLayout.visibility=View.VISIBLE

        binding.btnTeamNext.setOnClickListener {
            (activity as TeamActivity).replaceFragment(FragmentFriend())
        }


        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}