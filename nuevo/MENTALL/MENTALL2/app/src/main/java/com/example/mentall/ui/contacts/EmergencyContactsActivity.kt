package com.example.mentall.ui.contacts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mentall.R

class EmergencyContactsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_contacts)
        title = "Contactos de emergencia"
    }
}
