package com.atclabs.firebaseauthconnectiondemo

import android.content.Intent   //sirve para navegar entre pantallas.
import android.os.Bundle
import androidx.activity.enableEdgeToEdge   ////es una función para optimizar el diseño en pantallas completas
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.atclabs.firebaseauthconnectiondemo.databinding.ActivityMainBinding
//permite usar ViewBinding para acceder a los elementos de la interfaz (botones, textos, etc.).
import com.google.android.material.snackbar.Snackbar  //muestra mensajes flotantes en pantalla.

class MainActivity : AppCompatActivity() {

    // Declare ViewBinding variable
    private lateinit var binding: ActivityMainBinding
    /*Declara una variable llamada binding.
lateinit: La inicializaremos más tarde
(cuando la interfaz esté lista).
ActivityMainBinding: Esta clase se genera automáticamente para vincular
elementos de tu diseño (activity_main.xml) con tu código.*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable Edge-to-Edge
        enableEdgeToEdge()

        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        * ActivityMainBinding.inflate(layoutInflater): Carga el diseño activity_main.xml
        * en la actividad usando ViewBinding.
setContentView(binding.root): Muestra el diseño en la pantalla.
        *
        * */

        /*
        * Ajusta el diseño para que no se superponga con las barras del sistema
        * (como la barra de estado o navegación).
        getInsets obtiene los márgenes necesarios para evitar estas barras.
        view.setPadding aplica los márgenes al diseño principal (binding.main).
        *
        *
        * */
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*
        *   signupBtn: Es un botón en el diseño.
            setOnClickListener: Define qué ocurre al hacer clic.
            Intent(this, Signup::class.java): Navega a la actividad Signup.
            Snackbar: Muestra un mensaje de error si algo falla.
        *
        *
        * */
        binding.signupBtn.setOnClickListener {
            try {
                startActivity(Intent(this, Signup::class.java))
            } catch (e: Exception) {
                Snackbar.make(binding.signupBtn, "Error: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }

        /*
        *   loginBtn: Es un botón en el diseño.
            setOnClickListener: Define qué ocurre al hacer clic.
            Intent(this, Signup::class.java): Navega a la actividad Login.
            Snackbar: Muestra un mensaje de error si algo falla.
        *
        * */


        binding.loginBtn.setOnClickListener {
            try {
                startActivity(Intent(this, Login::class.java))
            } catch (e: Exception) {
                Snackbar.make(binding.signupBtn, "Error: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
