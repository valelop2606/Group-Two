package com.example.grouptwo

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
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.grouptwo.dataclases.Coctel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class BuscadorActivity : AppCompatActivity() {

    private val ingredientesSeleccionados = mutableListOf<String>()
    private var todosLosCocteles = listOf<Coctel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscador)

        todosLosCocteles = cargarCoctelesDesdeJSON()

        configurarVistas()
        configurarChips()
    }

    private fun configurarVistas() {
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val etBuscar = findViewById<EditText>(R.id.etBuscarIngrediente)
        val btnVerPreparar = findViewById<Button>(R.id.btnVerPreparar)

        btnBack.setOnClickListener {
            finish()
        }

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filtrarChips(s.toString())
            }
        })

        btnVerPreparar.setOnClickListener {
            mostrarCoctelesDisponibles()
        }

        actualizarContadorBoton()
    }

    private fun configurarChips() {
        val chipGroupAlcoholes = findViewById<ChipGroup>(R.id.chipGroupAlcoholes)
        val chipGroupFrutas = findViewById<ChipGroup>(R.id.chipGroupFrutas)
        val chipGroupOtros = findViewById<ChipGroup>(R.id.chipGroupOtros)

        configurarChipGroup(chipGroupAlcoholes)
        configurarChipGroup(chipGroupFrutas)
        configurarChipGroup(chipGroupOtros)
    }

    private fun configurarChipGroup(chipGroup: ChipGroup) {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? Chip
            chip?.setOnCheckedChangeListener { buttonView, isChecked ->
                val nombreIngrediente = buttonView.text.toString()

                if (isChecked) {
                    if (!ingredientesSeleccionados.contains(nombreIngrediente)) {
                        ingredientesSeleccionados.add(nombreIngrediente)
                        agregarChipSeleccionado(nombreIngrediente)
                    }
                } else {
                    ingredientesSeleccionados.remove(nombreIngrediente)
                    quitarChipSeleccionado(nombreIngrediente)
                }

                actualizarContadorBoton()
            }
        }
    }

    private fun agregarChipSeleccionado(nombre: String) {
        val chipGroupSeleccionados = findViewById<ChipGroup>(R.id.chipGroupSeleccionados)

        val chip = Chip(this)
        chip.text = nombre
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            ingredientesSeleccionados.remove(nombre)
            chipGroupSeleccionados.removeView(chip)
            desmarcarChipOriginal(nombre)
            actualizarContadorBoton()
        }

        chipGroupSeleccionados.addView(chip)
    }

    private fun quitarChipSeleccionado(nombre: String) {
        val chipGroupSeleccionados = findViewById<ChipGroup>(R.id.chipGroupSeleccionados)

        for (i in 0 until chipGroupSeleccionados.childCount) {
            val chip = chipGroupSeleccionados.getChildAt(i) as? Chip
            if (chip?.text.toString() == nombre) {
                chipGroupSeleccionados.removeView(chip)
                break
            }
        }
    }

    private fun desmarcarChipOriginal(nombre: String) {
        val chipGroupAlcoholes = findViewById<ChipGroup>(R.id.chipGroupAlcoholes)
        val chipGroupFrutas = findViewById<ChipGroup>(R.id.chipGroupFrutas)
        val chipGroupOtros = findViewById<ChipGroup>(R.id.chipGroupOtros)

        desmarcarEnGrupo(chipGroupAlcoholes, nombre)
        desmarcarEnGrupo(chipGroupFrutas, nombre)
        desmarcarEnGrupo(chipGroupOtros, nombre)
    }

    private fun desmarcarEnGrupo(chipGroup: ChipGroup, nombre: String) {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? Chip
            if (chip?.text.toString() == nombre) {
                chip?.isChecked = false
                break
            }
        }
    }

    private fun actualizarContadorBoton() {
        val btnVerPreparar = findViewById<Button>(R.id.btnVerPreparar)
        val cantidad = ingredientesSeleccionados.size
        btnVerPreparar.text = "Ver qué puedo preparar ($cantidad)"
    }

    private fun filtrarChips(busqueda: String) {
        val textoBusqueda = busqueda.lowercase()

        val chipGroupAlcoholes = findViewById<ChipGroup>(R.id.chipGroupAlcoholes)
        val chipGroupFrutas = findViewById<ChipGroup>(R.id.chipGroupFrutas)
        val chipGroupOtros = findViewById<ChipGroup>(R.id.chipGroupOtros)

        filtrarGrupo(chipGroupAlcoholes, textoBusqueda)
        filtrarGrupo(chipGroupFrutas, textoBusqueda)
        filtrarGrupo(chipGroupOtros, textoBusqueda)
    }

    private fun filtrarGrupo(chipGroup: ChipGroup, busqueda: String) {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? Chip
            if (chip != null) {
                if (busqueda.isEmpty()) {
                    chip.visibility = View.VISIBLE
                } else {
                    val coincide = chip.text.toString().lowercase().contains(busqueda)
                    chip.visibility = if (coincide) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun mostrarCoctelesDisponibles() {
        val containerResultados = findViewById<LinearLayout>(R.id.containerResultados)
        val tvResultadosTitle = findViewById<TextView>(R.id.tvResultadosTitle)

        containerResultados.removeAllViews()

        if (ingredientesSeleccionados.isEmpty()) {
            tvResultadosTitle.visibility = View.GONE
            containerResultados.visibility = View.GONE
            return
        }

        val coctelesDisponibles = mutableListOf<Coctel>()

        for (coctel in todosLosCocteles) {
            var coincidencias = 0

            for (ingrediente in coctel.ingredientes) {
                val nombreIngrediente = ingrediente.nombre.lowercase()

                for (seleccionado in ingredientesSeleccionados) {
                    if (nombreIngrediente.contains(seleccionado.lowercase()) ||
                        seleccionado.lowercase().contains(nombreIngrediente)) {
                        coincidencias = coincidencias + 1
                        break
                    }
                }
            }

            if (coincidencias > 0) {
                coctelesDisponibles.add(coctel)
            }
        }

        if (coctelesDisponibles.isEmpty()) {
            tvResultadosTitle.visibility = View.GONE
            containerResultados.visibility = View.GONE
        } else {
            tvResultadosTitle.visibility = View.VISIBLE
            containerResultados.visibility = View.VISIBLE

            for (coctel in coctelesDisponibles) {
                agregarCoctelView(coctel, containerResultados)
            }
        }
    }

    private fun agregarCoctelView(coctel: Coctel, container: LinearLayout) {
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
            // Aquí puedes abrir la pantalla de detalle del coctel
        }

        container.addView(coctelView)
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