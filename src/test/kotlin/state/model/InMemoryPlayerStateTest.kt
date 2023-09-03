package state.model

import game.PalindromeGameImplTest
import game.model.Player
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class InMemoryPlayerStateTest {

    private val playerId = 1L
    private val player = Player(playerId, "name")

    private lateinit var playerState: InMemoryPlayerState

    @BeforeEach
    fun warmUp() {
        playerState = InMemoryPlayerState(player)
    }

    @Test
    fun `addWord for new word`() {
        var sum = 0L
        repeat(100_000) {
            val word = PalindromeGameImplTest.generatePalindrome()
            sum += word.length
            assertEquals(sum, playerState.addWord(word, word.length))
            assertEquals(sum, playerState.getScore())
        }
    }

    @Test
    fun `addWord for present word`() {
        val word = PalindromeGameImplTest.generatePalindrome()
        repeat(100_000) {
            assertEquals(word.length.toLong(), playerState.addWord(word, word.length))
            assertEquals(word.length.toLong(), playerState.getScore())
        }
    }

    @Test
    fun isWordUsed() {
        repeat(100_000) {
            val word = PalindromeGameImplTest.generatePalindrome()
            val anotherWord = PalindromeGameImplTest.generatePalindrome()
            playerState.addWord(word, word.length)
            assertTrue(playerState.isWordUsed(word))
            assertFalse(playerState.isWordUsed(anotherWord))
        }
    }
}