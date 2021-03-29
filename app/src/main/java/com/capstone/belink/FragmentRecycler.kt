package com.capstone.belink

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.databinding.FragmentRecyclerBinding

class FragmentRecycler: Fragment() {
    private var mBinding:FragmentRecyclerBinding?=null
    private val binding get() = mBinding!!

    private var mContext:Context?=null
    private val xContext get() = mContext!!

    val DataList = arrayListOf(
        ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29"),
        ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29"),
        ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29"),
        ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29"),
        ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29")
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding= FragmentRecyclerBinding.inflate(inflater,container,false)
        val view = binding.root

        binding.viewRecycler.layoutManager=LinearLayoutManager(xContext)
        val adapter =CustomAdapter(xContext)
        adapter.DataList=DataList
        binding.viewRecycler.adapter=adapter

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