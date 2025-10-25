package com.example.grouptwo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class IngredientesAdapter :
    ListAdapter<Ingrediente, IngredientesAdapter.VH>(Diff()) {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.imgIcono)
        val nombre = itemView.findViewById<TextView>(R.id.txtNombre)
        val cantidad = itemView.findViewById<TextView>(R.id.txtCantidad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingrediente, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.nombre.text = item.nombre
        holder.cantidad.text = item.cantidad
    }

    private class Diff : DiffUtil.ItemCallback<Ingrediente>() {
        override fun areItemsTheSame(a: Ingrediente, b: Ingrediente) =
            a.nombre == b.nombre
        override fun areContentsTheSame(a: Ingrediente, b: Ingrediente) = a == b
    }
}
