package com.dolan.dnclientsqlite

import android.database.Cursor

interface LoadNotesCallback {
    fun postExecute(notes: Cursor?)
}