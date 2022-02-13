package edu.rosehulman.grouptodo

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.grouptodo.databinding.ActivityMainBinding
import edu.rosehulman.grouptodo.model.UserViewModel
import android.view.View
import android.widget.ImageView
import coil.load
import coil.transform.CircleCropTransformation
import edu.rosehulman.grouptodo.model.ListItemViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var headerView: View
    private lateinit var listItemModel: ListItemViewModel

    val signinLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { /* empty since the auth listener already responds .*/ }

    override fun onStart() {
        super.onStart()
        Firebase.auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        Firebase.auth.removeAuthStateListener(authStateListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeAuthListener()
        setSupportActionBar(binding.appBarMain.toolbar)
        listItemModel = ViewModelProvider(this).get(ListItemViewModel::class.java)
        binding.appBarMain.showFinished.setOnClickListener {
            listItemModel.toggleShowComplete()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        //updating user info into side bar navigation
        headerView = navView.getHeaderView(0)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_user, R.id.nav_groups, R.id.nav_list
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun initializeAuthListener() {
        authStateListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user = auth.currentUser
            if (user == null) {
                setupAuthUI()
            } else {
                with(user) {
                    Log.d(Constants.TAG, "user: $uid, $email, $displayName")
                }
                val userModel = ViewModelProvider(this).get(UserViewModel::class.java)
                userModel.getOrMakeUser {
                    if (userModel.hasCompletedSetup()) {
                        if (userModel.user!!.storageUriString.isNotEmpty()){
                            headerView.findViewById<TextView>(R.id.nav_user_name).setText(Firebase.auth.currentUser!!.displayName.toString())
                            headerView.findViewById<TextView>(R.id.nav_user_email).setText(Firebase.auth.currentUser!!.email.toString())
                            headerView.findViewById<ImageView>(R.id.nav_imageView)
                                .load(userModel.user!!.storageUriString){
                                crossfade(true)
                            }
                        }
                        val id = navController.currentDestination!!.id
                        if (id == R.id.nav_list) { // or your starting fragment
                            findNavController(R.id.nav_host_fragment_content_main)
                                .navigate(R.id.nav_list)}
                    } else {
                        navController.navigate(R.id.nav_user_edit)
                    }
                }
            }
        }
    }

    private fun setupAuthUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
//            AuthUI.IdpConfig.PhoneBuilder().build(),
//            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signinIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setLogo(R.mipmap.ic_launcher_round)
            .build()
        signinLauncher.launch(signinIntent)
    }

}