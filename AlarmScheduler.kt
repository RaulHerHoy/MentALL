package com.example.mentall.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import java.util.*

class AlarmScheduler(private val context: Context) {
    
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    
    /**
     * Programar una llamada
     * @param id ID único de la alarma
     * @param telefono Número a llamar
     * @param diaSemana 1=Lunes, 2=Martes... 7=Domingo, null=todos los días
     * @param hora Formato "HH:mm" (24h)
     */
    fun scheduleCall(id: Int, telefono: String, diaSemana: Int?, hora: String) {
        val (hour, minute) = hora.split(":").map { it.toInt() }
        
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            
            // Si ya pasó la hora de hoy, programar para mañana
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
            
            // Si es un día específico de la semana
            if (diaSemana != null) {
                val diferenciasDias = (diaSemana - get(Calendar.DAY_OF_WEEK) + 7) % 7
                if (diferenciasDias > 0 || timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_YEAR, diferenciasDias)
                }
            }
        }
        
        val intent = Intent(context, CallAlarmReceiver::class.java).apply {
            putExtra("telefono", telefono)
            putExtra("id", id)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Programar alarma repetitiva
        val interval = if (diaSemana != null) {
            AlarmManager.INTERVAL_DAY * 7 // Cada semana
        } else {
            AlarmManager.INTERVAL_DAY // Cada día
        }
        
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            interval,
            pendingIntent
        )
    }
    
    /**
     * Cancelar una llamada programada
     */
    fun cancelCall(id: Int) {
        val intent = Intent(context, CallAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        alarmManager.cancel(pendingIntent)
    }
}

/**
 * BroadcastReceiver que se ejecuta cuando llega la hora de la llamada
 */
class CallAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val telefono = intent.getStringExtra("telefono") ?: return
        
        // Abrir el marcador con el número
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$telefono")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        
        context.startActivity(callIntent)
    }
}
