package state.repository

import game.PalindromeGameImplTest
import game.model.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

internal class InMemoryGameStateRepositoryTest {

    private val repository = InMemoryGameStateRepository()

    private fun generatePlayers(count: Int) = (1..count).map { i ->
        Player(i.toLong(), "player $i")
    }

    private fun generateAndRegisterPlayers(count: Int) = generatePlayers(count).onEach {
        repository.registerPlayer(it)
    }

    @BeforeEach
    fun warmUp() {
        repository.clear()
    }

    @Test
    fun `getAllPlayers when present`() {
        val count = Random.nextInt(10, 50)
        val players = generateAndRegisterPlayers(count)
        assertEquals(players, repository.getAllPlayers())
    }

    @Test
    fun `getAllPlayers when not present`() {
        assertEquals(0, repository.getAllPlayers().size)
    }

    @Test
    fun `getLeaders limit test`() {
        val count = Random.nextInt(10, 50)
        val players = generateAndRegisterPlayers(count)
        players.forEach {
            val word = PalindromeGameImplTest.generatePalindrome()
            repository.getPlayerStateById(it.id)?.addWord(word, word.length)
        }
        repeat(100_000) {
            val randomLimit = Random.nextInt(1, players.size * 2)
            assertEquals(minOf(randomLimit, players.size), repository.getLeaders(randomLimit).size)
            assertEquals(0, repository.getLeaders(0).size)
            assertThrows<IllegalArgumentException> {
                repository.getLeaders(randomLimit * -1)
            }
        }
    }

    @Test
    fun `getLeaders order test`() {
        val count = Random.nextInt(10, 50)
        val players = generateAndRegisterPlayers(count)
        players.forEach {
            val word = PalindromeGameImplTest.generatePalindrome()
            repository.getPlayerStateById(it.id)?.addWord(word, word.length)
        }
        repeat(100_000) {
            val randomLimit = Random.nextInt(2, players.size * 2)
            val word = PalindromeGameImplTest.generatePalindrome()
            val randomPlayer = players[Random.nextInt(players.size)]
            repository
                .getPlayerStateById(randomPlayer.id)!!.addWord(word, word.length)
            assertDoesNotThrow {
                repository.getLeaders(randomLimit).reduce { prev, curr ->
                    if (curr.score > prev.score) throw Exception("$prev > $curr")
                    curr
                }
            }
        }
    }

    @Test
    fun getPlayerResultById() {
        val count = Random.nextInt(10, 50)
        val players = generateAndRegisterPlayers(count)
        repeat(100_000) {
            val word = PalindromeGameImplTest.generatePalindrome()
            val randomPlayer = players[Random.nextInt(players.size)]
            // NPE приравнивается к ошибке или в тесте или в коде
            // поэтому в тестах использовать !! плюс-минус уместно, в коде обрабатывается иначе
            val state = repository.getPlayerStateById(randomPlayer.id)!!
            state.addWord(word, word.length)
            assertEquals(state.player.id, repository.getPlayerResultById(state.player.id)!!.player.id)
            assertEquals(state.getScore(), repository.getPlayerResultById(state.player.id)!!.score)
        }
    }

    @Test
    fun getPlayerStateById() {
        val count = Random.nextInt(10, 50)
        val players = generateAndRegisterPlayers(count)
        players.forEach {
            val word = PalindromeGameImplTest.generatePalindrome()
            val state = repository.getPlayerStateById(it.id)!!
            val result = state.addWord(word, word.length)
            assertEquals(repository.getPlayerStateById(it.id)!!.getScore(), result)
        }
    }

    @Test
    fun `registerPlayer when not present`() {
        val count = Random.nextInt(10, 50)
        val players = generatePlayers(count)
        players.shuffled().forEach {
            assertTrue(repository.registerPlayer(it))
        }
    }

    @Test
    fun `registerPlayer when present`() {
        val count = Random.nextInt(10, 50)
        val players = generateAndRegisterPlayers(count)
        players.shuffled().forEach {
            assertFalse(repository.registerPlayer(it))
        }
    }

    @Test
    fun `removePlayer when present`() {
        val count = Random.nextInt(10, 50)
        val players = generateAndRegisterPlayers(count)
        players.shuffled().forEach {
            assertTrue(repository.removePlayer(it.id))
        }
    }

    @Test
    fun `removePlayer when not present`() {
        val count = Random.nextInt(10, 50)
        val players = generatePlayers(count)
        players.shuffled().forEach {
            assertFalse(repository.removePlayer(it.id))
        }
        players.shuffled().forEach {
            assertTrue(repository.registerPlayer(it))
        }
        players.shuffled().forEach {
            assertTrue(repository.removePlayer(it.id))
        }
        players.shuffled().forEach {
            assertFalse(repository.removePlayer(it.id))
        }
    }

    @Test
    fun `clear when empty`() {
        `removePlayer when not present`()
        assertEquals(0, repository.getAllPlayers().size)
        repository.clear()
        assertEquals(0, repository.getAllPlayers().size)
    }

    @Test
    fun `clear when not empty`() {
        `registerPlayer when present`()
        assertNotEquals(0, repository.getAllPlayers().size)
        repository.clear()
        assertEquals(0, repository.getAllPlayers().size)
    }
}