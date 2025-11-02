package com.example.grouptwo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grouptwo.dataclases.Coctel
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class PantallaInicialActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var todosLosCocteles = listOf<Coctel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_inicial)

        sharedPreferences = this.getSharedPreferences("GroupTwo", MODE_PRIVATE)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        todosLosCocteles = cargarCoctelesDesdeJSON()

        setupListeners()
        configurarBuscador()
    }

    private fun setupListeners() {

        val btnBuscarIcon = findViewById<ImageButton>(R.id.btnBuscarIcon)
        btnBuscarIcon.setOnClickListener {
            iniciarActivity(BuscadorActivity::class.java)
        }

        val btnCalculadora = findViewById<ImageButton>(R.id.btnCalculadora)
        btnCalculadora.setOnClickListener {
            iniciarActivity(CalculadoraActivity::class.java)
        }

        val btnPerfil = findViewById<ImageButton>(R.id.perfil)
        btnPerfil.setOnClickListener {
            iniciarActivity(PerfilActivity::class.java)
        }

        val btnTengoEnCasa = findViewById<Button>(R.id.btnTengoEnCasa)
        btnTengoEnCasa.setOnClickListener {
            iniciarActivity(BuscadorActivity::class.java)
        }

        val botonCategorias = findViewById<Button>(R.id.boton_categorias)
        botonCategorias.setOnClickListener {
            iniciarActivity(CategoriasActivity::class.java)
        }

        val btnMojito = findViewById<ImageButton>(R.id.btnMojito)
        btnMojito.setOnClickListener {
            abrirDetalleCoctel("ckt_mojito")
        }

        val btnCosmopolitan = findViewById<ImageButton>(R.id.btnCosmopolitan)
        btnCosmopolitan.setOnClickListener {
            abrirDetalleCoctel("ckt_cosmopolitan")
        }

        val btnMargarita = findViewById<ImageButton>(R.id.btnMargarita)
        btnMargarita.setOnClickListener {
            abrirDetalleCoctel("ckt_margarita")
        }

        val btnNegroni = findViewById<ImageButton>(R.id.btnNegroni)
        btnNegroni.setOnClickListener {
            abrirDetalleCoctel("ckt_negroni")
        }
    }

    private fun configurarBuscador() {
        val etBuscar = findViewById<EditText>(R.id.etBuscarIngrediente)
        val containerResultados = findViewById<LinearLayout>(R.id.containerResultados)
        val tvResultadosTitle = findViewById<TextView>(R.id.tvResultadosTitle)
        val contenidoEstatico = findViewById<LinearLayout>(R.id.contenidoEstatico)

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val busqueda = s.toString()

                if (busqueda.isEmpty()) {
                    tvResultadosTitle.visibility = View.GONE
                    containerResultados.visibility = View.GONE
                    contenidoEstatico.visibility = View.VISIBLE
                } else {
                    buscarCocteles(busqueda)
                }
            }
        })
    }

    private fun buscarCocteles(busqueda: String) {
        val containerResultados = findViewById<LinearLayout>(R.id.containerResultados)
        val tvResultadosTitle = findViewById<TextView>(R.id.tvResultadosTitle)
        val contenidoEstatico = findViewById<LinearLayout>(R.id.contenidoEstatico)

        containerResultados.removeAllViews()

        val textoBusqueda = busqueda.lowercase()
        val resultados = mutableListOf<Coctel>()

        for (coctel in todosLosCocteles) {
            var encontrado = false

            if (coctel.nombre.lowercase().contains(textoBusqueda)) {
                encontrado = true
            }

            if (!encontrado) {
                for (ingrediente in coctel.ingredientes) {
                    if (ingrediente.nombre.lowercase().contains(textoBusqueda)) {
                        encontrado = true
                        break
                    }
                }
            }

            if (!encontrado) {
                if (coctel.dificultad.lowercase().contains(textoBusqueda) ||
                    coctel.nivel_alcohol.lowercase().contains(textoBusqueda) ||
                    coctel.sabor.lowercase().contains(textoBusqueda)) {
                    encontrado = true
                }
            }

            if (encontrado) {
                resultados.add(coctel)
            }
        }

        if (resultados.isEmpty()) {
            tvResultadosTitle.visibility = View.GONE
            containerResultados.visibility = View.GONE
            contenidoEstatico.visibility = View.VISIBLE
        } else {
            tvResultadosTitle.visibility = View.VISIBLE
            containerResultados.visibility = View.VISIBLE
            contenidoEstatico.visibility = View.GONE

            for (coctel in resultados) {
                agregarCoctelResultado(coctel, containerResultados)
            }
        }
    }

    private fun agregarCoctelResultado(coctel: Coctel, container: LinearLayout) {
        val coctelView = LayoutInflater.from(this).inflate(
            R.layout.item_coctel_calculadora,
            container,
            false
        )

        val tvNombreCoctel = coctelView.findViewById<TextView>(R.id.tvNombreCoctel)
        val tvBebidasCoctel = coctelView.findViewById<TextView>(R.id.tvBebidasCoctel)

        tvNombreCoctel.text = coctel.nombre
        tvBebidasCoctel.text = coctel.dificultad + " - " + coctel.nivel_alcohol

        coctelView.setOnClickListener {
            abrirDetalleCoctel(coctel.id)
        }

        container.addView(coctelView)
    }

    private fun abrirDetalleCoctel(coctelId: String) {
        // Buscar el coctel en la lista
        var coctelEncontrado: Coctel? = null

        for (coctel in todosLosCocteles) {
            if (coctel.id == coctelId) {
                coctelEncontrado = coctel
                break
            }
        }

    }

    private fun iniciarActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    private fun cargarCoctelesDesdeJSON(): List<Coctel> {
        try {
            val inputStream = assets.open("cocteles.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val jsonString = String(buffer, Charsets.UTF_8)

            val json = Json { ignoreUnknownKeys = true }
            val database = json.decodeFromString<com.example.grouptwo.dataclases.CoctelesDatabase>(jsonString)

            return database.cocteles
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}