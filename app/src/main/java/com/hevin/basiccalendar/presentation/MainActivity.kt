package com.hevin.basiccalendar.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.hevin.basiccalendar.presentation.ui.CalendarFragment
import com.hevin.basiccalendar.R
import com.hevin.basiccalendar.common.Constants.BASE_TAG
import com.hevin.basiccalendar.databinding.ActivityMainBinding
import com.hevin.basiccalendar.utillities.FragmentSessionUtils

class MainActivity : AppCompatActivity() {

    private final val TAG = BASE_TAG + MainActivity::class.java.simpleName

//    lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        /*setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        // Set the content view using view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Use binding to access the root view (main) and apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val fragmentSessionUtils = FragmentSessionUtils.getInstance()

        if (savedInstanceState == null){
            val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
            if (currentFragment !is CalendarFragment) { fragmentSessionUtils.switchFragment(
                supportFragmentManager,
                CalendarFragment(),//HomeFragment.newInstance(param1 = "file1", param2 = "file2"),//HomeFragment(),
                false,
                )
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
    //https://www.youtube.com/watch?v=YOKDdAicnr4   33:00