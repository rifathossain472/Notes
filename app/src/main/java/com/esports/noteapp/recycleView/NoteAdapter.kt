package com.esports.noteapp.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esports.noteapp.databinding.ItemViewerBinding
import com.esports.noteapp.db.Notes

class NoteAdapter(private val listener: HandleClickListener,val notes: List<Notes>): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {


    interface HandleClickListener{

        fun editClickListener(notes: Notes)
        fun deleteClickListener(notes: Notes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemViewerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        notes[position].let { notes ->
            holder.binding.apply {
                tvTitle.text = notes.title
                tvDetail.text = notes.details
                tvDate.text = notes.date

                clickLayout.setOnClickListener {
                    listener.editClickListener(notes)
                }

                ivDelete.setOnClickListener {
                    listener.deleteClickListener(notes)
                }
            }

        }
    }

    class ViewHolder(val binding: ItemViewerBinding): RecyclerView.ViewHolder(binding.root)
}