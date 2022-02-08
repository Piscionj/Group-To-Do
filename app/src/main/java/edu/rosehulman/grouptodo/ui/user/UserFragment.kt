package edu.rosehulman.grouptodo.ui.user

import android.os.Bundle
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
import androidmads.library.qrgenearator.QRGEncoder

import android.graphics.Bitmap

import android.widget.EditText
import android.widget.ImageView
import com.google.zxing.WriterException

import androidmads.library.qrgenearator.QRGContents

import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.util.Log
import android.view.*

import androidx.core.content.ContextCompat.getSystemService

import androidx.core.content.ContextCompat


class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null

    private lateinit var qrCodeIV: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var qrgEncoder: QRGEncoder

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.profileName.text = userModel.user!!.name
        binding.uid.text = Firebase.auth.currentUser!!.uid
        if (userModel.user!!.storageUriString.isNotEmpty()){
            binding.userImage.load(userModel.user!!.storageUriString){
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }

        qrCode()

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


    private fun qrCode(){
        qrCodeIV = binding.idIVQrcode
        qrgEncoder = QRGEncoder(Firebase.auth.currentUser!!.uid, null, QRGContents.Type.TEXT, 200)
        try {
            bitmap = qrgEncoder.encodeAsBitmap()
            qrCodeIV.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            Log.e("Tag", e.toString())
        }
    }

}