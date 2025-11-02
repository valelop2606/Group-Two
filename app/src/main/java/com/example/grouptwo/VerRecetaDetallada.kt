package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grouptwo.adapters.IngredientesAdapter
import com.example.grouptwo.adapters.PasosAdapter
import com.example.grouptwo.databinding.ActivityVerRecetaDetalladaBinding
import com.example.grouptwo.dataclases.Coctel
import com.example.grouptwo.dataclases.CoctelesDatabase
import kotlinx.serialization.json.Json
import androidx.core.net.toUri

class VerRecetaDetalladaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerRecetaDetalladaBinding
    private val ingredientesAdapter = IngredientesAdapter()
    private val pasosAdapter = PasosAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerRecetaDetalladaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idCoctel: String? = intent.getStringExtra("COCTEL_ID")

        val coctel: Coctel? = econtrarCoctelEnLaLista(cargarTodosLosCocteles(), idCoctel)

        setupRecyclerViews()
        mostrarCoctel(coctel)
        setupListeners(coctel)
    }

    private fun econtrarCoctelEnLaLista(cocteles: List<Coctel>, idBuscado: String?): Coctel? {
        for (i in 0 until cocteles.size) {
            val item = cocteles[i]
            if (item.id == idBuscado) return item
        }
        return null
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


    private fun setupListeners(coctel: Coctel?) {
        binding.btnVolver.setOnClickListener {
            finish()
        }
        binding.btnVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, coctel?.url_video_tutorial?.toUri())
            startActivity(intent)
        }

        binding.btnNavInicio.setOnClickListener {
            val intent = Intent(this, PantallaInicialActivity::class.java)
            startActivity(intent)
        }
        binding.btnNavBuscar.setOnClickListener {
            val intent = Intent(this, BuscadorActivity::class.java)
            startActivity(intent)
        }
        binding.btnNavCalculadora.setOnClickListener {
            val intent = Intent(this, CalculadoraActivity::class.java)
            startActivity(intent)
        }
        binding.btnNavPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }
    }

    fun TextView.setTitulo(coctel: Coctel?) {
        text = coctel?.nombre
    }

    fun TextView.tvNivelDificultad(coctel: Coctel?) {
        text = coctel?.dificultad
    }

    fun TextView.setSabor(coctel: Coctel?) {
        text = coctel?.sabor
    }

    fun TextView.setNivelAlcohol(coctel: Coctel?) {
        text = coctel?.nivel_alcohol
    }


    fun mostrarCoctel(coctel: Coctel?) {
        binding.txtTitulo.setTitulo(coctel)
        binding.tvNivelDificultad.tvNivelDificultad(coctel)
        binding.tvSabor.setSabor(coctel)
        binding.tvNivelAlcohol.setNivelAlcohol(coctel)
        ingredientesAdapter.addDataCards(coctel?.ingredientes ?: emptyList())
        pasosAdapter.addDataCards(coctel?.pasos ?: emptyList())
    }

    fun cargarTodosLosCocteles(): List<Coctel> {
        val txt = assets.open("cocteles.json").bufferedReader().use { it.readText() }
        val db = Json.decodeFromString<CoctelesDatabase>(txt)
        return db.cocteles
    }
}