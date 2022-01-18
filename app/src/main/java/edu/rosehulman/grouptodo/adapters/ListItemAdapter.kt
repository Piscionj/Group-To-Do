package edu.rosehulman.grouptodo.adapters

import edu.rosehulman.grouptodo.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
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

    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val quoteTextView: TextView = itemView.findViewById(R.id.row_quote_text_view)
//        val movieTextView: TextView = itemView.findViewById(R.id.row_movie_text_view)

        init {

        }

        fun bind(itemList: ListItem) {
//            quoteTextView.text = movieQuote.quote
//            movieTextView.text = movieQuote.movie

        }
    }



}