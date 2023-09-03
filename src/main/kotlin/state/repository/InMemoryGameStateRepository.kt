package state.repository

import game.model.Player
import game.model.PlayerResult
import state.model.InMemoryPlayerState
import state.model.PlayerState
import state.model.toPlayerResult
import java.util.concurrent.ConcurrentHashMap

class InMemoryGameStateRepository : GameStateRepository {

    private val states = ConcurrentHashMap<Long, InMemoryPlayerState>()

    override fun getAllPlayers(): List<Player> = states.values.map { it.player }

    // в другой реализации здесь мог бы быть запрос в БД
    override fun getLeaders(limit: Int): List<PlayerResult> = states.values
        .sortedByDescending { it.getScore() }.asSequence()
        .take(limit)
        .filter { it.getScore() > 0 }
        .map { it.toPlayerResult() }.toList()

    override fun getPlayerResultById(id: Long): PlayerResult? = getPlayerStateById(id)?.toPlayerResult()

    override fun getPlayerStateById(id: Long): PlayerState? = states[id]

    override fun registerPlayer(player: Player): Boolean {
        val playerState = InMemoryPlayerState(player)
        return states.putIfAbsent(player.id, playerState) == null
    }

    override fun removePlayer(playerId: Long): Boolean {
        return states.remove(playerId) != null
    }

    override fun clear() {
        states.clear()
    }
}