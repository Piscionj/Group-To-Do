package edu.rosehulman.grouptodo.ui

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import edu.rosehulman.grouptodo.BuildConfig
import edu.rosehulman.grouptodo.R
import edu.rosehulman.grouptodo.databinding.FragmentUserEditBinding
import edu.rosehulman.grouptodo.model.UserViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.random.Random

class UserEditFragment: Fragment() {

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    binding.profileImage.setImageURI(uri)
                    addPhotoFromUri(uri)
                }
            }
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.profileImage.setImageURI(uri)
                addPhotoFromUri(uri)
            }
        }

    private var latestTmpUri: Uri? = null
    private lateinit var binding: FragmentUserEditBinding
    private lateinit var userModel: UserViewModel


    private val storageImagesRef = Firebase.storage
        .reference
        .child("images")
    private var storageUriStringInFragment: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        binding = FragmentUserEditBinding.inflate(inflater, container, false)
        binding.userEditDoneButton.setOnClickListener {
            // Save user info into Firestore.
            userModel.update(
                newName=binding.userEditNameEditText.text.toString(),
                newStroageUriString = storageUriStringInFragment,
                newHasCompletedSetup=true
            )
            findNavController().navigate(R.id.nav_user)
        }

        binding.userEditUploadPhotoButton.setOnClickListener {
            showPictureDialog()
        }

        userModel.getOrMakeUser {
            with(userModel.user!!) {
                binding.userEditNameEditText.setText(name)
                storageUriStringInFragment = storageUriString
            }
        }
        return binding.root
    }

    private fun showPictureDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose a photo source")
        builder.setMessage("Would you like to take a new picture?\nOr choose an existing one?")
        builder.setPositiveButton("Take Picture") { _, _ ->
            binding.userEditDoneButton.isEnabled = false
            binding.userEditDoneButton.text = "Loading image"
            takeImage()
        }

        builder.setNegativeButton("Choose Picture") { _, _ ->
            binding.userEditDoneButton.isEnabled = false
            binding.userEditDoneButton.text = "Loading image"
            selectImageFromGallery()
        }
        builder.create().show()
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val storageDir: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val tmpFile = File.createTempFile("JPEG_${timeStamp}_", ".png", storageDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun addPhotoFromUri(uri: Uri?) {
        if (uri == null) {
            return
        }
// https://stackoverflow.com/a/5657557
        val stream = requireActivity().contentResolver.openInputStream(uri)
        if (stream == null) {
            return
        }

        // TODO: Add to storage

        val imageId = abs(Random.nextLong()).toString()

        storageImagesRef.child(imageId).putStream(stream)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageImagesRef.child(imageId).downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageUriStringInFragment = task.result.toString()
                    binding.userEditDoneButton.text = "done"
                    binding.userEditDoneButton.isEnabled = true
                } else {
                    // Handle failures
                    // ...
                }
            }

    }

}