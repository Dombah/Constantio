package com.domagojleskovic.constantio

import android.os.Bundle

class SignUpActivity : EmailPasswordActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = "slavko@gmail.com"
        val password = "password"
        createAccount(email,password)
    }
}