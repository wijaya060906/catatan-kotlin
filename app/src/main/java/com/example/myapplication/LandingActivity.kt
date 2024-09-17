package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.media.Image
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.NoteAdapter

class LandingActivity : AppCompatActivity() {

    private lateinit var dbHelper: NoteDbHelper
    private lateinit var rvNotes: RecyclerView
    private lateinit var buttonSearch: ImageButton
    private lateinit var etSearch: EditText
    private lateinit var noteEmpty: ImageView
    private lateinit var buttoncreate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing)


        dbHelper = NoteDbHelper(this)
        rvNotes = findViewById(R.id.rvNotes)
        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.layoutManager = GridLayoutManager(this, 2)
        buttonSearch =findViewById(R.id.buttonSearch)
        etSearch = findViewById(R.id.serchbar)
        noteEmpty = findViewById(R.id.noteempty)
        buttoncreate = findViewById(R.id.btn_create_catatan)

        buttoncreate.setOnClickListener(){
            val intent = Intent(this,AddNoteActivity::class.java)
            startActivity(intent);
        }

        noteEmpty.setOnClickListener(){

        }

        var isSearchVisible = false

        etSearch.setVisibility(View.GONE)

        buttonSearch.setOnClickListener {
            isSearchVisible = !isSearchVisible
            etSearch.setVisibility(if (isSearchVisible) View.VISIBLE else View.GONE)

            if (isSearchVisible) {
                etSearch.requestFocus()
                etSearch.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        val searchQuery = etSearch.text.toString()
                        val filteredNotes = dbHelper.searchNotes(searchQuery)
                        displayNotes(filteredNotes)
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })
            }
        }


        val buttonPluss = findViewById<ImageButton>(R.id.btnAddNote)
        buttonPluss.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        displayNotes(dbHelper.getAllNotes())

    }



    private fun displayNotes(notes: List<Note>) {

        val adapter = NoteAdapter(notes, object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                val intent = Intent(this@LandingActivity, EditNoteActivity::class.java)
                intent.putExtra("note_id", note.id)
                startActivity(intent)
            }
        })
        rvNotes.adapter = adapter


        if (notes.isEmpty()){
            noteEmpty.visibility = View.VISIBLE
            rvNotes.visibility = View.GONE
        }else{
            noteEmpty.visibility = View.GONE
            rvNotes.visibility = View.VISIBLE
        }
    }



}