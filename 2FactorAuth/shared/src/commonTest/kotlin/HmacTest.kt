import com.soywiz.krypto.HMAC
import com.soywiz.krypto.encoding.Hex
import io.ktor.utils.io.core.*
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class HmacTest {
    @Test
    fun testHmacString() {
        val secretBytes = byteArrayOf(0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30)
        val value = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val hash = HMAC.hmacSHA1(secretBytes, value)
        assertEquals("cc93cf18508d94934c64b65d8ba7667fb7cde4b0", hash.hex)
    }

    @Test
    fun testHmacBytes() {
        val secretBytes = byteArrayOf(0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30)
        val value = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val hash = HMAC.hmacSHA1(secretBytes, value)
        val expected = Hex.decode("cc93cf18508d94934c64b65d8ba7667fb7cde4b0")
        assertContentEquals(expected, hash.bytes)
    }

    @Test
    fun testStringToByteConversion() {
        val secret = "12345678901234567890"
        val secretBytes = byteArrayOf(0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30)

        assertContentEquals(secretBytes, secret.toByteArray())
    }

    @Test
    fun testHmacToStringSecretAsString() {
        val secret = "12345678901234567890"
        val value = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val hash = HMAC.hmacSHA1(secret.toByteArray(), value)
        assertEquals("cc93cf18508d94934c64b65d8ba7667fb7cde4b0", hash.hex)
    }
}