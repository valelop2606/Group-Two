package com.example.grouptwo

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.grouptwo.dataclases.Coctel


data class CoctelCalculadora(
    val id: Int,
    val nombre: String,
    var cantidad: Int = 5
)

class CalculadoraActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnMenosInvitados: ImageButton
    private lateinit var btnMasInvitados: ImageButton
    private lateinit var tvNumeroInvitados: TextView
    private lateinit var tvTotalBebidas: TextView
    private lateinit var containerCocteles: LinearLayout
    private lateinit var tvResumenInvitados: TextView
    private lateinit var tvResumenBebidas: TextView
    private lateinit var tvResumenPromedio: TextView
    private lateinit var btnVerListaCompras: Button
    private lateinit var btnAgregarCoctel: Button
    private lateinit var tvNoCocteles: TextView

    // ✅ CORRECCIÓN 1: Inicialización con Context usando by lazy
    private var numeroInvitados = 10
    private val coctelesSeleccionados = mutableListOf<CoctelCalculadora>()
    // ✅ CORRECCIÓN 2: El tipo debe ser List<CoctelDetalle>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_de_calculadora)

        inicializarVistas()
        configurarListeners()
        actualizarUI()
    }

    private fun inicializarVistas() {
        btnBack = findViewById(R.id.btnBack)
        btnMenosInvitados = findViewById(R.id.btnMenosInvitados)
        btnMasInvitados = findViewById(R.id.btnMasInvitados)
        tvNumeroInvitados = findViewById(R.id.tvNumeroInvitados)
        tvTotalBebidas = findViewById(R.id.tvTotalBebidas)
        containerCocteles = findViewById(R.id.containerCocteles)
        tvResumenInvitados = findViewById(R.id.tvResumenInvitados)
        tvResumenBebidas = findViewById(R.id.tvResumenBebidas)
        tvResumenPromedio = findViewById(R.id.tvResumenPromedio)
        btnVerListaCompras = findViewById(R.id.btnVerListaCompras)
        btnAgregarCoctel = findViewById(R.id.btnAgregarCoctel)
        tvNoCocteles = findViewById(R.id.tvNoCocteles)
    }



    private fun configurarListeners() {
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



        btnVerListaCompras.setOnClickListener {
            // Nota: Debes pasar los datos necesarios a ListaDeCompras
            val intent = Intent(this, ListaDeCompras::class.java)
            // Aquí deberías pasar 'numeroInvitados' y 'coctelesSeleccionados'
            startActivity(intent)
        }

    }


    private fun actualizarUI() {
        // Actualizar número de invitados
        tvNumeroInvitados.text = numeroInvitados.toString()

        // Mostrar/ocultar mensaje de no cócteles
        if (coctelesSeleccionados.isEmpty()) {
            tvNoCocteles.visibility = View.VISIBLE
            containerCocteles.visibility = View.GONE
        } else {
            tvNoCocteles.visibility = View.GONE
            containerCocteles.visibility = View.VISIBLE
        }

        // Actualizar lista de cócteles
        containerCocteles.removeAllViews()
        for (coctel in coctelesSeleccionados) {
            agregarCoctelView(coctel)
        }

        // Calcular totales
        val totalBebidas = coctelesSeleccionados.sumOf { it.cantidad }
        val promedio = if (numeroInvitados > 0) totalBebidas.toFloat() / numeroInvitados else 0f

        // Actualizar badges y resumen
        tvTotalBebidas.text = "$totalBebidas bebidas"
        tvResumenInvitados.text = "$numeroInvitados personas"
        tvResumenBebidas.text = "$totalBebidas cócteles"
        tvResumenPromedio.text = String.format("%.1f bebidas", promedio)
    }

    private fun agregarCoctelView(coctel: CoctelCalculadora) {
        val coctelView = LayoutInflater.from(this).inflate(
            R.layout.item_coctel_calculadora,
            containerCocteles,
            false
        )

        val cardCoctel = coctelView.findViewById<CardView>(R.id.cardCoctel)
        val tvNombreCoctel = coctelView.findViewById<TextView>(R.id.tvNombreCoctel)
        val tvBebidasCoctel = coctelView.findViewById<TextView>(R.id.tvBebidasCoctel)
        val btnMenos = coctelView.findViewById<Button>(R.id.btnMenosCoctel)
        val tvCantidad = coctelView.findViewById<TextView>(R.id.tvCantidadCoctel)
        val btnMas = coctelView.findViewById<Button>(R.id.btnMasCoctel)

        // El ImageView no es necesario para la lógica de la calculadora, lo mantengo comentado
        // val ivCoctel = coctelView.findViewById<android.widget.ImageView>(R.id.ivCoctel)

        // Configurar datos
        tvNombreCoctel.text = coctel.nombre
        tvBebidasCoctel.text = "${coctel.cantidad} bebidas"
        tvCantidad.text = coctel.cantidad.toString()

        // Botón menos
        btnMenos.setOnClickListener {
            if (coctel.cantidad > 1) {
                coctel.cantidad--
                actualizarUI()
            } else {
                // Si llega a 1 y se presiona menos, preguntar si quiere eliminar
                AlertDialog.Builder(this)
                    .setTitle("Eliminar cóctel")
                    .setMessage("¿Quieres eliminar ${coctel.nombre} de la lista?")
                    .setPositiveButton("Eliminar") { _, _ ->
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        }

        // Botón más
        btnMas.setOnClickListener {
            coctel.cantidad++
            actualizarUI()
        }

        // Long click para eliminar directamente
        cardCoctel.setOnLongClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar cóctel")
                .setMessage("¿Quieres eliminar ${coctel.nombre} de la lista?")
                .setPositiveButton("Eliminar") { _, _ ->
                }
                .setNegativeButton("Cancelar", null)
                .show()
            true
        }


        containerCocteles.addView(coctelView)
    }
}