package com.domagojleskovic.constantio

import android.os.Bundle

class SignUpActivity : EmailPasswordActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent?.getStringExtra("email") ?: "Empty"
        val password = intent?.getStringExtra("password") ?: "Empty"
        createAccount(email,password)
    }

}