package com.example.grouptwo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grouptwo.adapters.IngredientesAdapter
import com.example.grouptwo.databinding.ActivityPantallaInicialBinding
import com.example.grouptwo.databinding.PantallaDeBuscarBinding
import com.example.grouptwo.dataclases.Coctel
import com.example.grouptwo.dataclases.CoctelesDatabase
import kotlinx.serialization.json.Json

class PantallaInicialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPantallaInicialBinding
    val context: Context = this
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPantallaInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = this.getSharedPreferences("GroupTwo", MODE_PRIVATE)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val cocteles = cargarTodosLosCocteles()

        binding.perfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        binding.botonCategorias.setOnClickListener {
            val intent = Intent(this, CategoriasActivity::class.java)
            startActivity(intent)
        }


        binding.btnMojito.setOnClickListener {
            val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
            intent.putExtra(VerRecetaDetalladaActivity.EXTRA_COCKTAIL_ID, "ckt_mojito")
            startActivity(intent)
        }

        binding.btnCosmopolitan.setOnClickListener {
            val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
            intent.putExtra(VerRecetaDetalladaActivity.EXTRA_COCKTAIL_ID, "ckt_cosmopolitan")
            startActivity(intent)
        }
        binding.btnMargarita.setOnClickListener {
            val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
            intent.putExtra(VerRecetaDetalladaActivity.EXTRA_COCKTAIL_ID, "ckt_margarita")
            startActivity(intent)
        }
        binding.btnNegroni.setOnClickListener {
            val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
            intent.putExtra(VerRecetaDetalladaActivity.EXTRA_COCKTAIL_ID, "ckt_negroni")
            startActivity(intent)
        }


        binding.btnBuscarIcon.setOnClickListener {
            val intent = Intent(this, BuscadorActivity::class.java)
            startActivity(intent)
        }
        binding.btnCalculadora.setOnClickListener {
            val intent = Intent(this, CalculadoraActivity::class.java)
            startActivity(intent)
        }


    }


    private fun guardarDataClass(proyecto: Coctel) {
        val asdfgh: String = Json.encodeToString(proyecto)
        val editor = sharedPreferences.edit()
        editor.putString("cocteles", asdfgh)
        editor.apply()
    }

    private fun obtenerDataClass(): Coctel? {
        val datoGuardado: String = sharedPreferences.getString("cocteles", null) ?: ""
        if (!datoGuardado.isEmpty()) {
            val objetoGuardado = Json.decodeFromString<Coctel>(datoGuardado)
            return objetoGuardado
        }
        return null
    }

    private fun obtenerDataDeFile(): Coctel? {
        val fileString: String =
            applicationContext.assets.open("cocteles.json").bufferedReader().use { it.readText() }
        val objetoGuardado = Json.decodeFromString<Coctel>(fileString)
        return objetoGuardado
    }
    private fun cargarTodosLosCocteles(): List<Coctel> {
        val txt = assets.open("cocteles.json").bufferedReader().use { it.readText() }
        val json = Json
        val db = json.decodeFromString<CoctelesDatabase>(txt)
        return db.cocteles
    }

}