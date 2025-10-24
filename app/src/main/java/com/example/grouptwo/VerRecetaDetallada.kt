package com.example.grouptwo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.grouptwo.databinding.ActivityVerRecetaDetalladaBinding

class VerRecetaDetalladaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerRecetaDetalladaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerRecetaDetalladaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.categoriasFlechaAtras.setOnClickListener {
            finish()
        }
    }
}
