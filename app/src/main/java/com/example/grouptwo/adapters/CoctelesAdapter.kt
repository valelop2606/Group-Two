package com.example.grouptwo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grouptwo.R
import com.example.grouptwo.dataclases.Coctel

class CoctelesAdapter(
    private val cocteles: List<Coctel>,
    private val onClick: (Coctel) -> Unit
) : RecyclerView.Adapter<CoctelesAdapter.CoctelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoctelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coctel, parent, false)
        return CoctelViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoctelViewHolder, position: Int) {
        holder.bind(cocteles[position])
        holder.itemView.setOnClickListener {
            onClick(cocteles[position])
        }

    }

    override fun getItemCount() = cocteles.size

    class CoctelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgCoctel: ImageView = itemView.findViewById(R.id.imgCoctel)
        private val txtNombre: TextView = itemView.findViewById(R.id.txtNombreCoctel)
        private val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcionCoctel)

        fun bind(c: Coctel) {
            txtNombre.text = c.nombre
            txtDescripcion.text = c.descripcion
            val contexto = itemView.context
            val resId = contexto.resources.getIdentifier(c.imagen, "drawable", contexto.packageName)
            if (resId != 0) imgCoctel.setImageResource(resId)
            else imgCoctel.setImageResource(R.drawable.img_margarita)
        }
    }
}
