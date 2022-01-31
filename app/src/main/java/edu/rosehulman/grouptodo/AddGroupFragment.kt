package edu.rosehulman.grouptodo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.grouptodo.databinding.FragmentAddGroupBinding
import edu.rosehulman.grouptodo.model.Group


class AddGroupFragment : Fragment() {

    private lateinit var binding: FragmentAddGroupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddGroupBinding.inflate(inflater, container, false)

        binding.doneButton.setOnClickListener {
            Log.d("GTD", "About to call create group")
            createGroup() }

        return binding.root
    }

    private fun createGroup() {
        val groupName: String = binding.groupNameEditText.text.toString()
        if(groupName.isNullOrBlank()) {
            Snackbar.make(requireView(), "Please enter a group name", Snackbar.LENGTH_LONG)
                .setAnchorView(requireActivity().findViewById(R.id.done_button))
                .show()
        } else {
            val newGroup = Group(groupName)
            val ref = Firebase.firestore.collection(Group.COLLECTION_PATH)
            ref.add(newGroup)
            findNavController().navigate(R.id.nav_list)
        }
    }

}