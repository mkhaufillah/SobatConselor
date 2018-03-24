package id.sobat.sobatconselor.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import id.sobat.sobatconselor.R

class VhInChat(view: View) : RecyclerView.ViewHolder(view) {
    val tvTextInChat = view.findViewById<TextView?>(R.id.tv_text_in_chat)
    val tvDateInChat = view.findViewById<TextView?>(R.id.tv_date_in_chat)
}