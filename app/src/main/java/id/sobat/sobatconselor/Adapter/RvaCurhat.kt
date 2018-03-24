package id.sobat.sobatconselor.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.sobat.sobatconselor.R
import java.util.*

class RvaCurhat(context: Context, private val listChat: List<HashMap<String, Any?>>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private val listenerClick = View.OnClickListener {
        //        val vHolder = it.tag as VhCurhat
//        val idCurhat = listChat[vHolder.adapterPosition]["id_curhat"].toString()
//        val intent = Intent(context, ChatActivity::class.java)
//        intent.putExtra(DataLocal.DATA_KEY_SHARE, idCurhat)
//        context.startActivity(intent)
    }

    override fun getItemViewType(position: Int): Int {
        if (position >= listChat.size) {
            return 1
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        if (viewType == 0) {
            view = inflater.inflate(R.layout.curhat, parent, false)
            return VhCurhat(view)
        }
        view = inflater.inflate(R.layout.loading, parent, false)
        return VhLoading(view)
    }

    override fun getItemCount(): Int {
        return listChat.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            val holder0 = holder as VhCurhat

            var nicknameUser = listChat[position]["nickname"].toString()
            if (nicknameUser.length > 24) {
                nicknameUser = nicknameUser.substring(0, 24) + "..."
            }
            val text = listChat[position]["text"].toString()
            val date = listChat[position]["date"].toString()

            holder0.btnCurhat?.setOnClickListener(listenerClick)
            holder0.btnCurhat?.tag = holder0
            holder0.tvNameCurhat?.text = nicknameUser
            holder0.tvTextCurhat?.text = text
            holder0.tvDateCurhat?.text = date
            holder0.ivCurhat?.setImageResource(Integer.parseInt(listChat[position]["avatar"].toString()))
        }
    }

}