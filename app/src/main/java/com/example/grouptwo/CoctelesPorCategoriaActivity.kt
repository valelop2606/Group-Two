package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grouptwo.adapters.CoctelesAdapter
import com.example.grouptwo.databinding.ActivityCoctelesPorCategoriaBinding
import com.example.grouptwo.dataclases.Coctel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.io.InputStream

class CoctelesPorCategoriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoctelesPorCategoriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoctelesPorCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoriaSeleccionada = intent.getStringExtra("categoria")
        binding.txtTituloCategoria.text = categoriaSeleccionada ?: "Cócteles"

        val listaCocteles = cargarCoctelesDesdeJson()
        val filtrados = listaCocteles.filter { c ->
            c.categorias.any { it.texto.equals(categoriaSeleccionada, ignoreCase = true) }
        }

        // ✅ Adapter modificado para abrir la receta detallada
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
