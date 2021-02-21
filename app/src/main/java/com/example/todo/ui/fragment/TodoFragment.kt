package com.example.todo.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.databinding.FragmentTodoBinding
import com.example.todo.model.todo.TodoRequest
import com.example.todo.other.EventObserver
import com.example.todo.other.Status
import com.example.todo.ui.activity.AuthActivity
import com.example.todo.ui.adapter.TodoAdapter
import com.example.todo.viewmodel.TodoViewModel
import kotlinx.coroutines.launch

class TodoFragment : Fragment() {
//    @Inject
//    lateinit var userPreferences: UserPreferences
    private lateinit var binding: FragmentTodoBinding
    private lateinit var viewModel: TodoViewModel
    lateinit var todoAdapter: TodoAdapter
    private var token : String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTodoBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)

        setupRecyclerview()
        getTokenFetchTodoList()
        observeTodoList()
        observeAddUpdateResponse()

        binding.fab.setOnClickListener {
            findNavController().navigate(TodoFragmentDirections.actionTodoFragmentToDetailFragment("NEW"))
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                viewModel.logout(token)
                Intent(requireContext(), AuthActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    requireActivity().startActivity(it)
                }
            }
        }
        return binding.root
    }

    private fun getTokenFetchTodoList() {
        viewModel.getAuthToken()
        viewModel.authToken.observe(viewLifecycleOwner, Observer {
            token = it
            viewModel.getTodoList(token, "application/json")
        })
    }

    private fun observeAddUpdateResponse() {
        viewModel.addUpdateResponse.observe(viewLifecycleOwner, EventObserver {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "Updated!", Toast.LENGTH_SHORT).show()
                }
                else -> {

                    Toast.makeText(requireContext(), "Can't update", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeTodoList() {
        viewModel.allTodoResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    val list = it.data
                    todoAdapter.submitList(list)
                }
            }
        })
    }

    private fun setupRecyclerview() {
        todoAdapter = TodoAdapter()
        binding.rv.adapter = todoAdapter
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        todoAdapter.setOnItemClickListener {
            findNavController().navigate(
                TodoFragmentDirections.actionTodoFragmentToDetailFragment(
                    it._id
                )
            )
        }
        todoAdapter.setOnCheckListener { todo, isChecked ->
            val item = TodoRequest(isChecked, null)
            viewModel.updateTodoByID(token, todo._id, item)
        }
    }


}