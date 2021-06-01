package com.example.credust.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.credust.databinding.InstructionCardBinding

class InstructionAdapter : RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>() {
    private var listInstructions = ArrayList<String>()

    fun setInstructions(instructions: List<String>?) {
        if (instructions == null) return
        this.listInstructions.clear()
        this.listInstructions.addAll(instructions)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val instructionCardBinding =
            InstructionCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InstructionViewHolder(instructionCardBinding)
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        val instruction = listInstructions[position]
        holder.bind(instruction,position)
    }

    override fun getItemCount(): Int = listInstructions.size

    inner class InstructionViewHolder(private val binding: InstructionCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(instruction: String,position: Int) {
            with(binding)
            {
                instructionText.text = instruction
                instructionNumber.text = (position+1).toString()
            }
        }
    }
}