package com.example.mentall.ui.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mentall.data.models.Mood
import com.example.mentall.databinding.ItemMoodHistoryBinding
import com.example.mentall.utils.Constants

class MoodHistoryAdapter(
    private var items: List<Mood>
) : RecyclerView.Adapter<MoodHistoryAdapter.VH>() {

    inner class VH(val b: ItemMoodHistoryBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMoodHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val b = holder.b

        val label = Constants.MOOD_LABELS[item.valor] ?: "Desconocido"
        b.tvTitle.text = "$label (${item.valor}/5)"

        val act = item.actividadRealizada ?: "—"
        val nota = item.nota ?: "—"
        b.tvSubtitle.text = "Actividad: $act | Nota: $nota"

        b.tvDate.text = item.fecha ?: ""

        val uriStr = item.imagenUri
        if (!uriStr.isNullOrBlank()) {
            try {
                b.img.setImageURI(Uri.parse(uriStr))
            } catch (_: Exception) {
                b.img.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        } else {
            b.img.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<Mood>) {
        items = newItems
        notifyDataSetChanged()
    }
}
