package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grouptwo.databinding.ActivityPerfilBinding
import com.example.grouptwo.repository.GuardarPerfil
import com.google.firebase.auth.FirebaseAuth

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Cargar nombre y descripción guardados
        val nombre = GuardarPerfil.loadNombre(this)
        val descripcion = GuardarPerfil.loadDescripcion(this)
        binding.tvNombreUsuario.text = if (!nombre.isNullOrBlank()) nombre else "Nombre de Usuario"
        binding.tvDescripcion.text = if (!descripcion.isNullOrBlank()) descripcion else "Descripción"

        // Abrir Favoritos (ya funciona con CoctelesPorCategoriaActivity)
        binding.btnFavoritos.setOnClickListener {
            val intent = Intent(this, CoctelesPorCategoriaActivity::class.java)
            intent.putExtra("mostrar_favoritos", true)
            startActivity(intent)
        }

        // Abrir Mis Recetas (usa el mismo diseño que categorías)
        binding.btnMisRecetas.setOnClickListener {
            val intent = Intent(this, CoctelesPorCategoriaActivity::class.java)
            intent.putExtra("mostrar_mis_recetas", true)
            startActivity(intent)
        }

        // Ir a Configuración
        binding.perfilConfiguracion.setOnClickListener {
            startActivity(Intent(this, ConfiguracionActivity::class.java))
        }

        // Atrás
        binding.perfilFlechaAtras.setOnClickListener { finish() }

        // Crear Receta
        binding.crearReceta.setOnClickListener {
            val intent = Intent(this, PantallaDeCrearReceta::class.java)
            startActivity(intent)
        }

        // Cerrar sesión
        binding.perfilCerrarSesion.setOnClickListener { logout() }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, InicioSesionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
