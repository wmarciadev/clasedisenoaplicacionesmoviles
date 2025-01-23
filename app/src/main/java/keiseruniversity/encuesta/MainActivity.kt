package keiseruniversity.encuesta

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Declaración de las vistas
    private lateinit var canadaCheckBox: CheckBox
    private lateinit var chinaCheckBox: CheckBox
    private lateinit var USACheckBox: CheckBox
    private lateinit var resultadoTextView: TextView
    private lateinit var textView4: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vinculación de vistas con los IDs del XML
        canadaCheckBox = findViewById(R.id.Canada)
        chinaCheckBox = findViewById(R.id.China)
        USACheckBox = findViewById(R.id.USA)
        resultadoTextView = findViewById(R.id.resultado)
        textView4 = findViewById(R.id.textView4)
    }

    // Función que se llama desde el XML cuando un CheckBox es clickeado
    fun onCheckBoxClicked(view: View) {
        updateResult()
    }

    // Función para actualizar el resultado en el TextView
    private fun updateResult() {
        val selectedCountries = mutableListOf<String>()

        // Verificar los estados de los CheckBox usando isChecked
        if (canadaCheckBox.isChecked) {
            selectedCountries.add("Canada, repuesta correcta. Tambien el frances")
        }
        if (chinaCheckBox.isChecked) {
            selectedCountries.add("China no es correcto")
        }
        if (USACheckBox.isChecked) {
            selectedCountries.add("USA, repuesta correcta")
        }

        // Mostrar el texto en el TextView, o un mensaje si ninguno está seleccionado
        resultadoTextView.text = if (selectedCountries.isNotEmpty()) {
            selectedCountries.joinToString("\n")
        } else {
            "Ningún país seleccionado"
        }
    }

    // Función que se llama desde el XML cuando un RadioButton es clickeado
    fun onRadioButtonClicked(view: View) {
        when (view.id) {
            R.id.radioyes -> textView4.text = "Estoy en el curso"
            R.id.radiono -> textView4.text = "No estoy en el curso"
        }
    }
}






