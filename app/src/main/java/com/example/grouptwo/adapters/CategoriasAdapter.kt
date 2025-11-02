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

data class Categoria(val nombre: String, val iconRes: Int)

class CategoriasAdapter(
    private val onClick: (Categoria) -> Unit
) : ListAdapter<Categoria, CategoriasAdapter.CategoriaVH>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return CategoriaVH(v, onClick)
    }

    override fun onBindViewHolder(holder: CategoriaVH, position: Int) {
        holder.bind(getItem(position))
    }

    class CategoriaVH(itemView: View, val onClick: (Categoria) -> Unit)
        : RecyclerView.ViewHolder(itemView) {

        private val img: ImageButton = itemView.findViewById(R.id.imgCategoria)
        private val txt: TextView = itemView.findViewById(R.id.txtCategoria)

        fun bind(item: Categoria) {
            txt.text = item.nombre
            img.setImageResource(item.iconRes)
            img.contentDescription = item.nombre
            itemView.setOnClickListener { onClick(item) }
            img.setOnClickListener { onClick(item) }
        }
    }

    class Diff : DiffUtil.ItemCallback<Categoria>() {
        override fun areItemsTheSame(o: Categoria, n: Categoria) = o.nombre == n.nombre
        override fun areContentsTheSame(o: Categoria, n: Categoria) = o == n
    }
}
