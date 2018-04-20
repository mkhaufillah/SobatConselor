package id.sobat.sobatconselor.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.sobat.sobatconselor.Model.ChatId
import id.sobat.sobatconselor.ChatActivity
import id.sobat.sobatconselor.Model.DataLocal
import id.sobat.sobatconselor.R

class RvaChat(context: Context, private val chats: List<ChatId>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private val listenerClick = View.OnClickListener {
        val vHolder = it.tag as VhChat
        val idChat = "${chats[vHolder.adapterPosition].idChat}"
        val intent = Intent(context, ChatActivity::class.java)
        intent.putExtra(DataLocal.DATA_KEY_SHARE, idChat)
        context.startActivity(intent)
    }

    override fun getItemViewType(position: Int): Int {
        if (position >= chats.size) return 1
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        if (viewType == 0) {
            view = inflater.inflate(R.layout.chat, parent, false)
            return VhChat(view)
        }
        view = inflater.inflate(R.layout.loading, parent, false)
        return VhLoading(view)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            val holder0 = holder as VhChat

            var nameCons = "${chats[position].nickname}"
            if (nameCons.length > 24) {
                nameCons = nameCons.substring(0, 24) + "..."
            }
            var lastChat = "${chats[position].lastChat}"
            if (lastChat.length > 56) {
                lastChat = lastChat.substring(0, 56) + "..."
            }
            val date = chats[position].date.toString()
            val unread = chats[position].unreadCons.toString()

            holder0.clChat?.setOnClickListener(listenerClick)
            holder0.clChat?.tag = holder0
            holder0.tvNameChat?.text = nameCons
            holder0.tvTextChat?.text = lastChat
            holder0.tvDateChat?.text = date
            holder0.ivChat?.setImageResource(chats[position].avatar!!)

            if (unread != "0") {
                holder0.lnChat?.visibility = View.VISIBLE
            } else {
                holder0.lnChat?.visibility = View.GONE
            }
        }
    }

}