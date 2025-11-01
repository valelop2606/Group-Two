package com.example.grouptwo

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

    companion object {
        const val EXTRA_COCKTAIL_ID = "extra_cocktail_id"
    }

    private lateinit var binding: ActivityVerRecetaDetalladaBinding
    private val ingredientesAdapter = IngredientesAdapter()
    private val pasosAdapter = PasosAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerRecetaDetalladaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        loadCocktailData()
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

    private fun loadCocktailData() {
        // 1) Leer el id que viene por Intent
        val cocktailId = intent?.getStringExtra(EXTRA_COCKTAIL_ID)
        if (cocktailId.isNullOrEmpty()) {
            Toast.makeText(this, "ID del cóctel no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        // 2) Leer JSON (usa Json con configuración tolerante)
        val txt = assets.open("cocteles.json").bufferedReader().use { it.readText() }
        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        val db = json.decodeFromString<CoctelesDatabase>(txt)

        // 3) Buscar el cóctel por id
        val cocktail = db.cocteles.firstOrNull { it.id == cocktailId }
        if (cocktail == null) {
            Toast.makeText(this, "Cóctel no encontrado: $cocktailId", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 4) Pintar la UI
        binding.tvTitulo.text = cocktail.nombre
        binding.tvDescripcion.text = cocktail.descripcion
        binding.tvDificultad.text = cocktail.dificultad
        binding.tvSabor.text = cocktail.sabor
        binding.tvNivel.text = cocktail.nivel_alcohol

        ingredientesAdapter.submitList(cocktail.ingredientes)
        pasosAdapter.submitList(cocktail.pasos.sortedBy { it.n })



        // Botón video (si luego lo habilitas)
        // binding.btnVideo.setOnClickListener {
        //     cocktail.url_video_tutorial?.let { url ->
        //         startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        //     } ?: Toast.makeText(this, "Video no disponible", Toast.LENGTH_SHORT).show()
        // }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }
    }
}
