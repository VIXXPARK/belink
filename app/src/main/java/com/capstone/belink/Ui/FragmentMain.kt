   package com.capstone.belink.Ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.Adapter.CustomAdapter
import com.capstone.belink.Adapter.ProfileData
import com.capstone.belink.MainActivity
import com.capstone.belink.R
import com.capstone.belink.databinding.FragmentMainBinding


   class FragmentMain : Fragment() {
    private var mBinding:FragmentMainBinding?=null
    private val binding get() = mBinding!!

    private var mContext:Context?=null
    private val xContext get() = mContext!!

    private var mActivity: Activity?=null
    private val xActivity get() = mActivity!!

   val DataList = arrayListOf(
           ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29"),
           ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29"),
           ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29"),
           ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29"),
           ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29")
   )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.viewRecycler.layoutManager= LinearLayoutManager(xContext)
        val adapter = CustomAdapter(xContext)
        adapter.DataList=DataList
        binding.viewRecycler.adapter=adapter

        binding.fabPersonal.setOnClickListener {
            binding.fragMain.visibility=View.INVISIBLE
            binding.fragRecycler.visibility=View.VISIBLE
        }
        binding.fabRecycler.setOnClickListener {
            binding.fragMain.visibility=View.VISIBLE
            binding.fragRecycler.visibility=View.INVISIBLE
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onAttach(context: Context) {
        mContext=context
        mActivity = activity as MainActivity?
        super.onAttach(context)
    }



   override fun onDestroy() {
    mBinding=null
    super.onDestroy()
    }
    companion object{
        const val KEY="key"
        fun newInstance(data: String) = FragmentMain().apply {
            arguments = Bundle().apply {
                putString(KEY, data)
            }
        }
    }
}


