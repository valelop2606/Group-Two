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

        // Datos
        ingredientesAdapter.submitList(
            listOf(
                Ingrediente("Limón", "2 u"),
                Ingrediente("Azúcar", "30 g"),
                Ingrediente("Hielo", "a gusto"),
                Ingrediente("Ron", "2 onzas"),
                Ingrediente("Four", "3 latas"),
                Ingrediente("cuba libre", "3 litros"),
                Ingrediente("Azucar", "a gusto")
            )
        )

        binding.categoriasFlechaAtras.setOnClickListener { finish() }
    }
}
