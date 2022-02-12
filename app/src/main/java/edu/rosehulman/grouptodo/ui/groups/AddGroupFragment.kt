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


class AddGroupFragment : Fragment() {

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
            val scanner = IntentIntegrator(activity)
            // QR Code Format
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            // Set Text Prompt at Bottom of QR code Scanner Activity
            scanner.setPrompt("Scan Friend's QR Code for User ID")
            // Start Scanner (don't use initiateScan() unless if you want to use OnActivityResult)
            qrResultLauncher.launch(scanner.createScanIntent())
        }

    }

    private fun qrCodeScanned(code: String) {
        Snackbar.make(requireView(), code, Snackbar.LENGTH_LONG)
            .setAnchorView(requireActivity().findViewById(R.id.save_group_button))
            .show()
        // add the member to the group in firebase
        model.updateCurrentMembers(code)
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