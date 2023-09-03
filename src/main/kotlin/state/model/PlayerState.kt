package state.model

import game.model.Player
import game.model.PlayerResult

/**
 * Абстрактный класс для хранения состояния игрока (очки, использованные слова)
 * Реализация может быть в том числе представлением из Базы Данных
 */
abstract class PlayerState(
    val player: Player
) {

    abstract fun getScore(): Long

    /**
     * Добавляет [cost] очков, если слово новое, иначе 0
     *
     * @return новое количество очков у игрока
     */
    abstract fun addWord(word: String, cost: Int): Long

    abstract fun isWordUsed(word: String): Boolean

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerState

        if (player.id != other.player.id) return false

        return true
    }

    override fun hashCode(): Int {
        return player.id.hashCode()
    }

    override fun toString(): String {
        return "PlayerState(playerId=${player.id}, score=${getScore()})"
    }
}

fun PlayerState.toPlayerResult() = PlayerResult(player, getScore())