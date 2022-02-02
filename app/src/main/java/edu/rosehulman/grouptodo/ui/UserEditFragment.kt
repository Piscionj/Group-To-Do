package edu.rosehulman.grouptodo.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.grouptodo.R
import edu.rosehulman.grouptodo.databinding.FragmentUserEditBinding
import edu.rosehulman.grouptodo.model.UserViewModel

class UserEditFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        val binding = FragmentUserEditBinding.inflate(inflater, container, false)
        binding.userEditDoneButton.setOnClickListener {
            // Save user info into Firestore.
            val newAgeString = binding.userEditAgeEditText.text.toString()
            userModel.update(
                newName=binding.userEditNameEditText.text.toString(),
                newAge=if (newAgeString.isNotBlank()) newAgeString.toInt() else -1,
                newMajor=binding.userEditMajorEditText.text.toString(),
                newHasCompletedSetup=true
            )
            findNavController().navigate(R.id.nav_user)
        }

        userModel.getOrMakeUser {
            with(userModel.user!!) {
                binding.userEditNameEditText.setText(name)
                binding.userEditAgeEditText.setText(age.toString())
                binding.userEditMajorEditText.setText(major)
            }
        }
        return binding.root
    }

}