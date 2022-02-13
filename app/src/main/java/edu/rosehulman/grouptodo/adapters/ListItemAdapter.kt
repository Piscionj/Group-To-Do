package edu.rosehulman.grouptodo.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.grouptodo.Constants
import edu.rosehulman.grouptodo.ui.listitems.ListFragment
import edu.rosehulman.grouptodo.model.GroupsViewModel
import edu.rosehulman.grouptodo.model.ListItem
import edu.rosehulman.grouptodo.model.ListItemViewModel

class ListItemAdapter(val fragment: ListFragment) : RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    val listItemModel = ViewModelProvider(fragment.requireActivity()).get(ListItemViewModel::class.java)
    val groupsModel = ViewModelProvider(fragment.requireActivity()).get(GroupsViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return ListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(listItemModel.getItemAt(position))
    }

    override fun getItemCount(): Int {
        return listItemModel.size()
    }

    fun addListener() {
        listItemModel.addListener(groupsModel.getCurrent().id) { notifyDataSetChanged() }
    }

    fun removeListener(){
        listItemModel.removeListener()
    }

    @SuppressLint("ResourceAsColor")
    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.row_item_text_view)
        val dateTextView: TextView = itemView.findViewById(R.id.row_item_due_date)
        val statusCircleImageView: ImageView = itemView.findViewById(R.id.status_circle)

        init {
            itemView.setOnClickListener{
                listItemModel.updatePos(adapterPosition)
                fragment.findNavController().navigate(R.id.nav_edit_list_item, null,
                navOptions{
                    anim{
                        enter = android.R.anim.slide_in_left
                        exit = android.R.anim.slide_out_right
                    }
                })
            }
            statusCircleImageView.setOnClickListener{
                listItemModel.updatePos(adapterPosition)
                listItemModel.toggleCurrentItem()
                notifyItemChanged(listItemModel.currentPos)
                Log.d("GTD", "isFinished: ${listItemModel.currentPos}")
            }
        }

        fun bind(itemList: ListItem) {
            nameTextView.text = itemList.name
            dateTextView.text = itemList.date
            Log.d(Constants.TAG, itemList.toString())
            if (itemList.isFinished){
                dateTextView.setTextColor(Color.parseColor("#D1D3D4"))
                nameTextView.setTextColor(Color.parseColor("#D1D3D4"))
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                itemView.setBackgroundResource(R.color.background_light_grey)
                statusCircleImageView.setColorFilter(android.R.color.holo_green_light)
                // still cant figure out changing circle color
            } else {
                dateTextView.setTextColor(Color.BLACK)
                nameTextView.setTextColor(Color.BLACK)
                nameTextView.setPaintFlags(0)
                itemView.setBackgroundResource(R.color.white)
            }

        }
    }



}