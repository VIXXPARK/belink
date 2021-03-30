package com.capstone.belink.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.belink.databinding.FragmentEtcetraBinding
import com.capstone.belink.databinding.FragmentMapBinding

class FragmentEtcetra:Fragment() {
    private var mBinding:FragmentEtcetraBinding?=null
    private val binding get() = mBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentEtcetraBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}