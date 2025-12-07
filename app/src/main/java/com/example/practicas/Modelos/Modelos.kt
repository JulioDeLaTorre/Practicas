import kotlinx.serialization.Serializable
@Serializable
data class ValorantResponse(
    val status: Int = 0,
    val data: List<Agent> = emptyList()
)

@Serializable
data class Agent(
    val uuid: String = "",
    val displayName: String = "Agente",
    val description: String = "",
    val displayIcon: String? = null,
    val fullPortrait: String? = null,
    val background: String? = null,
    // AGREGAMOS ESTO:
    val backgroundGradientColors: List<String> = emptyList(),
    val role: AgentRole? = null,
    val abilities: List<Ability> = emptyList()
)

@Serializable
data class AgentRole(
    val displayName: String = "Rol",
    val description: String = "",
    val displayIcon: String? = null
)

@Serializable
data class Ability(
    val slot: String = "",
    val displayName: String = "",
    val description: String = "",
    val displayIcon: String? = null
)