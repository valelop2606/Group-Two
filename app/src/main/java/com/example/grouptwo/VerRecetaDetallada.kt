package com.example.grouptwo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grouptwo.adapters.IngredientesAdapter
import com.example.grouptwo.adapters.PasosAdapter
import com.example.grouptwo.databinding.ActivityVerRecetaDetalladaBinding
import com.example.grouptwo.dataclases.CoctelesDatabase
import com.example.grouptwo.repository.Favoritos
//import com.example.grouptwo.repository.Favoritos
//import com.example.grouptwo.repository.Favoritos
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
        // 1) Leer el id que viene por Intent
        val cocktailId = intent?.getStringExtra(EXTRA_COCKTAIL_ID)
        if (cocktailId.isNullOrEmpty()) {
            Toast.makeText(this, "ID del cóctel no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        currentCocktailId = cocktailId

        val txt = assets.open("cocteles.json").bufferedReader().use { it.readText() }
        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        val db = json.decodeFromString<CoctelesDatabase>(txt)

        val cocktail = db.cocteles.firstOrNull { it.id == cocktailId }
        if (cocktail == null) {
            Toast.makeText(this, "Cóctel no encontrado: $cocktailId", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.txtTitulo.text = cocktail.nombre
        binding.tvDescripcion.text = cocktail.descripcion
        binding.tvDificultad.text = cocktail.dificultad
        binding.tvSabor.text = cocktail.sabor
        binding.tvNivelAlcohol.text = cocktail.nivel_alcohol


        ingredientesAdapter.addDataCards(cocktail.ingredientes)
        pasosAdapter.addDataCards(cocktail.pasos.sortedBy { it.n })

        // ✅ Botón favoritos (solo guardar/quitar de la lista)
//        binding.btnFavoritos.setOnClickListener {
//            val id = currentCocktailId ?: return@setOnClickListener
////            val ahoraEsFavorito = Favoritos.toggle(this, id)
//            val msg = if (ahoraEsFavorito) "Añadido a Favoritos" else "Eliminado de Favoritos"
//            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
//        }

        // Botón video (si luego lo habilitas)
        // binding.btnVideo.setOnClickListener {
        //     cocktail.url_video_tutorial?.let { url ->
        //         startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        //     } ?: Toast.makeText(this, "Video no disponible", Toast.LENGTH_SHORT).show()
        // }
    }

    private fun setupListeners() {
        binding.btnVolver.setOnClickListener { finish() }
        binding.btnFavoritos.setOnClickListener {
            val id = currentCocktailId ?: return@setOnClickListener
            val added = Favoritos.toggle(this, id)   // ← pasa this
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
