package id.sobat.sobatconselor.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import id.sobat.sobatconselor.R

class VhForum(view: View) : RecyclerView.ViewHolder(view) {
    val lnForum = view.findViewById<LinearLayout?>(R.id.ln_forum)
    val ivForum = view.findViewById<ImageView?>(R.id.iv_forum)
    val tvTitleForum = view.findViewById<TextView?>(R.id.tv_title_forum)
    val tvTextForum = view.findViewById<TextView?>(R.id.tv_text_forum)
    val tvDateForum = view.findViewById<TextView?>(R.id.tv_date_forum)
}