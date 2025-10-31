package com.example.grouptwo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grouptwo.R
import com.example.grouptwo.dataclases.Paso

class PasosAdapter : ListAdapter<Paso, PasosAdapter.PasoViewHolder>(PasoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_paso, parent, false)
        return PasoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PasoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtIndice: TextView = itemView.findViewById(R.id.txtIndice)
        private val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcion)
        private val btnEliminarPaso: ImageButton = itemView.findViewById(R.id.btnEliminarPaso)

        fun bind(paso: Paso) {
            txtIndice.text = "${paso.n}."
            txtDescripcion.text = paso.texto

            // Por ahora ocultamos el botón eliminar en la vista de receta
            // Solo lo mostraríamos en una pantalla de edición
            btnEliminarPaso.visibility = View.GONE
        }
    }

    class PasoDiffCallback : DiffUtil.ItemCallback<Paso>() {
        override fun areItemsTheSame(oldItem: Paso, newItem: Paso): Boolean {
            return oldItem.n == newItem.n
        }

        override fun areContentsTheSame(oldItem: Paso, newItem: Paso): Boolean {
            return oldItem == newItem
        }
    }
}