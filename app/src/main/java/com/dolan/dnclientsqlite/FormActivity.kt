package com.dolan.dnclientsqlite

import android.content.ContentResolver
import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dolan.dnclientsqlite.db.DatabaseContract.Companion.NoteColumn.Companion.CONTENT_URI
import com.dolan.dnclientsqlite.db.DatabaseContract.Companion.NoteColumn.Companion.DESC
import com.dolan.dnclientsqlite.db.DatabaseContract.Companion.NoteColumn.Companion.TITLE
import com.dolan.dnclientsqlite.db.Note
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity(), View.OnClickListener {

    private var noteItem: Note? = null

    private var isUpdate = false

    private lateinit var resolver: ContentResolver

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isUpdate) {
            menuInflater.inflate(R.menu.menu_delete, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.menu_delete -> {
                showAlertDeleteDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        resolver = contentResolver

        btn_submit.setOnClickListener(this)

        val uri = intent.data
        if (uri != null) {
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    noteItem = Note(cursor)
                }
                cursor.close()
            }
        }

        val actionBarTitle: String
        val btnActionTitle: String

        if (noteItem != null) {
            isUpdate = true
            actionBarTitle = "Update"
            btnActionTitle = "Simpan"

            edt_title.setText(noteItem?.title.toString())
            edt_description.setText(noteItem?.desc.toString())
        } else {
            actionBarTitle = "Tambah Baru"
            btnActionTitle = "Submit"
        }

        btn_submit.text = btnActionTitle

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_submit -> {
                val title = edt_title.text.toString()
                val desc = edt_description.text.toString()

                val contentValue = ContentValues()
                contentValue.put(TITLE, title)
                contentValue.put(DESC, desc)

                if (isUpdate) {
                    val uri = this.intent.data
                    if (uri != null) {
                        resolver.update(uri, contentValue, null, null)
                        Toast.makeText(baseContext, "Catatan Berhasil Diupdate", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    resolver.insert(CONTENT_URI, contentValue)
                    Toast.makeText(baseContext, "Catatan Berhasil Diinputkan", Toast.LENGTH_SHORT)
                        .show()
                }
                resolver.notifyChange(
                    CONTENT_URI,
                    MainActivity.DataObserver(Handler(), baseContext)
                )
                finish()
            }
        }
    }

    private fun showAlertDeleteDialog() {
        val alertBuilder = AlertDialog.Builder(this)
            .setTitle("Hapus Note")
            .setMessage("Apakah anda ingin menghapus?")
            .setCancelable(true)
            .setPositiveButton("Ya") { _, _ ->
                val uri = this.intent.data
                if (uri != null) {
                    resolver.delete(uri, null, null)
                    resolver.notifyChange(
                        CONTENT_URI,
                        MainActivity.DataObserver(Handler(), this@FormActivity)
                    )
                    Toast.makeText(baseContext, "Item Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.cancel()
            }
        alertBuilder.create().show()
    }
}
