package com.example.credust

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.credust.data.ProjectDataClass
import com.example.credust.databinding.CardviewBinding

class CardViewAdapter : RecyclerView.Adapter<CardViewAdapter.CardViewViewHolder>() {
    private var listProjects = ArrayList<ProjectDataClass>()

    fun setProjects(projects: List<ProjectDataClass>?) {
        if (projects == null) return
        this.listProjects.clear()
        this.listProjects.addAll(projects)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewAdapter.CardViewViewHolder {
        val cardViewBinding = CardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewViewHolder(cardViewBinding)
    }

    override fun onBindViewHolder(holder: CardViewAdapter.CardViewViewHolder, position: Int) {
        val project = listProjects[position]
        holder.bind(project)
    }

    override fun getItemCount(): Int = listProjects.size

    inner class CardViewViewHolder(private val binding: CardviewBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(project: ProjectDataClass) {
            with(binding)
            {
                nameTitle.text = project.title
                cardBg.setImageResource(
                        itemView.context.resources.getIdentifier(
                                project.image.substring(
                                        1
                                ), null, itemView.context.packageName
                        ))
                pointsText.text = project.points.toString()

            }
            itemView.setOnClickListener {
                val detailFragment = DetailFragment.newInstance()
                val bundle = Bundle()
                bundle.putParcelable("project", project)
                detailFragment.arguments = bundle
                val activity = itemView.context as AppCompatActivity
                val fragmentManager = activity.supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.frame_layout,detailFragment)
                    .addToBackStack(null)
                    .commit() }
        }

    }
}