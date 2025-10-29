package com.example.grouptwo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grouptwo.Ingrediente
import kotlin.math.abs

class IngredientesAdapter :
    ListAdapter<Ingrediente, IngredientesAdapter.VH>(Diff()) {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgIcono)
        val nombre: TextView = itemView.findViewById(R.id.txtNombre)
        val cantidad: TextView = itemView.findViewById(R.id.txtCantidad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingrediente, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)

        holder.nombre.text = buildString {
            append(item.nombre)
            if (!item.nota.isNullOrBlank()) append(" (${item.nota})")
        }

        // Formateo robusto: soporta null (ej. "a gusto"), enteros sin .0 y decimales
        val cantidadUi = when (val c = item.cantidad) {
            null -> item.unidad // "a gusto" u otro texto en unidad
            else -> {
                val isInt = abs(c - c.toInt()) < 1e-9
                if (isInt) "${c.toInt()} ${item.unidad}" else "$c ${item.unidad}"
            }
        }
        holder.cantidad.text = cantidadUi

        // (Opcional) setea un ícono según el nombre/unidad si quieres
        // holder.img.setImageResource(...)
    }

    private class Diff : DiffUtil.ItemCallback<Ingrediente>() {
        override fun areItemsTheSame(a: Ingrediente, b: Ingrediente) =
            a.nombre == b.nombre && a.nota == b.nota  // nombre(+nota) como key estable
        override fun areContentsTheSame(a: Ingrediente, b: Ingrediente) = a == b
    }
}





