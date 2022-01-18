package edu.rosehulman.grouptodo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.rosehulman.grouptodo.databinding.FragmentEditListItemBinding
import edu.rosehulman.grouptodo.model.ListItemViewModel

class ListEditFragment : Fragment(){

    private lateinit var model: ListItemViewModel
    private lateinit var binding: FragmentEditListItemBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        model =
            ViewModelProvider(requireActivity()).get(ListItemViewModel::class.java)

        binding = FragmentEditListItemBinding.inflate(inflater, container, false)
        setupButtons()
        updateView()
        return binding.root
    }

    private fun setupButtons() {
        binding.saveButton.setOnClickListener{
            val name = binding.listItemEditEventName.text.toString()
            model.updateCurrentItem(name)
            updateView()
        }

    }

    private fun updateView() {
        binding.listItemEditEventName.setText(model.getCurrent().toString())
    }

}