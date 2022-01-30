package edu.rosehulman.grouptodo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.rosehulman.grouptodo.databinding.FragmentEditListItemBinding

class EditListItemFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private lateinit var binding: FragmentEditListItemBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        binding = FragmentEditListItemBinding.inflate(inflater, container, false)

        return binding.root
    }


}