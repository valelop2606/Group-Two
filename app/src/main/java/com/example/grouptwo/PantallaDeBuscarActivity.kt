package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.example.grouptwo.repository.CoctelRepository
import com.example.grouptwo.models.CoctelDetalle // ✅ IMPORTAR CoctelDetalle
import com.example.grouptwo.R // ✅ Asegurar que R esté importado
import kotlinx.coroutines.launch

class PantallaDeBuscarActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var etBuscarIngrediente: EditText
    private lateinit var chipGroupAlcoholes: ChipGroup
    private lateinit var chipGroupFrutas: ChipGroup
    private lateinit var chipGroupOtros: ChipGroup
    private lateinit var chipGroupSeleccionados: ChipGroup
    private lateinit var btnVerPreparar: Button
    private lateinit var containerResultados: LinearLayout
    private lateinit var tvResultadosTitle: TextView

    private val seleccionados = mutableSetOf<String>()
    // ✅ CORRECCIÓN 1: Inicialización con Context usando by lazy
    private val repository by lazy { CoctelRepository(this) }
    // ✅ CORRECCIÓN 2: Tipo debe ser CoctelDetalle
    private var todosLosCocteles = listOf<CoctelDetalle>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_de_buscar)

        inicializarVistas()
        cargarCoctelesDesdeAPI()
        configurarListeners()
    }

    private fun inicializarVistas() {
        btnBack = findViewById(R.id.btnBack)
        etBuscarIngrediente = findViewById(R.id.etBuscarIngrediente)
        chipGroupAlcoholes = findViewById(R.id.chipGroupAlcoholes)
        chipGroupFrutas = findViewById(R.id.chipGroupFrutas)
        chipGroupOtros = findViewById(R.id.chipGroupOtros)
        chipGroupSeleccionados = findViewById(R.id.chipGroupSeleccionados)
        btnVerPreparar = findViewById(R.id.btnVerPreparar)
        containerResultados = findViewById(R.id.containerResultados)
        tvResultadosTitle = findViewById(R.id.tvResultadosTitle)
    }

    private fun cargarCoctelesDesdeAPI() {
        lifecycleScope.launch {
            repository.obtenerTodosCocteles()
                .onSuccess { cocteles ->
                    // ✅ Acepta List<CoctelDetalle>
                    todosLosCocteles = cocteles
                    Log.d("PantallaBuscar", "✅ Cargados ${cocteles.size} cócteles")
                }
                .onFailure { error ->
                    Log.e("PantallaBuscar", "❌ Error: ${error.message}")
                    Toast.makeText(
                        this@PantallaDeBuscarActivity,
                        "Error al cargar cócteles",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun configurarListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        // Configurar chips de ingredientes
        configurarChipsGrupo(chipGroupAlcoholes)
        configurarChipsGrupo(chipGroupFrutas)
        configurarChipsGrupo(chipGroupOtros)

        // Búsqueda en tiempo real
        etBuscarIngrediente.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.length >= 2) {
                    buscarCoctelesPorNombre(query)
                } else {
                    ocultarResultados()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Botón ver qué puedo preparar
        btnVerPreparar.setOnClickListener {
            if (seleccionados.isEmpty()) {
                Toast.makeText(this, "Selecciona al menos un ingrediente", Toast.LENGTH_SHORT).show()
            } else {
                buscarCoctelesPorIngredientes()
            }
        }
    }

    private fun configurarChipsGrupo(chipGroup: ChipGroup) {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            chip.setOnClickListener {
                toggleSeleccion(chip.text.toString(), chip)
            }
        }
    }

    private fun toggleSeleccion(nombre: String, chip: Chip) {
        if (seleccionados.contains(nombre)) {
            seleccionados.remove(nombre)
            chip.isChecked = false
        } else {
            seleccionados.add(nombre)
            chip.isChecked = true
        }
        actualizarChipsSeleccionados()
        actualizarBotonPreparar()
    }

    private fun actualizarChipsSeleccionados() {
        chipGroupSeleccionados.removeAllViews()
        for (nombre in seleccionados) {
            val chip = Chip(this).apply {
                text = nombre
                isCloseIconVisible = true
                setTextColor(resources.getColor(android.R.color.white, null))
                setChipBackgroundColorResource(com.google.android.material.R.color.material_dynamic_primary80)
                setOnCloseIconClickListener {
                    seleccionados.remove(nombre)
                    actualizarChipsSeleccionados()
                    actualizarChipEnGrupo(nombre, false, chipGroupAlcoholes)
                    actualizarChipEnGrupo(nombre, false, chipGroupFrutas)
                    actualizarChipEnGrupo(nombre, false, chipGroupOtros)
                    actualizarBotonPreparar()
                }
            }
            chipGroupSeleccionados.addView(chip)
        }
    }

    private fun actualizarChipEnGrupo(nombre: String, checked: Boolean, chipGroup: ChipGroup) {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.text == nombre) {
                chip.isChecked = checked
                break
            }
        }
    }

    private fun actualizarBotonPreparar() {
        val cantidad = seleccionados.size
        btnVerPreparar.text = "Ver qué puedo preparar ($cantidad)"
        btnVerPreparar.isEnabled = cantidad > 0
    }

    private fun buscarCoctelesPorNombre(query: String) {
        lifecycleScope.launch {
            // El repositorio retorna CoctelDetalle, que es lo que queremos
            repository.buscarCocteles(query)
                .onSuccess { resultados ->
                    if (resultados.isNotEmpty()) {
                        mostrarResultados(resultados, "Resultados: \"$query\"")
                    } else {
                        ocultarResultados()
                        Toast.makeText(
                            this@PantallaDeBuscarActivity,
                            "No se encontraron resultados",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .onFailure { error ->
                    Log.e("PantallaBuscar", "Error en búsqueda: ${error.message}")
                    ocultarResultados()
                }
        }
    }

    private fun buscarCoctelesPorIngredientes() {
        if (todosLosCocteles.isEmpty()) {
            Toast.makeText(this, "Cargando cócteles...", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ MEJORA: Filtrar cócteles que contengan AL MENOS UNO de los ingredientes seleccionados
        val resultados = todosLosCocteles.filter { coctel ->
            coctel.ingredientes?.any { ingredienteCoctel ->
                // Verificar si el nombre del ingrediente seleccionado está contenido
                seleccionados.any { ingredienteSeleccionado ->
                    ingredienteCoctel.nombre.contains(ingredienteSeleccionado, ignoreCase = true)
                }
            } ?: false
        }

        // Lógica de filtrado original (para fallback, por si los nombres de chip no son exactos)
        /*
        val resultados = todosLosCocteles.filter { coctel ->
            val nombreLower = coctel.nombre.lowercase()
            val descripcionLower = coctel.descripcion?.lowercase() ?: ""

            seleccionados.any { ingrediente ->
                nombreLower.contains(ingrediente.lowercase()) ||
                        descripcionLower.contains(ingrediente.lowercase())
            }
        }
        */

        if (resultados.isEmpty()) {
            Toast.makeText(
                this,
                "No se encontraron cócteles con esos ingredientes",
                Toast.LENGTH_LONG
            ).show()
            ocultarResultados()
        } else {
            val ingredientesTexto = seleccionados.joinToString(", ")
            mostrarResultados(resultados, "Cócteles con: $ingredientesTexto")
            Toast.makeText(
                this,
                "✅ Se encontraron ${resultados.size} cócteles",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // ✅ CORRECCIÓN 3: Tipo debe ser CoctelDetalle
    private fun mostrarResultados(cocteles: List<CoctelDetalle>, titulo: String) {
        tvResultadosTitle.text = titulo
        tvResultadosTitle.visibility = View.VISIBLE
        containerResultados.visibility = View.VISIBLE
        containerResultados.removeAllViews()

        for (coctel in cocteles) {
            agregarCoctelCard(coctel)
        }
    }

    private fun ocultarResultados() {
        tvResultadosTitle.visibility = View.GONE
        containerResultados.visibility = View.GONE
        containerResultados.removeAllViews()
    }

    // ✅ CORRECCIÓN 3: Tipo debe ser CoctelDetalle
    private fun agregarCoctelCard(coctel: CoctelDetalle) {
        val cardView = CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 24)
            }
            radius = 32f
            cardElevation = 0f
            setCardBackgroundColor(resources.getColor(R.color.card_background, null))
        }

        val linearLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        // Nombre del cóctel
        val tvNombre = TextView(this).apply {
            text = coctel.nombre
            textSize = 18f
            setTextColor(resources.getColor(android.R.color.white, null))
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        linearLayout.addView(tvNombre)

        // Descripción
        if (!coctel.descripcion.isNullOrEmpty()) {
            val tvDescripcion = TextView(this).apply {
                text = coctel.descripcion
                textSize = 14f
                setTextColor(resources.getColor(R.color.text_secondary, null))
                setPadding(0, 8, 0, 0)
            }
            linearLayout.addView(tvDescripcion)
        }

        // Info adicional
        val infoLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 16, 0, 0)
        }

        // Nivel de dificultad
        coctel.nivelDificultad?.let {
            val tvDificultad = TextView(this).apply {
                text = "📊 $it"
                textSize = 12f
                setTextColor(resources.getColor(android.R.color.white, null))
                setPadding(0, 0, 24, 0)
            }
            infoLayout.addView(tvDificultad)
        }

        // Nivel de alcohol
        coctel.nivelAlcohol?.let {
            val tvAlcohol = TextView(this).apply {
                text = "🍸 $it"
                textSize = 12f
                setTextColor(resources.getColor(android.R.color.white, null))
                setPadding(0, 0, 24, 0)
            }
            infoLayout.addView(tvAlcohol)
        }

        // Sabor
        coctel.saborPredominante?.let {
            val tvSabor = TextView(this).apply {
                text = "$it"
                textSize = 12f
                setTextColor(resources.getColor(android.R.color.white, null))
            }
            infoLayout.addView(tvSabor)
        }

        linearLayout.addView(infoLayout)
        cardView.addView(linearLayout)

        // Click para ver detalles
        cardView.setOnClickListener {
            Toast.makeText(
                this,
                "Ver detalles de ${coctel.nombre} (ID: ${coctel.id})",
                Toast.LENGTH_SHORT
            ).show()
            // ✅ NAVEGACIÓN COMPLETA A LA PANTALLA DE DETALLE
            VerRecetaDetalladaActivity.launch(this, coctel.id.toString())
        }

        containerResultados.addView(cardView)
    }
}