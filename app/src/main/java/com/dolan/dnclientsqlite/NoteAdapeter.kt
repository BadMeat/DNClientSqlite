package com.dolan.dnclientsqlite

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dolan.dnclientsqlite.db.DatabaseContract.Companion.NoteColumn.Companion.CONTENT_URI
import com.dolan.dnclientsqlite.db.Note

class NoteAdapeter(private val activity: Activity) :
    RecyclerView.Adapter<NoteAdapeter.NoteHolder>() {

    private val noteList = mutableListOf<Note>()

    fun setNoteList(list: MutableList<Note>) {
        noteList.clear()
        noteList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.note_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = noteList.size

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bindItem(noteList[position], position, activity)
    }

    class NoteHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtTitle: TextView = view.findViewById(R.id.txt_title)
        private val txtDesc: TextView = view.findViewById(R.id.txt_desc)

        fun bindItem(e: Note, position: Int, activity: Activity) {
            txtTitle.text = e.title
            txtDesc.text = e.desc
            itemView.setOnClickListener(
                CustomOnItemClickListener(
                    position,
                    object : CustomOnItemClickListener.OnItemCallBack {
                        override fun onItemClicked(view: View?, position: Int) {
                            val intent = Intent(activity, FormActivity::class.java)
                            val uri = Uri.parse("$CONTENT_URI/${e.id}")
                            intent.data = uri
                            activity.startActivity(intent)
                        }
                    })
            )
        }
    }
}