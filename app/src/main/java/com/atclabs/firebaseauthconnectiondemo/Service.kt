package com.atclabs.firebaseauthconnectiondemo

/*
* Descripcion:
* ViewBinding y FirebaseAuth: Se inicializan para gestionar la interfaz de usuario y la autenticación
* de Firebase.
* readFireStoreData(): Esta función consulta la base de datos de Firestore para obtener la información
* del usuario logueado (nombre, país).
* Consulta en Firestore: Se realiza una consulta en la colección "users" usando el userId del usuario
* autenticado. Si el documento existe, se muestra la información; si no, se muestra un mensaje
* de error o "No data found".
* fecha:29-01-2025
* version 1.0
*
* */

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.atclabs.firebaseauthconnectiondemo.databinding.ActivityServiceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Service : AppCompatActivity() {

    //  // Declarar ViewBinding y FirebaseAuth
    private lateinit var binding: ActivityServiceBinding  // Binding para interactuar con la interfaz de usuario
    private lateinit var auth: FirebaseAuth // Instancia de FirebaseAuth para gestionar la autenticación del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar ViewBinding
        binding = ActivityServiceBinding.inflate(layoutInflater) // Configurar el view binding para acceder a las vistas del layout
        auth = FirebaseAuth.getInstance() // Inicializar FirebaseAuth
        setContentView(binding.root) // Establecer la vista raíz del layout como contenido de la actividad

        // Llamar a la función para leer datos desde Firestore
        readFireStoreData()  // Obtener datos desde Firestore
    }
    // Función para leer los datos del usuario desde Firestore
    private fun readFireStoreData() {
        val currentUser = auth.currentUser // Obtener el usuario autenticado actual
        if (currentUser == null) { // Verificar si el usuario está logueado
            Log.e(TAG, "User not logged in") // Registrar un error si no hay usuario logueado
            return // Salir de la función si no hay un usuario autenticado
        }

        val userId = currentUser.uid // Obtener el ID único del usuario logueado
        val db = FirebaseFirestore.getInstance() // Obtener una instancia de Firebase Firestore

        // Acceder a la colección "users" y consultar por el UID del usuario
        db.collection("users")
            .document(userId) // Asumir que el UID del usuario es el ID del documento
            .get() // Obtener el documento desde Firestore
            .addOnCompleteListener { task -> // Manejar el resultado de la consult
                if (task.isSuccessful) {  // Verificar si la tarea fue exitosa
                    val document = task.result  // Obtener el resultado de la tarea
                    if (document != null && document.exists()) { // Si el documento existe
                        // Extraer la información del usuario (país y nombre) desde Firestore
                        val country = document.getString("country") ?: "N/A"
                        val name = document.getString("name") ?: "Unknown"
                        // Registrar el dato del país
                        // Actualizar la interfaz de usuario con los datos obtenidos
                        Log.d(TAG, "Retrieved country: $country")
                        // Update the UI with the retrieved data
                        binding.readCountryId.setText(country)
                        binding.nameId.setText(name)
                    } else {
                        Log.d(TAG, "No such document for user $userId") // Registrar si no se encontró el documento para el usuario
                        binding.readCountryId.setText("No data found") // Mostrar "No data found" en la interfaz
                    }
                } else {// //Registrar un error si la tarea falla
                    Log.e(TAG, "Error getting document", task.exception)
                }
            }
    }
}
