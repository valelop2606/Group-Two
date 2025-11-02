package com.example.grouptwo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grouptwo.databinding.ItemPasoBinding
import com.example.grouptwo.dataclases.Paso

class PasosAdapter :
    RecyclerView.Adapter<PasosAdapter.PasoViewHolder>() {

    private val dataCards = mutableListOf<Paso>()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasoViewHolder {
        context = parent.context
        return PasoViewHolder(
            ItemPasoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PasoViewHolder, position: Int) {
        holder.bind(dataCards[position])
    }

    override fun getItemCount(): Int = dataCards.size

    inner class PasoViewHolder(private val binding: ItemPasoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Paso) {
            binding.txtNumero.text = data.n.toString()
            binding.txtDescripcion.text = data.texto
        }
    }

    fun addDataCards(list: List<Paso>) {
        dataCards.clear()
        dataCards.addAll(list)
    }


}