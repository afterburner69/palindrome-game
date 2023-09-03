package game

import state.GameState
import game.model.Player
import game.model.PlayerResult
import game.exception.PalindromeGameException


class PalindromeGameImpl(
    private val gameState: GameState,
    private val palindromeChecker: PalindromeChecker,
) : PalindromeGame {

    override fun registerPlayer(player: Player) {
        gameState.registerPlayer(player)
    }

    override fun removePlayer(playerId: Long) {
        gameState.removePlayer(playerId)
    }

    override fun getLeaders(): List<PlayerResult> = gameState.getLeaders()

    override fun acceptWord(playerId: Long, word: String) {
        if (!palindromeChecker.checkWord(word)) throw PalindromeGameException(
            "Word '$word' is not palindrome"
        )
        gameState.addWordToPlayer(playerId, word)
    }
}