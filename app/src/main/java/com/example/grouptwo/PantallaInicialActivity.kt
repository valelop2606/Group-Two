package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PantallaInicialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_inicial)

        // Ajuste de insets (tu layout root tiene id 'main')
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // ==== Referencias a vistas que ya tienes en el XML ====
        val tvCosmo        = findViewById<TextView?>(R.id.textView_cosmopolitan)
        val imgCosmo       = findViewById<ImageButton?>(R.id.imagen_cosmopolitan)
        val btnCategorias  = findViewById<Button?>(R.id.boton_categorias)
        val btnMojito      = findViewById<ImageButton?>(R.id.btnMojito)

        // Nuevos (para ir a búsqueda)
        val etBuscar       = findViewById<EditText?>(R.id.etBuscarIngrediente)          // EditText del buscador
        val btnBuscarIcon  = findViewById<ImageButton?>(R.id.btnBuscarIcon)  // Icono "Buscar" del bottom
        val btnTengoEnCasa = findViewById<Button?>(R.id.btnTengoEnCasa)      // Botón "Lo que tengo en casa"

        // ==== Listeners existentes ====
        imgCosmo?.setOnClickListener { tvCosmo?.text = "Hola" }

        btnCategorias?.setOnClickListener {
            startActivity(Intent(this, CategoriasActivity::class.java))
        }

        btnMojito?.setOnClickListener {
            startActivity(Intent(this, VerRecetaDetalladaActivity::class.java))
        }

        btnBuscarIcon?.setOnClickListener {
            val intent = Intent(this, PantallaDeBuscarActivity::class.java)
//            val query = etBuscar?.text?.toString()?.trim()
//            if (!query.isNullOrEmpty()) intent.putExtra("query", query)
            startActivity(intent)
        }

        btnTengoEnCasa?.setOnClickListener {
            val intent = Intent(this, PantallaDeBuscarActivity::class.java)
            intent.putExtra("modo", "tengo_en_casa")
            startActivity(intent)
        }
    }
}
