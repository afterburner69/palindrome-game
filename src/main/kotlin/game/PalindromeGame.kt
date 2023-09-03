package game

import game.model.Player
import game.model.PlayerResult
import state.GameState

/**
 * Интерфейс игры в палиндром
 * Отвечает только за проверку корректности введенных слов (подразумевается что с помощью [PalindromeChecker])
 * Ничего не знает об уже введеных игроками словах и остальных правилах
 * Остальное делегирует в [GameState]
 */
interface PalindromeGame {

    fun registerPlayer(player: Player)

    fun removePlayer(playerId: Long)

    fun getLeaders(): List<PlayerResult>

    fun acceptWord(playerId: Long, word: String)
}