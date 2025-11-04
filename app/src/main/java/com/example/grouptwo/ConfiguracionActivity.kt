package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grouptwo.databinding.ActivityConfiguracionBinding
import com.example.grouptwo.databinding.ActivityPantallaInicialBinding
import com.google.firebase.auth.FirebaseAuth

class ConfiguracionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConfiguracionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.configuracionFlechaAtras.setOnClickListener {
            finish()
        }

        binding.cerrarSesion.setOnClickListener {
            logout()
        }
        binding.cardEditarPerfil.setOnClickListener {
            val intent = Intent(this, EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        binding.switchNotificaciones.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Notificaciones activadas", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notificaciones desactivadas", Toast.LENGTH_SHORT).show()
            }
        }
        binding.switchModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Ya esta en modo oscuro", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No existe de momento modo claro", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cardSoporte.setOnClickListener {
            val intent = Intent(this, SoporteActivity::class.java)
            startActivity(intent)
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