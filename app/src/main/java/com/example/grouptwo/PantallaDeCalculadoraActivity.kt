package com.example.grouptwo

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.grouptwo.dataclases.Coctel
import org.json.JSONObject


data class CoctelCalculadora(
    val id: String,
    val nombre: String,
    val imagen: String?, // <-- CAMBIO: Añadido campo para la imagen
    var cantidad: Int = 5
)

class CalculadoraActivity : AppCompatActivity() {

    private var numeroInvitados = 10
    private val coctelesSeleccionados = mutableListOf<CoctelCalculadora>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_de_calculadora)

        inicializarVistas()
        actualizarUI()
    }

    private fun inicializarVistas() {
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnMenosInvitados = findViewById<ImageButton>(R.id.btnMenosInvitados)
        val btnMasInvitados = findViewById<ImageButton>(R.id.btnMasInvitados)
        val btnAgregarCoctel = findViewById<Button>(R.id.btnAgregarCoctel)
        val btnVerListaCompras = findViewById<Button>(R.id.btnVerListaCompras)

        btnBack.setOnClickListener {
            finish()
        }

        btnMenosInvitados.setOnClickListener {
            if (numeroInvitados > 1) {
                numeroInvitados = numeroInvitados - 1
                actualizarUI()
            }
        }

        btnMasInvitados.setOnClickListener {
            numeroInvitados = numeroInvitados + 1
            actualizarUI()
        }

        btnAgregarCoctel.setOnClickListener {
            mostrarDialogoSeleccionarCoctel()
        }

        btnVerListaCompras.setOnClickListener {
            val intent = Intent(this, ListaDeCompras::class.java)
            startActivity(intent)
        }
    }

    private fun mostrarDialogoSeleccionarCoctel() {
        val todosLosCocteles = cargarCoctelesDesdeJSON()

        val nombresCocteles = Array(todosLosCocteles.size) { "" }
        var i = 0
        while (i < todosLosCocteles.size) {
            nombresCocteles[i] = todosLosCocteles[i].nombre
            i = i + 1
        }

        AlertDialog.Builder(this)
            .setTitle("Selecciona un cóctel")
            .setItems(nombresCocteles) { dialog, position ->
                val coctelSeleccionado = todosLosCocteles[position]

                var encontrado = false
                var j = 0
                while (j < coctelesSeleccionados.size) {
                    if (coctelesSeleccionados[j].id == coctelSeleccionado.id) {
                        coctelesSeleccionados[j].cantidad = coctelesSeleccionados[j].cantidad + 5
                        encontrado = true
                        break
                    }
                    j = j + 1
                }

                if (!encontrado) {
                    val nuevoCoctel = CoctelCalculadora(
                        id = coctelSeleccionado.id,
                        nombre = coctelSeleccionado.nombre,
                        imagen = coctelSeleccionado.imagen, // <-- CAMBIO: Guardamos la imagen
                        cantidad = 5
                    )
                    coctelesSeleccionados.add(nuevoCoctel)
                }

                actualizarUI()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cargarCoctelesDesdeJSON(): List<Coctel> {
        val listaCocteles = mutableListOf<Coctel>()

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
                        // cantidad = if (ingJson.has("cantidad")) ingJson.getInt("cantidad") else null,
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

                listaCocteles.add(coctel)
                i = i + 1
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return listaCocteles
    }

    private fun actualizarUI() {
        val tvNumeroInvitados = findViewById<TextView>(R.id.tvNumeroInvitados)
        val tvTotalBebidas = findViewById<TextView>(R.id.tvTotalBebidas)
        val containerCocteles = findViewById<LinearLayout>(R.id.containerCocteles)
        val tvResumenInvitados = findViewById<TextView>(R.id.tvResumenInvitados)
        val tvResumenBebidas = findViewById<TextView>(R.id.tvResumenBebidas)
        val tvResumenPromedio = findViewById<TextView>(R.id.tvResumenPromedio)
        val tvNoCocteles = findViewById<TextView>(R.id.tvNoCocteles)

        tvNumeroInvitados.text = numeroInvitados.toString()

        if (coctelesSeleccionados.isEmpty()) {
            tvNoCocteles.visibility = View.VISIBLE
            containerCocteles.visibility = View.GONE
        } else {
            tvNoCocteles.visibility = View.GONE
            containerCocteles.visibility = View.VISIBLE
        }

        containerCocteles.removeAllViews()
        var i = 0
        while (i < coctelesSeleccionados.size) {
            agregarCoctelView(coctelesSeleccionados[i])
            i = i + 1
        }

        var totalBebidas = 0
        var j = 0
        while (j < coctelesSeleccionados.size) {
            totalBebidas = totalBebidas + coctelesSeleccionados[j].cantidad
            j = j + 1
        }

        val promedio: Float = if (numeroInvitados > 0) {
            totalBebidas.toFloat() / numeroInvitados.toFloat()
        } else {
            0f
        }

        tvTotalBebidas.text = totalBebidas.toString() + " bebidas"
        tvResumenInvitados.text = numeroInvitados.toString() + " personas"
        tvResumenBebidas.text = totalBebidas.toString() + " cócteles"
        tvResumenPromedio.text = String.format("%.1f bebidas", promedio)
    }

    private fun agregarCoctelView(coctel: CoctelCalculadora) {
        val containerCocteles = findViewById<LinearLayout>(R.id.containerCocteles)

        val coctelView = LayoutInflater.from(this).inflate(
            R.layout.item_coctel_calculadora,
            containerCocteles,
            false
        )

        val cardCoctel = coctelView.findViewById<CardView>(R.id.cardCoctel)
        val ivCoctel = coctelView.findViewById<ImageView>(R.id.ivCoctel)
        val tvNombreCoctel = coctelView.findViewById<TextView>(R.id.tvNombreCoctel)
        val tvBebidasCoctel = coctelView.findViewById<TextView>(R.id.tvBebidasCoctel)
        val btnMenos = coctelView.findViewById< ImageButton>(R.id.btnMenosCoctel)
        val tvCantidad = coctelView.findViewById<TextView>(R.id.tvCantidadCoctel)
        val btnMas = coctelView.findViewById<ImageButton>(R.id.btnMasCoctel)

        tvNombreCoctel.text = coctel.nombre
        tvBebidasCoctel.text = coctel.cantidad.toString() + " bebidas"
        tvCantidad.text = coctel.cantidad.toString()

        if (coctel.imagen != null) {
            val resourceId = resources.getIdentifier(coctel.imagen, "drawable", packageName)
            if (resourceId != 0) {
                ivCoctel.setImageResource(resourceId)
            } else {
                ivCoctel.setImageResource(R.drawable.ic_launcher_background)
            }
        } else {
            ivCoctel.setImageResource(R.drawable.ic_launcher_background)
        }

        btnMenos.setOnClickListener {
            if (coctel.cantidad > 1) {
                coctel.cantidad = coctel.cantidad - 1
                actualizarUI()
            } else {
                mostrarDialogoEliminar(coctel)
            }
        }

        btnMas.setOnClickListener {
            coctel.cantidad = coctel.cantidad + 1
            actualizarUI()
        }

        cardCoctel.setOnLongClickListener {
            mostrarDialogoEliminar(coctel)
            true
        }


        ivCoctel.setOnClickListener {
            val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
            intent.putExtra(VerRecetaDetalladaActivity.EXTRA_COCKTAIL_ID, coctel.id)
            startActivity(intent)
        }

        containerCocteles.addView(coctelView)
    }

    private fun mostrarDialogoEliminar(coctel: CoctelCalculadora) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar cóctel")
            .setMessage("¿Quieres eliminar " + coctel.nombre + " de la lista?")
            .setPositiveButton("Eliminar") { dialog, which ->
                coctelesSeleccionados.remove(coctel)
                actualizarUI()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}