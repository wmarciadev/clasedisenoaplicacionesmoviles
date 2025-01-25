package keiseruniversity.snackbar

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mostrar el diálogo automáticamente al cargar la app
        mostrarDialogoAutomatico()

        // Configurar el botón para el Snackbar
        val button2: Button = findViewById(R.id.button2)
        button2.setOnClickListener { view ->
            val snackbar2 = Snackbar.make(
                view,
                "Su cita ha sido registrada, le esperamos!!",
                Snackbar.LENGTH_LONG
            )
            snackbar2.setTextColor(Color.WHITE)
            snackbar2.setBackgroundTint(Color.BLUE)
            snackbar2.show()
        }

        // Configuración para el manejo de márgenes en caso de usar bordes a borde
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Método para mostrar un diálogo automáticamente al iniciar
    private fun mostrarDialogoAutomatico() {
        val saveAlert = AlertDialog.Builder(this)
        saveAlert.setTitle("Bienvenid@ carschedule")
        saveAlert.setMessage("Bienvenida a la aplicación, escoge tu vehiculo")
        saveAlert.setCancelable(false) // Evita que el usuario lo cierre manualmente
        val dialog = saveAlert.create()
        dialog.show()
        // Cerrar el diálogo automáticamente después de 5 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 5000)
    }

    // Método para ir a la segunda actividad
    fun go2secondActivity(view: View) {
        val nameEditText: EditText = findViewById(R.id.name_ID)
        val intent = Intent(this, ThirdActivity::class.java)
        intent.putExtra("name", nameEditText.text.toString())
        startActivity(intent)
    }

    // Método para mostrar un diálogo al guardar la cita
    fun GuardarCita(view: View) {
        val saveAlert = AlertDialog.Builder(this)
        saveAlert.setTitle("Guardar Cita")
        saveAlert.setMessage("¿Estás seguro de la fecha y hora de la cita?")
        saveAlert.setPositiveButton("Sí") { _: DialogInterface, _: Int ->
            Snackbar.make(
                findViewById(R.id.button),
                "Cita guardada correctamente", Snackbar.LENGTH_LONG
            ).show()
        }
        saveAlert.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss() // Cierra el diálogo si se selecciona "No"
        }
        saveAlert.show()
    }
}
