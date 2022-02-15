package edu.rosehulman.grouptodo.ui.groups

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.grouptodo.R
import edu.rosehulman.grouptodo.databinding.FragmentAddGroupBinding
import edu.rosehulman.grouptodo.model.Group
import edu.rosehulman.grouptodo.model.GroupsViewModel
import edu.rosehulman.grouptodo.model.User
import androidx.annotation.NonNull
import com.firebase.ui.auth.viewmodel.RequestCodes

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FieldPath
import com.google.zxing.integration.android.IntentIntegrator
import edu.rosehulman.grouptodo.Constants


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                binding.userNameEditText.setText(result.contents)
            }
        }
    }

    private fun setupButtons(){

        binding.cameraFab.setOnClickListener {
            val integrator = IntentIntegrator.forSupportFragment(this)

            integrator.setOrientationLocked(false)
            integrator.setPrompt("Scan user's QR code")
            integrator.setBeepEnabled(false)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)

            integrator.initiateScan()
        }

        binding.saveGroupButton.setOnClickListener {
            Log.d(Constants.TAG, "About to call edit group")
            val newName = binding.groupNameEditText.text.toString()
            model.updateCurrentItem(newName)
            findNavController().navigate(R.id.nav_groups)
        }

        binding.cancelGroupButton.text = "DELETE"
        binding.cancelGroupButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this group?")
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    model.removeCurrentGroup()
                    findNavController().navigate(R.id.nav_groups)
                    updateView()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        binding.fab.setOnClickListener {
            // Nick uid for testing: 6EICZwEGVfVM3W9RZ8mJEp3U4bj1
            // need to check for if the user is a user
            val uid = binding.userNameEditText.text.toString()
            val docRef = Firebase.firestore.collection(User.COLLECTION_PATH).document(uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // add the member to the group in firebase
                        model.updateCurrentMembers(uid)
                        // clear out text so can add more users
                        binding.userNameEditText.setText("")
                        snackbarWithString("User Added")
                    } else {
                        snackbarWithString("No such user exists")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(Constants.TAG, "get failed with ", exception)
                }
        }

    }

    private fun snackbarWithString(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG)
            .setAnchorView(requireActivity().findViewById(R.id.save_group_button))
            .show()
    }

    private fun updateView() {
        binding.groupNameEditText.setText(model.getCurrent().name)
        //binding.userNameEditText.setText(model.getCurrent().user)
    }

}