package id.sobat.sobatconselor.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.sobat.sobatconselor.Model.DataLocal
import id.sobat.sobatconselor.R
import java.util.*

class RvaInChat(context: Context, private val listInChat: List<HashMap<String, Any?>>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (listInChat[viewType]["from"].toString() == "cons") {
            true -> return VhInChat(inflater.inflate(R.layout.in_chat_t, parent, false))
            false -> return VhInChat(inflater.inflate(R.layout.in_chat_f, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return listInChat.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as VhInChat
        holder.tvTextInChat?.text = listInChat[position]["text"].toString()
        if (listInChat[position]["date"] != null) {
            val time = (listInChat[position]["date"] as Date).time
            holder.tvDateInChat?.text = DataLocal.getTimeAgo(time)
        } else {
            holder.tvDateInChat?.visibility = View.GONE
        }
    }

}