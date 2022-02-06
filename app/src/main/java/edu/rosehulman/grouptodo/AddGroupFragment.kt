package edu.rosehulman.grouptodo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.grouptodo.databinding.FragmentAddGroupBinding
import edu.rosehulman.grouptodo.model.Group
import edu.rosehulman.grouptodo.model.GroupsViewModel
import edu.rosehulman.grouptodo.model.ListItemViewModel


class AddGroupFragment : Fragment() {

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
            Log.d("GTD", "About to call create group")
            createGroup()
        }
    }

    private fun createGroup() {
        val groupName: String = binding.groupNameEditText.text.toString()
        if(groupName.isNullOrBlank()) {
            Snackbar.make(requireView(), "Please enter a group name", Snackbar.LENGTH_LONG)
                .setAnchorView(requireActivity().findViewById(R.id.save_group_button))
                .show()
        } else {
            val members = ArrayList<String>()
            members.add(Firebase.auth.uid.toString())
            val newGroup = Group(groupName, Firebase.auth.uid.toString(), members)
            val ref = Firebase.firestore.collection(Group.COLLECTION_PATH)
            ref.add(newGroup)
            findNavController().navigate(R.id.nav_groups)
        }
    }

    private fun updateView() {
        binding.groupNameEditText.setText(model.getCurrent().name)
        //binding.userNameEditText.setText(model.getCurrent().user)
    }

}