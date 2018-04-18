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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import id.sobat.sobatconselor.Adapter.RvaForum
import id.sobat.sobatconselor.AuthActivity
import id.sobat.sobatconselor.CreateForumActivity
import id.sobat.sobatconselor.HistForumActivity
import id.sobat.sobatconselor.Model.DataLocal
import id.sobat.sobatconselor.Model.ForumId
import id.sobat.sobatconselor.R
import kotlin.collections.ArrayList

class ForumFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var srForums: SwipeRefreshLayout
    private var forums = ArrayList<ForumId>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_forum, container, false)
        // Setting toolbar
        setHasOptionsMenu(true)
        val titleBar = activity!!.findViewById<TextView>(R.id.title_bar)
        titleBar.visibility = View.VISIBLE
        titleBar.text = getString(R.string.forum)

        mAuth = FirebaseAuth.getInstance()

        srForums = view.findViewById(R.id.sr_forums)
        srForums.setOnRefreshListener {
            getDbCons(view)
            srForums.isRefreshing = false
        }

        getDbCons(view)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.forum_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.edit_forum -> {
                val intent = Intent(context, CreateForumActivity::class.java)
                context?.startActivity(intent)
                return true
            }
            R.id.archive_forum -> {
                val intent = Intent(context, HistForumActivity::class.java)
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

    private fun initRVForums(context: Context, rv: RecyclerView) {
        val adapter = RvaForum(context, forums)
        rv.adapter = adapter
        rv.setHasFixedSize(false)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = linearLayoutManager
    }

    private fun getDbCons(view: View) {
        val rvForums = view.findViewById<RecyclerView>(R.id.rv_forums)
        srForums.isRefreshing = true

        db.collection("forums")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        forums = ArrayList()
                        for (doc in it.result) {
                            val forum = doc.toObject(ForumId::class.java)
                            forum.idForum = doc.id
                            forums.add(forum)
                        }
                        srForums.isRefreshing = false
                        initRVForums(view.context, rvForums)
                    } else {
                        Log.d(DataLocal.TAG_QUERY, "Error getting documents: ", it.exception)
                    }
                }
    }

}
