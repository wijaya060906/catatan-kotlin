package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import java.io.ByteArrayOutputStream

class AddNoteActivity : AppCompatActivity() {


    private lateinit var btnSave: ImageButton
    private lateinit var etTitle: EditText
    private lateinit var etDesk: EditText
    private lateinit var btnUploadImage: Button
    private lateinit var ivImage: ImageView
    private lateinit var tvNoteAPp:TextView

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_note)


        btnSave = findViewById(R.id.btnsave)
        etTitle = findViewById(R.id.et_title)
        etDesk = findViewById(R.id.et_Desk)
        btnUploadImage = findViewById(R.id.btnUploadImage)
        ivImage = findViewById(R.id.ivImage)
        tvNoteAPp = findViewById(R.id.tvnnoteapp)

        // Initially, hide the image view and show the upload button
        ivImage.visibility = View.GONE
        btnUploadImage.visibility = View.VISIBLE

        tvNoteAPp.setOnClickListener(){
            val intent =Intent(this,LandingActivity::class.java)
            startActivity(intent)
        }

        btnUploadImage.setOnClickListener {
            // Open the gallery to select an image
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }

        btnSave.setOnClickListener {
            // Save the note

            val title = etTitle.text.toString()
            val description = etDesk.text.toString()
            var imageByteArray: ByteArray? = null

            // Check if an image has been uploaded
            if (imageUri != null) {
                try {
                    // Convert the image Uri to a ByteArray
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
                    imageByteArray = byteArrayOutputStream.toByteArray()
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed to convert image! Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            val note = Note("", title, description, imageByteArray)
            val dbHelper = NoteDbHelper(this)
            val success = dbHelper.insertNote(note)

            if (success) {
                Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to save note!", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun Note(id: String = 0.toString(), title: String, description: ByteArray?): Note {
        return Note(id, title, description.toString(), null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            ivImage.setImageURI(imageUri)

            // Show the image view and hide the upload button
            ivImage.visibility = View.VISIBLE
            btnUploadImage.visibility = View.VISIBLE
        }
    }
}



class Note(val id: String, val title: String, val description: String, val image: ByteArray?){

}

class NoteDbHelper(context: Context) : SQLiteOpenHelper(context, "notes.db", null, 1) {

    fun deleteNote(noteId: Int): Boolean {
        val db = writableDatabase
        val success = db.delete("notes", "_id = ?", arrayOf(noteId.toString())) > 0
        return success
    }

    fun updateNote(note: Note): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("title", note.title)
            put("description", note.description)
            if (note.image != null) {
                put("picture", note.image)
            }
        }

        val success = db.update("notes", contentValues, "_id = ?", arrayOf(note.id)) > 0
        return success
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS notes")
        if (db != null) {
            onCreate(db)
        }
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE notes (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, picture BLOB)")
    }




    @SuppressLint("Range")
    fun getAllNotes(): List<Note> {
        val db = readableDatabase
        val notes = ArrayList<Note>()



        val cursor = db.query("notes", null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val picture = cursor.getBlob(cursor.getColumnIndexOrThrow("picture"))

                val note = Note(id.toString(), title, description, picture)
                notes.add(note)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return notes
    }



    fun insertNote(note: Note): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("title", note.title)
            put("description", note.description)
            if (note.image != null) {
                put("picture", note.image)
            }
        }

        val rowId = db.insert("notes", null, contentValues)
        return rowId != -1L
    }

    fun getNote(noteId: Int, context: Context): Note? {
        val db = this.writableDatabase
        val cursor = db.query("notes",
            arrayOf("_id", "title", "description", "picture"),
            "_id = ?",
            arrayOf(noteId.toString()),
            null,
            null,
            null
        )

        var note: Note? = null
        if (cursor.moveToFirst()) {
            note = Note(
                cursor.getInt(0).toString(),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getBlob(3)
            )
        }
        cursor.close()
        return note
    }

    private fun Note(id: String, title: String, description: ByteArray?): Note {
        return Note(id, title, description.toString(), description)
    }

    fun getNote(noteId: Int): Note? {
        val db = readableDatabase
        val cursor = db.query("notes",
            arrayOf("_id", "title", "description", "picture"),
            "_id = ?",
            arrayOf(noteId.toString()),
            null,
            null,
            null
        )

        var note: Note? = null
        if (cursor.moveToFirst()) {
            note = Note(
                cursor.getInt(0).toString(),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getBlob(3)
            )
        }
        cursor.close()
        return note
    }

    companion object {
        fun insertNote(noteDbHelper: NoteDbHelper, note: Note): Boolean {
            val db = noteDbHelper.writableDatabase
            val contentValues = ContentValues().apply {
                put("_id", note.id.toInt()) // Add this line
                put("title", note.title)
                put("description", note.description)
                put("image", note.image)
            }

            val rowId = db.insert("notes", null, contentValues)
            db.close()
            return rowId != -1L
        }
    }

    fun searchNotes(query: String): List<Note> {
        val db = writableDatabase
        val cursor = db.query(
            "notes",
            arrayOf("_id", "title", "description", "picture"),
            "title LIKE ? OR description LIKE ?",
            arrayOf("%$query%", "%$query%"),
            null,
            null,
            null
        )

        val notes = mutableListOf<Note>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val description = cursor.getString(2)
            val picture = cursor.getBlob(3)

            val note = Note(id.toString(), title, description, picture)
            notes.add(note)
        }
        cursor.close()
        return notes
    }
}

private fun <E> java.util.ArrayList<E>.add(element: com.example.myapplication.model.Note) {
    TODO("Not yet implemented")
}

class NoteDatabaseHelper(private val context: AddNoteActivity) : SQLiteOpenHelper(context, "notes.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE notes (_id INTEGER PRIMARY KEY, title TEXT, description TEXT, image BLOB)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS notes")
        onCreate(db)
    }

}