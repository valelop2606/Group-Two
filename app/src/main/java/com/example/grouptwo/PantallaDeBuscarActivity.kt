package com.example.grouptwo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PantallaDeBuscarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_de_buscar)

        // Leer extras opcionales enviados desde la pantalla inicial
        val query = intent.getStringExtra("query")
        val modo = intent.getStringExtra("modo")

        if (!query.isNullOrEmpty()) {
            // Temporal: mostrar un Toast con la consulta (reemplaza por lógica real)
            Toast.makeText(this, "Buscando: $query", Toast.LENGTH_SHORT).show()
            // Aquí ejecutarías la búsqueda y poblarías la UI de pantalla_de_buscar
        } else if (modo == "tengo_en_casa") {
            Toast.makeText(this, "Modo: Lo que tengo en casa", Toast.LENGTH_SHORT).show()
            // Aquí filtrarías por los ingredientes disponibles
        }
    }
}
