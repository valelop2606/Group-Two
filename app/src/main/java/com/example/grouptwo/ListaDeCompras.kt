package com.example.grouptwo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grouptwo.adapters.IngredientesAdapter
import com.example.grouptwo.databinding.ListaDeComprasBinding
import com.example.grouptwo.dataclases.Coctel
import com.example.grouptwo.dataclases.CoctelesDatabase
import com.example.grouptwo.dataclases.Ingrediente
import kotlinx.serialization.json.Json
import java.text.Normalizer

data class CoctelConCantidad(
    val coctel: Coctel,
    val cantidadAPreparar: Int
)
class ListaDeCompras : AppCompatActivity() {

    private lateinit var binding: ListaDeComprasBinding
    private val ingredientesAdapter = IngredientesAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListaDeComprasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvIngredientes.layoutManager = LinearLayoutManager(this)
        binding.rvIngredientes.setHasFixedSize(true)
        binding.rvIngredientes.adapter = ingredientesAdapter

        val coctelesCalculadora: List<CoctelCalculadora> =
            intent.getSerializableExtra("coctelesConCantidad") as? List<CoctelCalculadora> ?: emptyList()

        val todosLosCocteles: List<Coctel> = cargarTodosLosCocteles()

        val coctelesAComprar = coctelesCalculadora.mapNotNull { calc ->
            val coctelBase = todosLosCocteles.find { it.id == calc.id }
            if (coctelBase != null) CoctelConCantidad(coctelBase, calc.cantidad) else null
        }

        val listaDeCompras = crearListaDeCompras(coctelesAComprar)

        ingredientesAdapter.addDataCards(listaDeCompras)

        binding.categoriasFlechaAtras.setOnClickListener { finish() }
    }

    private fun cargarTodosLosCocteles(): List<Coctel> {
        val txt = assets.open("cocteles.json").bufferedReader().use { it.readText() }
        val db = Json.decodeFromString<CoctelesDatabase>(txt)
        return db.cocteles
    }



    private fun canon(nombre: String): String {
        var n = nombre.lowercase().trim()
        n = Normalizer.normalize(n, Normalizer.Form.NFD)
        n = n.replace("\\p{M}+".toRegex(), "")
        n = n.replace("\\s+".toRegex(), " ")
        n = n.removePrefix("zumo de ").removePrefix("jugo de ")
        return n
    }

    private fun mismoIngrediente(a: Ingrediente, b: Ingrediente): Boolean {
        return canon(a.nombre) == canon(b.nombre) &&
                a.unidad.lowercase().trim() == b.unidad.lowercase().trim()
    }

    fun crearListaDeCompras(coctelesAComprar: List<CoctelConCantidad>): List<Ingrediente> {
        val resultado = mutableListOf<Ingrediente>()

        for (prep in coctelesAComprar) {
            val cantidadCoctel = prep.cantidadAPreparar
            for (ing in prep.coctel.ingredientes) {
                val cantidadBase = ing.cantidad ?: 0.0
                val cantidadTotal = cantidadBase * cantidadCoctel

                val ingredienteTotalizado = ing.copy(cantidad = cantidadTotal)

                var unido = false
                for (i in 0 until resultado.size) {
                    val actual = resultado[i]
                    if (mismoIngrediente(actual, ingredienteTotalizado)) {
                        val nuevaCantidad = if (actual.cantidad != null && ingredienteTotalizado.cantidad != null)
                            actual.cantidad + ingredienteTotalizado.cantidad
                        else
                            actual.cantidad ?: ingredienteTotalizado.cantidad

                        resultado[i] = actual.copy(cantidad = nuevaCantidad)
                        unido = true
                        break
                    }
                }

                if (!unido) {
                    resultado.add(
                        Ingrediente(
                            nombre = canon(ing.nombre).replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase() else it.toString()
                            },
                            cantidad = cantidadTotal,
                            unidad = ing.unidad,
                            nota = ing.nota
                        )
                    )
                }
            }
        }

        resultado.sortWith(compareBy<Ingrediente> { canon(it.nombre) }.thenBy { it.unidad.lowercase() })
        return resultado
    }
}
