package com.example.grouptwo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.grouptwo.data.GuardarMiReceta
import com.example.grouptwo.dataclases.Categoria
import com.example.grouptwo.dataclases.Coctel
import com.example.grouptwo.dataclases.Ingrediente
import com.example.grouptwo.dataclases.Paso
import com.example.grouptwo.databinding.DialogAgregarIngredienteBinding
import com.example.grouptwo.databinding.DialogAgregarPasoBinding
import com.example.grouptwo.databinding.ItemIngredienteBinding
import com.example.grouptwo.databinding.ItemIngredienteNuevoBinding
import com.example.grouptwo.databinding.ItemPasoBinding
import com.example.grouptwo.databinding.ItemPasoNuevoBinding
import com.example.grouptwo.databinding.PantallaCrearRecetaBinding
import com.google.android.material.chip.Chip

class PantallaDeCrearReceta : AppCompatActivity() {

    private lateinit var binding: PantallaCrearRecetaBinding

    // Estado temporal de la UI
    private val ingredientes = mutableListOf<Pair<String, String>>() // (nombre, cantidad)
    private val pasos = mutableListOf<String>()                      // texto del paso

    // Si quieres TODAS las categorías, las inyectamos sin tocar tu XML:
    private val extraCategorias = listOf(
        "tropical",
        "refrescante",
        "dulce",
        "acido_citrico",
        "amargo",
        "espumoso_burbujeante",
        "cafe_y_desayunos",
        "aperitivo"
        // ya tienes en el XML: clasico, creativo, sin_alcohol, tematicos
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PantallaCrearRecetaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        agregarChipsExtra()     // añade chips faltantes manteniendo singleSelection
        configurarListeners()
    }

    private fun configurarListeners() = with(binding) {
        btnBack.setOnClickListener { finish() }
        btnGuardar.setOnClickListener { guardarCoctel() }
        btnAgregarIngrediente.setOnClickListener { mostrarDialogoAgregarIngrediente() }
        btnAgregarPaso.setOnClickListener { mostrarDialogoAgregarPaso() }
    }

    /** Agrega chips adicionales (opcional) respetando singleSelection del ChipGroup */
    private fun agregarChipsExtra() {
        extraCategorias.forEach { slug ->
            val chip = Chip(this).apply {
                text = slug.replace("_", " ")
                isCheckable = true
            }
            binding.chipGroupCategoria.addView(chip)
        }
    }

    private fun mostrarDialogoAgregarIngrediente() {
        val dialogBinding = DialogAgregarIngredienteBinding.inflate(layoutInflater)

        AlertDialog.Builder(this)
            .setTitle("Agregar Ingrediente")
            .setView(dialogBinding.root)
            .setPositiveButton("Agregar") { d, _ ->
                val ing = dialogBinding.etIngredienteDialog.text.toString().trim()
                val cant = dialogBinding.etCantidadDialog.text.toString().trim()
                if (ing.isNotEmpty() && cant.isNotEmpty()) {
                    ingredientes.add(ing to cant)
                    actualizarListaIngredientes()
                } else {
                    Toast.makeText(this, "Completa ambos campos", Toast.LENGTH_SHORT).show()
                }
                d.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoAgregarPaso() {
        val dialogBinding = DialogAgregarPasoBinding.inflate(layoutInflater)

        AlertDialog.Builder(this)
            .setTitle("Paso ${pasos.size + 1}")
            .setView(dialogBinding.root)
            .setPositiveButton("Agregar") { d, _ ->
                val paso = dialogBinding.etPasoDialog.text.toString().trim()
                if (paso.isNotEmpty()) {
                    pasos.add(paso)
                    actualizarListaPasos()
                } else {
                    Toast.makeText(this, "Describe el paso", Toast.LENGTH_SHORT).show()
                }
                d.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

        private fun actualizarListaIngredientes()  {
            binding.containerIngredientes.removeAllViews()
            ingredientes.forEachIndexed { index, (nombre, cantidad) ->
                val itemBinding = ItemIngredienteNuevoBinding.inflate(layoutInflater, binding.containerIngredientes, false)
                itemBinding.txtNombre.text = nombre
                itemBinding.txtCantidad.text = cantidad
                itemBinding.btnEliminar.setOnClickListener {
                    ingredientes.removeAt(index)
                    actualizarListaIngredientes()
                }
                binding.containerIngredientes.addView(itemBinding.root)
            }
        }

        private fun actualizarListaPasos()  {
            binding.containerPasos.removeAllViews()
            pasos.forEachIndexed { index, texto ->
                val itemBinding = ItemPasoNuevoBinding.inflate(layoutInflater, binding.containerPasos, false)
                itemBinding.txtIndice.text = "${index + 1}"
                itemBinding.txtDescripcion.text = texto
                // Si quieres eliminar pasos, puedes hacerlo tocando el card completo:
                itemBinding.root.setOnLongClickListener {
                    pasos.removeAt(index)
                    actualizarListaPasos()
                    true
                }
                binding.containerPasos.addView(itemBinding.root)
            }
        }


    /** Lee el chip seleccionado y lo convierte al slug que guardamos en JSON */
    private fun categoriaSeleccionada(): String? {
        val id = binding.chipGroupCategoria.checkedChipId
        if (id == -1) return null
        val chip = binding.chipGroupCategoria.findViewById<Chip>(id)
        return chip.text.toString().trim().lowercase().replace(" ", "_")
    }

    private fun guardarCoctel() = with(binding) {
        val nombre = etNombre.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val perfilSabor = etPerfilSabor.text.toString().trim() // lo usamos como "sabor"

        if (nombre.isEmpty()) {
            Toast.makeText(this@PantallaDeCrearReceta, "Nombre obligatorio", Toast.LENGTH_SHORT).show()
            return@with
        }
        val cat = categoriaSeleccionada()
        if (cat == null) {
            Toast.makeText(this@PantallaDeCrearReceta, "Selecciona una categoría", Toast.LENGTH_SHORT).show()
            return@with
        }
        if (ingredientes.isEmpty()) {
            Toast.makeText(this@PantallaDeCrearReceta, "Agrega al menos 1 ingrediente", Toast.LENGTH_SHORT).show()
            return@with
        }
        if (pasos.isEmpty()) {
            Toast.makeText(this@PantallaDeCrearReceta, "Agrega al menos 1 paso", Toast.LENGTH_SHORT).show()
            return@with
        }

        // Mapear a tus dataclasses (unidad es obligatoria → "a gusto" por defecto)
        val ingList = ingredientes.map { (nom, cantStr) ->
            val cant = cantStr.toDoubleOrNull()
            Ingrediente(nombre = nom, cantidad = cant, unidad = "a gusto", nota = null)
        }
        val pasosList = pasos.mapIndexed { i, t -> Paso(n = i + 1, texto = t) }
        val categoriasList = listOf(Categoria(n = 1, texto = cat))

        val coctel = Coctel(
            id = GuardarMiReceta.generarIdDesdeNombre(nombre),
            nombre = nombre,
            descripcion = descripcion.ifBlank { null },
            dificultad = "facil",                      // no tienes campo específico en el XML
            nivel_alcohol = "medio",                   // idem
            sabor = if (perfilSabor.isBlank()) "neutro" else perfilSabor.lowercase(),
            ingredientes = ingList,
            pasos = pasosList,
            categorias = categoriasList,
            imagen = "moderno_complejo",               // siempre mipmap/moderno_complejo
            ultima_actualizacion = GuardarMiReceta.hoyISO(),
            url_video_tutorial = null
        )

        GuardarMiReceta.addRecipe(this@PantallaDeCrearReceta, coctel)

        Toast.makeText(
            this@PantallaDeCrearReceta,
            "Receta creada. Procesaremos tu solicitud 🍹",
            Toast.LENGTH_SHORT
        ).show()

        // Limpieza de UI
        etNombre.text?.clear()
        etDescripcion.text?.clear()
        etPerfilSabor.text?.clear()
        etTiempo.text?.clear()
        ingredientes.clear()
        pasos.clear()
        actualizarListaIngredientes()
        actualizarListaPasos()
        chipGroupCategoria.clearCheck()
    }
}
