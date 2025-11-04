package com.example.grouptwo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grouptwo.adapters.IngredientesAdapter
import com.example.grouptwo.adapters.PasosAdapter
import com.example.grouptwo.data.GuardarMiReceta
import com.example.grouptwo.databinding.ActivityVerRecetaDetalladaBinding
import com.example.grouptwo.dataclases.CoctelesDatabase
import com.example.grouptwo.dataclases.Coctel
import com.example.grouptwo.repository.Favoritos
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class VerRecetaDetalladaActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COCKTAIL_ID = "extra_cocktail_id"
    }

    private lateinit var binding: ActivityVerRecetaDetalladaBinding
    private val ingredientesAdapter = IngredientesAdapter()
    private val pasosAdapter = PasosAdapter()
    private var currentCocktailId: String? = null

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
        val cocktailId = intent?.getStringExtra(EXTRA_COCKTAIL_ID)
        if (cocktailId.isNullOrEmpty()) {
            Toast.makeText(this, "ID del cóctel no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        currentCocktailId = cocktailId

        // ✅ 1) Intentar cargar desde JSON
        val txt = assets.open("cocteles.json").bufferedReader().use { it.readText() }
        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        val db = json.decodeFromString<CoctelesDatabase>(txt)
        var cocktail: Coctel? = db.cocteles.firstOrNull { it.id == cocktailId }

        // ✅ 2) Si no está en el JSON, buscar en las recetas creadas por el usuario
        if (cocktail == null) {
            val guardadas = GuardarMiReceta.getAll(this)
            cocktail = guardadas.firstOrNull { it.id == cocktailId }
        }

        // ✅ 3) Si no existe en ningún lado, salir
        val cocktail = db.cocteles.firstOrNull { it.id == cocktailId }
        if (cocktail == null) {
            Toast.makeText(this, "Cóctel no encontrado: $cocktailId", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ✅ 4) Pintar UI
        binding.txtTitulo.text = cocktail.nombre
        binding.tvDescripcion.text = cocktail.descripcion
        binding.tvDificultad.text = cocktail.dificultad
        binding.tvSabor.text = cocktail.sabor
        binding.tvNivelAlcohol.text = cocktail.nivel_alcohol

        ingredientesAdapter.addDataCards(cocktail.ingredientes)
        pasosAdapter.addDataCards(cocktail.pasos.sortedBy { it.n })
    }

    private fun setupListeners() {
        binding.btnVolver.setOnClickListener { finish() }
        binding.btnFavoritos.setOnClickListener {
            val id = currentCocktailId ?: return@setOnClickListener
            val added = Favoritos.toggle(this, id)
            val msg = if (added) "Añadido a favoritos" else "Quitado de favoritos"
            if (added) {
                binding.btnFavoritos.setImageResource(R.drawable.favorito_on)
            } else {
                binding.btnFavoritos.setImageResource(R.drawable.favorito)
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}
