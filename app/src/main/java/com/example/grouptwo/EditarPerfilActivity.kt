package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grouptwo.databinding.ActivityEditarPerfilBinding
import com.example.grouptwo.databinding.ActivityPantallaInicialBinding
import com.example.grouptwo.repository.GuardarPerfil

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPerfilBinding

    companion object {
        val ID_NOMBRE_DE_USUARIO = "ID_NOMBRE_DE_USUARIO"
        val ID_DESCRIPCION = "ID_DESCRIPCION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.EditTextNombreDeUsuario.setText(GuardarPerfil.loadNombre(this) ?: "")
        binding.EditTextDescripcion.setText(GuardarPerfil.loadDescripcion(this) ?: "")


        binding.EditarPerfilFlechaAtras.setOnClickListener {
            finish()
        }

        binding.cardAceptarCambios.setOnClickListener {
            val nombreUsuarioNuevo: String = binding.EditTextNombreDeUsuario.text.toString()
            val descripcionNuevo: String = binding.EditTextDescripcion.text.toString()

            GuardarPerfil.save(this, nombreUsuarioNuevo, descripcionNuevo)

            val intentGuardarCambios = Intent(this, PerfilActivity::class.java).apply {
                putExtra(ID_NOMBRE_DE_USUARIO, nombreUsuarioNuevo)
                putExtra(ID_DESCRIPCION, descripcionNuevo)
            }

            finish()
            startActivity(intentGuardarCambios)
        }




    }
}