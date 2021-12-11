import com.landry.shared.otp.Totp
import kotlin.test.Test
import kotlin.test.assertEquals

class TotpTest {
    private val secret = "12345678901234567890"

    @Test
    fun testTotpUsingRFC4226Examples() {
        val totp = Totp(secret, codeLength = 8)

        totp.setTime(59)
        assertEquals("94287082", totp.generatePin())
        totp.setTime(1111111109L)
        assertEquals("07081804", totp.generatePin())
        totp.setTime(1111111111L)
        assertEquals("14050471", totp.generatePin())
        totp.setTime(1234567890L)
        assertEquals("89005924", totp.generatePin())
        totp.setTime(2000000000L)
        assertEquals("69279037", totp.generatePin())
        totp.setTime(20000000000L)
        assertEquals("65353130", totp.generatePin())
    }

    @Test
    fun testTotpTValue() {
        val totp = Totp(secret)

        val knownIO = arrayOf(
            59L to 1L,
            1111111109L to 0x23523ECL,
            1111111111L to 0x23523EDL,
            1234567890L to 0x273EF07L,
            2000000000L to 0x3F940AAL,
            20000000000L to 0x27BC86AAL
        )

        for(io in knownIO) {
            totp.setTime(io.first)
            assertEquals(io.second, totp.getT())
        }
    }
}