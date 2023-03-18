package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    // Creamos la referencia del objeto FirebaseAuth
    private lateinit var auth: FirebaseAuth

    //Referencia a componentes de nuestro Layout
    private lateinit var btnLogin: Button
    private lateinit var textViewRegister: TextView

    private lateinit var googleSignInButton: SignInButton
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        googleSignInButton = findViewById(R.id.google_sign_in_button)
        googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Inicializamos el objeto FirebaseAuth
        auth = FirebaseAuth.getInstance()
        btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
            val password = findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
            this.login(email, password)

        }

        textViewRegister = findViewById<TextView>(R.id.textViewRegister)

        textViewRegister.setOnClickListener {
            this.goToRegister()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val intent = Intent( this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: ApiException) {
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val RC_SIGN_IN = 123
    }
    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent( this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    private fun goToRegister() {
        val intent = Intent( this, RegisterActivity::class.java)
        startActivity(intent)

    }
}

