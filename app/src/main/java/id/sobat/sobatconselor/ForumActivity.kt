package id.sobat.sobatconselor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import id.sobat.sobatconselor.Model.DataLocal

class ForumActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum)

        mAuth = FirebaseAuth.getInstance()

        val tvNameAuthor = findViewById<TextView>(R.id.tv_name_author)
        val tvDateForum = findViewById<TextView>(R.id.tv_date_forum)
        val ivAvatarAuthor = findViewById<ImageView>(R.id.iv_avatar_author)
        val ivForum = findViewById<ImageView>(R.id.iv_forum)
        val tvTitleForum = findViewById<TextView>(R.id.tv_title_forum)
        val tvTextForum = findViewById<TextView>(R.id.tv_text_forum)
        val key = intent.getStringExtra(DataLocal.DATA_KEY_SHARE)

        db.collection("forums").document(key)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val data: HashMap<String, Any?> = hashMapOf(
                                "author" to it.result["author"],
                                "date" to it.result["date"],
                                "name" to it.result["name"],
                                "nickname" to it.result["nickname"],
                                "text" to it.result["text"],
                                "title" to it.result["title"],
                                "avatar" to it.result["avatar"],
                                "photo" to it.result["photo"],
                                "banner" to it.result["banner"]
                        )

                        if (data["name"] != null && data["photo"] != null) {
                            tvNameAuthor.text = data["name"].toString()
                            Picasso.get()
                                    .load(data["photo"].toString())
                                    .placeholder(R.color.ic_launcher_background)
                                    .error(R.drawable.default_forum_img)
                                    .into(ivAvatarAuthor)
                        } else {
                            tvNameAuthor.text = data["nickname"].toString()
                            ivAvatarAuthor.setImageResource(Integer.parseInt(data["avatar"].toString()))
                        }
                        tvDateForum.text = data["date"].toString()
                        tvTitleForum.text = data["title"].toString()
                        tvTextForum.text = data["text"].toString()

                        Picasso.get()
                                .load(data["banner"].toString())
                                .placeholder(R.color.ic_launcher_background)
                                .error(R.drawable.default_forum_img)
                                .into(ivForum)

                    } else {
                        Log.d(DataLocal.TAG_QUERY, "Error getting documents: ", it.exception)
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
