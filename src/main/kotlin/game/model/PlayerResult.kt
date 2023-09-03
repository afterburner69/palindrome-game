package game.model

/**
 * Immutable отображение результатов игрока для работы в верхних слоях логики
 */
data class PlayerResult(
    val player: Player,
    val score: Long,
)
