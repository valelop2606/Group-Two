package com.example.grouptwo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grouptwo.databinding.ListaDeComprasBinding

class ListaDeCompras : AppCompatActivity() {

    private lateinit var binding: ListaDeComprasBinding
    private val ingredientesAdapter = IngredientesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ListaDeComprasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView
        binding.rvIngredientes.layoutManager = LinearLayoutManager(this)
        binding.rvIngredientes.setHasFixedSize(true)
        binding.rvIngredientes.adapter = ingredientesAdapter



        binding.categoriasFlechaAtras.setOnClickListener { finish() }
    }
}
