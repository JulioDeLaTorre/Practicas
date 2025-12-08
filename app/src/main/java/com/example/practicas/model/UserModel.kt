package com.example.practicas.model

data class UserModel(
    val userId: String = "",      // <--- IMPORTANTE: Valor por defecto
    val email: String = "",       // <--- IMPORTANTE: Valor por defecto
    val username: String = "",    // <--- IMPORTANTE: Valor por defecto
    val phoneNumber: String = ""  // <--- IMPORTANTE: Valor por defecto
){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "userId" to this.userId,
            "email" to this.email,
            "username" to this.username,
            "phoneNumber" to this.phoneNumber
        )
    }
}
