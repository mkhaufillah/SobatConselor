package id.sobat.sobatconselor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import id.sobat.sobatconselor.Model.CurhatId
import id.sobat.sobatconselor.Model.Chat
import id.sobat.sobatconselor.Model.Message
import id.sobat.sobatconselor.Model.DataLocal
import java.util.Date

class CurhatActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private var curhat: CurhatId? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curhat)

        mAuth = FirebaseAuth.getInstance()

        val btnCurhat = findViewById<Button?>(R.id.btn_curhat)
        val ivCurhat = findViewById<ImageView?>(R.id.iv_curhat)
        val tvNameCurhat = findViewById<TextView?>(R.id.tv_name_curhat)
        val tvTextCurhat = findViewById<TextView?>(R.id.tv_text_curhat)
        val tvDateCurhat = findViewById<TextView?>(R.id.tv_date_curhat)
        val etCurhat = findViewById<EditText?>(R.id.et_curhat)
        val id = intent.getStringExtra(DataLocal.DATA_KEY_SHARE)

        db.collection("curhats")
                .document(id)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        curhat = it.result.toObject(CurhatId::class.java)
                        curhat?.idCurhat = it.result.id
                        ivCurhat?.setImageResource(curhat?.avatar!!)
                        tvNameCurhat?.text = curhat?.nickname.orEmpty()
                        tvTextCurhat?.text = curhat?.text.orEmpty()
                        tvDateCurhat?.text = curhat?.date.toString()
                    } else {
                        Log.d(DataLocal.TAG_QUERY, "Error getting documents: ", it.exception)
                    }
                }

        btnCurhat?.setOnClickListener {
            if (etCurhat?.text.toString() == "" || curhat == null) return@setOnClickListener
            btnCurhat.text = getString(R.string.wait)

            val chat = Chat()
            chat.idUser = curhat?.from.orEmpty()
            chat.idCons = DataLocal.user?.idConselor.orEmpty()
            chat.date = Date()
            chat.name = DataLocal.user?.name.orEmpty()
            chat.nickname = curhat?.nickname.orEmpty()
            chat.lastChat = etCurhat?.text.toString()
            chat.photo = DataLocal.user?.photo
            chat.avatar = curhat?.avatar
            chat.unreadCons = 0
            chat.unreadUser = 1

            db.collection("chats").document(id).set(chat)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            db.collection("curhats")
                                    .document(id)
                                    .update("accept", true)
                                    .addOnSuccessListener {
                                        Log.d(DataLocal.TAG_QUERY, "DocumentSnapshot successfully updated!")
                                    }
                                    .addOnFailureListener {
                                        Log.w(DataLocal.TAG_QUERY, "Error updating document", it)
                                    }

                            val message0 = Message()
                            message0.text = curhat?.text
                            message0.date = curhat?.date
                            message0.from = "user"

                            val message1 = Message()
                            message1.text = etCurhat?.text.toString()
                            message1.date = Date()
                            message1.from = "cons"

                            var x = 0

                            db.collection("chats/$id/messages").document().set(message1)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            x++
                                            if (x >= 2) finish()
                                        } else {
                                            Toast.makeText(this, getString(R.string.error_database), Toast.LENGTH_SHORT).show()
                                            Log.w(DataLocal.TAG_QUERY, "Error getting documents.", it.exception)
                                        }
                                    }
                            db.collection("chats/$id/messages").document().set(message0)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            x++
                                            if (x >= 2) finish()
                                        } else {
                                            Toast.makeText(this, getString(R.string.error_database), Toast.LENGTH_SHORT).show()
                                            Log.w(DataLocal.TAG_QUERY, "Error getting documents.", it.exception)
                                        }
                                    }
                        } else {
                            Toast.makeText(this, getString(R.string.error_database), Toast.LENGTH_SHORT).show()
                            Log.w(DataLocal.TAG_NICKNAME, "Error getting documents.", it.exception)
                        }
                    }
        }

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
