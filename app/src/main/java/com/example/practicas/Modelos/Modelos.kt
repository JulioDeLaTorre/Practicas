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

@Serializable
data class CocktailResponse(
    val drinks: List<Drink>? = null // Puede venir nulo si no encuentra nada
)

@Serializable
data class Drink(
    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String, // La foto de la bebida
    val strInstructions: String? = null, // Instrucciones en inglés
    val strInstructionsES: String? = null, // Instrucciones en español (si hay)
    // De momento ignoramos los ingredientes para no complicar la vista principal

    val strIngredient1: String? = null,val strMeasure1: String? = null,
    val strIngredient2: String? = null,val strMeasure2: String? = null,
    val strIngredient3: String? = null,val strMeasure3: String? = null,
    val strIngredient4: String? = null,val strMeasure4: String? = null,
    val strIngredient5: String? = null,val strMeasure5: String? = null,
    val strIngredient6: String? = null,val strMeasure6: String? = null,
    val strIngredient7: String? = null,val strMeasure7: String? = null,
    val strIngredient8: String? = null,val strMeasure8: String? = null,
    val strIngredient9: String? = null,val strMeasure9: String? = null,
    val strIngredient10: String? = null,val strMeasure10: String? = null,
    val strIngredient11: String? = null,val strMeasure11: String? = null,
    val strIngredient12: String? = null,val strMeasure12: String? = null,
    val strIngredient13: String? = null,val strMeasure13: String? = null,
    val strIngredient14: String? = null,val strMeasure14: String? = null,
    val strIngredient15: String? = null,val strMeasure15: String? = null
)