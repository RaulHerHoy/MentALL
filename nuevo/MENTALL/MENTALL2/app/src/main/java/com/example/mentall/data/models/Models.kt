package com.example.mentall.data.models

import com.google.gson.annotations.SerializedName

// ========== API Response ==========
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

// ========== User ==========
data class User(
    @SerializedName("id_usuario")
    val idUsuario: Int,
    val nombre: String,
    val email: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String
)

data class UpdateNameRequest(
    @SerializedName("id_usuario")
    val idUsuario: Int,
    val nombre: String
)

data class UpdatePasswordRequest(
    @SerializedName("id_usuario")
    val idUsuario: Int,
    @SerializedName("password_actual")
    val passwordActual: String,
    @SerializedName("password_nueva")
    val passwordNueva: String
)

// ========== Mood ==========
data class Mood(
    @SerializedName("id_registro")
    val idRegistro: Int? = null,
    @SerializedName("id_usuario")
    val idUsuario: Int,
    val valor: Int,
    val etiqueta: String? = null,
    @SerializedName("actividad_realizada")
    val actividadRealizada: String? = null,
    val nota: String? = null,
    @SerializedName("imagen_uri")
    val imagenUri: String? = null,
    val fecha: String? = null
)

data class MoodCreateRequest(
    @SerializedName("id_usuario")
    val idUsuario: Int,
    val valor: Int,
    val etiqueta: String? = null,
    @SerializedName("actividad_realizada")
    val actividadRealizada: String? = null,
    val nota: String? = null,
    @SerializedName("imagen_uri")
    val imagenUri: String? = null
)

data class MoodListResponse(
    val total: Int,
    val registros: List<Mood>
)

data class MoodStatsResponse(
    @SerializedName("dias_analizados")
    val diasAnalizados: Int,
    @SerializedName("total_registros")
    val totalRegistros: Int,
    val promedio: Float,
    @SerializedName("ultimos_valores")
    val ultimosValores: List<Int>,
    @SerializedName("ultimo_registro")
    val ultimoRegistro: UltimoRegistro?
)

data class UltimoRegistro(
    val valor: Int,
    @SerializedName("actividad_realizada")
    val actividadRealizada: String?,
    val nota: String?,
    val fecha: String
)

// ========== Activity ==========
data class Activity(
    @SerializedName("id_actividad")
    val idActividad: Int,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    @SerializedName("duracion_min")
    val duracionMin: Int,
    val enlace: String? = null
)

data class ActivityListResponse(
    val total: Int,
    val actividades: List<Activity>
)

// ========== Recommendation ==========
data class Recommendation(
    @SerializedName("id_actividad")
    val idActividad: Int,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    @SerializedName("duracion_min")
    val duracionMin: Int,
    val enlace: String? = null,
    val motivo: String,
    @SerializedName("mood_valor")
    val moodValor: Int? = null
)

data class RecommendationResponse(
    val total: Int,
    val recomendaciones: List<Recommendation>
)

// ========== Skill ==========
data class Skill(
    @SerializedName("id_skill")
    val idSkill: Int? = null,
    @SerializedName("id_usuario")
    val idUsuario: Int,
    val titulo: String,
    val nivel: String,
    val descripcion: String? = null,
    @SerializedName("usuario_nombre")
    val usuarioNombre: String? = null,
    val fecha: String? = null
)

data class SkillCreateRequest(
    @SerializedName("id_usuario")
    val idUsuario: Int,
    val titulo: String,
    val nivel: String,
    val descripcion: String? = null
)

data class SkillListResponse(
    val total: Int,
    val skills: List<Skill>
)

// ========== Contact ==========
data class Contact(
    @SerializedName("id_contacto")
    val idContacto: Int? = null,
    @SerializedName("id_usuario")
    val idUsuario: Int,
    val nombre: String,
    val telefono: String,
    val descripcion: String? = null,
    @SerializedName("es_emergencia")
    val esEmergencia: Int = 0,
    val orden: Int = 0
)

data class ContactCreateRequest(
    @SerializedName("id_usuario")
    val idUsuario: Int,
    val nombre: String,
    val telefono: String,
    val descripcion: String? = null,
    @SerializedName("es_emergencia")
    val esEmergencia: Int = 0,
    val orden: Int = 0
)

data class ContactListResponse(
    val total: Int,
    val contactos: List<Contact>
)

// ========== Scheduled Call ==========
data class ScheduledCall(
    @SerializedName("id_llamada")
    val idLlamada: Int? = null,
    @SerializedName("id_usuario")
    val idUsuario: Int,
    val telefono: String,
    val motivo: String? = null,
    @SerializedName("dia_semana")
    val diaSemana: Int? = null,
    @SerializedName("dia_mes")
    val diaMes: Int? = null,
    val hora: String,
    val activa: Int = 1,
    val descripcion: String? = null
)

data class ScheduledCallRequest(
    @SerializedName("id_usuario")
    val idUsuario: Int,
    val telefono: String,
    val motivo: String? = null,
    @SerializedName("dia_semana")
    val diaSemana: Int? = null,
    @SerializedName("dia_mes")
    val diaMes: Int? = null,
    val hora: String
)

data class CallListResponse(
    val total: Int,
    val llamadas: List<ScheduledCall>
)

data class CallToggleRequest(
    @SerializedName("id_llamada")
    val idLlamada: Int,
    val activa: Int
)
