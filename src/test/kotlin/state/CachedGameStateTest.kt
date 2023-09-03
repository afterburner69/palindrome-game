package state

import game.PalindromeGameImplTest
import game.model.Player
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import state.repository.InMemoryGameStateRepository
import kotlin.random.Random

internal class CachedGameStateTest {

    companion object {

        private const val LEADERS_CAPACITY = 5
        private val repository = InMemoryGameStateRepository()
        private val state = CachedGameState(LEADERS_CAPACITY, repository)
        private val guarantor = NaiveGameState(LEADERS_CAPACITY, repository)

        private class GameStateTestingFacade {

            fun registerPlayer(player: Player): Boolean {
                guarantor.registerPlayer(player)
                return state.registerPlayer(player)
            }

            fun removePlayer(playerId: Long): Boolean {
                guarantor.removePlayer(playerId)
                return state.removePlayer(playerId)
            }

            fun clear() {
                state.clear()
                guarantor.clear()
            }

            fun addWordToPlayer(playerId: Long, word: String): Long {
                guarantor.addWordToPlayer(playerId, word)
                return state.addWordToPlayer(playerId, word)
            }
        }

        private val testingFacade = GameStateTestingFacade()

        fun generateAndRegisterPlayers(size: Int): List<Player> = (1L..size).map {
            Player(it, "player $it")
        }.onEach {
            testingFacade.registerPlayer(it)
        }
    }

    @BeforeEach
    fun warmUp() {
        testingFacade.clear()
    }

    @Test
    fun `getLeaders cache correctness stress test`() {
        val players = generateAndRegisterPlayers(50)
        repeat(1_000_000) {
            val randomPlayerId = Random.nextLong(players.size.toLong())
            when (Random.nextInt(10)) {
                0 -> testingFacade.removePlayer(randomPlayerId)
                else -> runCatching {
                    testingFacade.addWordToPlayer(randomPlayerId, PalindromeGameImplTest.generatePalindrome())
                }.onFailure {
                    testingFacade.registerPlayer(players[randomPlayerId.toInt()])
                }
            }
            assertEquals(
                guarantor.getLeaders(),
                state.getLeaders()
            )
        }
    }

    @Test
    fun `getPlayerResultById when player is present`() {
        val playerId = generateAndRegisterPlayers(1)[0].id
        assertEquals(playerId, state.getPlayerResultById(playerId)?.player?.id)
    }

    @Test
    fun `getPlayerResultById when player is not present`() {
        val playerId = generateAndRegisterPlayers(1)[0].id
        assertEquals(null, state.getPlayerResultById(playerId + 1))
    }

    @Test
    fun `registerPlayer when player is present`() {
        val player = generateAndRegisterPlayers(1)[0]
        assertFalse(state.registerPlayer(player))
    }

    @Test
    fun `registerPlayer when player is not present`() {
        val player = Player(1L, "player")
        assertTrue(state.registerPlayer(player))
    }

    @Test
    fun `removePlayer when player is present`() {
        val playerId = generateAndRegisterPlayers(1)[0].id
        assertTrue(state.removePlayer(playerId))
    }

    @Test
    fun `removePlayer when player is not present`() {
        val playerId = generateAndRegisterPlayers(1)[0].id
        state.removePlayer(playerId)
        assertFalse(state.removePlayer(playerId))
    }
}