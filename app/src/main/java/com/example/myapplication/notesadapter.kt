package com.example.myapplication

import android.icu.text.CaseMap.Title
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.NotesitemBinding

class notesadapter(private val notes: List<notes>,private val itemClickListener:OnItemClickListener):
    RecyclerView.Adapter<notesadapter.NoteViewHolder>() {
    interface OnItemClickListener{
        fun onDeleteClick(nodeid:String)
        fun onUpdateClick(nodeid:String,title: String, description:String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding=NotesitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note=notes[position]
        holder.bind(note)
        holder.binding.update.setOnClickListener(){
            itemClickListener.onUpdateClick(note.noteId,note.title,note.description)
        }
        holder.binding.delete.setOnClickListener(){
            itemClickListener.onDeleteClick(note.noteId)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(val binding:NotesitemBinding):RecyclerView.ViewHolder (binding.root){
        fun bind(note: notes) {
            binding.itemTitle.text=note.title
            binding.itemDescription.text=note.description

        }

    }

}