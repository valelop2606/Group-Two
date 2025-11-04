package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grouptwo.adapters.Categoria
import com.example.grouptwo.adapters.CategoriasAdapter

class CategoriasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorias)

        val btnBack = findViewById<ImageButton>(R.id.categorias_flecha_atras)
        val rv = findViewById<RecyclerView>(R.id.rvCategorias)

        btnBack.setOnClickListener { finish() }

        val categorias = listOf(
            // Usa tus mipmaps/imagenes. Reutilicé algunos nombres de tu layout previo:
            Categoria("Clasico", R.mipmap.clasicos_foreground),
            Categoria("Moderno_Complejo", R.mipmap.creativosfinal_foreground),
            Categoria("Tropical", R.mipmap.tropical_foreground),
            Categoria("Refrescante", R.mipmap.refrescante_foreground),
            Categoria("Dulce", R.mipmap.dulces_foreground),
            Categoria("Acido_Citrico", R.mipmap.citrico_foreground),
            Categoria("Amargo", R.mipmap.amargo_foreground),
            Categoria("Espumoso_Burbujeante", R.mipmap.espumoso_foreground),
            Categoria("Cafe_Desayuno", R.mipmap.cafe_foreground),
            Categoria("Aperitivo", R.mipmap.aperitivo_foreground)
        ).distinctBy { it.nombre }

        val adapter = CategoriasAdapter { cat ->
            val intent = Intent(this, CoctelesPorCategoriaActivity::class.java)
            intent.putExtra("categoria", cat.nombre)
            startActivity(intent)
        }

        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = adapter
        adapter.submitList(categorias)
    }
}
