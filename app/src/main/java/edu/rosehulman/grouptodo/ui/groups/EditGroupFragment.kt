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

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FieldPath
import com.google.zxing.integration.android.IntentIntegrator
import edu.rosehulman.grouptodo.Constants


class EditGroupFragment : Fragment() {

    private lateinit var model: GroupsViewModel
    private lateinit var binding: FragmentAddGroupBinding
    private lateinit var qrResultLauncher : ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model =
            ViewModelProvider(requireActivity()).get(GroupsViewModel::class.java)

        // Inflate the layout for this fragment
        binding = FragmentAddGroupBinding.inflate(inflater, container, false)

        qrResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK) {
                // bug is in this line of code -> need to get requestCode for first parameter
                val result = IntentIntegrator.parseActivityResult(it.describeContents(), it.resultCode, it.data)

                if(result.contents != null) {
                    // Do something with the contents (this is usually a URL)
                    println(result.contents)
                    qrCodeScanned(result.contents)
                }
            }
        }

        updateView()
        setupButtons()

        return binding.root
    }

    private fun qrCodeScanned(code: String) {
        Snackbar.make(requireView(), code, Snackbar.LENGTH_LONG)
            .setAnchorView(requireActivity().findViewById(R.id.save_group_button))
            .show()
        // add the member to the group in firebase
        model.updateCurrentMembers(code)
    }

    private fun setupButtons(){


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
            var text = "User Added"
//            val ref: CollectionReference = Firebase.firestore.collection(User.COLLECTION_PATH)
//            var query = ref.whereArrayContains("uid", Firebase.auth.uid.toString())
//            var users = ArrayList<User>()
//            ref.addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
//                    error?.let {
//                        return@addSnapshotListener
//                    }
//                    users.clear()
//                    snapshot?.documents?.forEach{
//                        users.add(User.from(it))
//                    }
//                }
//            Log.d("GTD", "users?: ${users}, size: ${users.size} this thing: ${Firebase.firestore.collection(User.COLLECTION_PATH).whereEqualTo("name","Nick")}")

            // snackbar saying user added or user does not exsist
            Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG)
                .setAnchorView(requireActivity().findViewById(R.id.save_group_button))
                .show()

            // add the member to the group in firebase
            model.updateCurrentMembers(binding.userNameEditText.text.toString())

            // clear out text so can add more users
            binding.userNameEditText.setText("")
        }

        binding.cameraFab.setOnClickListener {
            val scanner = IntentIntegrator(activity)
            // QR Code Format
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            // Set Text Prompt at Bottom of QR code Scanner Activity
            scanner.setPrompt("Scan Friend's QR Code for User ID")
            // Start Scanner (don't use initiateScan() unless if you want to use OnActivityResult)
            qrResultLauncher.launch(scanner.createScanIntent())
        }

    }



    private fun updateView() {
        binding.groupNameEditText.setText(model.getCurrent().name)
        //binding.userNameEditText.setText(model.getCurrent().user)
    }

}