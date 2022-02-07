package edu.rosehulman.grouptodo.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.grouptodo.R
import edu.rosehulman.grouptodo.databinding.FragmentUserBinding
import edu.rosehulman.grouptodo.model.UserViewModel

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
        val userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.profileName.text = userModel.user!!.name
        binding.uid.text = Firebase.auth.currentUser!!.uid
        if (userModel.user!!.storageUriString.isNotEmpty()){
            binding.centerImage.load(userModel.user!!.storageUriString){
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }

        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            userModel.user = null
        }
        binding.editButton.setOnClickListener {
            findNavController().navigate(R.id.nav_user_edit)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}