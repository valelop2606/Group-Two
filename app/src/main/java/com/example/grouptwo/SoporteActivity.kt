package com.example.grouptwo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.grouptwo.databinding.ActivitySoporteBinding

class SoporteActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoporteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoporteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.soporteFlechaAtras.setOnClickListener {
            finish()
        }

        binding.cardEnviarComentario.setOnClickListener {
            val comentario = binding.EditTextComentario.text.toString()

            if (comentario.isNotEmpty()) {
                binding.EditTextComentario.text.clear()
                Toast.makeText(
                    this,
                    "Se envió el comentario, procesaremos tu solicitud",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, "Por favor escribe un comentario", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
