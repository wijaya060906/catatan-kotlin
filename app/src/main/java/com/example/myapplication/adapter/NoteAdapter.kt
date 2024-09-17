package com.example.myapplication.adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.EditNoteActivity
import com.example.myapplication.Note
import com.example.myapplication.R

class NoteAdapter(private val notes: List<Note>, private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView.findViewById(R.id.noteTitle)
        val imgNote: ImageView = itemView.findViewById(R.id.imgNote)
        val noteDesc: TextView = itemView.findViewById(R.id.noteDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.noteTitle.text = note.title
        if (note.image != null) {
            val bitmap = BitmapFactory.decodeByteArray(note.image, 0, note.image.size)
            holder.imgNote.setImageBitmap(bitmap)
            holder.imgNote.visibility = View.VISIBLE
        } else {
            holder.imgNote.visibility = View.GONE
        }
        holder.noteDesc.text = note.description

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(note)
            val intent = Intent(holder.itemView.context, EditNoteActivity::class.java)
            intent.putExtra("note_id", note.id.toInt())
            intent.putExtra("note_title", note.title)
            intent.putExtra("note_description", note.description)
            intent.putExtra("note_image", note.image)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}