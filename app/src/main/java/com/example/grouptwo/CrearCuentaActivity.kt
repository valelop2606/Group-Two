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
            val correo = binding.TextoUsername.text.toString()
            val pass = binding.TextoPassword.text.toString()
            if(pass.length < 8 && correo.isNotEmpty()){
             Toast.makeText(
                    baseContext,
                    "Debe ingresar una contraseña de 8 o más dígitos.",
                    Toast.LENGTH_SHORT,
                ).show()
            } else {
                crearUsuario(correo, pass)
            }
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
                    finish()
                    val intentLogin = Intent(context, CrearCuentaActivity::class.java)
                    startActivity(intentLogin)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}