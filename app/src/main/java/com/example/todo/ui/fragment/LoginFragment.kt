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
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentLoginBinding
import com.example.todo.model.auth.AuthRequest
import com.example.todo.other.Status
import com.example.todo.ui.activity.TodoActivity
import com.example.todo.viewmodel.TodoViewModel
import timber.log.Timber

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: TodoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)

        binding.btnLogin.setOnClickListener {

            val email = binding.edUser.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()
            val user = AuthRequest(age = null,email = email,name = null,password = password)
            viewModel.login(user)

        }

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            Timber.d("message = ${it.message}, data = ${it.data}, status = ${it.status}")
            when(it.status){
                Status.SUCCESS-> {
                    //Toast.makeText(requireContext(),"token =${it.data?.token}",Toast.LENGTH_SHORT).show()
                    val token = it.data?.token!!
                    Timber.d("token=${token}")
                    viewModel.saveToken(token)

                    Intent(requireContext(),TodoActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        requireActivity().startActivity(it)
                    }
                }
                Status.ERROR-> {Toast.makeText(requireContext(),"please login again",Toast.LENGTH_SHORT).show()}
            }
        })

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }


}