package edu.rosehulman.grouptodo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.grouptodo.GroupsFragment
import edu.rosehulman.grouptodo.R
import edu.rosehulman.grouptodo.model.Group
import edu.rosehulman.grouptodo.model.GroupsViewModel

class GroupsAdapter(val fragment: GroupsFragment) : RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder>() {

    val model = ViewModelProvider(fragment.requireActivity()).get(GroupsViewModel::class.java)


    fun addListener(fragmentName: String){
        model.addListener(fragmentName) {
            notifyDataSetChanged()
        }
    }

    fun removeListener(fragmentName: String){
        model.removeListener(fragmentName)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.groups_row_item, parent, false)
        return GroupsViewHolder(view)
    }
    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        holder.bind(model.getGroupAt(position))
    }

    override fun getItemCount(): Int {
        return model.size()
    }

    fun addGroup(group: Group?) {
        model.addGroup(group)
        notifyDataSetChanged()
    }

    @SuppressLint("ResourceAsColor")
    inner class GroupsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupTextView: TextView = itemView.findViewById(R.id.row_item_group)

        init {
            itemView.setOnClickListener{
                model.updatePos(adapterPosition)
//                fragment.findNavController().navigate(R.id.nav_add_group, null,
//                    navOptions{
//                        anim{
//                            enter = android.R.anim.slide_in_left
//                            exit = android.R.anim.slide_out_right
//                        }
//                    })
                fragment.findNavController().navigate(R.id.nav_list, null,
                    navOptions{
                        anim{
                            enter = android.R.anim.slide_in_left
                            exit = android.R.anim.slide_out_right
                        }
                    })
            }


        }

        fun bind(group: Group) {
            groupTextView.text = group.name
        }
    }




}