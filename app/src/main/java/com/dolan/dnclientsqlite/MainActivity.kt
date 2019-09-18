package com.dolan.dnclientsqlite

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dolan.dnclientsqlite.db.DatabaseContract.Companion.NoteColumn.Companion.CONTENT_URI
import com.dolan.dnclientsqlite.db.MapHelper.Companion.mapCursorToArrayList
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), LoadNotesCallback {

    private lateinit var noteAdapter: NoteAdapeter

    private lateinit var dataObserver: DataObserver

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_add -> {
                val intent = Intent(baseContext, FormActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteAdapter = NoteAdapeter(this)

        rv_note.adapter = noteAdapter
        rv_note.layoutManager = LinearLayoutManager(baseContext)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()

        val handler = Handler(handlerThread.looper)

        dataObserver = DataObserver(handler, this)

        contentResolver.registerContentObserver(CONTENT_URI, true, dataObserver)

        Log.d("CONTENT RESOLVER", "$contentResolver")

        GetData(this, this).execute()
    }

    override fun postExecute(notes: Cursor?) {

        if (notes != null) {
            val list = mapCursorToArrayList(notes)
            noteAdapter.setNoteList(list.toMutableList())
        } else {
            Toast.makeText(baseContext, "Tidak Ada Data", Toast.LENGTH_SHORT).show()
            noteAdapter.setNoteList(mutableListOf())
        }
    }

    private class GetData(
        context: Context?,
        loadNotesCallback: LoadNotesCallback
    ) : AsyncTask<Void, Void, Cursor?>() {

        private val ctx = WeakReference<Context>(context)
        private val loadNotesCallback = WeakReference(loadNotesCallback)

        override fun doInBackground(vararg params: Void?): Cursor? {
            return ctx.get()?.contentResolver?.query(CONTENT_URI, null, null, null, null)
        }

        override fun onPostExecute(result: Cursor?) {
            super.onPostExecute(result)
            loadNotesCallback.get()?.postExecute(result)
        }
    }

    class DataObserver(handler: Handler, context: Context) : ContentObserver(handler) {
        private val ctx = context

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            GetData(ctx, (ctx as MainActivity)).execute()
        }
    }
}
