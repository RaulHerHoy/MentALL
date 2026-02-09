package com.example.mentall.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mentall.data.models.Contact

/**
 * DatabaseHelper para gestión local de contactos de emergencia
 * Requisito TFG: Base de datos interna SQLite para contactos del usuario
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mentall_local.db"
        private const val DATABASE_VERSION = 1

        // Tabla de contactos de emergencia
        private const val TABLE_CONTACTS = "emergency_contacts"
        private const val COL_ID = "id"
        private const val COL_USER_ID = "user_id"
        private const val COL_NAME = "name"
        private const val COL_PHONE = "phone"
        private const val COL_DESCRIPTION = "description"
        private const val COL_IS_EMERGENCY = "is_emergency"
        private const val COL_ORDER = "order_priority"
        private const val COL_SYNCED = "synced_with_server"
        private const val COL_CREATED_AT = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_CONTACTS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_ID INTEGER NOT NULL,
                $COL_NAME TEXT NOT NULL,
                $COL_PHONE TEXT NOT NULL,
                $COL_DESCRIPTION TEXT,
                $COL_IS_EMERGENCY INTEGER DEFAULT 1,
                $COL_ORDER INTEGER DEFAULT 0,
                $COL_SYNCED INTEGER DEFAULT 0,
                $COL_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()
        
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    // ========== CRUD Operations ==========

    /**
     * Insertar contacto de emergencia local
     */
    fun insertContact(contact: Contact, userId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_USER_ID, userId)
            put(COL_NAME, contact.nombre)
            put(COL_PHONE, contact.telefono)
            put(COL_DESCRIPTION, contact.descripcion)
            put(COL_IS_EMERGENCY, contact.esEmergencia)
            put(COL_ORDER, contact.orden)
            put(COL_SYNCED, 0) // No sincronizado aún
        }
        return db.insert(TABLE_CONTACTS, null, values)
    }

    /**
     * Obtener todos los contactos del usuario desde SQLite
     */
    fun getContacts(userId: Int): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val db = this.readableDatabase
        
        val cursor: Cursor = db.query(
            TABLE_CONTACTS,
            null,
            "$COL_USER_ID = ?",
            arrayOf(userId.toString()),
            null,
            null,
            "$COL_ORDER ASC, $COL_IS_EMERGENCY DESC"
        )

        with(cursor) {
            while (moveToNext()) {
                val contact = Contact(
                    idContacto = getInt(getColumnIndexOrThrow(COL_ID)),
                    idUsuario = userId,
                    nombre = getString(getColumnIndexOrThrow(COL_NAME)),
                    telefono = getString(getColumnIndexOrThrow(COL_PHONE)),
                    descripcion = getString(getColumnIndexOrThrow(COL_DESCRIPTION)),
                    esEmergencia = getInt(getColumnIndexOrThrow(COL_IS_EMERGENCY)),
                    orden = getInt(getColumnIndexOrThrow(COL_ORDER))
                )
                contacts.add(contact)
            }
        }
        cursor.close()
        return contacts
    }

    /**
     * Actualizar contacto existente
     */
    fun updateContact(contactId: Int, contact: Contact): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, contact.nombre)
            put(COL_PHONE, contact.telefono)
            put(COL_DESCRIPTION, contact.descripcion)
            put(COL_IS_EMERGENCY, contact.esEmergencia)
            put(COL_ORDER, contact.orden)
            put(COL_SYNCED, 0) // Marcar como no sincronizado
        }
        return db.update(TABLE_CONTACTS, values, "$COL_ID = ?", arrayOf(contactId.toString()))
    }

    /**
     * Eliminar contacto
     */
    fun deleteContact(contactId: Int?): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_CONTACTS, "$COL_ID = ?", arrayOf(contactId.toString()))
    }

    /**
     * Eliminar todos los contactos del usuario (al cerrar sesión)
     */
    fun deleteAllContactsForUser(userId: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_CONTACTS, "$COL_USER_ID = ?", arrayOf(userId.toString()))
    }

    /**
     * Marcar contacto como sincronizado con el servidor
     */
    fun markContactAsSynced(contactId: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_SYNCED, 1)
        }
        db.update(TABLE_CONTACTS, values, "$COL_ID = ?", arrayOf(contactId.toString()))
    }

    /**
     * Obtener contactos no sincronizados
     */
    fun getUnsyncedContacts(userId: Int): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val db = this.readableDatabase
        
        val cursor: Cursor = db.query(
            TABLE_CONTACTS,
            null,
            "$COL_USER_ID = ? AND $COL_SYNCED = 0",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val contact = Contact(
                    idContacto = getInt(getColumnIndexOrThrow(COL_ID)),
                    idUsuario = userId,
                    nombre = getString(getColumnIndexOrThrow(COL_NAME)),
                    telefono = getString(getColumnIndexOrThrow(COL_PHONE)),
                    descripcion = getString(getColumnIndexOrThrow(COL_DESCRIPTION)),
                    esEmergencia = getInt(getColumnIndexOrThrow(COL_IS_EMERGENCY)),
                    orden = getInt(getColumnIndexOrThrow(COL_ORDER))
                )
                contacts.add(contact)
            }
        }
        cursor.close()
        return contacts
    }

    /**
     * Obtener el número de contactos del usuario
     */
    fun getContactCount(userId: Int): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_CONTACTS WHERE $COL_USER_ID = ?",
            arrayOf(userId.toString())
        )
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }
}
