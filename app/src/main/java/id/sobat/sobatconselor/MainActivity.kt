package id.sobat.sobatconselor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import id.sobat.sobatconselor.FragmentMain.ChatFragment
import id.sobat.sobatconselor.FragmentMain.DonationFragment
import id.sobat.sobatconselor.FragmentMain.ForumFragment
import id.sobat.sobatconselor.FragmentMain.HomeFragment
import id.sobat.sobatconselor.Model.DataLocal
import id.sobat.sobatconselor.Model.DataLocal.Companion.TAG_NICKNAME
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get width
        val dm = resources.displayMetrics
        DataLocal.width = dm.widthPixels

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        mAuth = FirebaseAuth.getInstance()

        setBottomBar()
    }

    private fun setBottomBar() {
        // Bottom bar
        // Get element of bottom navigation bar
        val bNav = findViewById<AHBottomNavigation>(R.id.bottom_navigation)

        // Create item
        val home = AHBottomNavigationItem(R.string.home, R.drawable.ic_home, R.color.colorPrimary)
        val transaction = AHBottomNavigationItem(R.string.chat, R.drawable.ic_chat, R.color.colorPrimary)
        val timeline = AHBottomNavigationItem(R.string.forum, R.drawable.ic_rss, R.color.colorPrimary)
        val message = AHBottomNavigationItem(R.string.donation, R.drawable.ic_money, R.color.colorPrimary)

        // Add item
        bNav.addItem(home)
        bNav.addItem(transaction)
        bNav.addItem(timeline)
        bNav.addItem(message)

        // Disable the translation inside the CoordinatorLayout
        bNav.isBehaviorTranslationEnabled = true

        // Change colors
        bNav.setColoredModeColors(Color.parseColor("#FFFFFF"), Color.parseColor("#BDBDBD"))

        // Force to tint the drawable (useful for font with icon for example)
        bNav.isForceTint = true

        // Display color under navigation bar (API 21+)
        // Don't forget these lines in your style-v21
        // <item name="android:windowTranslucentNavigation">true</item>
        // <item name="android:fitsSystemWindows">true</item>
        bNav.isTranslucentNavigationEnabled = true

        // Manage titles
        bNav.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        // Use colored navigation with circle reveal effect
        bNav.isColored = true

        // Customize notification (title, background, typeface)
//        bNav.setNotificationBackgroundColor(Color.parseColor("#F4511E"))

        // Add or remove notification for each item
//        bNav.setNotification("New", 3)

        // Set listeners
        bNav.setOnTabSelectedListener({ position, _ ->
            val trans = supportFragmentManager.beginTransaction()
            when (position) {
                0 -> trans.replace(R.id.content, HomeFragment())
                1 -> trans.replace(R.id.content, ChatFragment())
                2 -> trans.replace(R.id.content, ForumFragment())
                3 -> trans.replace(R.id.content, DonationFragment())
            }
            trans.commit()
            true
        })

        // Set current item programmatically
        bNav.currentItem = 0
    }

    private fun checkDb() {
        db.collection("conselors").document("${mAuth.currentUser?.uid}").get()
                .addOnCompleteListener {
                    if (it.result.exists() && it.isSuccessful) {
                        DataLocal.user = hashMapOf(
                                "id_user" to it.result.id,
                                "name" to it.result["name"],
                                "point" to it.result["point"],
                                "photo" to it.result["photo"],
                                "cover" to it.result["cover"],
                                "client" to it.result["client"],
                                "description" to it.result["description"],
                                "email" to it.result["email"],
                                "location" to it.result["location"],
                                "birth" to it.result["birth"],
                                "date" to it.result["date"]
                        )
                    } else if (!it.result.exists()) {
                        val user: HashMap<String, Any?> = hashMapOf(
                                "id_user" to mAuth.currentUser?.uid,
                                "name" to mAuth.currentUser?.displayName,
                                "point" to 0,
                                "photo" to null,
                                "cover" to null,
                                "client" to 0,
                                "description" to "Tidak ada deskripsi",
                                "email" to mAuth.currentUser?.displayName,
                                "location" to null,
                                "birth" to null,
                                "date" to Date()
                        )
                        db.collection("conselors").document("${mAuth.currentUser?.uid}").set(user)
                                .addOnCompleteListener {
                                    if (!it.isSuccessful) {
                                        Toast.makeText(applicationContext, getString(R.string.error_database), Toast.LENGTH_SHORT).show()
                                        Log.w(TAG_NICKNAME, "Error getting documents.", it.exception)
                                    }
                                }
                    } else {
                        Log.w(DataLocal.TAG_NICKNAME, "Error getting documents.", it.exception)
                    }
                }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            checkDb()
        }
    }

    override fun onBackPressed() {
        if (DataLocal.out) {
            super.onBackPressed()
            return
        }

        DataLocal.out = true
        Toast.makeText(this, getString(R.string.close_back_click), Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ DataLocal.out = false }, 2000)
    }
}
