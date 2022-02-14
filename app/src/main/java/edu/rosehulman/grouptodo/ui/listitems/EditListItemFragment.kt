package edu.rosehulman.grouptodo.ui.listitems

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.grouptodo.R
import edu.rosehulman.grouptodo.databinding.FragmentEditListItemBinding
import edu.rosehulman.grouptodo.model.Group
import edu.rosehulman.grouptodo.model.ListItemViewModel

class EditListItemFragment : Fragment(){

    private lateinit var model: ListItemViewModel
    private lateinit var binding: FragmentEditListItemBinding
    var groups = ArrayList<String>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        model =
            ViewModelProvider(requireActivity()).get(ListItemViewModel::class.java)

        binding = FragmentEditListItemBinding.inflate(inflater, container, false)
        setupButtons()
        setupGroupsSpinner()
        updateView()
        return binding.root
    }


    private fun setupGroupsSpinner() {
        val values = arrayOf(
            "Group 1",
            "Group 2",
            "Group 3",
        )
        val uid = Firebase.auth.currentUser!!.uid
        val ref = Firebase.firestore.collection(Group.COLLECTION_PATH)
        val subscription = ref
            .orderBy(Group.CREATED_KEY, Query.Direction.ASCENDING)
            .whereEqualTo("creator", uid)
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    return@addSnapshotListener
                }
                groups.clear()
                groups.add("Select a Group")
                snapshot?.documents?.forEach {
                    groups.add(Group.from(it).name)
                }
                var adapter = ArrayAdapter(this.activity!!, android.R.layout.simple_spinner_item, groups)
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                binding.addGroupSpinner.onItemSelectedListener
                binding.addGroupSpinner.adapter = adapter
            }


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

        binding.deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    model.removeCurrentItem()
                    findNavController().navigate(R.id.nav_groups)
                    updateView()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }


        binding.saveButton.setOnClickListener{
            val name = binding.listItemEditEventName.text.toString()
            val date = binding.dueDateButton.text.toString()
            model.updateCurrentItem(name, date)
            findNavController().navigate(R.id.nav_list)
        }

        binding.clearButton.setOnClickListener{
            findNavController().navigate(R.id.nav_list)
        }

    }

    private fun updateView() {
        binding.listItemEditEventName.setText(model.getCurrent().name)
        binding.dueDateButton.text = model.getCurrent().date
    }

}