package com.esports.noteapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Notes::class] , version = 2)
abstract class NoteDatabase: RoomDatabase() {

abstract fun getNoteDao(): NoteDao

}