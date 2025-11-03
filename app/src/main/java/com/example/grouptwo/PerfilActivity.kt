package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grouptwo.EditarPerfilActivity.Companion.ID_DESCRIPCION
import com.example.grouptwo.EditarPerfilActivity.Companion.ID_NOMBRE_DE_USUARIO
import com.example.grouptwo.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.grouptwo.repository.GuardarPerfil
import com.example.grouptwo.repository.Favoritos // ✅ importa el objeto

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

        val nombre = GuardarPerfil.loadNombre(this)
        val descripcion = GuardarPerfil.loadDescripcion(this)

        binding.tvNombreUsuario.text = if (!nombre.isNullOrBlank()) nombre else "Nombre de Usuario"
        binding.tvDescripcion.text = if (!descripcion.isNullOrBlank()) descripcion else "Descripción"


        // ✅ Nuevo: botón para ver lista de favoritos
        binding.btnFavoritos.setOnClickListener {
            val intent = Intent(this, CoctelesPorCategoriaActivity::class.java)
            intent.putExtra("mostrar_favoritos", true) // decimos que muestre favoritos
            startActivity(intent)
        }

        binding.perfilConfiguracion.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        binding.perfilFlechaAtras.setOnClickListener {
            finish()
        }

        binding.perfilCerrarSesion.setOnClickListener {
            logout()
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, InicioSesionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
