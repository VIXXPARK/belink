package com.capstone.belink.Utils

object HexUtils {

    private const val HEX_CHAR_STRING = "0123456789ABCDEF"

    private val HEX_CHAR_ARRAY = "0123456789ABCDEF".toCharArray()

    fun hexToByte(hex: String): ByteArray {
        val ret = ByteArray(hex.length / 2)
        for (i in hex.indices step 2) {
            val firstIndex = HEX_CHAR_STRING.indexOf(hex[i]);
            val secondIndex = HEX_CHAR_STRING.indexOf(hex[i + 1]);
            val octet = firstIndex.shl(4).or(secondIndex)
            ret[i.shr(1)] = octet.toByte()
        }
        return ret
    }

    fun byteToHex(byteArray: ByteArray): String {
        val ret = StringBuffer()
        byteArray.forEach {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            ret.append(HEX_CHAR_ARRAY[firstIndex])
            ret.append(HEX_CHAR_ARRAY[secondIndex])
        }
        return ret.toString()
    }
}