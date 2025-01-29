package com.atclabs.firebaseauthconnectiondemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.atclabs.firebaseauthconnectiondemo.databinding.ActivityThankyouBinding

class Thankyou : AppCompatActivity() {

    private lateinit var binding: ActivityThankyouBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityThankyouBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the button
        binding.portalBtn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }
}
