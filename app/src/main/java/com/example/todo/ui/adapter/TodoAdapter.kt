package com.example.todo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ItemTodoBinding
import com.example.todo.model.todo.allListResponse.Todo

class TodoAdapter() : ListAdapter<Todo, TodoAdapter.Vholder>(TodoDiffCallBack()) {
    class Vholder(val binding: ItemTodoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vholder {
        return Vholder(ItemTodoBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: Vholder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            tvTodo.text = item.description
            checkBox.isChecked = item.completed
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                onCheckListener?.let {
                    it(item,isChecked)
                }
            }

        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {click->
                click(item)
            }
        }
//        holder.binding.tvTodo.text = item.description
//        holder.binding.checkBox.isChecked = item.completed
    }

    class TodoDiffCallBack : DiffUtil.ItemCallback<Todo>(){
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }

    }

    private var onItemClickListener: ((Todo) -> Unit)? = null
    fun setOnItemClickListener(listener: (Todo) -> Unit) {
        onItemClickListener = listener
    }

    private var onCheckListener: ((Todo, Boolean) -> Unit)? = null
    fun setOnCheckListener(listener: (Todo, Boolean) -> Unit) {
        onCheckListener = listener
    }

}