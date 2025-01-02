package com.example.kotlintodopractice.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kotlintodopractice.R
import com.example.kotlintodopractice.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setupSpinners()
        loadSettings()

        binding.saveButton.setOnClickListener {
            saveSettings()
        }

        binding.backButton.setOnClickListener {
            navController.navigate(R.id.action_settingsFragment_to_homeFragment)
        }
    }

    private fun setupSpinners() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.unit_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.unitSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.language_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.languageSpinner.adapter = adapter
        }
    }

    private fun loadSettings() {
        val userId = mAuth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        binding.nameEditText.setText(document.getString("name") ?: "")
                        binding.surnameEditText.setText(document.getString("surname") ?: "")
                        binding.ageEditText.setText(document.getLong("age")?.toString() ?: "")
                        binding.weightEditText.setText(document.getDouble("weight")?.toString() ?: "")
                        binding.heightEditText.setText(document.getDouble("height")?.toString() ?: "")
                        binding.unitSpinner.setSelection(if (document.getString("unit") == "metric") 0 else 1)
                        binding.languageSpinner.setSelection(if (document.getString("language") == "english") 0 else 1)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("SettingsFragment", "Failed to load settings", e)
                    Toast.makeText(context, "Failed to load settings: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveSettings() {
        val userId = mAuth.currentUser?.uid
        if (userId != null) {
            val settingsMap = hashMapOf(
                "name" to binding.nameEditText.text.toString(),
                "surname" to binding.surnameEditText.text.toString(),
                "age" to (binding.ageEditText.text.toString().toIntOrNull() ?: 0),
                "weight" to (binding.weightEditText.text.toString().toDoubleOrNull() ?: 0.0),
                "height" to (binding.heightEditText.text.toString().toDoubleOrNull() ?: 0.0),
                "unit" to if (binding.unitSpinner.selectedItemPosition == 0) "metric" else "imperial",
                "language" to if (binding.languageSpinner.selectedItemPosition == 0) "english" else "afrikaans"
            )

            db.collection("users").document(userId).set(settingsMap)
                .addOnSuccessListener {
                    Toast.makeText(context, "Settings saved successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("SettingsFragment", "Failed to save settings", e)
                    Toast.makeText(context, "Failed to save settings: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}