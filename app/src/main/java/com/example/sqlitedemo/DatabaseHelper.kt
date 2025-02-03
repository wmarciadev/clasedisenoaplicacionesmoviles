package com.example.sqlitedemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Clase DatabaseHelper que maneja la base de datos SQLite
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "contacts.db"  // Nombre de la base de datos
        private const val DATABASE_VERSION = 2  // Se incrementa la versión para aplicar cambios
        private const val TABLE_NAME = "contacts"  // Nombre de la tabla
        private const val COLUMN_ID = "id"  // Columna para el ID
        private const val COLUMN_NAME = "name"  // Columna para el nombre
        private const val COLUMN_PHONE = "phone"  // Columna para el teléfono

        // Consulta SQL para crear la tabla con restricciones UNIQUE en nombre y teléfono
        private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT UNIQUE, " +  // Se agrega UNIQUE en el nombre
                "$COLUMN_PHONE TEXT UNIQUE)"    // Se agrega UNIQUE en el teléfono
    }

    // Método que se ejecuta cuando se crea la base de datos
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    // Método que se ejecuta cuando se actualiza la base de datos
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }

    // Método para insertar un nuevo contacto en la base de datos
    fun insertContact(name: String, phone: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name.trim())
            put(COLUMN_PHONE, phone.trim())
        }
        return try {
            db.insertOrThrow(TABLE_NAME, null, values)  // Evita duplicados por la restricción UNIQUE
        } catch (e: Exception) {
            -1  // Retorna -1 si el contacto ya existe
        }
    }

    // Método para obtener todos los contactos almacenados en la base de datos
    fun getAllContacts(): List<String> {
        val contacts = mutableListOf<String>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
                contacts.add("$id - $name - $phone")
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contacts
    }

    // Método para verificar si un contacto ya existe en la base de datos
    fun contactExists(name: String, phone: String, excludeId: Int = -1): Boolean {
        val db = readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_NAME WHERE (TRIM($COLUMN_NAME) = ? OR TRIM($COLUMN_PHONE) = ?) AND $COLUMN_ID != ?"
        val cursor = db.rawQuery(query, arrayOf(name.trim(), phone.trim(), excludeId.toString()))

        var exists = false
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0
        }
        cursor.close()
        return exists
    }

    // Método para actualizar un contacto existente en la base de datos
    fun updateContact(id: Int, newName: String, newPhone: String): Int {
        val db = writableDatabase

        if (contactExists(newName, newPhone, id)) {
            return -1  // No permite actualizar con datos de otro contacto existente
        }

        val values = ContentValues().apply {
            put(COLUMN_NAME, newName.trim())
            put(COLUMN_PHONE, newPhone.trim())
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    // Método para eliminar un contacto de la base de datos
    fun deleteContact(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
    }
}

// para ver la base de datos entrar View/Tool Windows/Device File Explorer.
//Navega a /data/data/com.example.sqlitedemo/databases/.