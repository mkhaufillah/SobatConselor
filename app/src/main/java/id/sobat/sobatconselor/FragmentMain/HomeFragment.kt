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
import id.sobat.sobatconselor.Adapter.RvaCurhat
import id.sobat.sobatconselor.AuthActivity
import id.sobat.sobatconselor.Model.ConselorId
import id.sobat.sobatconselor.Model.Curhat
import id.sobat.sobatconselor.Model.CurhatId
import id.sobat.sobatconselor.Model.DataLocal
import id.sobat.sobatconselor.NotificationActivity
import id.sobat.sobatconselor.ProfileActivity
import id.sobat.sobatconselor.R

class HomeFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: RvaCurhat
    private var curhats = ArrayList<CurhatId>()
    private lateinit var srHome: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Setting toolbar
        setHasOptionsMenu(true)
        val titleBar = activity!!.findViewById<TextView>(R.id.title_bar)
        titleBar.visibility = View.VISIBLE
        titleBar.text = getString(R.string.home)

        mAuth = FirebaseAuth.getInstance()

        // Refresh layout
        srHome = view.findViewById(R.id.sr_home)
        getDbCons(view)
        srHome.setOnRefreshListener {
            getDbCons(view)
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.notification_home -> {
                val intent = Intent(context, NotificationActivity::class.java)
                context?.startActivity(intent)
                return true
            }
            R.id.profile_home -> {
                val intent = Intent(context, ProfileActivity::class.java)
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

    private fun initRvCurhat(context: Context, rv: RecyclerView) {
        adapter = RvaCurhat(context, curhats)
        rv.adapter = adapter
        rv.setHasFixedSize(false)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = linearLayoutManager
    }

    private fun getDbCons(view: View) {
        val rvCurhat = view.findViewById<RecyclerView>(R.id.rv_curhat)
        srHome.isRefreshing = true
        db.collection("curhats")
                .whereEqualTo("accept", false)
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.d(DataLocal.TAG_QUERY, "Error getting documents: ", e)
                    } else {
                        curhats = ArrayList()
                        for (doc: DocumentSnapshot in value!!) {
                            val curhat = doc.toObject(CurhatId::class.java)
                            curhat?.idCurhat = doc.id
                            curhats.add(curhat!!)
                        }
                        initRvCurhat(view.context, rvCurhat)
                        srHome.isRefreshing = false
                    }
                }
    }
}
