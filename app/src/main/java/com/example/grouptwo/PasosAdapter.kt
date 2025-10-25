package com.example.grouptwo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class PasosAdapter :
    ListAdapter<Paso, PasosAdapter.VH>(Diff()) {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val indice = itemView.findViewById<TextView>(R.id.txtIndice)
        val descripcion = itemView.findViewById<TextView>(R.id.txtDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_paso, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.indice.text = "${item.indice}."
        holder.descripcion.text = item.descripcion
    }

    private class Diff : DiffUtil.ItemCallback<Paso>() {
        override fun areItemsTheSame(a: Paso, b: Paso) = a.indice == b.indice
        override fun areContentsTheSame(a: Paso, b: Paso) = a == b
    }
}
