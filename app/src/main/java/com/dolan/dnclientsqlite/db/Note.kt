package com.dolan.dnclientsqlite.db

import android.database.Cursor
import android.os.Parcelable
import android.provider.BaseColumns._ID
import com.dolan.dnclientsqlite.db.DatabaseContract.Companion.NoteColumn.Companion.DESC
import com.dolan.dnclientsqlite.db.DatabaseContract.Companion.NoteColumn.Companion.TITLE
import com.dolan.dnclientsqlite.db.DatabaseContract.Companion.getColumnInt
import com.dolan.dnclientsqlite.db.DatabaseContract.Companion.getColumnString
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    var id: Int? = 0,
    var title: String? = "TITLE",
    var desc: String? = "DESC"
) : Parcelable {
    constructor(cursor: Cursor?) : this() {
        id = getColumnInt(cursor, _ID)
        title = getColumnString(cursor, TITLE)
        desc = getColumnString(cursor, DESC)
    }
}