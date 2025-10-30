//package com.example.grouptwo
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.grouptwo.databinding.ActivityVerRecetaDetalladaBinding
//import com.example.grouptwo.repository.CoctelRepositori
//
//class VerRecetaDetalladaActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityVerRecetaDetalladaBinding
//    private val ingredientesAdapter = IngredientesAdapter()
//    private val pasosAdapter = PasosAdapter()
//
//    companion object {
//        private const val EXTRA_ID = "cocktail_id"
//
//        fun launch(context: Context, cocktailId: String) {
//            val i = Intent(context, VerRecetaDetalladaActivity::class.java)
//                .putExtra(EXTRA_ID, cocktailId)
//            if (context !is Activity) i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(i)
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityVerRecetaDetalladaBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setupRecyclerViews()
//        loadCocktailData()
//        setupListeners()
//    }
//
//    private fun setupRecyclerViews() {
//        binding.rvIngredientes.apply {
//            layoutManager = LinearLayoutManager(this@VerRecetaDetalladaActivity)
//            adapter = ingredientesAdapter
//            isNestedScrollingEnabled = false
//        }
//
//        binding.rvPasos.apply {
//            layoutManager = LinearLayoutManager(this@VerRecetaDetalladaActivity)
//            adapter = pasosAdapter
//            isNestedScrollingEnabled = false
//        }
//    }
//
//    private fun loadCocktailData() {
//        val coctelId = intent.getStringExtra(EXTRA_ID)
//        // Nota: Asumiendo que CoctelDetalle es la clase correcta en tu paquete.
//        val coctel = coctelId?.let {
//            CoctelRepositori.getById(this, it)
//        } ?: CoctelRepositori.getAll(this).firstOrNull()
//
//        if (coctel == null) {
//            Toast.makeText(this, "Cóctel no encontrado", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//
//        setupUI(coctel)
//    }
//
//    private fun setupUI(coctel: com.example.grouptwo.models.CoctelDetalle) {
//        binding.tvTitulo.text = coctel.nombre
//
//        // Mostrar ingredientes (si existen)
//        coctel.ingredientes?.let { ingredientes ->
//            ingredientesAdapter.submitList(ingredientes)
//        } ?: run {
//            ingredientesAdapter.submitList(emptyList())
//        }
//
//        // Mostrar pasos ordenados (si existen)
//        coctel.pasos?.let { pasos ->
//            pasosAdapter.submitList(pasos.sortedBy { it.n })
//        } ?: run {
//            pasosAdapter.submitList(emptyList())
//        }
//
//        // TODO: Actualizar los badges (tiempo, sabor, nivel) con los datos de 'coctel'
//    }
//
//    private fun setupListeners() {
//        // CORREGIDO: Usando el ID correcto 'btnBack'
//        binding.btnBack.setOnClickListener {
//            finish()
//        }
//
//        // Lógica de la barra de navegación inferior
//        binding.navInicio.setOnClickListener {
//            Toast.makeText(this, "Navegando a Inicio", Toast.LENGTH_SHORT).show()
//            // Aquí iría el Intent a la Activity de Inicio
//        }
//        binding.navBuscar.setOnClickListener {
//            Toast.makeText(this, "Navegando a Buscar", Toast.LENGTH_SHORT).show()
//            // Aquí iría el Intent a la Activity de Búsqueda
//        }
//        binding.navCalculadora.setOnClickListener {
//            Toast.makeText(this, "Navegando a Calculadora", Toast.LENGTH_SHORT).show()
//            // Aquí iría el Intent a la Activity de Calculadora
//        }
//        binding.navPerfil.setOnClickListener {
//            Toast.makeText(this, "Navegando a Perfil", Toast.LENGTH_SHORT).show()
//            // Aquí iría el Intent a la Activity de Perfil
//        }
//
//        // Botón ver video
//        binding.btnVideo.setOnClickListener {
//            val coctelId = intent.getStringExtra(EXTRA_ID)
//            val coctel = coctelId?.let { CoctelRepositori.getById(this, it) }
//
//            coctel?.urlVideo?.let { url ->
//                try {
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                    startActivity(intent)
//                } catch (e: Exception) {
//                    Toast.makeText(
//                        this,
//                        "No se pudo abrir el video",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            } ?: run {
//                Toast.makeText(
//                    this,
//                    "Video no disponible",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
//}