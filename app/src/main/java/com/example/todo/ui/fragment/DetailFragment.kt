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
import androidx.navigation.fragment.navArgs
import com.example.todo.databinding.FragmentDetailBinding
import com.example.todo.model.todo.TodoRequest
import com.example.todo.other.EventObserver
import com.example.todo.other.Status
import com.example.todo.ui.activity.AuthActivity
import com.example.todo.viewmodel.TodoViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: TodoViewModel
    private val args : DetailFragmentArgs by navArgs()
    private var token : String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)

        setUI()

        saveButtonListener()
        observeAddUpdateTodoResponse()
        deleteButtonListener()
        observeDeleteTodoResponse()


        return binding.root
    }

    private fun observeTodoResponse() {
        viewModel.todoDetailResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { todo ->
                        binding.edDetail.setText(todo.description)
                    }
                }
                Status.ERROR->{
                    if(it.message=="401"){
                        clearToken_backToLogin()
                    }else{
                        Toast.makeText(requireContext(),"Cannot load data",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun clearToken_backToLogin() {
        lifecycleScope.launch {
            viewModel.clearToken()
            Toast.makeText(requireContext(),"Session Timeout",Toast.LENGTH_SHORT).show()
            Intent(requireContext(), AuthActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                requireActivity().startActivity(it)
            }
        }
    }

    private fun setUI() {
        if (args.id == "NEW") {
            binding.apply {
                btnDelete.visibility = View.GONE
                binding.edDetail.text.clear()
            }
        } else {
            viewModel.getAuthToken()
            binding.btnDelete.visibility = View.VISIBLE
            observeTodoResponse()
        }

        viewModel.authToken.observe(viewLifecycleOwner, Observer { it ->
            token = it
            viewModel.getTodoByID(token, "application/json", args.id)
        })




    }

    private fun observeDeleteTodoResponse() {
        viewModel.deleteResponse.observe(viewLifecycleOwner, EventObserver {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "delete!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                else -> {
                    Toast.makeText(requireContext(), "cannot delete", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun deleteButtonListener() {
        binding.btnDelete.setOnClickListener {
            viewModel.deleteTodoByID(token, "application/json", args.id)
        }
    }

    private fun observeAddUpdateTodoResponse() {
        viewModel.addUpdateResponse.observe(viewLifecycleOwner, EventObserver {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "saved!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                Status.ERROR->{
                    if(it.message=="401"){
                        clearToken_backToLogin()
                    }else{
                        Toast.makeText(requireContext(), "cannot save", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {

                }
            }
        })
    }

    private fun saveButtonListener() {
        binding.btnSave.setOnClickListener {
            val todo = TodoRequest(null, description = binding.edDetail.text.toString())
            if (args.id == "NEW") {
                viewModel.addTodo(token, todo)
            } else {
                viewModel.updateTodoByID(token, args.id, todo)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        binding.edDetail.text.clear()
    }


}