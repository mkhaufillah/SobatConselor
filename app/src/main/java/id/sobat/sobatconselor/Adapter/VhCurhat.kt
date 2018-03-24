package id.sobat.sobatconselor.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import id.sobat.sobatconselor.R

class VhCurhat(view: View) : RecyclerView.ViewHolder(view) {
    val btnCurhat = view.findViewById<Button?>(R.id.btn_curhat)
    val ivCurhat = view.findViewById<ImageView?>(R.id.iv_curhat)
    val tvNameCurhat = view.findViewById<TextView?>(R.id.tv_name_curhat)
    val tvTextCurhat = view.findViewById<TextView?>(R.id.tv_text_curhat)
    val tvDateCurhat = view.findViewById<TextView?>(R.id.tv_date_curhat)
}