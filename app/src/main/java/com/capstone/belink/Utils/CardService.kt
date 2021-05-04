package com.capstone.belink.Utils

import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import java.util.*

public class CardService : HostApduService() {

    private final val TAG = "CardService"
    private final val SAMPLE_LOYALTY_CARD_AID = "F222222222"
    private final val SELECT_APDU_HEADER = "00A40400"

    private final val SELECT_OK_SW = HexStringToByteArray("9000")

    private final val UNKNOWN_CMD_SW = HexStringToByteArray("0000")
    private final val SELECT_APDU = BuildSelectApdu(SAMPLE_LOYALTY_CARD_AID)

    @Throws(IllegalArgumentException::class)
    fun HexStringToByteArray(s: String): ByteArray? {
        val len = s.length
        require(len % 2 != 1) { "Hex string must have even number of characters" }
        val data = ByteArray(len / 2) // Allocate 1 byte per 2 hex characters
        var i = 0
        while (i < len) {
            // Convert each character into a integer (base-16), then bit-shift into place
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    private fun BuildSelectApdu(aid: String): ByteArray? {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(SELECT_APDU_HEADER + String.format("%02X", aid.length / 2) + aid)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return super.onStartCommand(intent, flags, startId)
        val extras = intent!!.extras
        return START_STICKY

    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray? {
        if(Arrays.equals(SELECT_APDU,commandApdu)){
            Log.i(TAG,"Application selected")
            return SELECT_OK_SW
        }else{
            return UNKNOWN_CMD_SW
        }
    }

    override fun onDeactivated(reason: Int) {
        Log.i(TAG, "Deactivated$reason")
    }
}