package com.dolan.dnclientsqlite.db

import android.database.Cursor
import android.provider.BaseColumns._ID
import com.dolan.dnclientsqlite.db.DatabaseContract.Companion.NoteColumn.Companion.DESC
import com.dolan.dnclientsqlite.db.DatabaseContract.Companion.NoteColumn.Companion.TITLE

class MapHelper {
    companion object {
        fun mapCursorToArrayList(cursor: Cursor): List<Note> {
            val noteList = mutableListOf<Note>()
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(_ID))
                val title = cursor.getString(cursor.getColumnIndex(TITLE))
                val desc = cursor.getString(cursor.getColumnIndex(DESC))
                noteList.add(Note(id, title, desc))
            }

            return noteList
        }
    }
}