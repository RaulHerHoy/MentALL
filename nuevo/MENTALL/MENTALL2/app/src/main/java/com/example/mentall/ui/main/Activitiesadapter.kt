package com.example.mentall.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mentall.data.models.Activity
import com.example.mentall.databinding.ItemActivityBinding

class ActivitiesAdapter(
    private var activities: List<Activity>,
    private val onStartClick: (Activity) -> Unit
) : RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder>() {

    class ActivityViewHolder(val binding: ItemActivityBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemActivityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]

        holder.binding.apply {
            tvTitle.text = activity.titulo
            tvCategory.text = activity.categoria
            tvDescription.text = activity.descripcion
            chipDuration.text = "${activity.duracionMin} min"

            btnStart.setOnClickListener {
                onStartClick(activity)
            }
        }
    }

    override fun getItemCount() = activities.size

    fun updateActivities(newActivities: List<Activity>) {
        activities = newActivities
        notifyDataSetChanged()
    }
} 