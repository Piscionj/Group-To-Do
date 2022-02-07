package edu.rosehulman.grouptodo.ui.listitems

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.grouptodo.R
import edu.rosehulman.grouptodo.adapters.ListItemAdapter
import edu.rosehulman.grouptodo.databinding.FragmentListBinding
import edu.rosehulman.grouptodo.model.GroupsViewModel
import edu.rosehulman.grouptodo.ui.groups.GroupsFragment


class ListFragment : Fragment() {

    private lateinit var groupsModel: GroupsViewModel
    private lateinit var binding : FragmentListBinding
    private lateinit var adapter: ListItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        groupsModel = ViewModelProvider(requireActivity()).get(GroupsViewModel::class.java)
        setActivityTitle(groupsModel.getCurrent().name)
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
            findNavController().navigate(R.id.nav_add_list_item)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener(GroupsFragment.fragmentName)
    }

    fun Fragment.setActivityTitle(title: String) {
        (activity as AppCompatActivity?)?.supportActionBar?.title = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_edit -> {
            findNavController().navigate(R.id.nav_edit_group, null,
                navOptions{
                    anim{
                        enter = android.R.anim.slide_in_left
                        exit = android.R.anim.slide_out_right
                    }
                })
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        const val fragmentName = "ListFragment"
    }

}