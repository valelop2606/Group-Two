package com.example.grouptwo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grouptwo.databinding.ActivityCategoriasBinding
import com.example.grouptwo.databinding.ActivityInicioSesionBinding
import com.example.grouptwo.databinding.ActivityPantallaInicialBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class InicioSesionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInicioSesionBinding
    private lateinit var auth: FirebaseAuth
    val context: Context = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        binding.botonInicioSesion.setOnClickListener {
            val correo = binding.TextoUsername.text.toString()
            val pass = binding.TextoPassword.text.toString()
            if(correo.isNotEmpty() && pass.isNotEmpty()){
                loginValidation(correo, pass)
            } else {
                Toast.makeText(
                    baseContext,
                    "Debe ingresar correo y contraseña.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

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
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intentPantallaPrincipal = Intent(context, PantallaInicialActivity::class.java)
            startActivity(intentPantallaPrincipal)
        }
    }

    fun crearUsuario(
        correo: String,
        pass:String
    ){
        auth.createUserWithEmailAndPassword(correo, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intentLogin =
                        Intent(context, InicioSesionActivity::class.java)
                    startActivity(intentLogin)

                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    fun loginValidation(
        correo: String,
        pass: String
    ) {
        auth.signInWithEmailAndPassword(correo, pass).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val intent = Intent(this, PantallaInicialActivity::class.java).apply {
                }
                startActivity(intent)
            } else {
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }


}