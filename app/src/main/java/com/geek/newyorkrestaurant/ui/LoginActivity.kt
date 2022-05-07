package com.geek.newyorkrestaurant.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.geek.newyorkrestaurant.R
import com.geek.newyorkrestaurant.databinding.ActivityLoginBinding
import com.geek.newyorkrestaurant.newYorkApp
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private var user: User? = null
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = newYorkApp.currentUser()

        if(user != null){
            startActivity(Intent(this, NewYorkActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener{
            login(false)
        }
        binding.buttonCreate.setOnClickListener{
            login(true)
        }
    }

    private fun login(createUser: Boolean) {

        if(!validateCredentials()){
            onLoginFailed("Fields cannot be empty")
            return
        }

        // while this operation completes, disable the buttons to login or create a new account
        binding.buttonLogin.isEnabled = false
        binding.buttonCreate.isEnabled = false

        val email = binding.username.text.toString()
        val password = binding.password.text.toString()


        if(createUser){
            //register  a user
            newYorkApp.emailPassword.registerUserAsync(email, password){
                if(it.isSuccess){
                    Timber.d("User successfully registered")
                    // when the account has been created successfully, log in to the account
                    login(false)
                }else {
                    onLoginFailed(it.error.errorMessage ?: "An error occurred on registering")
                    enableButtons()
                }
            }

        } else {
            val credentials = Credentials.emailPassword(email, password)
            newYorkApp.loginAsync(credentials){
                if(!it.isSuccess){
                    enableButtons()
                    onLoginFailed(it.error.errorMessage ?: "An error occurred")
                } else {
                    startActivity(Intent(this, NewYorkActivity::class.java))
                }
            }
        }
    }

    private fun enableButtons() {
        binding.buttonCreate.isEnabled = true
        binding.buttonLogin.isEnabled = true
    }

    private fun onLoginFailed(errorMsg: String) {

        Timber.e(errorMsg)
        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }

    private fun validateCredentials(): Boolean = when {
        // zero-length usernames and passwords are not valid (or secure), so prevent users from creating accounts with those client-side.
        binding.username.text.toString().isEmpty() -> false
        binding.username.text.toString().isEmpty() -> false
        else -> true
    }
}