import kotlinx.serialization.Serializable

@Serializable
data class ValorantResponse(
    val status: Int,
    val data: List<Agent>
)

@Serializable
data class Agent(
    val uuid: String,
    val displayName: String,
    val description: String,
    val displayIcon: String?, // El icono pequeño
    val fullPortrait: String?, // La imagen grande
    val background: String?,
    val role: AgentRole?,
    val abilities: List<Ability>
)

@Serializable
data class AgentRole(
    val displayName: String,
    val description: String,
    val displayIcon: String?
)

@Serializable
data class Ability(
    val slot: String,
    val displayName: String,
    val description: String,
    val displayIcon: String?
)