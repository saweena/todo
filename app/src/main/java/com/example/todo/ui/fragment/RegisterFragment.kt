package com.example.todo.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentLoginBinding
import com.example.todo.databinding.FragmentRegisterBinding
import com.example.todo.databinding.FragmentTodoBinding
import com.example.todo.model.auth.AuthRequest
import com.example.todo.other.EventObserver
import com.example.todo.other.Status
import com.example.todo.ui.activity.TodoActivity
import com.example.todo.viewmodel.TodoViewModel

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: TodoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)


        registerButtonListener()
        observeRegisterResponse()


        return binding.root
    }

    private fun clearEditText() {
        binding.apply {
            edAge.text.clear()
            edEmail.text.clear()
            edName.text.clear()
            edPass1.text.clear()
            edPass2.text.clear()
        }
    }

    private fun observeRegisterResponse() {
        viewModel.registerResponse.observe(viewLifecycleOwner, EventObserver {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "Register completed", Toast.LENGTH_SHORT)
                        .show()
                    it.data?.token?.let { token ->
                        viewModel.saveToken(token)

                        Intent(requireContext(), TodoActivity::class.java).also {
                            it.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            requireActivity().startActivity(it)
                        }
                    }
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Register error. Please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun registerButtonListener() {
        binding.btnSignup.setOnClickListener {
            val name = binding.edName.text.toString()
            val email = binding.edEmail.text.toString()
            val pass1 = binding.edPass1.text.toString()
            val pass2 = binding.edPass2.text.toString()
            val age = binding.edAge.text.toString()
            if (age.isEmpty() || email.isEmpty() || name.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
                Toast.makeText(requireContext(), "Please complete all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (pass1 != pass2) {
                Toast.makeText(requireContext(), "Password are not matching", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (!email.contains("@") || !email.contains(".")) {
                Toast.makeText(requireContext(), "Check your email again", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            viewModel.register(AuthRequest(age.toInt(), email, name, pass1))
        }
    }

    override fun onStop() {
        super.onStop()
        clearEditText()
    }

}