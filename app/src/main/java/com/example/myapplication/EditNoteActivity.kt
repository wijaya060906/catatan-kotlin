package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream



class EditNoteActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDesk: EditText
    private lateinit var ivImage: ImageView
    private lateinit var btnUpdate: ImageButton
    private lateinit var btnDelete: ImageButton
    private lateinit var tvNoteApp: TextView
    private lateinit var btnuplod:Button

    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        etTitle = findViewById(R.id.noteTitle)
        etDesk = findViewById(R.id.noteDesc)
        ivImage = findViewById(R.id.imgNote)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        tvNoteApp = findViewById(R.id.tvnnoteapp)
        btnuplod = findViewById(R.id.btnUploadImage)

        ivImage.visibility = View.GONE
        btnuplod.visibility = View.VISIBLE

        btnuplod.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }

        tvNoteApp.setOnClickListener(){
            val intent = Intent(this,LandingActivity::class.java)
            startActivity(intent)
        }


        noteId = intent.getIntExtra("note_id", -1)
        val noteTitle = intent.getStringExtra("note_title")
        val noteDescription = intent.getStringExtra("note_description")
        val noteImage = intent.getByteArrayExtra("note_image")

        if (noteId != -1) {
            etTitle.setText(noteTitle)
            etDesk.setText(noteDescription)

            if (noteImage != null) {
                val bitmap = BitmapFactory.decodeByteArray(noteImage, 0, noteImage.size)
                ivImage.setImageBitmap(bitmap)
                ivImage.visibility = View.VISIBLE
                btnuplod.visibility = View.VISIBLE
            } else {
                ivImage.visibility = View.GONE
                btnuplod.visibility = View.VISIBLE
            }
        }

        btnUpdate.setOnClickListener {
            // Update the note data
            val title = etTitle.text.toString()
            val description = etDesk.text.toString()
            val imageByteArray: ByteArray? = null // Atau set gambar default

            val updatedNote = Note(noteId.toString(), title, description, imageByteArray)
            val noteDbHelper = NoteDbHelper(this) // Inisialisasi noteDbHelper dengan objek yang valid
            val success = noteDbHelper.updateNote(updatedNote)

            if (success) {
                Toast.makeText(this, "Note updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update note!", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            val dbHelper = NoteDbHelper(this)

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Delete Note")
            alertDialog.setMessage("Are you sure you want to delete this note?")

            alertDialog.setPositiveButton("Yes") { _, _ ->
                val success = dbHelper.deleteNote(noteId)
                if (success) {
                    Toast.makeText(this, "Note deleted successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to delete note!", Toast.LENGTH_SHORT).show()
                }
            }

            alertDialog.setNegativeButton("Cancel") { _, _ ->
            }

            alertDialog.show()
        }
    }
}

private fun Nothing?.updateNote(updatedNote: Note): Boolean {
    TODO("Not yet implemented")
}
