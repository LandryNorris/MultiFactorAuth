import com.landry.shared.otp.Hotp
import kotlin.test.Test
import kotlin.test.assertEquals

class HotpTest {
    private val secret = "12345678901234567890"

    /**
     * This test uses the examples given in the RFC for HOTP (RFC 4226).
     * https://datatracker.ietf.org/doc/html/rfc4226#section-5.4
     */
    @Test
    fun testHotpUsingRFC4226Examples() {
        val hotp = Hotp(secret, 0)
        val expectedCodes = arrayOf("755224", "287082", "359152", "969429", "338314", "254676", "287922", "162583", "399871", "520489")

        for(code in expectedCodes) {
            assertEquals(code, hotp.generatePin())
            hotp.incrementCounter()
        }
    }
}