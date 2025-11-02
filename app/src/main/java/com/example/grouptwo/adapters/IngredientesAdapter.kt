package com.example.grouptwo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grouptwo.databinding.ItemIngredienteBinding
import com.example.grouptwo.dataclases.Ingrediente

class IngredientesAdapter :
    RecyclerView.Adapter<IngredientesAdapter.IngredienteViewHolder>() {

    private val dataCards = mutableListOf<Ingrediente>()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredienteViewHolder {
        context = parent.context
        return IngredienteViewHolder(
            ItemIngredienteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: IngredienteViewHolder, position: Int) {
        holder.bind(dataCards[position])
    }

    override fun getItemCount(): Int = dataCards.size

    inner class IngredienteViewHolder(private val binding: ItemIngredienteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Ingrediente) {
            binding.txtNombre.text = data.nombre
            binding.txtCantidad.text = data.cantidad?.toString()?: ""
        }
    }

    fun addDataCards(list: List<Ingrediente>) {
        dataCards.clear()
        dataCards.addAll(list)
    }


}