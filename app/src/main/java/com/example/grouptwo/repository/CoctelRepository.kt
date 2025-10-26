package com.example.grouptwo.repository

import com.example.grouptwo.models.Coctel
import com.example.grouptwo.models.CoctelDetalle
import com.example.grouptwo.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoctelRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun obtenerTodosCocteles(): Result<List<Coctel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCocteles()
                if (response.isSuccessful && response.body()?.success == true) {
                    Result.success(response.body()?.data ?: emptyList())
                } else {
                    Result.failure(Exception("Error al obtener cócteles: ${response.body()?.message}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun obtenerCoctelPorId(id: Int): Result<CoctelDetalle> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCoctelById(id)
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let {
                        Result.success(it)
                    } ?: Result.failure(Exception("Cóctel no encontrado"))
                } else {
                    Result.failure(Exception("Error: ${response.body()?.message}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun buscarCocteles(query: String): Result<List<Coctel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.buscarCocteles(query)
                if (response.isSuccessful && response.body()?.success == true) {
                    Result.success(response.body()?.data ?: emptyList())
                } else {
                    Result.failure(Exception("No se encontraron resultados"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}