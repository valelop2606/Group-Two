package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.grouptwo.dataclases.Coctel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.json.JSONObject

class BuscadorActivity : AppCompatActivity() {

    private val ingredientesSeleccionados = mutableListOf<String>()
    private var todosLosCocteles = mutableListOf<Coctel>()
    private lateinit var etBuscar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscador)

        cargarCoctelesDesdeJSON()
        configurarVistas()
        configurarChips()
    }

    private fun configurarVistas() {
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        etBuscar = findViewById<EditText>(R.id.etBuscarIngrediente)
        val btnVerPreparar = findViewById<Button>(R.id.btnVerPreparar)

        btnBack.setOnClickListener {
            finish()
        }

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val busqueda = s.toString()

                if (ingredientesSeleccionados.isEmpty()) {
                    mostrarCoctelesDisponibles()
                }

                filtrarChips(busqueda)
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
        var i = 0
        while (i < chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? Chip
            chip?.setOnCheckedChangeListener { buttonView, isChecked ->
                val nombreIngrediente = buttonView.text.toString()

                if (isChecked) {
                    var existe = false
                    var j = 0
                    while (j < ingredientesSeleccionados.size) {
                        if (ingredientesSeleccionados[j] == nombreIngrediente) {
                            existe = true
                            break
                        }
                        j = j + 1
                    }

                    if (!existe) {
                        ingredientesSeleccionados.add(nombreIngrediente)
                        agregarChipSeleccionado(nombreIngrediente)
                    }
                } else {
                    ingredientesSeleccionados.remove(nombreIngrediente)
                    quitarChipSeleccionado(nombreIngrediente)
                }

                actualizarContadorBoton()
            }
            i = i + 1
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

        var i = 0
        while (i < chipGroupSeleccionados.childCount) {
            val chip = chipGroupSeleccionados.getChildAt(i) as? Chip
            if (chip?.text.toString() == nombre) {
                chipGroupSeleccionados.removeView(chip)
                break
            }
            i = i + 1
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
        var i = 0
        while (i < chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? Chip
            if (chip?.text.toString() == nombre) {
                chip?.isChecked = false
                break
            }
            i = i + 1
        }
    }

    private fun actualizarContadorBoton() {
        val btnVerPreparar = findViewById<Button>(R.id.btnVerPreparar)
        val cantidad = ingredientesSeleccionados.size
        btnVerPreparar.text = "Ver qué puedo preparar (" + cantidad.toString() + ")"
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
        var i = 0
        while (i < chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? Chip
            if (chip != null) {
                if (busqueda.isEmpty()) {
                    chip.visibility = View.VISIBLE
                } else {
                    val coincide = chip.text.toString().lowercase().contains(busqueda)
                    chip.visibility = if (coincide) View.VISIBLE else View.GONE
                }
            }
            i = i + 1
        }
    }

    private fun mostrarCoctelesDisponibles() {
        val containerResultados = findViewById<LinearLayout>(R.id.containerResultados)
        val tvResultadosTitle = findViewById<TextView>(R.id.tvResultadosTitle)

        containerResultados.removeAllViews()

        val busquedaPorNombre = etBuscar.text.toString().lowercase()
        val hayBusquedaPorNombre = busquedaPorNombre.isNotEmpty()

        val coctelesDisponibles = mutableListOf<Coctel>()

        if (ingredientesSeleccionados.isEmpty() && !hayBusquedaPorNombre) {
            tvResultadosTitle.visibility = View.GONE
            containerResultados.visibility = View.GONE
            return
        }

        var i = 0
        while (i < todosLosCocteles.size) {
            val coctel = todosLosCocteles[i]

            val cumpleFiltroNombre = if (hayBusquedaPorNombre) {
                coctel.nombre.lowercase().contains(busquedaPorNombre)
            } else {
                true
            }

            if (ingredientesSeleccionados.isEmpty()) {
                if (cumpleFiltroNombre) {
                    coctelesDisponibles.add(coctel)
                }

            } else {

                var coincidenciasIngredientes = 0
                var j = 0
                while (j < coctel.ingredientes.size) {
                    val ingrediente = coctel.ingredientes[j]
                    val nombreIngrediente = ingrediente.nombre.lowercase()

                    var k = 0
                    while (k < ingredientesSeleccionados.size) {
                        val seleccionado = ingredientesSeleccionados[k]
                        if (nombreIngrediente.contains(seleccionado.lowercase()) ||
                            seleccionado.lowercase().contains(nombreIngrediente)) {
                            coincidenciasIngredientes = coincidenciasIngredientes + 1
                            break
                        }
                        k = k + 1
                    }
                    j = j + 1
                }

                if (coincidenciasIngredientes > 0 && cumpleFiltroNombre) {
                    coctelesDisponibles.add(coctel)
                }
            }

            i = i + 1
        }

        if (coctelesDisponibles.isEmpty()) {
            tvResultadosTitle.text = "No se encontraron cócteles."
            tvResultadosTitle.visibility = View.VISIBLE
            containerResultados.visibility = View.GONE
        } else {
            tvResultadosTitle.text = "Cócteles disponibles (" + coctelesDisponibles.size + ")"
            tvResultadosTitle.visibility = View.VISIBLE
            containerResultados.visibility = View.VISIBLE

            var m = 0
            while (m < coctelesDisponibles.size) {
                agregarCoctelView(coctelesDisponibles[m], containerResultados)
                m = m + 1
            }
        }
    }

    private fun agregarCoctelView(coctel: Coctel, container: LinearLayout) {
        val coctelView = LayoutInflater.from(this).inflate(
            R.layout.item_coctel,
            container,
            false
        )

        val imgCoctel = coctelView.findViewById<ImageView>(R.id.imgCoctel)
        val tvNombreCoctel = coctelView.findViewById<TextView>(R.id.txtNombreCoctel)
        val tvDescripcionCoctel = coctelView.findViewById<TextView>(R.id.txtDescripcionCoctel)

        tvNombreCoctel.text = coctel.nombre
        tvDescripcionCoctel.text = coctel.dificultad + " - " + coctel.nivel_alcohol

        if (coctel.imagen != null) {
            val resourceId = resources.getIdentifier(coctel.imagen, "drawable", packageName)
            if (resourceId != 0) {
                imgCoctel.setImageResource(resourceId)
            } else {
                imgCoctel.setImageResource(R.drawable.ic_launcher_background)
            }
        } else {
            imgCoctel.setImageResource(R.drawable.ic_launcher_background)
        }


        coctelView.setOnClickListener {
            val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
            intent.putExtra(VerRecetaDetalladaActivity.EXTRA_COCKTAIL_ID, coctel.id)
            startActivity(intent)
        }

        container.addView(coctelView)
    }


    private fun cargarCoctelesDesdeJSON() {
        try {
            val inputStream = assets.open("cocteles.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val jsonString = String(buffer, Charsets.UTF_8)
            val jsonObject = JSONObject(jsonString)
            val coctelesArray = jsonObject.getJSONArray("cocteles")

            var i = 0
            while (i < coctelesArray.length()) {
                val coctelJson = coctelesArray.getJSONObject(i)

                val ingredientesList = mutableListOf<com.example.grouptwo.dataclases.Ingrediente>()
                val ingredientesArray = coctelJson.getJSONArray("ingredientes")
                var j = 0
                while (j < ingredientesArray.length()) {
                    val ingJson = ingredientesArray.getJSONObject(j)
                    val ingrediente = com.example.grouptwo.dataclases.Ingrediente(
                        nombre = ingJson.getString("nombre"),
                        unidad = ingJson.getString("unidad")
                    )
                    ingredientesList.add(ingrediente)
                    j = j + 1
                }

                val pasosList = mutableListOf<com.example.grouptwo.dataclases.Paso>()
                val pasosArray = coctelJson.getJSONArray("pasos")
                var k = 0
                while (k < pasosArray.length()) {
                    val pasoJson = pasosArray.getJSONObject(k)
                    val paso = com.example.grouptwo.dataclases.Paso(
                        n = pasoJson.getInt("n"),
                        texto = pasoJson.getString("texto")
                    )
                    pasosList.add(paso)
                    k = k + 1
                }

                val categoriasList = mutableListOf<com.example.grouptwo.dataclases.Categoria>()
                val categoriasArray = coctelJson.getJSONArray("categorias")
                var l = 0
                while (l < categoriasArray.length()) {
                    val catJson = categoriasArray.getJSONObject(l)
                    val categoria = com.example.grouptwo.dataclases.Categoria(
                        n = catJson.getInt("n"),
                        texto = catJson.getString("texto")
                    )
                    categoriasList.add(categoria)
                    l = l + 1
                }

                val coctel = Coctel(
                    id = coctelJson.getString("id"),
                    nombre = coctelJson.getString("nombre"),
                    descripcion = if (coctelJson.has("descripcion")) coctelJson.getString("descripcion") else "",
                    dificultad = coctelJson.getString("dificultad"),
                    nivel_alcohol = coctelJson.getString("nivel_alcohol"),
                    sabor = coctelJson.getString("sabor"),
                    ingredientes = ingredientesList,
                    pasos = pasosList,
                    categorias = categoriasList,
                    imagen = if (coctelJson.has("imagen")) coctelJson.getString("imagen") else null,
                    ultima_actualizacion = coctelJson.getString("ultima_actualizacion"),
                    url_video_tutorial = if (coctelJson.has("url_video_tutorial")) coctelJson.getString("url_video_tutorial") else null
                )

                todosLosCocteles.add(coctel)
                i = i + 1
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}