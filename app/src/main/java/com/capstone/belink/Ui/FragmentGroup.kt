package com.capstone.belink.Ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.Adapter.ProfileData
import com.capstone.belink.Adapter.RecyclerAdapter
import com.capstone.belink.R
import com.capstone.belink.Utils.ItemMoveCallbackListener
import com.capstone.belink.Utils.getGroupPref
import com.capstone.belink.databinding.FragmentGroupBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentGroup.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentGroup : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var mBinding:FragmentGroupBinding?=null
    private val binding get() = mBinding!!

    private var mContext: Context?=null
    private val xContext get() = mContext!!

    private lateinit var touchHelper: ItemTouchHelper


    override fun onAttach(context: Context) {
        mContext=context
        super.onAttach(context)
    }

    val DataList = arrayListOf(
            ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29"),
            ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29"),
            ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29"),
            ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29"),
            ProfileData(R.drawable.picachu,"팀플용","박수한,김현진,김민섭,김경재","2021-03-29")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentGroupBinding.inflate(inflater,container,false)


        val teamList= getGroupPref(xContext,"groupContext")



        val adapter = RecyclerAdapter(xContext)
        val callback:ItemTouchHelper.Callback = ItemMoveCallbackListener(adapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.viewRecycler)
        binding.viewRecycler.layoutManager= LinearLayoutManager(xContext)
        adapter.DataList=DataList
        binding.viewRecycler.adapter=adapter



        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentGroup.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                FragmentGroup().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}