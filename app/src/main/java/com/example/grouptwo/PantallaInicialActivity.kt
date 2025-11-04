package com.example.grouptwo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grouptwo.VerRecetaDetalladaActivity.Companion.EXTRA_COCKTAIL_ID
import com.example.grouptwo.adapters.IngredientesAdapter
import com.example.grouptwo.databinding.ActivityPantallaInicialBinding
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

        val cocteles = cargarTodosLosCocteles()
        val recomendados: List<Coctel> = cocteles.shuffled().take(4)

        val primerCoctel  = recomendados[0]
        val segundoCoctel = recomendados[1]
        val tercerCoctel  = recomendados[2]
        val cuartoCoctel  = recomendados[3]


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        mostrarPrimerCoctel(primerCoctel)
        mostrarSegundoCoctel(segundoCoctel)
        mostrarTercerCoctel(tercerCoctel)
        mostrarCuartoCoctel(cuartoCoctel)

        binding.btnLoTengoEnCasa.setOnClickListener {
            val intent = Intent(this, BuscadorActivity::class.java)
            startActivity(intent)
        }
        binding.btnCategorias.setOnClickListener {
            val intent = Intent(this, CategoriasActivity::class.java)
            startActivity(intent)
        }

        binding.btnPrimerCoctel.setOnClickListener {
            val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
            intent.putExtra(EXTRA_COCKTAIL_ID, primerCoctel.id)
            startActivity(intent)
        }
        binding.btnSegundoCoctel.setOnClickListener {
            val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
            intent.putExtra(EXTRA_COCKTAIL_ID, segundoCoctel.id)
            startActivity(intent)
        }
        binding.btnTercerCoctel.setOnClickListener {
            val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
            intent.putExtra(EXTRA_COCKTAIL_ID, tercerCoctel.id)
            startActivity(intent)
        }
        binding.btnCuartoCoctel.setOnClickListener {
            val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
            intent.putExtra(EXTRA_COCKTAIL_ID, cuartoCoctel.id)
            startActivity(intent)
        }

        binding.perfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
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
        binding.btnElegirAlAzar.setOnClickListener {
            elegirCoctelAlAzarYAbrirReceta(cocteles)
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

    // Función para cargar todos los cocteles desde el JSON
    fun cargarTodosLosCocteles(): List<Coctel> {
        val txt = assets.open("cocteles.json").bufferedReader().use { it.readText() }
        val db = Json.decodeFromString<CoctelesDatabase>(txt)
        return db.cocteles
    }

    //Funciones para setear informacion de cocteles
    fun TextView.setNombre(coctel: Coctel) {
        text = coctel.nombre
    }
    fun TextView.setDificultad(coctel: Coctel) {
        text = coctel.dificultad
    }
    fun TextView.setSabor(coctel: Coctel) {
        text = coctel.sabor
    }
    fun ImageView.setImagen(coctel: Coctel) {
        val idImagen = context.resources.getIdentifier(coctel.imagen, "drawable", context.packageName)
        if (idImagen != 0) setImageResource(idImagen)
    }

    fun mostrarPrimerCoctel(coctel: Coctel) {
        binding.btnPrimerCoctel.setImagen(coctel)
        binding.txtTituloPrimerCoctel.setNombre(coctel)
        binding.txtDificultadPrimerCoctel.setDificultad(coctel)
        binding.textSaborPrimerCoctel.setSabor(coctel)
    }
    fun mostrarSegundoCoctel(coctel: Coctel) {
        binding.btnSegundoCoctel.setImagen(coctel)
        binding.textTituloSegundoCoctel.setNombre(coctel)
        binding.txtDificultadSegundoCoctel.setDificultad(coctel)
        binding.textSaborSegundoCoctel.setSabor(coctel)
    }
    fun mostrarTercerCoctel(coctel: Coctel) {
        binding.btnTercerCoctel.setImagen(coctel)
        binding.txtTituloTercerCoctel.setNombre(coctel)
        binding.txtDificultadTercerCoctel.setDificultad(coctel)
        binding.textSaborTercerCoctel.setSabor(coctel)
    }
    fun mostrarCuartoCoctel(coctel: Coctel) {
        binding.btnCuartoCoctel.setImagen(coctel)
        binding.txtTituloCuartoCoctel.setNombre(coctel)
        binding.txtDificultadCuartoCoctel.setDificultad(coctel)
        binding.textSaborCuartoCoctel.setSabor(coctel)
    }
    private fun elegirCoctelAlAzarYAbrirReceta(cocteles: List<Coctel>) {

        val coctelElegido = cocteles.random()

        val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
        intent.putExtra(EXTRA_COCKTAIL_ID, coctelElegido.id)
        startActivity(intent)
    }
}