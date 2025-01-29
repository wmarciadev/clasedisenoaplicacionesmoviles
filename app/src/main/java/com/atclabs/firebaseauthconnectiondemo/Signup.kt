package com.atclabs.firebaseauthconnectiondemo
/*
*Inicia FirebaseAuth y Firestore: Se inicializan las herramientas
* necesarias para la autenticación de Firebase y la base de datos Firestore.
Crea un nuevo usuario: Si el correo y la contraseña son válidos, se registra
* un nuevo usuario en Firebase Authentication y luego se guarda información adicional
* en Firestore.
* Guarda información adicional: Se obtiene el UID del usuario logueado y se almacena
* información adicional como nombre, ciudad, país y teléfono en Firestore, asociada a ese UID
* fecha: 29 de enero 2025
* version: 1.0
 */

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log //Para registrar mensajes en la consola durante la ejecución
import androidx.appcompat.app.AppCompatActivity
import com.atclabs.firebaseauthconnectiondemo.databinding.ActivitySignupBinding
//vincula elementos de la interfaz gráfica (activity_signup.xml) con el código
import com.google.android.material.snackbar.Snackbar  //Muestra mensajes flotantes al usuario
import com.google.firebase.auth.FirebaseAuth  //Para autenticar usuarios con Firebase.
import com.google.firebase.firestore.ktx.firestore // Para acceder a Firestore, la base de datos en la nube.
import com.google.firebase.ktx.Firebase



class Signup : AppCompatActivity() {
/*
binding: Maneja los elementos de la interfaz con ViewBinding.
auth: Instancia para manejar la autenticación con Firebase.
db: Referencia a la base de datos Firestore.
 */
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vincula el diseño XML (activity_signup.xml) con el código.
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtiene una instancia de FirebaseAuth para manejar la autenticación.
        auth = FirebaseAuth.getInstance()

        /*
        * mail: El correo electrónico del usuario.
        * password: La contraseña del usuario.
        * Si los campos no están vacíos, llama al método createUser para registrar al usuario.
        * Si algún campo está vacío, muestra un Snackbar con un mensaje de error
        * */
        binding.createBtn.setOnClickListener {
            val email = binding.emailSignupId.text.trim().toString()
            val password = binding.passwordSignupId.text.trim().toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                createUser(email, password)
            } else {
                Snackbar.make(
                    binding.createBtn,
                    "Check your username and password then try again",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
    /*
    * createUserWithEmailAndPassword
    *Intenta registrar al usuario en Firebase Authentication usando el correo y contraseña
    *  proporcionados.
    addOnCompleteListener: Escucha si la operación fue exitosa o falló.
    * Si es exitoso:
    * Llama a newCustomer para guardar información adicional del usuario en Firestore.
    * Navega a la actividad Thankyou (pantalla de agradecimiento).
    * Si falla:
    * Muestra un mensaje con un Snackbar indicando que los datos son inválidos
    * (por ejemplo, contraseña menor a 6 caracteres).
    *
    * */

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Llama a la función newCustomer después de crear exitosamente el usuario
                    newCustomer()

                    // Navega a la pantalla de agradecimiento
                    startActivity(Intent(this, Thankyou::class.java))
                } else {
                    // Muestra un mensaje de error si la creación del usuario falla
                    Snackbar.make(
                        binding.createBtn,
                        "Ingrese un usuario y contrasena valida (6 caracteres)",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    /*
    *
    * Obtiene el usuario actual (currentUser) de Firebase Authentication.
    *  Si no hay un usuario logueado, registra un error en la consola y termina el método.
    *
    * */
    private fun newCustomer() {
        val currentUser = auth.currentUser // Obtener el usuario logueado
        if (currentUser == null) { // Si no hay usuario logueado, terminar
            Log.e(TAG, "usuario no tiene inicio de sesión")
            return
        }

        val userId = currentUser.uid // // Obtener el UID del usuario logueado

        /*
        *Crea un mapa (HashMap) con la información adicional del cliente obtenida
        * de los campos en la interfaz gráfica:
        * name: Nombre del usuario.
        * city: Ciudad del usuario.
        * country: País del usuario.
        * phoneNumber: Número de teléfono.
        * */
        val customer = hashMapOf(
            "name" to binding.nameId.text.toString().trim(),
            "city" to binding.cityId.text.toString().trim(),
            "country" to binding.countryId.text.toString().trim(),
            "phoneNumber" to binding.phoneId.text.toString().trim()
        )

        Log.d(TAG, "User id: $userId, $customer")
        //  Guarda el documento con el UID del usuario como ID del documento
        db.collection("users")
            .document(userId)    // Usar UID como ID de documento
            .set(customer) // evitar un ID generado automáticamente
            .addOnSuccessListener {
                Log.d(TAG, "Documento completamente escrito para el usuario $userId")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error escribiendo el documento", e) // Registrar un error si falla al guardar
            }
    }
}
