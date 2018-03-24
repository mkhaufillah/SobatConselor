package id.sobat.sobatconselor.Adapter

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import id.sobat.sobatconselor.R

class VhChat(view: View) : RecyclerView.ViewHolder(view) {
    val clChat = view.findViewById<ConstraintLayout?>(R.id.cl_chat)
    val ivChat = view.findViewById<ImageView?>(R.id.iv_chat)
    val tvNameChat = view.findViewById<TextView?>(R.id.tv_name_chat)
    val tvTextChat = view.findViewById<TextView?>(R.id.tv_text_chat)
    val tvDateChat = view.findViewById<TextView?>(R.id.tv_date_chat)
    val lnChat = view.findViewById<LinearLayout?>(R.id.ln_chat)
}