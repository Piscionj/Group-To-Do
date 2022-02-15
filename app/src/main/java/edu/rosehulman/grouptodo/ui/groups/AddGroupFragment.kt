package edu.rosehulman.grouptodo.ui.groups

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import edu.rosehulman.grouptodo.R
import edu.rosehulman.grouptodo.databinding.FragmentAddGroupBinding
import edu.rosehulman.grouptodo.model.Group
import edu.rosehulman.grouptodo.model.GroupsViewModel
import android.content.Intent.getIntent
import android.content.Intent.parseIntent
import android.widget.Toast

import com.google.zxing.integration.android.IntentResult
import edu.rosehulman.grouptodo.Constants


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

        setupButtons()

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                binding.userNameEditText.setText(result.contents)
            }
        }
    }

    private fun setupButtons(){
        binding.cancelGroupButton.setOnClickListener {
            findNavController().navigate(R.id.nav_groups)
        }

        binding.saveGroupButton.setOnClickListener {
            Log.d("GTD", "About to call create group")
            createGroup()
        }

        binding.fab.setOnClickListener {
            var text = "User Added"
            // need to check if user exists
            Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG)
                .setAnchorView(requireActivity().findViewById(R.id.save_group_button))
                .show()
            model.updateCurrentMembers(binding.userNameEditText.text.toString())

            binding.userNameEditText.setText("")
        }

        binding.cameraFab.setOnClickListener {
            val integrator = IntentIntegrator.forSupportFragment(this)

            integrator.setOrientationLocked(false)
            integrator.setPrompt("Scan user's QR code")
            integrator.setBeepEnabled(false)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)

            integrator.initiateScan()
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


}