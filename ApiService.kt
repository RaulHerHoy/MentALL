package com.example.mentall.data.api

import com.example.mentall.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ========== Auth ==========
    @POST("auth/login.php")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<User>>

    @POST("auth/register.php")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<User>>

    // ========== Moods ==========
    @POST("moods/create.php")
    suspend fun createMood(@Body request: MoodCreateRequest): Response<ApiResponse<Mood>>

    @GET("moods/list.php")
    suspend fun listMoods(
        @Query("id_usuario") idUsuario: Int,
        @Query("limit") limit: Int = 50
    ): Response<ApiResponse<MoodListResponse>>

    @GET("moods/stats.php")
    suspend fun getMoodStats(
        @Query("id_usuario") idUsuario: Int,
        @Query("days") days: Int = 7
    ): Response<ApiResponse<MoodStatsResponse>>

    // ========== Activities ==========
    @GET("activities/list.php")
    suspend fun listActivities(): Response<ApiResponse<ActivityListResponse>>

    // ========== Recommendations ==========
    @GET("recommendations/get.php")
    suspend fun getRecommendations(
        @Query("id_usuario") idUsuario: Int
    ): Response<ApiResponse<RecommendationResponse>>

    // ========== Skills ==========
    @GET("skills/list.php")
    suspend fun listSkills(
        @Query("search") search: String? = null,
        @Query("limit") limit: Int = 50
    ): Response<ApiResponse<SkillListResponse>>

    @POST("skills/create.php")
    suspend fun createSkill(@Body request: SkillCreateRequest): Response<ApiResponse<Skill>>

    // ========== Contacts ==========
    @GET("contacts/list.php")
    suspend fun listContacts(
        @Query("id_usuario") idUsuario: Int
    ): Response<ApiResponse<ContactListResponse>>

    @POST("contacts/create.php")
    suspend fun createContact(@Body request: ContactCreateRequest): Response<ApiResponse<Contact>>

    // ========== Scheduled Calls ==========
    @GET("calls/list.php")
    suspend fun listScheduledCalls(
        @Query("id_usuario") idUsuario: Int
    ): Response<ApiResponse<CallListResponse>>

    @POST("calls/create.php")
    suspend fun createScheduledCall(@Body request: ScheduledCallRequest): Response<ApiResponse<ScheduledCall>>

    @PUT("calls/toggle.php")
    suspend fun toggleCall(@Body request: CallToggleRequest): Response<ApiResponse<Any>>
}
