package com.example.grouptwo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grouptwo.databinding.ActivityVerRecetaDetalladaBinding

class VerRecetaDetalladaActivity : AppCompatActivity() {

    private lateinit var rvIngredientes: RecyclerView
    private lateinit var rvPasos: RecyclerView
    private val ingredientesAdapter = IngredientesAdapter()
    private val pasosAdapter = PasosAdapter()
    private lateinit var binding: ActivityVerRecetaDetalladaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_receta_detallada)

        binding = ActivityVerRecetaDetalladaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvIngredientes = findViewById(R.id.rvIngredientes)
        rvPasos = findViewById(R.id.rvPasos)

        rvIngredientes.layoutManager = LinearLayoutManager(this)
        rvPasos.layoutManager = LinearLayoutManager(this)

        rvIngredientes.adapter = ingredientesAdapter
        rvPasos.adapter = pasosAdapter

        // 3) Datos de prueba
        ingredientesAdapter.submitList(
            listOf(
                Ingrediente("Limón", "2 u"),
                Ingrediente("Azúcar", "30 g"),
                Ingrediente("Hielo", "a gusto"),
                Ingrediente("Ron","2 onzas"),
                Ingrediente("Four","3 latas"),
                Ingrediente("cuba libre","3 litros"),
                Ingrediente("Azucar","a gusto")
            )
        )
        pasosAdapter.submitList(
            listOf(
                Paso(1, "Majar la menta con el azúcar."),
                Paso(2, "Agregar jugo de limón."),
                Paso(3, "Añadir hielo y completar con soda.")
            )
        )
        binding.categoriasFlechaAtras.setOnClickListener {
            finish()
        }
    }
}
