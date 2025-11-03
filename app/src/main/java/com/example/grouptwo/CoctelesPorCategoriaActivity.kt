package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grouptwo.adapters.CoctelesAdapter
import com.example.grouptwo.databinding.ActivityCoctelesPorCategoriaBinding
import com.example.grouptwo.dataclases.Coctel
import com.example.grouptwo.repository.Favoritos
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.io.InputStream

class CoctelesPorCategoriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoctelesPorCategoriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoctelesPorCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mostrarFavoritos = intent.getBooleanExtra("mostrar_favoritos", false)
        val categoriaSeleccionada = intent.getStringExtra("categoria")

        val listaCocteles = cargarCoctelesDesdeJson()
        val filtrados = if (mostrarFavoritos) {
            // ✅ Favoritos usando el contexto actual
            val favoritosIds = Favoritos.all(this)
            listaCocteles.filter { it.id in favoritosIds }
        } else {
            // ✅ Filtrado por categoría (comportamiento normal)
            listaCocteles.filter { c ->
                c.categorias.any { it.texto.equals(categoriaSeleccionada, ignoreCase = true) }
            }
        }

        binding.txtTituloCategoria.text =
            if (mostrarFavoritos) "Tus Favoritos" else (categoriaSeleccionada ?: "Cócteles")

        val adapter = CoctelesAdapter(filtrados) { coctel ->
            val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
            intent.putExtra(VerRecetaDetalladaActivity.EXTRA_COCKTAIL_ID, coctel.id)
            startActivity(intent)
        }

        binding.rvCocteles.layoutManager = LinearLayoutManager(this)
        binding.rvCocteles.adapter = adapter

        binding.btnAtras.setOnClickListener { finish() }
    }

    private fun cargarCoctelesDesdeJson(): List<Coctel> {
        val inputStream: InputStream = assets.open("cocteles.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val formato = Json { ignoreUnknownKeys = true }
        val jsonObject = formato.parseToJsonElement(jsonString).jsonObject
        val coctelesJson = jsonObject["cocteles"]!!.toString()

        return formato.decodeFromString<List<Coctel>>(coctelesJson)
    }
}
