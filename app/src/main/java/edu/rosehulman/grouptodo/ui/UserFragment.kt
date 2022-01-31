package edu.rosehulman.grouptodo.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.grouptodo.R
import edu.rosehulman.grouptodo.databinding.FragmentHomeBinding
import edu.rosehulman.grouptodo.databinding.FragmentUserBinding

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        _binding = FragmentUserBinding.inflate(inflater, container, false)

        binding.logoutButton.setOnClickListener {
//            Firebase.auth.signOut()
//            userModel.user = null
        }
        binding.editButton.setOnClickListener {
//            findNavController().navigate(R.id.navigation_user_edit)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}