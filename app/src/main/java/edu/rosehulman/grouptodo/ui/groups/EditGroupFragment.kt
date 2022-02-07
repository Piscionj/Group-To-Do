package edu.rosehulman.grouptodo.ui.groups

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.grouptodo.R
import edu.rosehulman.grouptodo.databinding.FragmentAddGroupBinding
import edu.rosehulman.grouptodo.model.Group
import edu.rosehulman.grouptodo.model.GroupsViewModel


class EditGroupFragment : Fragment() {

    private lateinit var model: GroupsViewModel
    private lateinit var binding: FragmentAddGroupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model =
            ViewModelProvider(requireActivity()).get(GroupsViewModel::class.java)

        // Inflate the layout for this fragment
        binding = FragmentAddGroupBinding.inflate(inflater, container, false)

        updateView()
        setupButtons()

        return binding.root
    }

    private fun setupButtons(){
        binding.cancelGroupButton.setOnClickListener {
            findNavController().navigate(R.id.nav_groups)
        }

        binding.saveGroupButton.setOnClickListener {
            Log.d("GTD", "About to call edit group")
            val newName = binding.groupNameEditText.text.toString()
            model.updateCurrentItem(newName)
            findNavController().navigate(R.id.nav_groups)
        }
    }



    private fun updateView() {
        binding.groupNameEditText.setText(model.getCurrent().name)
        //binding.userNameEditText.setText(model.getCurrent().user)
    }

}