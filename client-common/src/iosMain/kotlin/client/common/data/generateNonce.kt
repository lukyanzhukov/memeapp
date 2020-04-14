package client.common.data

import io.ktor.util.hex
import kotlinx.cinterop.*
import platform.posix.*

private const val NONCE_SIZE_IN_BYTES = 16


/**	/**
 * Generates a nonce string. Could block if the system's entropy source is empty	 * Generates a nonce string 16 characters long. Could block if the system's entropy source is empty
*/	 */
actual fun generateNonce() = hex(generateRandomByteArray())

private fun generateRandomByteArray(size: Int = NONCE_SIZE_IN_BYTES): ByteArray {
    memScoped {
        val result = ByteArray(size)
        val tmp = allocArray<ByteVar>(size)
        val ptr = tmp.getPointer(this)
        val file = fopen("/dev/urandom", "rb")
        if (file != null) {
            fread(ptr, 1.convert(), result.size.convert(), file)
            for (n in result.indices) result[n] = ptr[n]
            fclose(file)
            return result
        }
        error("SECURITY FAILURE: Could not generate random byte array! Reason: '/dev/urandom' could not be found")
    }
}