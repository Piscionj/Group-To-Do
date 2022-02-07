package edu.rosehulman.grouptodo.ui.listitems

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import edu.rosehulman.grouptodo.R
import edu.rosehulman.grouptodo.databinding.FragmentEditListItemBinding
import edu.rosehulman.grouptodo.model.ListItemViewModel

class AddListItemFragment : Fragment(){

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
        //updateView()
        return binding.root
    }

    private fun setupButtons() {
        binding.dueDateButton.setOnClickListener {
            Log.d("cli", "clicking buttton")
            val picker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .build()
            picker.addOnPositiveButtonClickListener{
                val s = String.format("Date: ${picker.headerText}")
                binding.dueDateButton.text = s
            }
            picker.show(parentFragmentManager, "TAG")
        }


        binding.saveButton.setOnClickListener{
            val name = binding.listItemEditEventName.text.toString()
            val date = binding.dueDateButton.text.toString()
            model.addItem(name, date)
            findNavController().navigate(R.id.nav_list)
        }

        binding.clearButton.setOnClickListener{
            findNavController().navigate(R.id.nav_list)
        }

    }


}