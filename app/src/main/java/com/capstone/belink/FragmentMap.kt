package com.capstone.belink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.belink.databinding.FragmentMapBinding

class FragmentMap:Fragment() {
    private var mBinding:FragmentMapBinding?=null
    private val binding get() = mBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentMapBinding.inflate(inflater,container,false)
        val view = binding.root


        return view
    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}