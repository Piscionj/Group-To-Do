package edu.rosehulman.grouptodo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.grouptodo.adapters.ListItemAdapter
import edu.rosehulman.grouptodo.databinding.FragmentListBinding


class ListFragment : Fragment() {


    private lateinit var binding : FragmentListBinding
    private lateinit var adapter: ListItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        // make recycler
        adapter = ListItemAdapter(this)
        // set recyclerview and adapter properties
        binding.recyclerView.adapter = adapter
        adapter.addListener(fragmentName)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // to make grid do GridLayoutManger ^^
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        binding.fab.setOnClickListener{
            Log.d("cli", "clicking add buttton")
            findNavController().navigate(R.id.nav_edit_list_item)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener(GroupsFragment.fragmentName)
    }

    companion object {
        const val fragmentName = "ListFragment"
    }

}