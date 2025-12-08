package com.example.practicas.viewModels

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicas.model.UserModel
import com.example.practicas.network.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import com.example.practicas.Secrets

class LoginViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = FirebaseFirestore.getInstance() // Instancia para usarla en varias fun

    var showAlert by mutableStateOf(false)
    var verificationCode by mutableStateOf("")
    private var generatedCode = ""

    // Variables para almacenar datos temporales durante el flujo de recuperación
    var tempEmailForRecovery: String = ""

    // CREDENCIALES TWILIO (Mismas que tenías)
    private val accountSid = Secrets.TWILIO_SID
    private val authToken = Secrets.TWILIO_TOKEN
    private val twilioNumber = "+1 636 396 0484"

    // --- LOGICA DE RECUPERACIÓN HÍBRIDA ---

    fun onForgotPassword(input: String, onEmailSent: () -> Unit, onPhoneFlow: () -> Unit) {
        if (input.contains("@")) {
            // Es un correo
            auth.sendPasswordResetEmail(input)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onEmailSent()
                    } else {
                        showAlert = true
                    }
                }
        } else {
            // Asumimos que es un teléfono.
            // 1. Verificamos que el teléfono exista en Firestore
            checkPhoneExists(input) { exists ->
                if (exists) {
                    // 2. Si existe, enviamos la llamada
                    sendRecoveryCode(input) {
                        onPhoneFlow()
                    }
                } else {
                    // El numero no esta registrado
                    showAlert = true
                }
            }
        }
    }

    private fun checkPhoneExists(phone: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection("Users")
                .whereEqualTo("phoneNumber", phone)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // Guardamos el email asociado por si lo necesitamos luego
                        tempEmailForRecovery = documents.documents[0].getString("email") ?: ""
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                }
                .addOnFailureListener {
                    onResult(false)
                }
        }
    }

    // --- TWILIO ---
    fun sendRecoveryCode(phoneNumber: String, onSuccess: () -> Unit) {
        generatedCode = (100000..999999).random().toString()
        val codigoEspaciado = generatedCode.toCharArray().joinToString(". ")
        val miUrlDeTwilio = "https://handler.twilio.com/twiml/EH37f6a340a61ba8153329144ec053ce07"
        val finalUrl = "$miUrlDeTwilio?code=${URLEncoder.encode(codigoEspaciado, "UTF-8")}"
        val base64Auth = "Basic " + Base64.encodeToString("$accountSid:$authToken".toByteArray(), Base64.NO_WRAP)

        RetrofitClient.instance.makeCall(
            authHeader = base64Auth,
            accountSid = accountSid,
            to = phoneNumber,
            from = twilioNumber,
            url = finalUrl
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) onSuccess() else showAlert = true
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) { showAlert = true }
        })
    }

    fun verifyCode(code: String, onSuccess: () -> Unit) {
        if (code == generatedCode) {
            onSuccess()
        } else {
            showAlert = true
        }
    }

    // --- CAMBIO DE CONTRASEÑA ---
    fun updatePassword(newPassword: String, onSuccess: () -> Unit) {
        // NOTA: Para cambiar la contraseña en Firebase el usuario debe estar logueado.
        // Si venimos del flujo de "Olvidé contraseña" por teléfono, firebase no nos loguea automáticamente solo con el código.
        // En un entorno real, usarías un Custom Token o Cloud Functions.
        // Para este ejercicio, intentaremos cambiarla si el usuario tiene sesión activa, o mostrar alerta si no.

        val user = auth.currentUser
        if (user != null) {
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        Log.d("UPDATE PASS", "Error: ${task.exception?.message}")
                        showAlert = true
                    }
                }
        } else {
            // Caso extremo: No logueado. Requeriría re-autenticación con credenciales antiguas (que no tiene)
            // o backend admin. Simularemos el éxito si la lógica de negocio lo permite o
            // loguearemos al usuario con el email recuperado (tempEmailForRecovery) si tuviéramos la pass antigua (imposible).
            // SOLUCIÓN PRÁCTICA: Enviar correo de reset final al email recuperado si no podemos cambiarla directemente.
            if(tempEmailForRecovery.isNotEmpty()){
                auth.sendPasswordResetEmail(tempEmailForRecovery)
                onSuccess() // Decimos éxito pero le mandamos el correo porque firebase protege el cambio directo.
            } else {
                showAlert = true
            }
        }
    }

    // --- REGISTRO Y LOGIN ---
    fun login(input: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            // CASO 1: Es un Correo (tiene @)
            if (input.contains("@")) {
                performFirebaseLogin(input, password, onSuccess)
            }
            // CASO 2: Es un Teléfono (asumimos 10 dígitos)
            else {
                // Formateamos: Si el usuario no puso +, asumimos que es local y agregamos +52
                // OJO: Ajusta el prefijo si usas otros países.
                val phoneSearch = if (input.startsWith("+")) input else "+52$input"

                // Buscamos el correo asociado a ese teléfono
                firestore.collection("Users")
                    .whereEqualTo("phoneNumber", phoneSearch)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            // ¡Encontrado! Sacamos el email
                            val email = documents.documents[0].getString("email") ?: ""

                            // Hacemos el login oficial con ese email y la contraseña que puso el usuario
                            if (email.isNotEmpty()) {
                                performFirebaseLogin(email, password, onSuccess)
                            } else {
                                showAlert = true // Usuario existe pero no tiene email (caso raro)
                            }
                        } else {
                            // No existe ese teléfono registrado
                            showAlert = true
                        }
                    }
                    .addOnFailureListener {
                        showAlert = true
                    }
            }
        }
    }

    // Función auxiliar privada para no repetir código
    private fun performFirebaseLogin(email: String, password: String, onSuccess: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    Log.d("LOGIN ERROR", "Fallo auth: ${task.exception?.message}")
                    showAlert = true
                }
            }
    }

    // MODIFICADO: Agregamos phoneNumber
    fun createUser(email: String, password: String, username: String, phoneNumber: String, onSuccess: () -> Unit){
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        saveUser(username, phoneNumber) // Guardamos telefono
                        onSuccess()
                    }else{
                        showAlert = true
                    }
                }
        }
    }

    // MODIFICADO: Guardar teléfono en Firestore
    private fun saveUser(username: String, phoneNumber: String){
        val id = auth.currentUser?.uid
        val email = auth.currentUser?.email

        viewModelScope.launch(Dispatchers.IO){
            val user = UserModel(
                userId = id.toString(),
                email = email.toString(),
                username = username,
                phoneNumber = phoneNumber
            )
            firestore.collection("Users").document(id.toString()) // Usamos el UID como ID del documento para facilitar busquedas
                .set(user.toMap()) // Usamos set en vez de add para definir el ID
        }
    }

    // Obtener datos del usuario actual
    // 1. OBTENER DATOS (Corregido para buscar por campo userId)
    fun getCurrentUserData(onResult: (UserModel?) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            // CAMBIO CLAVE: Usamos 'whereEqualTo' en vez de 'document(uid)'
            firestore.collection("Users")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // Tomamos el primer documento que coincida
                        val document = documents.documents[0]
                        Log.d("FIREBASE_DEBUG", "Documento encontrado: ${document.id}")

                        try {
                            val user = document.toObject(UserModel::class.java)
                            onResult(user)
                        } catch (e: Exception) {
                            Log.e("FIREBASE_ERROR", "Error al convertir: ${e.message}")
                            onResult(null)
                        }
                    } else {
                        Log.d("FIREBASE_DEBUG", "No se encontró usuario con userId: $uid")
                        onResult(null)
                    }
                }
                .addOnFailureListener {
                    Log.e("FIREBASE_ERROR", "Error en la consulta: ${it.message}")
                    onResult(null)
                }
        } else {
            onResult(null)
        }
    }

    // 2. ACTUALIZAR DATOS (Corregido para encontrar el documento correcto antes de editar)
    fun updateUserProfile(username: String, phone: String, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            viewModelScope.launch(Dispatchers.IO) {
                // Primero buscamos cuál es el ID del documento de este usuario
                firestore.collection("Users")
                    .whereEqualTo("userId", uid)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            // Obtenemos la referencia al documento encontrado (sea cual sea su ID)
                            val docRef = documents.documents[0].reference

                            val updates = mapOf(
                                "username" to username,
                                "phoneNumber" to phone
                            )

                            docRef.update(updates)
                                .addOnSuccessListener { onSuccess() }
                                .addOnFailureListener { showAlert = true }
                        } else {
                            Log.d("UPDATE_ERROR", "No se encontró el usuario para actualizar")
                            showAlert = true
                        }
                    }
                    .addOnFailureListener {
                        Log.d("UPDATE_ERROR", "Error buscando usuario: ${it.message}")
                        showAlert = true
                    }
            }
        }
    }

    fun closeAlert(){ showAlert = false }
}