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
import id.sobat.sobatconselor.Model.Forum

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
                        val forum = it.result.toObject(Forum::class.java)

                        if (forum?.name != null && forum.photo != null) {
                            tvNameAuthor.text = forum.name
                            Picasso.get()
                                    .load(forum.photo)
                                    .placeholder(R.color.ic_launcher_background)
                                    .error(R.drawable.default_forum_img)
                                    .into(ivAvatarAuthor)
                        } else {
                            tvNameAuthor.text = forum?.nickname.orEmpty()
                            ivAvatarAuthor.setImageResource(forum?.avatar!!)
                        }
                        tvDateForum.text = forum.date.toString()
                        tvTitleForum.text = forum.title.orEmpty()
                        tvTextForum.text = forum.text.orEmpty()

                        Picasso.get()
                                .load(forum.banner.orEmpty())
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
