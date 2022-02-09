package ipca.am2.projeto2122.friendschat



import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import ipca.am2.projeto2122.friendschat.databinding.ActivityMainBinding
import ipca.am2.projeto2122.friendschat.ui.intro.WelcomeActivity
import ipca.am2.projeto2122.friendschat.ui.model.Users


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var referentUser : DatabaseReference?    = null
    private var firebaseUser : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setSupportActionBar(findViewById(R.id.toolbar_main))

        firebaseUser = FirebaseAuth.getInstance().currentUser
        referentUser = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        val toolbar : Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar?.setDisplayShowTitleEnabled(false)


        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_search,
                R.id.navigation_settings,
                R.id.navigation_chatting ,
                R.id.navigation_add_picture_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

            referentUser!!.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val user: Users? = p0.getValue(Users::class.java)
                    binding.textViewToolbarUserName.text = user?.getUsername().toString()
                    //Picasso.get().load(user.getProfile()).into(binding.imageViewToolbarProfileImage)

                }
            }
                override fun onCancelled(p0: DatabaseError) {

                }
        })

    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId)
        {
            R.id.action_sign_out ->
            {
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

                return true
            }
        }
        return false
    }

}