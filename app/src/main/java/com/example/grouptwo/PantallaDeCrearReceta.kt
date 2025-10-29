package com.example.grouptwo

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipGroup

class PantallaDeCrearReceta : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnGuardar: Button
    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var chipGroupCategoria: ChipGroup
    private lateinit var etPerfilSabor: EditText
    private lateinit var etTiempo: EditText
    private lateinit var btnAgregarIngrediente: Button
    private lateinit var btnAgregarPaso: Button
    private lateinit var containerIngredientes: LinearLayout
    private lateinit var containerPasos: LinearLayout

    private val ingredientes = mutableListOf<Pair<String, String>>()
    private val pasos = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_crear_receta) // ← Usa el XML unificado

        inicializarVistas()
        configurarListeners()
    }

    private fun inicializarVistas() {
        btnBack = findViewById(R.id.btnBack)
        btnGuardar = findViewById(R.id.btnGuardar)
        etNombre = findViewById(R.id.etNombre)
        etDescripcion = findViewById(R.id.etDescripcion)
        chipGroupCategoria = findViewById(R.id.chipGroupCategoria)
        etPerfilSabor = findViewById(R.id.etPerfilSabor)
        etTiempo = findViewById(R.id.etTiempo)
        btnAgregarIngrediente = findViewById(R.id.btnAgregarIngrediente)
        btnAgregarPaso = findViewById(R.id.btnAgregarPaso)
        containerIngredientes = findViewById(R.id.containerIngredientes)
        containerPasos = findViewById(R.id.containerPasos)
    }

    private fun configurarListeners() {
        btnBack.setOnClickListener { finish() }
        btnGuardar.setOnClickListener { guardarCoctel() }
        btnAgregarIngrediente.setOnClickListener { mostrarDialogoAgregarIngrediente() }
        btnAgregarPaso.setOnClickListener { mostrarDialogoAgregarPaso() }
    }

    private fun mostrarDialogoAgregarIngrediente() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_ingrediente, null)
        val etIngrediente = dialogView.findViewById<EditText>(R.id.etIngredienteDialog)
        val etCantidad = dialogView.findViewById<EditText>(R.id.etCantidadDialog)

        AlertDialog.Builder(this)
            .setTitle("Agregar Ingrediente")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val ingrediente = etIngrediente.text.toString().trim()
                val cantidad = etCantidad.text.toString().trim()
                if (ingrediente.isNotEmpty() && cantidad.isNotEmpty()) {
                    ingredientes.add(Pair(ingrediente, cantidad))
                    actualizarListaIngredientes()
                } else {
                    Toast.makeText(this, "Completa ambos campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoAgregarPaso() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_paso, null)
        val etPaso = dialogView.findViewById<EditText>(R.id.etPasoDialog)

        AlertDialog.Builder(this)
            .setTitle("Paso ${pasos.size + 1}")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val paso = etPaso.text.toString().trim()
                if (paso.isNotEmpty()) {
                    pasos.add(paso)
                    actualizarListaPasos()
                } else {
                    Toast.makeText(this, "Describe el paso", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun actualizarListaIngredientes() {
        containerIngredientes.removeAllViews()

        ingredientes.forEachIndexed { index, ingrediente ->
            val (ing, cant) = ingrediente

            val itemView = LayoutInflater.from(this).inflate(R.layout.item_ingrediente_nuevo, containerIngredientes, false)
            itemView.findViewById<TextView>(R.id.txtNombre).text = ing
            itemView.findViewById<TextView>(R.id.txtCantidad).text = cant

            itemView.findViewById<ImageButton>(R.id.btnEliminar).setOnClickListener {
                ingredientes.remove(ingrediente)
                actualizarListaIngredientes()
            }
            containerIngredientes.addView(itemView)
        }
    }

    private fun actualizarListaPasos() {
        containerPasos.removeAllViews()
        pasos.forEachIndexed { index, paso ->
            val itemView = LayoutInflater.from(this).inflate(R.layout.item_paso_nuevo, containerPasos, false)
            itemView.findViewById<TextView>(R.id.txtIndice).text = "${index + 1}."
            itemView.findViewById<TextView>(R.id.txtDescripcion).text = paso

            itemView.findViewById<ImageButton>(R.id.btnEliminarPaso).setOnClickListener {
                val indexAEliminar = pasos.indexOf(paso)

                if (indexAEliminar != -1) {
                    pasos.removeAt(indexAEliminar)
                    actualizarListaPasos()
                }
            }
            containerPasos.addView(itemView)
        }
    }

    private fun guardarCoctel() {
        val nombre = etNombre.text.toString().trim()
        val categoriaId = chipGroupCategoria.checkedChipId

        if (nombre.isEmpty()) return Toast.makeText(this, "Nombre obligatorio", Toast.LENGTH_SHORT).show()
        if (categoriaId == -1) return Toast.makeText(this, "Selecciona categoría", Toast.LENGTH_SHORT).show()
        if (ingredientes.isEmpty()) return Toast.makeText(this, "Agrega al menos 1 ingrediente", Toast.LENGTH_SHORT).show()
        if (pasos.isEmpty()) return Toast.makeText(this, "Agrega al menos 1 paso", Toast.LENGTH_SHORT).show()

        val categoria = when (categoriaId) {
            R.id.chipClasicos -> "Clásicos"
            R.id.chipCreativos -> "Creativos"
            R.id.chipSinAlcohol -> "Sin Alcohol"
            R.id.chipTematicos -> "Temáticos"
            else -> ""
        }

        Toast.makeText(
            this,
            "Cóctel '$nombre' guardado!\nCategoría: $categoria\nIngredientes: ${ingredientes.size}\nPasos: ${pasos.size}",
            Toast.LENGTH_LONG
        ).show()

        finish()
    }
}