package com.example.solorguard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView

class ProfileFragment : Fragment() {

    private lateinit var profileImage: ShapeableImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvLocation: TextView

    private val sharedPrefs by lazy {
        requireActivity().getSharedPreferences("SolarGuard_Prefs", Context.MODE_PRIVATE)
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            imageUri?.let {
                profileImage.setImageURI(it)
                sharedPrefs.edit().putString("USER_IMAGE", it.toString()).apply()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        loadUserData()
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun initUI(view: View) {
        profileImage = view.findViewById(R.id.profile_image)
        tvUserName = view.findViewById(R.id.tv_user_name)
        val editFab = view.findViewById<View>(R.id.fab_edit_profile)
        val btnEditAll = view.findViewById<Button>(R.id.btn_edit_all)

        val emailRow = view.findViewById<View>(R.id.itemEmail)
        val phoneRow = view.findViewById<View>(R.id.itemPhone)
        val locationRow = view.findViewById<View>(R.id.itemLocation)

        tvEmail = emailRow.findViewById(R.id.info_text)
        tvPhone = phoneRow.findViewById(R.id.info_text)
        tvLocation = locationRow.findViewById(R.id.info_text)

        val openGallery = View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            pickImageLauncher.launch(intent)
        }
        profileImage.setOnClickListener(openGallery)
        editFab.setOnClickListener(openGallery)
        btnEditAll.setOnClickListener { showEditDialog() }

        setupInfoRow(emailRow, android.R.drawable.ic_dialog_email, "#007AFF")
        setupInfoRow(phoneRow, android.R.drawable.stat_sys_phone_call, "#34C759")
        setupInfoRow(locationRow, android.R.drawable.ic_dialog_map, "#FF9500")
    }

    private fun loadUserData() {
        tvUserName.text = sharedPrefs.getString("USER_NAME", "Aslzoda Bozorboyeva")
        tvEmail.text = sharedPrefs.getString("USER_EMAIL", "aslzoda@gmail.com")
        tvPhone.text = sharedPrefs.getString("USER_PHONE", "+998 99 999 99 99")
        // Manzilni xotiradan o'qiymiz, agar bo'sh bo'lsa default qiymat qo'yamiz
        tvLocation.text = sharedPrefs.getString("USER_LOCATION", "Khorezm, Uzbekistan")

        val savedImage = sharedPrefs.getString("USER_IMAGE", null)
        if (savedImage != null) {
            profileImage.setImageURI(Uri.parse(savedImage))
        } else {
            profileImage.setImageResource(R.drawable.user)
        }
    }

    private fun showEditDialog() {
        // Eski ma'lumotlarni saqlab turamiz (Cancel bosilsa qaytarish uchun)
        val oldName = tvUserName.text.toString()
        val oldEmail = tvEmail.text.toString()
        val oldPhone = tvPhone.text.toString()
        val oldLocation = tvLocation.text.toString()

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 40, 60, 10)
        }

        val etName = EditText(requireContext()).apply { hint = "Full Name"; setText(oldName) }
        val etEmail = EditText(requireContext()).apply { hint = "Email Address"; setText(oldEmail) }
        val etPhone = EditText(requireContext()).apply { hint = "Phone Number"; setText(oldPhone) }
        val etLocation = EditText(requireContext()).apply { hint = "Location"; setText(oldLocation) }

        // Real vaqtda yangilash funksiyasi (TextWatcher wrapper)
        fun EditText.addRealTimeSync(targetTextView: TextView) {
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    targetTextView.text = s.toString()
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        // Barcha fieldlarni real vaqtda sinxronlash
        etName.addRealTimeSync(tvUserName)
        etEmail.addRealTimeSync(tvEmail)
        etPhone.addRealTimeSync(tvPhone)
        etLocation.addRealTimeSync(tvLocation)

        layout.addView(etName)
        layout.addView(etEmail)
        layout.addView(etPhone)
        layout.addView(etLocation)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Update Profile")
            .setView(layout)
            .setCancelable(false)
            .setPositiveButton("Save") { _, _ ->
                sharedPrefs.edit().apply {
                    putString("USER_NAME", etName.text.toString())
                    putString("USER_EMAIL", etEmail.text.toString())
                    putString("USER_PHONE", etPhone.text.toString())
                    putString("USER_LOCATION", etLocation.text.toString())
                    apply()
                }
                Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Bekor qilinsa, UI-ni eski holatiga qaytaramiz
                tvUserName.text = oldName
                tvEmail.text = oldEmail
                tvPhone.text = oldPhone
                tvLocation.text = oldLocation
            }
            .show()
    }

    private fun setupInfoRow(rowView: View, iconRes: Int, iconBgColor: String) {
        val infoIcon = rowView.findViewById<ImageView>(R.id.info_icon)
        val iconCard = rowView.findViewById<com.google.android.material.card.MaterialCardView>(R.id.iconCard)
        infoIcon.setImageResource(iconRes)
        infoIcon.setColorFilter(Color.WHITE)
        iconCard.setCardBackgroundColor(Color.parseColor(iconBgColor))
    }
}