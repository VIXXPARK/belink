package com.capstone.belink.UIActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.capstone.belink.R
import com.capstone.belink.Ui.FragmentTeam
import com.capstone.belink.databinding.ActivityTeamBinding

class TeamActivity : AppCompatActivity() {
    private var mBinding:ActivityTeamBinding?=null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.team_frame_layout,FragmentTeam()).commit()

    }

    fun replaceFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.team_frame_layout,fragment).commit()
    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}