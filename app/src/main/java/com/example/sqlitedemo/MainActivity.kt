package com.example.sqlitedemo

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Variables para manejar la base de datos y la UI
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var nameInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var addButton: Button
    private lateinit var showButton: Button
    private lateinit var contactsList: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var contacts: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// Inicialización de componentes
        dbHelper = DatabaseHelper(this)
        nameInput = findViewById(R.id.nameInput)
        phoneInput = findViewById(R.id.phoneInput)
        addButton = findViewById(R.id.addButton)
        showButton = findViewById(R.id.showButton)
        contactsList = findViewById(R.id.contactsList)
// Cargar contactos al iniciar la app
        loadContacts()
// Evento para agregar un nuevo contacto
        addButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                val result = dbHelper.insertContact(name, phone)
                if (result == -1L) {
                    Toast.makeText(this, "El contacto ya existe", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Contacto agregado", Toast.LENGTH_SHORT).show()
                    nameInput.text.clear()
                    phoneInput.text.clear()
                    loadContacts()
                }
            } else {
                Toast.makeText(this, "Llene ambos campos", Toast.LENGTH_SHORT).show()
            }
        }
// Evento para mostrar contactos actualizados
        showButton.setOnClickListener {
            loadContacts()
        }
        // Evento para actualizar un contacto al hacer clic en la lista
        contactsList.setOnItemClickListener { _, _, position, _ ->
            val selectedContact = contacts[position]
            val parts = selectedContact.split(" - ")
            if (parts.size == 3) {
                val id = parts[0].toInt()
                val name = parts[1]
                val phone = parts[2]
                showUpdateDialog(id, name, phone)
            }
        }
    }
    // Carga los contactos desde la base de datos y actualiza la UI
    private fun loadContacts() {
        contacts = dbHelper.getAllContacts().toMutableList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contacts)
        contactsList.adapter = adapter
    }
    // Muestra un cuadro de diálogo para actualizar un contacto
    private fun showUpdateDialog(contactId: Int, oldName: String, oldPhone: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_contact, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.updateNameInput)
        val phoneInput = dialogView.findViewById<EditText>(R.id.updatePhoneInput)
// Prellenar campos con la información actual
        nameInput.setText(oldName)
        phoneInput.setText(oldPhone)

        AlertDialog.Builder(this)
            .setTitle("Actualizar Contacto")
            .setView(dialogView)
            .setPositiveButton("Actualizar") { _, _ ->
                val newName = nameInput.text.toString().trim()
                val newPhone = phoneInput.text.toString().trim()

                val result = dbHelper.updateContact(contactId, newName, newPhone)
                if (result == -1) {
                    Toast.makeText(this, "Error: El contacto ya existe", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Contacto actualizado", Toast.LENGTH_SHORT).show()
                    loadContacts()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
