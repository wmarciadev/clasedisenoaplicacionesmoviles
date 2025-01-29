/*
* Descripcion del script
* Explicación breve:
* ViewBinding: Se usa para acceder a los elementos del layout sin usar findViewById.
* FirebaseAuth: Se utiliza para gestionar el proceso de autenticación de usuarios.
* Login Button: Cuando el usuario presiona el botón de login, se verifican las credenciales.
* Si son válidas, se redirige a otra actividad; si no, muestra un mensaje de error.
** fecha:29-01-2025
* version: 1.0
* */


package com.atclabs.firebaseauthconnectiondemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.atclabs.firebaseauthconnectiondemo.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    // Declare ViewBinding and FirebaseAuth
    private lateinit var binding: ActivityLoginBinding  // Para acceder a las vistas del layout
    private lateinit var auth: FirebaseAuth // Para gestionar la autenticación con Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)  // // Infla el layout usando ViewBinding
        setContentView(binding.root)   // Establece el layout en la actividad

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance() // Obtiene la instancia de FirebaseAuth

        // Handle login button click
        binding.loginBtn.setOnClickListener { // Configura el clic en el botón de login
            val email = binding.emailLoginId.text.trim().toString() // Obtiene el correo introducido
            val password = binding.passwordLoginId.text.trim().toString() // Obtiene la contraseña introducida


            // Si el correo y la contraseña no están vacíos, se intenta hacer login
            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)  // Llama a la función de login
            } else {
                showSnackbar("Verifigue su usuario y contrasena y trate de nuevo.") // Muestra un error si los campos están vacíos
            }
        }
    }
    //implementacion de la funcion login donde se pasan los parametros obtenidos de correo y password
    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Si el login es exitoso, abre la actividad Service
                    startActivity(Intent(this, Service::class.java))
                } else {
                    // Si el login falla, muestra un mensaje de error
                    showSnackbar("Ingrese un usuario y contrasena valido.")
                }
            }
    }

// Muestra un mensaje temporal en la parte inferior de la pantalla
    private fun showSnackbar(message: String) {
        // Intenta autenticar al usuario con Firebase
        Snackbar.make(binding.loginBtn, message, Snackbar.LENGTH_LONG).show()
    }
}
