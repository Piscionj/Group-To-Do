package edu.rosehulman.grouptodo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.grouptodo.adapters.GroupsAdapter
import edu.rosehulman.grouptodo.databinding.FragmentGroupsBinding

class GroupsFragment: Fragment() {


    private lateinit var binding : FragmentGroupsBinding
    private lateinit var adapter: GroupsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        // make recycler
        adapter = GroupsAdapter(this)
        // set recyclerview and adapter properties
        binding.recyclerView.adapter = adapter
        adapter.addListener(fragmentName)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // to make grid do GridLayoutManger ^^
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        binding.fab.setOnClickListener{
            Log.d("cli", "clicking add buttton")
            findNavController().navigate(R.id.nav_add_group)
            adapter.addGroup(null)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener(fragmentName)
    }

    companion object {
        const val fragmentName = "GroupsFragment"
    }
}