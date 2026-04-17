package com.example.solorguard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefs = requireActivity().getSharedPreferences("SolarGuard_Prefs", Context.MODE_PRIVATE)

        view.findViewById<TextView>(R.id.btn_clear_data).setOnClickListener {
            showIOSDialog("Clear All Data", "This will reset all your profile information to default. Continue?", "Reset") {
                sharedPrefs.edit().clear().apply() // Barcha ma'lumot o'chdi
                Toast.makeText(context, "All data reset to default", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<TextView>(R.id.btn_delete_account).setOnClickListener {
            showIOSDialog("Delete Account", "Your account and data will be permanently removed.", "Delete") {
                sharedPrefs.edit().clear().apply()
                requireActivity().finishAffinity() // Ilovadan butunlay chiqish
            }
        }

        view.findViewById<TextView>(R.id.btn_logout).setOnClickListener {
            showIOSDialog("Log Out", "Are you sure you want to exit?", "Log Out") {
                requireActivity().finish()
            }
        }
    }

    private fun showIOSDialog(title: String, message: String, posBtn: String, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton("Cancel", null)
            .setPositiveButton(posBtn) { _, _ -> onConfirm() }
            .show()
    }
}