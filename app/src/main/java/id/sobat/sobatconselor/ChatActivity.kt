package id.sobat.sobatconselor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import id.sobat.sobatconselor.Adapter.RvaInChat
import id.sobat.sobatconselor.Model.DataLocal
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: RvaInChat
    private val data = ArrayList<HashMap<String, Any?>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.chat_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        mAuth = FirebaseAuth.getInstance()

        val id = intent.getStringExtra(DataLocal.DATA_KEY_SHARE)
        val ivInChat = findViewById<ImageView>(R.id.iv_in_chat)
        val tvNameInChat = findViewById<TextView>(R.id.tv_name_in_chat)
        val etInChat = findViewById<EditText>(R.id.et_in_chat)
        val ivSendInChat = findViewById<ImageView>(R.id.iv_send_in_chat)
        val rvInChat = findViewById<RecyclerView>(R.id.rv_in_chat)

        ivSendInChat.setOnClickListener {
            val text = etInChat.text.toString()

            if (text == "") return@setOnClickListener

            val dataPart: HashMap<String, Any?> = hashMapOf(
                    "text" to text,
                    "date" to Date(),
                    "from" to "user"
            )

            db.collection("chats/$id/messages").document().set(dataPart)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            etInChat.setText("")
                        } else {
                            Toast.makeText(applicationContext, getString(R.string.error_database), Toast.LENGTH_SHORT).show()
                            Log.w(DataLocal.TAG_QUERY, "Error getting documents.", it.exception)
                        }
                    }
        }

        db.collection("chats")
                .document(id)
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        ivInChat.setImageResource(Integer.parseInt(it.result["avatar"].toString()))
                        tvNameInChat.text = it.result["nickname"].toString()
                        if (Integer.parseInt(it.result["unread_cons"].toString()) > 0) {
                            db.collection("chats")
                                    .document(id)
                                    .update("unread_cons", 0)
                                    .addOnSuccessListener {
                                        Log.d(DataLocal.TAG_QUERY, "DocumentSnapshot successfully updated!");
                                    }
                                    .addOnFailureListener {
                                        Log.w(DataLocal.TAG_QUERY, "Error updating document", it)
                                    }

                        }
                    } else {
                        Log.d(DataLocal.TAG_QUERY, "Error getting documents: ", it.exception)
                    }
                }

        initRVForums(rvInChat)

        db.collection("chats/$id/messages")
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w(DataLocal.TAG_QUERY, "Listen failed.", e)
                    } else {
                        for (doc: DocumentChange in value.documentChanges) {
                            if (doc.type == DocumentChange.Type.ADDED) {
                                val dataPart: HashMap<String, Any?> = hashMapOf(
                                        "id_message" to doc.document.id,
                                        "text" to doc.document.data["text"],
                                        "date" to doc.document.data["date"],
                                        "from" to doc.document.data["from"]
                                )
                                if (dataPart["from"] == "user") etInChat.setText("")
                                data.add(dataPart)
                                adapter.notifyDataSetChanged()
                                rvInChat.smoothScrollToPosition(data.size - 1)
                            }
                        }
                    }
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.in_chat_menu, menu)
        return true
    }

    private fun initRVForums(rv: RecyclerView) {
        adapter = RvaInChat(this, data)
        rv.adapter = adapter
        rv.setHasFixedSize(false)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.stackFromEnd = true
        rv.layoutManager = linearLayoutManager
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
