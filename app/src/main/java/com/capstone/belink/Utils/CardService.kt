package com.capstone.belink.Utils

import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import java.util.*

public class CardService : HostApduService() {

    companion object {

        const val STATUS_SUCCESS = "9000"
        const val STATUS_FAILED = "6F00"

        const val MIN_APDU_LENGTH = 12
        const val AID = "A0000002471001"

        const val DEFAULT_CLA = "00"
        const val CLA_NOT_SUPPORTED = "6E00"
        const val SELECT_INS = "A4"
        const val INS_NOT_SUPPORTED = "6D00"
    }

    override fun onDeactivated(reason: Int) {
        Log.d("####", "onDeactivated: $reason")
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu == null) {
            return HexUtils.hexToByte(STATUS_FAILED)
        }

        val hexCommandApdu = HexUtils.byteToHex(commandApdu)
        if (hexCommandApdu.length < MIN_APDU_LENGTH) {
            return HexUtils.hexToByte(STATUS_FAILED)
        }

        if (hexCommandApdu.substring(0, 2) != DEFAULT_CLA) {
            return HexUtils.hexToByte(CLA_NOT_SUPPORTED)
        }

        if (hexCommandApdu.substring(2, 4) != SELECT_INS) {
            return HexUtils.hexToByte(INS_NOT_SUPPORTED)
        }

        if (hexCommandApdu.substring(10, 24) == AID) {
            return HexUtils.hexToByte(STATUS_SUCCESS)
        }

        return HexUtils.hexToByte(STATUS_FAILED)
    }
}