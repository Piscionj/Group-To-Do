package edu.rosehulman.grouptodo.adapters

import android.graphics.Color
import edu.rosehulman.grouptodo.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.grouptodo.ListFragment
import edu.rosehulman.grouptodo.model.ListItem
import edu.rosehulman.grouptodo.model.ListItemViewModel

class ListItemAdapter(val fragment: ListFragment) : RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    val model = ViewModelProvider(fragment.requireActivity()).get(ListItemViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return ListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(model.getItemAt(position))
    }

    override fun getItemCount(): Int {
        return model.size()
    }

    fun addItem(listItem: ListItem?) {
        model.addItem(listItem)
        notifyDataSetChanged()
    }

    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.row_item_text_view)
//        val statusCircleImageView: ImageView = itemView.findViewById(R.id.row_item_text_view)

        init {
            itemView.setOnClickListener{
                model.updatePos(adapterPosition)
                fragment.findNavController().navigate(R.id.nav_edit_list_item, null,
                navOptions{
                    anim{
                        enter = android.R.anim.slide_in_left
                        exit = android.R.anim.slide_out_right
                    }
                })
            }

        }

        fun bind(itemList: ListItem) {
            nameTextView.text = itemList.name
//            statusCircleImageView.setBackgroundColor(
//                if (itemList.isFinished){
//                    Color.GREEN
//                } else {
//                    Color.YELLOW
//                })

        }
    }



}