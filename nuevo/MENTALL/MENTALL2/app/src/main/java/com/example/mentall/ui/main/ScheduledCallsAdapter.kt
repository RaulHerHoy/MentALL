package com.example.mentall.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mentall.data.models.ScheduledCall
import com.example.mentall.databinding.ItemScheduledCallBinding

class ScheduledCallsAdapter(
    private var items: List<ScheduledCall>,
    private val onToggle: (ScheduledCall, Boolean) -> Unit
) : RecyclerView.Adapter<ScheduledCallsAdapter.VH>() {

    inner class VH(val binding: ItemScheduledCallBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemScheduledCallBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        // Texto principal
        holder.binding.tvDescripcion.text = buildDescripcion(item)

        // Teléfono
        holder.binding.tvTelefono.text = item.telefono

        // IMPORTANTE: evitar que el listener se dispare al reutilizar celdas
        holder.binding.switchActiva.setOnCheckedChangeListener(null)

        // Si tu modelo tiene campo "activa" úsalo; si no, ponlo a true por defecto
        val activa = tryGetActiva(item)
        holder.binding.switchActiva.isChecked = activa

        holder.binding.switchActiva.setOnCheckedChangeListener { _, isChecked ->
            onToggle(item, isChecked)
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<ScheduledCall>) {
        items = newItems
        notifyDataSetChanged()
    }

    private fun buildDescripcion(item: ScheduledCall): String {
        val dia = when (item.diaSemana) {
            null, 0 -> "Todos los días"
            1 -> "Todos los Lunes"
            2 -> "Todos los Martes"
            3 -> "Todos los Miércoles"
            4 -> "Todos los Jueves"
            5 -> "Todos los Viernes"
            6 -> "Todos los Sábados"
            7 -> "Todos los Domingos"
            else -> "Programada"
        }
        val hora = item.hora ?: "--:--"
        return "$dia a las $hora"
    }

    /**
     * Si tu data class ScheduledCall NO tiene "activa",
     * lo dejamos en true (mostrará el switch activado).
     * Si SÍ lo tiene, lo usa.
     */
    private fun tryGetActiva(item: ScheduledCall): Boolean {
        return try {
            val field = item::class.java.getDeclaredField("activa")
            field.isAccessible = true
            (field.get(item) as? Boolean) ?: true
        } catch (_: Exception) {
            true
        }
    }
}
