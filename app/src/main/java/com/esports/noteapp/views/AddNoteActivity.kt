package com.esports.noteapp.views

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.esports.noteapp.databinding.ActivityAddNoteBinding
import com.esports.noteapp.db.NoteDao
import com.esports.noteapp.db.NoteDatabase
import com.esports.noteapp.db.Notes
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale


class AddNoteActivity : AppCompatActivity() {

    private val dateTime: String = getCurrentDateTime()
    private val date: String = getCurrentDate()

    companion object {
        const val editKey = "edit"
    }

    private var userId = 0

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var dao: NoteDao
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.speechToText.setOnClickListener(View.OnClickListener {
            val intentTextToSpeech = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intentTextToSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intentTextToSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intentTextToSpeech.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something...")
            try {
                activityResultLauncher.launch(intentTextToSpeech)
            }catch (Exp: ActivityNotFoundException){
                Toast.makeText(applicationContext, "Your Device does not supported", Toast.LENGTH_SHORT).show()
            }
        })

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult? ->
            if (result!!.resultCode == RESULT_OK && result!!.data!=null){
                val speechText = result!!.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<Editable>
                binding.etTitle.text = speechText[0]
            }
        }
//gfdfgfd
        val db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "noteDB"
        ).allowMainThreadQueries().build()

        dao = db.getNoteDao()

        binding.ivBackArrow.setOnClickListener {
            homePage()
        }
        binding.tvDateTime.text = dateTime

        if (intent.hasExtra(editKey)) {
            val notes = intent.getParcelableExtra<Notes>(editKey)

            binding.etTitle.setText(notes?.title)
            binding.etDetails.setText(notes?.details)
            userId = notes!!.id
            binding.ivTick.setOnClickListener {
                val updatedTitle = binding.etTitle.text.toString()
                val updatedDetails = binding.etDetails.text.toString()
                val updatedNote = Notes(userId, updatedTitle, updatedDetails, notes.date)
                Log.d("TAG", "${notes.date} ")
                update(updatedNote)
                homePage()
            }

        } else {
            binding.ivTick.setOnClickListener {
                val title = binding.etTitle.text.toString()
                val details = binding.etDetails.text.toString()
                addNote(title, details)
                homePage()
            }
        }
    }

    private fun update(notes: Notes) {
        val note = Notes(notes.id, notes.title, notes.details, date)
        dao.updateNote(note)
    }

    private fun addNote(title: String, details: String) {
        val note = Notes(0, title, details, date)
        dao.addNote(note)
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("MMMM dd", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd MMMM hh:mm a", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private fun homePage() {
        val homeIntent = Intent(this@AddNoteActivity, MainActivity::class.java)
        startActivity(homeIntent)
    }
}