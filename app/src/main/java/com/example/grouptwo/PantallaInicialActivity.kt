package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grouptwo.databinding.ActivityPantallaInicialBinding

class PantallaInicialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPantallaInicialBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textview1: TextView = findViewById(R.id.textView_cosmopolitan)
        val botonCosmopolitan: ImageButton = findViewById(R.id.imagen_cosmopolitan)
        botonCosmopolitan.setOnClickListener { textview1.text = "Hola" }

        binding.botonCategorias.setOnClickListener {
            val intent = Intent(this, CategoriasActivity::class.java)
            startActivity(intent)
        }
        binding.btnMojito.setOnClickListener {
            val intent = Intent(this, VerRecetaDetalladaActivity::class.java)
            startActivity(intent)
        }


    }




}