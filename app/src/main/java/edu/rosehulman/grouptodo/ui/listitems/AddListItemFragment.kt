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
import edu.rosehulman.grouptodo.R
import edu.rosehulman.grouptodo.databinding.FragmentEditListItemBinding
import edu.rosehulman.grouptodo.model.ListItemViewModel
import android.widget.Spinner
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.grouptodo.model.Group


class AddListItemFragment : Fragment(){

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
        //updateView()
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
                snapshot?.documents?.forEach {
                    groups.add(Group.from(it).name)
                }
            }
        val arr = arrayOf(groups)
        var adapter = ArrayAdapter(this.activity!!, android.R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.addGroupSpinner.onItemSelectedListener
        binding.addGroupSpinner.adapter = adapter

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


//        binding.addGroupSpinner.setOnItemClickListener(this)

        binding.saveButton.setOnClickListener{
            val name = binding.listItemEditEventName.text.toString()
            val date = binding.dueDateButton.text.toString()
//            val group = binding.addGroupSpinner.selectedItem.toString()
//            Log.d("GTD", group)
            model.addItem(name, date)
            findNavController().navigate(R.id.nav_list)
        }

        binding.clearButton.setOnClickListener{
            findNavController().navigate(R.id.nav_list)
        }

    }


}