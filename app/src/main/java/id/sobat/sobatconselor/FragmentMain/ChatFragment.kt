package id.sobat.sobatconselor.FragmentMain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import id.sobat.sobatconselor.Adapter.RvaChat
import id.sobat.sobatconselor.AuthActivity
import id.sobat.sobatconselor.Model.ChatId
import id.sobat.sobatconselor.Model.DataLocal
import id.sobat.sobatconselor.R
import id.sobat.sobatconselor.SearchActivity
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var srChat: SwipeRefreshLayout
    private lateinit var adapter: RvaChat
    private var chats = ArrayList<ChatId>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        // Setting toolbar
        setHasOptionsMenu(true)
        val titleBar = activity!!.findViewById<TextView>(R.id.title_bar)
        titleBar.visibility = View.VISIBLE
        titleBar.text = getString(R.string.chat)

        mAuth = FirebaseAuth.getInstance()

        srChat = view.findViewById(R.id.sr_chats)
        srChat.setOnRefreshListener {
            getDbCons(view)
        }

        // Init rv
        getDbCons(view)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.chat_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.search_chat -> {
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra(DataLocal.DATA_KEY_SHARE, "chat")
                context?.startActivity(intent)
                return true
            }
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            val intent = Intent(context, AuthActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun initRvChats(context: Context, rv: RecyclerView) {
        adapter = RvaChat(context, chats)
        rv.adapter = adapter
        rv.setHasFixedSize(false)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = linearLayoutManager
    }

    private fun getDbCons(view: View) {
        val rvChats = view.findViewById<RecyclerView>(R.id.rv_chats)
        srChat.isRefreshing = true
        db.collection("chats")
                .whereEqualTo("idCons", mAuth.currentUser?.uid)
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.d(DataLocal.TAG_QUERY, "Error getting documents: ", e)
                    } else {
                        chats = ArrayList()
                        for (doc: DocumentSnapshot in value!!) {
                            val chat = doc.toObject(ChatId::class.java)
                            chat?.idChat = doc.id
                            chats.add(chat!!)
                        }
                        initRvChats(view.context, rvChats)
                        srChat.isRefreshing = false
                    }
                }
    }

}
