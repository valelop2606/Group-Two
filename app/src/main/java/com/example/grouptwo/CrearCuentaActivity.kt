package com.example.grouptwo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grouptwo.databinding.ActivityCrearCuentaBinding
import com.example.grouptwo.databinding.ActivityInicioSesionBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class CrearCuentaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearCuentaBinding

    val context: Context = this
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth


        binding.botonCrearCuenta.setOnClickListener {
            val correo = binding.TextoCorreo.text.toString()
            val pass = binding.TextoPassword.text.toString()
            if (correo.isEmpty()) {
                Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show()
            } else if (pass.length < 8) {
                Toast.makeText(
                    this,
                    "La contraseña debe tener al menos 8 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                crearUsuario(correo, pass)
            }
        }


        binding.YaTengoCuenta.setOnClickListener {
            val intent = Intent(this, InicioSesionActivity::class.java)
            finish()
            startActivity(intent)

        }

    }
    fun crearUsuario(
        correo: String,
        pass:String
    ){
        auth.createUserWithEmailAndPassword(correo, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    finish()
                    val intentLogin = Intent(context, InicioSesionActivity::class.java)
                    startActivity(intentLogin)

                } else {
                    val error = task.exception?.message ?: "Error al crear usuario"
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
            }
    }
}