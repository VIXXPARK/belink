package com.capstone.belink.UIActivity

import android.Manifest
import android.app.*
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.capstone.belink.Adapter.FragmentStateAdapter
import com.capstone.belink.Adapter.IsoDepAdapter
import com.capstone.belink.Adapter.IsoDepTransceiver
import com.capstone.belink.Adapter.LoyaltyCardReader
import com.capstone.belink.Model.GetMyTeam
import com.capstone.belink.Model.TeamRoom
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.R
import com.capstone.belink.Ui.*
import com.capstone.belink.Utils.setGroupPref
import com.capstone.belink.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MainActivity : AppCompatActivity(),IsoDepTransceiver.OnMessageReceived,LoyaltyCardReader.AccountCallback {
    //layout bind
    private var mBinding:ActivityMainBinding?=null
    val binding get() = mBinding!!

    //http 통신
    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    //저장된 값 가져오는 변수
    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor

    //nfc관련 변수
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var isoDepAdapter:IsoDepAdapter
    private lateinit var mLoyaltyCardReader: LoyaltyCardReader
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 1

    //페이지
    private var fragmentLists = listOf(FragmentMain(), FragmentGroup(), FragmentMap(), FragmentEtcetra())


    override fun onResume() {
        super.onResume()
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter.enableReaderMode(
                this,
                mLoyaltyCardReader,
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                null
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNfcModule()
        setSharedPreferences()
        invalidateOptionsMenu()
        requestContactPermission()
        initRetrofit()
        init()
    }

    private fun setNfcModule() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        isoDepAdapter = IsoDepAdapter(layoutInflater)
        mLoyaltyCardReader = LoyaltyCardReader(this)
    }

    private fun setSharedPreferences() {
        pref =getSharedPreferences("auto", Activity.MODE_PRIVATE)!!
        prefEdit=pref.edit()

    }

    fun requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                    buildDiaglogue()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS) }
            } else {
                checkContacts()
            }
        } else {
            checkContacts()
        }
    }

    private fun buildDiaglogue() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Read Contacts permission")
        builder.setPositiveButton(android.R.string.ok, null)
        builder.setMessage("Please enable access to contacts.")
        builder.setOnDismissListener(DialogInterface.OnDismissListener {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_CONTACTS
                ), PERMISSIONS_REQUEST_READ_CONTACTS
            )
        })
        builder.show()
    }

    /**연락처 동의*/

    private fun checkContacts() {
        Log.d("연락처","가져옴")
    }

    // 서버 관련 함수
    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance(this)
        supplementService = retrofit.create(RetrofitService::class.java)
    }


    /**
     * 뷰페이저에 관한 것을 묶은 함수
     * 처음에 해당 컨텍스트를 adapter에 전달하고 뷰페이저에 전달할 fragmentList를 전달한다.
     * 그리고 페이지가 넘어감에 따라 when 분기문으로 그에 맞는 내용을 작성한다.
     * 밑에 바텀네비게이션뷰에 대한 처리는 해당 페이지가 넘어갔을 때 그에 맞는 아이콘을 표시하기 위해
     * 처리한 구문이다.*/
    private fun init() {
        val adapter = FragmentStateAdapter(this)
        adapter.fragmentList=fragmentLists
        binding.viewPager.adapter=adapter
        setViewPager()
        setBottomNavigationView()
    }


    private fun setViewPager() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> {
                        binding.bottomNavigationView.selectedItemId = R.id.main
                        supportActionBar?.title="개인"
                        println("개인페이지")

                    }
                    1 -> {
                        binding.bottomNavigationView.selectedItemId = R.id.friend
                        supportActionBar?.title="그룹"
                        println("그룹페이지")
                    }
                    2 -> {
                        binding.bottomNavigationView.selectedItemId = R.id.map
                        supportActionBar?.title="방문장소"
                        println("방문장소페이지")
                    }
                    3 -> {
                        binding.bottomNavigationView.selectedItemId = R.id.etcetra
                        supportActionBar?.title="설정"
                        println("설정페이지")
                    }
                }

            }
        })
    }

    private fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.main -> {
                    binding.viewPager.currentItem = 0
                }
                R.id.friend -> {
                    binding.viewPager.currentItem = 1
                }
                R.id.map -> {
                    binding.viewPager.currentItem = 2
                }
                R.id.etcetra -> {
                    binding.viewPager.currentItem = 3
                }
            }
            prefEdit.apply()
            return@setOnNavigationItemSelectedListener true
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action){

            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                for(message in messages){
                    for(record in message.records){
                        println(record.toString())
                    }
                }
            }
        }
    }

    /**
     * onActivityResult 상속 함수는 startActivityForResult 인텐트된 액티비티에서 활동을 끝나고 값을 전달했을떼
     * 그에 관한 처리를 담당하는 함수
     * requestCode는 코드작성자가 명시한 숫자로서 그에 대하여 when 분기문으로 처리하면 된다.
     * 그리고 resultCode를 통해 해당 메시지에 따른 처리를 어떻게 할 것인지 결정하면 된다.*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("requestCode: $requestCode, resultCode: $resultCode")
            when(requestCode){
                0 ->{
                    if(resultCode==Activity.RESULT_OK ) {
                        Toast.makeText(this, "방이 만들어졌습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                1 ->{
                    if(resultCode==Activity.RESULT_OK ) {
                        Toast.makeText(this, "회원 정보 수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                2 ->{
                    if(resultCode==Activity.RESULT_OK ) {
                        Toast.makeText(this, "인증 되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }


    /**
     * 액션바의 메뉴를 클릭했을 때 그에 해당하는 내용을 처리할 수 있는 상속 함수
     * 반환값은 진리값으로 반환한다.*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean= when(item.itemId){
            R.id.action_plus ->{
                val intent = Intent(this, TeamActivity::class.java)
                startActivityForResult(intent,0)
                true
            }
            R.id.action_alert ->{
                val intent = Intent(this, AlarmActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_exchange -> {
                retrofitGetMyTeam()
                true
            }
        R.id.action_setting_group_delete->{
            //val intent = Intent(this, TeamDeleteActivity::class.java)
            // startActivityForResult(intent,0)
            //startActivity(intent)
            true
        }
            else -> super.onOptionsItemSelected(item)
        }

    private fun retrofitGetMyTeam() {
        supplementService.getMyTeam(pref.getString("userId","")!!).enqueue(object : Callback<GetMyTeam> {
            override fun onResponse(
                call: Call<GetMyTeam>,
                response: Response<GetMyTeam>
            ) {
                if(response.message()=="OK"){
                    val teamList:ArrayList<TeamRoom> = ArrayList()
                    response.body()!!.result.forEach{
                        val element = TeamRoom(id =it.teamRoom.id, teamName = it.teamRoom.teamName,data = arrayListOf())
                        teamList.add(element)
                    }
                    setGroupPref(this@MainActivity,"groupContext",teamList)
                }
            }
            override fun onFailure(call: Call<GetMyTeam>, t: Throwable) {
                Log.d("태그","$t")
            }
        })
    }


    /**
     * 우리가 원하는 메뉴를 추가하고 싶을 때에는 onCreateOptionMenu 상속 함수를 호출하고
     * menuInflater를 호출하여 여기에 해당하는 메뉴를 inflate 시키면 된다.*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu,menu)
        return true
    }


    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter.disableReaderMode(this)

    }


    override fun onMessage(message: ByteArray?) {

    }

    override fun onError(exception: Exception?) {

    }

    override fun onAccountReceived(account: String?) {
        runOnUiThread {
            println("TEST:${mLoyaltyCardReader.gotData}")
            isoDepAdapter.addMessage(account)
            val intent = Intent(this,SendGroupActivity::class.java)
            intent.putExtra("storeId",mLoyaltyCardReader.gotData)
            startActivityForResult(intent,2)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] === PackageManager.PERMISSION_GRANTED
                ) {
                    checkContacts()
                } else {
                    Toast.makeText(this, "주소록 접근 권한이 없습니다.", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

}

