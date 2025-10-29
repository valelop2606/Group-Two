package com.example.grouptwo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grouptwo.repository.CoctelRepository  // ✅ Nombre correcto

class MainActivity : AppCompatActivity() {

    // ✅ Crear instancia correctamente
    private val repository by lazy { CoctelRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        probarCargaJSON()
    }

    private fun probarCargaJSON() {
        Log.d("JSON_TEST", "========================================")
        Log.d("JSON_TEST", "INICIANDO PRUEBAS DEL JSON")
        Log.d("JSON_TEST", "========================================")

        try {
            // ✅ PRUEBA 1: Cargar todos los cócteles (SÍNCRONO)
            Log.d("JSON_TEST", "\n📋 PRUEBA 1: Cargar todos los cócteles")
            val todosCocteles = repository.getAll()  // ✅ Correcto
            Log.d("JSON_TEST", "✅ Total de cócteles cargados: ${todosCocteles.size}")

            if (todosCocteles.isEmpty()) {
                Log.e("JSON_TEST", "❌ ERROR: No se cargaron cócteles")
                Toast.makeText(this, "❌ Error: No se cargaron cócteles", Toast.LENGTH_LONG).show()
                return
            }

            // ✅ PRUEBA 2: Primeros 3 cócteles
            Log.d("JSON_TEST", "\n📋 PRUEBA 2: Primeros 3 cócteles:")
            todosCocteles.take(3).forEach { coctel ->
                Log.d("JSON_TEST", """
                    ─────────────────────────
                    ID: ${coctel.id}
                    Nombre: ${coctel.nombre}
                    Descripción: ${coctel.descripcion}
                    Dificultad: ${coctel.nivelDificultad}
                    Alcohol: ${coctel.nivelAlcohol}
                    Sabor: ${coctel.saborPredominante}
                    Categorías: ${coctel.categorias?.joinToString(", ")}
                """.trimIndent())
            }

            // ✅ PRUEBA 3: Margarita por ID
            Log.d("JSON_TEST", "\n🍹 PRUEBA 3: Detalle de Margarita (ID: 1)")
            val margarita = repository.getById("1")  // ✅ Correcto

            if (margarita != null) {
                Log.d("JSON_TEST", "✅ Margarita encontrada: ${margarita.nombre}")
                // ... resto del código igual
            }

            // ✅ PRUEBA 4: Búsqueda (usar suspend function)
            Log.d("JSON_TEST", "\n🔍 PRUEBA 4: Buscar 'mojito' (suspend)")
            // Para pruebas rápidas, usar versión síncrona o lifecycleScope
            // Por ahora, comentar o usar getAll().filter()
            val todos = repository.getAll()
            val resultadosMojito = todos.filter {
                it.nombre.contains("mojito", ignoreCase = true)
            }
            Log.d("JSON_TEST", "✅ Resultados encontrados: ${resultadosMojito.size}")

            // Resto de pruebas similares...

            Toast.makeText(
                this,
                "✅ JSON cargado correctamente!\n${todosCocteles.size} cócteles encontrados",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {
            Log.e("JSON_TEST", "❌ ERROR CRÍTICO: ${e.message}", e)
            Toast.makeText(this, "❌ Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}