package game

import game.exception.PalindromeGameException
import game.model.Player
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertThrows
import state.CachedGameState
import state.repository.InMemoryGameStateRepository
import kotlin.random.Random

internal class PalindromeGameImplTest {

    companion object {
        private val state = CachedGameState(1, InMemoryGameStateRepository())
        private val game = PalindromeGameImpl(state, BasicPalindromeChecker())
        private lateinit var testPlayer: Player

        @BeforeAll
        @JvmStatic
        fun init() {
            testPlayer = Player(Random.nextLong(), "name")
            game.registerPlayer(testPlayer)
        }

        private val charPool = ('a'..'z').toList()

        private fun randomChar() = Random.nextInt(0, charPool.size).let { charPool[it] }

        fun generatePalindrome(
            correct: Boolean = true,
            even: Boolean = true,
            caseInsensitive: Boolean = false,
        ): String {
            var leftPart = (1..Random.nextInt(10, 50))
                .map { randomChar() }
                .joinToString("")
            if (!even) {
                leftPart = leftPart.plus(randomChar())
            }
            var rightPart = if (correct) {
                leftPart.reversed()
            } else {
                leftPart.plus(randomChar())
            }
            if (caseInsensitive) {
                rightPart = rightPart.uppercase()
            }
            return (leftPart + rightPart)
        }
    }

    @Test
    fun `acceptWord correct, even, case sensitive`() {
        game.acceptWord(testPlayer.id, generatePalindrome())
    }

    @Test
    fun `acceptWord correct, even, case insensitive`() {
        game.acceptWord(testPlayer.id, generatePalindrome(caseInsensitive = true))
    }

    @Test
    fun `acceptWord correct, odd, case sensitive`() {
        game.acceptWord(testPlayer.id, generatePalindrome(even = false))
    }

    @Test
    fun `acceptWord correct, odd, case insensitive`() {
        game.acceptWord(testPlayer.id, generatePalindrome(even = false, caseInsensitive = true))
    }

    @Test
    fun `acceptWord incorrect, even, case sensitive`() {
        assertThrows<PalindromeGameException> {
            game.acceptWord(testPlayer.id, generatePalindrome(correct = false))
        }
    }

    @Test
    fun `acceptWord incorrect, odd, case insensitive`() {
        assertThrows<PalindromeGameException> {
            game.acceptWord(testPlayer.id, generatePalindrome(correct = false, caseInsensitive = true))
        }
    }
}