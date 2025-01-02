package com.example.kotlintodopractice.utils.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlintodopractice.R
import com.example.kotlintodopractice.databinding.EachTodoItemBinding
import com.example.kotlintodopractice.utils.model.ToDoData

class TaskAdapter(private val list: MutableList<ToDoData>,  private val onEditClick: (ToDoData) -> Unit,
                  private val onDeleteClick: (ToDoData) -> Unit) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val selectedLots = mutableListOf<ToDoData>()

    fun getSelectedLots(): List<ToDoData> = selectedLots

    class TaskViewHolder(val binding: EachTodoItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = EachTodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }


    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val lot = list[position]
        with(holder.binding) {
            todoTask.text = "Desc: ${lot.DESCRIPTION}"
            todoTask2.text = "Lot ID: ${lot.LOT_ID}\nLot No: ${lot.LOT_NO}\nReg No: ${lot.REG_NO}"

            Glide.with(taskImageView.context)
                .load(lot.imageUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(taskImageView)




//            editButton.setOnClickListener {
//                onEditClick(lot)
//            }

            deleteButton.setOnClickListener {
                onDeleteClick(lot)
            }
        }
    }



    override fun getItemCount(): Int = list.size

    fun updateTasks(newTasks: List<ToDoData>) {
        list.clear()
        list.addAll(newTasks)
        notifyDataSetChanged()
    }

    fun clearTasks() {
        list.clear()
        notifyDataSetChanged()
    }
}
