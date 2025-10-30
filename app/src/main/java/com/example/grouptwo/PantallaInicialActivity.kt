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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grouptwo.databinding.ActivityPantallaInicialBinding

class PantallaInicialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPantallaInicialBinding


    private lateinit var etBuscar: EditText
    private lateinit var tvResultadosTitle: TextView
    private lateinit var containerResultados: LinearLayout
    private lateinit var contenidoEstatico: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPantallaInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        inicializarVistas()
        configurarListeners()
    }

    private fun inicializarVistas() {
        // Asegúrate de usar binding si las vistas no tienen ID en el root.
        // Usaré findViewById basado en tu código original, pero es mejor usar binding.
        etBuscar = findViewById(R.id.etBuscarIngrediente)
        tvResultadosTitle = findViewById(R.id.tvResultadosTitle)
        containerResultados = findViewById(R.id.containerResultados)
        contenidoEstatico = findViewById(R.id.contenidoEstatico)
    }

    private fun configurarListeners() {
        val btnCosmopolitan = findViewById<ImageButton?>(R.id.btnCosmopolitan)
        val btnCategorias = findViewById<Button?>(R.id.boton_categorias)
        val btnMojito = findViewById<ImageButton?>(R.id.btnMojito)
        val btnMargarita = findViewById<ImageButton>(R.id.btnMargarita)
        val btnNegroni = findViewById<ImageButton>(R.id.btnNegroni)
        val btnBuscarIcon = findViewById<ImageButton?>(R.id.btnBuscarIcon)
        val btnTengoEnCasa = findViewById<Button?>(R.id.btnTengoEnCasa)
        val btnCalculadora = findViewById<ImageButton?>(R.id.btnCalculadora)

        // Nota: Si usas IDs fijos (como "ckt_cosmopolitan"), asegúrate de que el JSON/Parser
        // los maneje correctamente, o usa los IDs numéricos (ej: "1", "2").
//        btnCosmopolitan?.setOnClickListener {
//            VerRecetaDetalladaActivity.launch(this, "ckt_cosmopolitan")
//        }
//
//        btnNegroni?.setOnClickListener {
//            VerRecetaDetalladaActivity.launch(this,"ckt_negroni")
//        }

        btnCategorias?.setOnClickListener {
            startActivity(Intent(this, CategoriasActivity::class.java))
        }

//        btnMojito?.setOnClickListener {
//            VerRecetaDetalladaActivity.launch(this, "ckt_mojito")
//        }
//        btnMargarita?.setOnClickListener {
//            VerRecetaDetalladaActivity.launch(this, "ckt_margarita")
//        }


        btnBuscarIcon?.setOnClickListener {
            // Este icono abre la PantallaDeBuscarActivity
            val intent = Intent(this, BuscadorActivity::class.java)
            startActivity(intent)
        }

        btnTengoEnCasa?.setOnClickListener {
            // Navega a la PantallaDeBuscarActivity con el modo "tengo_en_casa"
            val intent = Intent(this, BuscadorActivity::class.java)
            intent.putExtra("modo", "tengo_en_casa")
            startActivity(intent)
        }

        btnCalculadora?.setOnClickListener {
            startActivity(Intent(this, CalculadoraActivity::class.java))
        }

        binding.perfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        // Asumiendo que 'binding.buscador' es el ícono de la barra inferior
        binding.buscador.setOnClickListener {
            startActivity(Intent(this, BuscadorActivity::class.java))
        }

        // Búsqueda en tiempo real

    }



//    private fun buscarCocteles(query: String) {
//        if (todosLosCocteles.isEmpty()) {
//            Toast.makeText(this, "Cargando cócteles...", Toast.LENGTH_SHORT).show()
//            return
//        }





    private fun ocultarResultados() {
        tvResultadosTitle.visibility = View.GONE
        containerResultados.visibility = View.GONE
        containerResultados.removeAllViews()

        // Limpieza de listeners duplicados (no deberían estar aquí)
        // El listener de 'binding.perfil' y 'binding.buscador' ya se configuró en configurarListeners()
        // Los elimino para limpiar el código, asumiendo que el ID main es el correcto.

        // Mostrar contenido estático
        contenidoEstatico.visibility = View.VISIBLE
    }

    // ✅ Usa CoctelDetalle
    private fun agregarCoctelCard(coctel: Coctel) {
        val cardView = CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 24)
            }
            radius = 32f
            cardElevation = 0f
            // Usar R.color.card_background o un color más apropiado si existe
            setCardBackgroundColor(resources.getColor(android.R.color.darker_gray, null))
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
                // Nota: Usar darker_gray para el texto sobre darker_gray puede ser ilegible
                setTextColor(resources.getColor(android.R.color.darker_gray, null))
                setPadding(0, 8, 0, 0)
            }
            linearLayout.addView(tvDescripcion)
        }
        // ... (Resto del código para dificultad, alcohol, sabor)

        // Info adicional
        val infoLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 16, 0, 0)
        }

        // Nivel de dificultad
//        coctel.nivelDificultad?.let {
//            val tvDificultad = TextView(this).apply {
//                text = "📊 $it"
//                textSize = 12f
//                setTextColor(resources.getColor(android.R.color.white, null))
//                setPadding(0, 0, 24, 0)
//            }
//            infoLayout.addView(tvDificultad)
//        }

        // Nivel de alcohol
//        coctel.nivelAlcohol?.let {
//            val tvAlcohol = TextView(this).apply {
//                text = "🍸 $it"
//                textSize = 12f
//                setTextColor(resources.getColor(android.R.color.white, null))
//                setPadding(0, 0, 24, 0)
//            }
//            infoLayout.addView(tvAlcohol)
//        }
//
//        // Sabor
//        coctel.saborPredominante?.let {
//            val tvSabor = TextView(this).apply {
//                text = "😋 $it"
//                textSize = 12f
//                setTextColor(resources.getColor(android.R.color.white, null))
//            }
//            infoLayout.addView(tvSabor)
//        }
//
//        linearLayout.addView(infoLayout)
//        cardView.addView(linearLayout)
//
//        // Click para ver detalles
//        cardView.setOnClickListener {
//            Toast.makeText(
//                this,
//                "Ver detalles de ${coctel.nombre} (ID: ${coctel.id})",
//                Toast.LENGTH_SHORT
//            ).show()
//            // ✅ Implementación de la navegación al detalle
//            VerRecetaDetalladaActivity.launch(this, coctel.id.toString())
//        }

        containerResultados.addView(cardView)
    }
}