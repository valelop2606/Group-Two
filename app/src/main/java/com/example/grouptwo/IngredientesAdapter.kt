package com.example.grouptwo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class IngredientesAdapter : ListAdapter<Ingrediente, IngredientesAdapter.IngredienteViewHolder>(
    IngredienteDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingrediente, parent, false)
        return IngredienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredienteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class IngredienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgIcono: ImageView = itemView.findViewById(R.id.imgIcono)
        private val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        private val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidad)
        private val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)

        fun bind(ingrediente: Ingrediente) {
            txtNombre.text = ingrediente.nombre

            // Formatear cantidad
            val cantidadTexto = if (ingrediente.cantidad != null) {
                "${ingrediente.cantidad} ${ingrediente.unidad}"
            } else {
                ingrediente.unidad
            }
            txtCantidad.text = cantidadTexto

            // Si hay nota, agregarla
            if (!ingrediente.nota.isNullOrBlank()) {
                txtCantidad.text = "$cantidadTexto\n${ingrediente.nota}"
            }

            // Por ahora ocultamos el botón eliminar en la vista de receta
            // Solo lo mostraríamos en una pantalla de edición
            btnEliminar.visibility = View.GONE

            // Opcional: Podrías agregar iconos diferentes según el ingrediente
            // Por ahora usamos el icono por defecto
        }
    }

    class IngredienteDiffCallback : DiffUtil.ItemCallback<Ingrediente>() {
        override fun areItemsTheSame(oldItem: Ingrediente, newItem: Ingrediente): Boolean {
            return oldItem.nombre == newItem.nombre
        }

        override fun areContentsTheSame(oldItem: Ingrediente, newItem: Ingrediente): Boolean {
            return oldItem == newItem
        }
    }
}





