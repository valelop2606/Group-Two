package com.example.grouptwo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grouptwo.databinding.ActivityVerRecetaDetalladaBinding
import com.example.grouptwo.repository.CoctelRepositori
import com.example.grouptwo.Coctel

class VerRecetaDetalladaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerRecetaDetalladaBinding
    private val ingredientesAdapter = IngredientesAdapter()
    private val pasosAdapter = PasosAdapter()

    companion object {
        private const val EXTRA_ID = "cocktail_id"

        fun launch(context: Context, cocktailId: String) {
            val i = Intent(context, VerRecetaDetalladaActivity::class.java)
                .putExtra(EXTRA_ID, cocktailId)
            if (context !is Activity) i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityVerRecetaDetalladaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvIngredientes.apply {
            layoutManager = LinearLayoutManager(this@VerRecetaDetalladaActivity)
            adapter = ingredientesAdapter
            isNestedScrollingEnabled = false
        }

        binding.rvPasos.apply {
            layoutManager = LinearLayoutManager(this@VerRecetaDetalladaActivity)
            adapter = pasosAdapter
            isNestedScrollingEnabled = false
        }
        val coctelId = intent.getStringExtra(EXTRA_ID)
        val coctel: Coctel? = coctelId?.let { CoctelRepositori.getById(this, it) }
            ?: CoctelRepositori.getAll(this).firstOrNull()

        if (coctel == null) {
            finish()
            return
        }

        setupUI(coctel)

        binding.categoriasFlechaAtras.setOnClickListener { finish() }
    }

    private fun setupUI(c: Coctel) {
        binding.tvTitulo.text = c.nombre
        ingredientesAdapter.submitList(c.ingredientes)
        pasosAdapter.submitList(c.pasos.sortedBy { it.n })
    }

}
