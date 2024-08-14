package com.esports.noteapp.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import androidx.room.RoomDatabase
import com.esports.noteapp.R
import com.esports.noteapp.databinding.ActivityMainBinding
import com.esports.noteapp.db.NoteDao
import com.esports.noteapp.db.NoteDatabase
import com.esports.noteapp.db.Notes
import com.esports.noteapp.recycleView.NoteAdapter

class MainActivity : AppCompatActivity(), NoteAdapter.HandleClickListener {
    private lateinit var binding: ActivityMainBinding
    lateinit var dao: NoteDao
    lateinit var adapter: NoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "noteDB"
        ).allowMainThreadQueries().build()

        dao = db.getNoteDao()

        setHomePage()

        binding.btnAddNote.setOnClickListener {
            val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
            startActivity(intent)
        }

    }

    override fun editClickListener(notes: Notes) {
        val editIntent = Intent(this@MainActivity, AddNoteActivity::class.java)
        editIntent.putExtra(AddNoteActivity.editKey, notes)
        startActivity(editIntent)
    }


    override fun deleteClickListener(notes: Notes) {
        dao.deleteNote(notes)
        Toast.makeText(this, "Note Deleted Successfully", Toast.LENGTH_SHORT).show()
        setHomePage()
    }

    private fun setHomePage() {
        val noteList = dao.getAllNotes()
        adapter = NoteAdapter(this@MainActivity, noteList)
        binding.rvLayout.adapter = adapter
    }
}