package com.example.grouptwo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grouptwo.adapters.IngredientesAdapter
import com.example.grouptwo.adapters.PasosAdapter
import com.example.grouptwo.databinding.ActivityVerRecetaDetalladaBinding
import com.example.grouptwo.dataclases.CoctelesDatabase
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class VerRecetaDetalladaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerRecetaDetalladaBinding
    private val ingredientesAdapter = IngredientesAdapter()
    private val pasosAdapter = PasosAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerRecetaDetalladaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        loadMojitoData()
        setupListeners()
    }

    private fun setupRecyclerViews() {
        binding.rvIngredientes.apply {
            layoutManager = LinearLayoutManager(this@VerRecetaDetalladaActivity)
            adapter = ingredientesAdapter
            isNestedScrollingEnabled = false
        }

        binding.rvPasos.apply {
            layoutManager = LinearLayoutManager(this@VerRecetaDetalladaActivity)
            adapter = pasosAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun loadMojitoData() {
        // Leer JSON desde assets
        val txt = assets.open("cocteles.json").bufferedReader().use { it.readText() }
        val db = Json.decodeFromString<CoctelesDatabase>(txt)

        // Buscar el Mojito por id
        val mojito = db.cocteles.firstOrNull { it.id == "ckt_mojito" }

        if (mojito == null) {
            Toast.makeText(this, "Cóctel Mojito no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Pintar en la UI
        binding.tvTitulo.text = mojito.nombre
        binding.tvDescripcion.text = mojito.descripcion
        binding.tvDificultad.text = mojito.dificultad
        binding.tvSabor.text = mojito.sabor
        binding.tvNivel.text = mojito.nivel_alcohol

        // ✅ Asegurarse de que el adapter esté listo antes de pasar la lista
        binding.rvIngredientes.adapter = ingredientesAdapter
        ingredientesAdapter.submitList(mojito.ingredientes)

        binding.rvPasos.adapter = pasosAdapter
        pasosAdapter.submitList(mojito.pasos.sortedBy { it.n })
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

//        binding.btnVideo.setOnClickListener {
//            val txt = assets.open("cocteles.json").bufferedReader().use { it.readText() }
//            val db = Json.decodeFromString<CoctelesDatabase>(txt)
//            val mojito = db.cocteles.firstOrNull { it.id == "ckt_mojito" }
//
//            mojito?.url_video_tutorial?.let { url ->
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                startActivity(intent)
//            } ?: Toast.makeText(this, "Video no disponible", Toast.LENGTH_SHORT).show()
//        }
    }
}