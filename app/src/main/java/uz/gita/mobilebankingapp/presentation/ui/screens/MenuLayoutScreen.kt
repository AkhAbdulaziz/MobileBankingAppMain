package uz.gita.mobilebankingapp.presentation.ui.screens

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.navigation.NavigationView
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.ScreenMenuLayoutBinding
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

class MenuLayoutScreen : Fragment(R.layout.screen_menu_layout) {
    private val binding by viewBinding(ScreenMenuLayoutBinding::bind)
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        val drawerLayout: DrawerLayout = view.findViewById(R.id.drawerLayout)
        val navView: NavigationView = view.findViewById(R.id.navView)

        toggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            R.string.open,
            R.string.close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        navView.setNavigationItemSelectedListener { MenuItem ->
            when (MenuItem.itemId) {
                R.id.nav_settings -> {
                    showToast("Settings selected")
                }
                R.id.nav_theme -> {
                    showToast("them selected")
                }
                R.id.nav_share -> {
                    showToast("share selected")
                }
                R.id.nav_help -> {
                    showToast("help selected")
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}