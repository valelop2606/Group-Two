package com.example.grouptwo

import android.os.Bundle
import android.widget.ImageButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import androidx.appcompat.app.AppCompatActivity

class PantallaDeBuscarActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var chipGroupAlcoholes: ChipGroup
    private lateinit var chipGroupFrutas: ChipGroup
    private lateinit var chipGroupOtros: ChipGroup
    private lateinit var chipGroupSeleccionados: ChipGroup
    private val seleccionados = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_de_buscar)

        btnBack = findViewById(R.id.btnBack)
        chipGroupAlcoholes = findViewById(R.id.chipGroupAlcoholes)
        chipGroupFrutas = findViewById(R.id.chipGroupFrutas)
        chipGroupOtros = findViewById(R.id.chipGroupOtros)
        chipGroupSeleccionados = findViewById(R.id.chipGroupSeleccionados)

        // Configurar botón de regreso
        btnBack.setOnClickListener {
            finish() // Cierra esta actividad y regresa a la anterior
        }

        // Configurar listeners para todos los chips de alcoholes
        configurarChipsGrupo(chipGroupAlcoholes)
        // Configurar listeners para todos los chips de frutas
        configurarChipsGrupo(chipGroupFrutas)
        // Configurar listeners para todos los chips de otros
        configurarChipsGrupo(chipGroupOtros)
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
    }

    private fun actualizarChipsSeleccionados() {
        chipGroupSeleccionados.removeAllViews()
        for (nombre in seleccionados) {
            val chip = Chip(this).apply {
                text = nombre
                isCloseIconVisible = true
                setTextColor(resources.getColor(android.R.color.white))
                setChipBackgroundColorResource(com.google.android.material.R.color.material_dynamic_primary80)
                setOnCloseIconClickListener {
                    seleccionados.remove(nombre)
                    actualizarChipsSeleccionados()
                    actualizarChipEnGrupo(nombre, false, chipGroupAlcoholes)
                    actualizarChipEnGrupo(nombre, false, chipGroupFrutas)
                    actualizarChipEnGrupo(nombre, false, chipGroupOtros)
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
}