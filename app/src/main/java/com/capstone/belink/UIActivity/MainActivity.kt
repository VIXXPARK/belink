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
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.capstone.belink.Adapter.*
import com.capstone.belink.Model.*
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.R
import com.capstone.belink.Ui.*
import com.capstone.belink.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Math.*
import kotlin.math.pow
import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.capstone.belink.Utils.*
import java.io.IOException
import java.util.*

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

    //gps관련 변수
    private val r = 6372.8 * 1000
    private lateinit var adapter: SearchAdapter
    private var gpsTracker: GpsTracker? = null
    private val GPS_ENABLE_REQUEST_CODE = 2001
    private val PERMISSIONS_REQUEST_CODE = 100
    var REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)


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
        search()
    }

    private fun search(){

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
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->                     //사용자가 GPS 활성 했는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음")
                        checkRunTimePermission()
                        return
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
            R.id.action_exchange ->{
                retrofitGetMyTeam()
                true
            }
            R.id.action_setting_group_delete->{
                val intent = Intent(this, TeamDeleteActivity::class.java)
                startActivity(intent)
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
        /**
         * 검색 기능
         */
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            checkRunTimePermission()
        }
        gpsTracker = GpsTracker(this@MainActivity)
        val latitude = gpsTracker!!.getLatitude()
        val longitude = gpsTracker!!.getLongitude()
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object :  SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                // keyword : query
                supplementService.searchPlace(query)
                        .enqueue(object : Callback<Search>{
                            override fun onResponse(call: Call<Search>, response: Response<Search>) {
                                val searchList= response.body()!!.data as MutableList<SearchLocation>
                                Log.d("성공",searchList[0].place_name)
                                println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                                println("$latitude $longitude")
                                for (i in 0 until searchList.size) {
                                    searchList[i].distance=getDistance(latitude,longitude,searchList[i].x.toDouble(),searchList[i].y.toDouble()).toString()
                                }
                                setSearchPref(this@MainActivity,"searchContext",searchList)
                                refreshAdapter()
                               // val searchListSub = getSearchPref(this@MainActivity,"searchContext")
                               // Log.d("성공",searchListSub[0].distance)
                                true
                            }
                            override fun onFailure(call: Call<Search>, t: Throwable) {
                                Log.d("실패","$t")
                            }
                        })
                return false
            }
        })
        return true
    }

    fun refreshAdapter(){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.main_frame,FragmentMain()).commit()
    }

    fun replaceFragment(fragment: Fragment){ //액티비티에서 프라그먼토 교체 함수
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.search_recycler,fragment).commit()
    }

    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
        val c = 2 * asin(sqrt(a))
        return (r * c).toInt()
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
        if (requestCode == PERMISSIONS_REQUEST_CODE &&
                grantResults.size == REQUIRED_PERMISSIONS.size) {
            var check_result = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {
                //위치 값을 가져올 수 있음
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(this@MainActivity, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해 주세요.", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this@MainActivity, "퍼미션이 거부되었습니다. 설정에서 퍼미션을 허용해주세요", Toast.LENGTH_LONG).show()
                }
            }
        }

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
    //ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드


    fun checkRunTimePermission() {
        //런타임 퍼미션 처리
        //1. 위치 퍼미션을 가지고 있는지 체크
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermssion = ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermssion == PackageManager.PERMISSION_GRANTED) {
            //2. 이미 퍼미션이 있을 경우
            //3. 위치 값을 가져올 수 있음
        } else {
            //퍼미션 요청하지 않았을 경우 2가지 퍼미션 요청이 필요함
            //3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                            REQUIRED_PERMISSIONS[0])) {
                //3-2. 요청을 진행하기 전에 사용자에게 퍼미션이 필요한 이유를 설명해줘야함
                Toast.makeText(this@MainActivity, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                //3-3. 사용자에게 퍼미션 요청, 요청 결과는 onRequestPermissionResult에서 수신
                ActivityCompat.requestPermissions(this@MainActivity, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            } else {
                //4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 함
                //요청 결과는 onRequestPermissionResult에서 수신됨.
                ActivityCompat.requestPermissions(this@MainActivity, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    fun getCurrentAddress(latitude: Double, longitude: Double): String {
        //GeoCorder -> GPS를 주소로 변환
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>
        addresses = try {
            geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7)
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용 불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용 불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }
        if (addresses == null || addresses.size == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show()
            return "주소 미발견"
        }
        val address = addresses[0]
        return """
            ${address.getAddressLine(0)}
            
            """.trimIndent()
    }

    //GPS 활성화를 위한 메소드

    //GPS 활성화를 위한 메소드
    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("""
    앱을 사용하기 위해서는 위치 서비스가 필요합니다
    위치 설정을 수정하시겠습니까?
    """.trimIndent())
        builder.setCancelable(true)
        builder.setPositiveButton("설정") { dialog, id ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        }
        builder.setNegativeButton("취소") { dialog, id -> dialog.cancel() }
        builder.create().show()
    }


    fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }


}

private fun SearchView.setOnQueryTextListener() {

}

