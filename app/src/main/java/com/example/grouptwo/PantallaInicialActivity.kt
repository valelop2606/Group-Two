package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.grouptwo.repository.CoctelRepository
import kotlinx.coroutines.launch

class PantallaInicialActivity : AppCompatActivity() {

    private val repository = CoctelRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_inicial)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val tvCosmo        = findViewById<TextView?>(R.id.textView_cosmopolitan)
        val imgCosmo       = findViewById<ImageButton?>(R.id.imagen_cosmopolitan)
        val btnCategorias  = findViewById<Button?>(R.id.boton_categorias)
        val btnMojito      = findViewById<ImageButton?>(R.id.btnMojito)
        val etBuscar       = findViewById<EditText?>(R.id.etBuscarIngrediente)
        val btnBuscarIcon  = findViewById<ImageButton?>(R.id.btnBuscarIcon)
        val btnTengoEnCasa = findViewById<Button?>(R.id.btnTengoEnCasa)
        val btnCalculadora = findViewById<ImageButton?>(R.id.btnCalculadora)

        // Cargar cócteles desde la API
        cargarCoctelesDesdeAPI()

        imgCosmo?.setOnClickListener { tvCosmo?.text = "Hola" }

        btnCategorias?.setOnClickListener {
            startActivity(Intent(this, CategoriasActivity::class.java))
        }

        btnMojito?.setOnClickListener {
            startActivity(Intent(this, VerRecetaDetalladaActivity::class.java))
        }

        btnBuscarIcon?.setOnClickListener {
            val intent = Intent(this, PantallaDeBuscarActivity::class.java)
            startActivity(intent)
        }

        btnTengoEnCasa?.setOnClickListener {
            val intent = Intent(this, PantallaDeBuscarActivity::class.java)
            intent.putExtra("modo", "tengo_en_casa")
            startActivity(intent)
        }

        btnCalculadora?.setOnClickListener {
            startActivity(Intent(this, CalculadoraActivity::class.java))
        }
    }

    private fun cargarCoctelesDesdeAPI() {
        lifecycleScope.launch {
            repository.obtenerTodosCocteles()
                .onSuccess { cocteles ->
                    // Éxito: se cargaron los cócteles
                    Log.d("PantallaInicial", "✅ Se cargaron ${cocteles.size} cócteles desde la API")

                    // Mostrar un mensaje al usuario
                    Toast.makeText(
                        this@PantallaInicialActivity,
                        "Cargados ${cocteles.size} cócteles",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Aquí puedes hacer algo con los datos, por ejemplo:
                    // - Actualizar RecyclerView
                    // - Guardar en memoria
                    // - Mostrar en logs

                    // Por ahora, mostramos los primeros 5 en el log
                    cocteles.take(5).forEach { coctel ->
                        Log.d("PantallaInicial", "📋 Cóctel: ${coctel.nombre} - ${coctel.descripcion}")
                    }
                }
                .onFailure { error ->
                    // Error al cargar
                    Log.e("PantallaInicial", "❌ Error al cargar cócteles: ${error.message}")
                    Toast.makeText(
                        this@PantallaInicialActivity,
                        "Error al cargar cócteles: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}
