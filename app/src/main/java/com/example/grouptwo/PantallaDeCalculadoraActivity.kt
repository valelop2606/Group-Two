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
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString


data class CoctelCalculadora(
    val id: String,
    val nombre: String,
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
                numeroInvitados--
                actualizarUI()
            }
        }

        btnMasInvitados.setOnClickListener {
            numeroInvitados++
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
        // Cargar cócteles desde el JSON
        val todosLosCocteles = cargarCoctelesDesdeJSON()

        val nombresCocteles = Array(todosLosCocteles.size) { i ->
            todosLosCocteles[i].nombre
        }

        AlertDialog.Builder(this)
            .setTitle("Selecciona un cóctel")
            .setItems(nombresCocteles) { dialog, position ->
                val coctelSeleccionado: Coctel = todosLosCocteles[position]

                var encontrado = false
                for (coctel in coctelesSeleccionados) {
                    if (coctel.id == coctelSeleccionado.id) {
                        coctel.cantidad = coctel.cantidad + 5
                        encontrado = true
                        break
                    }
                }

                if (!encontrado) {
                    val nuevoCoctel = CoctelCalculadora(
                        id = coctelSeleccionado.id,
                        nombre = coctelSeleccionado.nombre,
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
        for (coctel in coctelesSeleccionados) {
            agregarCoctelView(coctel)
        }

        var totalBebidas = 0
        for (coctel in coctelesSeleccionados) {
            totalBebidas = totalBebidas + coctel.cantidad
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
        val btnMenos = coctelView.findViewById<Button>(R.id.btnMenosCoctel)
        val tvCantidad = coctelView.findViewById<TextView>(R.id.tvCantidadCoctel)
        val btnMas = coctelView.findViewById<Button>(R.id.btnMasCoctel)

        tvNombreCoctel.text = coctel.nombre
        tvBebidasCoctel.text = coctel.cantidad.toString() + " bebidas"
        tvCantidad.text = coctel.cantidad.toString()

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