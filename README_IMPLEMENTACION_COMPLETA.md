# MentALL - Sistema Completo de Salud Mental

## 📋 Contenido del Proyecto

Este proyecto contiene:

1. **Backend PHP** - API REST completa
2. **Base de Datos MySQL** - Schema actualizado con DATETIME
3. **App Android** - Aplicación con Fragments y Retrofit

---

## 🚀 Guía de Instalación Completa

### PASO 1: Configurar Backend PHP

#### 1.1 Instalar XAMPP
```
1. Descarga XAMPP desde: https://www.apachefriends.org
2. Instala y ejecuta Apache y MySQL
```

#### 1.2 Crear Base de Datos
```
1. Abre http://localhost/phpmyadmin
2. Crea una nueva base de datos llamada "mentall"
3. Importa el archivo: mentall_database_updated.sql
4. Verifica que se crearon todas las tablas
```

#### 1.3 Instalar Backend
```
1. Copia la carpeta "backend" a: C:/xampp/htdocs/
2. Ruta final: C:/xampp/htdocs/backend/
3. Verifica que Apache esté corriendo
```

#### 1.4 Probar API
```
Abre en el navegador:
http://localhost/backend/api/activities/list.php

Deberías ver una respuesta JSON con actividades.
```

---

### PASO 2: Configurar App Android

#### 2.1 Abrir Proyecto en Android Studio
```
1. Abre Android Studio
2. File > Open > Selecciona la carpeta "android_app"
3. Espera a que Gradle sincronice
```

#### 2.2 Configurar URL del Backend

**Edita el archivo:**
```
android_app/app/src/main/java/com/example/mentall/data/api/RetrofitClient.kt
```

**Cambia BASE_URL según tu caso:**

Para **Emulador Android**:
```kotlin
private const val BASE_URL = "http://10.0.2.2/backend/api/"
```

Para **Dispositivo físico** (encuentra tu IP con `ipconfig` en Windows):
```kotlin
private const val BASE_URL = "http://192.168.X.X/backend/api/"
```

Para **dispositivo en la misma red WiFi**:
1. Abre CMD y escribe: `ipconfig`
2. Busca "Dirección IPv4": ejemplo 192.168.1.100
3. Usa: `http://192.168.1.100/backend/api/`

#### 2.3 Agregar Permisos en AndroidManifest.xml

**Archivo:** `android_app/app/src/main/AndroidManifest.xml`

Añade ANTES de `<application>`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
<uses-permission android:name="android.permission.USE_EXACT_ALARM" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

Añade DENTRO de `<application>`:
```xml
<application
    android:usesCleartextTraffic="true"
    ...>
    
    <!-- Otras activities -->
    
    <receiver android:name=".utils.CallAlarmReceiver"
        android:enabled="true"
        android:exported="false" />
        
</application>
```

---

## 📱 Estructura de la App Android

### Archivos Creados

```
android_app/
├── app/
│   ├── build.gradle.kts         ✅ Dependencias (Retrofit, etc)
│   └── src/main/
│       ├── java/com/example/mentall/
│       │   ├── MainActivity.kt          ✅ Activity principal
│       │   ├── data/
│       │   │   ├── api/
│       │   │   │   ├── ApiService.kt    ✅ Endpoints Retrofit
│       │   │   │   └── RetrofitClient.kt ✅ Cliente HTTP
│       │   │   ├── models/
│       │   │   │   └── Models.kt        ✅ Modelos de datos
│       │   │   └── prefs/
│       │   │       └── SessionManager.kt ✅ SharedPreferences
│       │   ├── ui/
│       │   │   ├── auth/
│       │   │   │   ├── LoginActivity.kt     ✅ Login
│       │   │   │   └── RegisterActivity.kt  🔴 POR HACER
│       │   │   ├── main/
│       │   │   │   ├── HomeFragment.kt      ✅ Home
│       │   │   │   ├── EmotionEntryFragment.kt  🔴 POR HACER
│       │   │   │   ├── ActivitiesFragment.kt    🔴 POR HACER
│       │   │   │   ├── SosFragment.kt           🔴 POR HACER
│       │   │   │   └── SkillsFragment.kt        🔴 POR HACER
│       │   │   └── profile/
│       │   │       └── ProfileFragment.kt       🔴 POR HACER
│       │   └── utils/
│       │       ├── AlarmScheduler.kt     ✅ Alarmas SOS
│       │       └── Constants.kt          ✅ Constantes
│       └── res/
│           ├── layout/
│           │   ├── activity_main.xml     ✅ Layout principal
│           │   ├── fragment_login.xml    ✅ Login
│           │   ├── fragment_home.xml     🔴 POR CREAR
│           │   └── (otros layouts)       🔴 POR CREAR
│           └── menu/
│               ├── bottom_nav_menu.xml   ✅ Navegación inferior
│               └── toolbar_menu.xml      ✅ Menú superior
```

---

## 🔨 Archivos que DEBES CREAR

### 1. RegisterActivity.kt
```kotlin
// Similar a LoginActivity pero llamando a register()
// Ver LoginActivity.kt como referencia
```

### 2. EmotionEntryFragment.kt
```kotlin
// Fragment para registrar emociones
// Debe incluir:
// - Slider para mood (1-5)
// - Campo para actividad realizada
// - Campo para nota
// - Botón para foto
// - Llamar a RetrofitClient.apiService.createMood()
```

### 3. ActivitiesFragment.kt
```kotlin
// Fragment con RecyclerView de actividades
// Debe incluir:
// - Campo de búsqueda
// - RecyclerView con adapter
// - Llamar a RetrofitClient.apiService.listActivities()
```

### 4. SosFragment.kt
```kotlin
// Fragment de ayuda de emergencia
// Debe incluir:
// - Botón para llamar 112
// - Botón para llamar Teléfono de la Esperanza
// - Lista de contactos personales
// - Sección para programar llamadas
// - Llamar a RetrofitClient.apiService.createScheduledCall()
// - Usar AlarmScheduler para programar
```

### 5. SkillsFragment.kt
```kotlin
// Fragment de red social de habilidades
// Debe incluir:
// - Formulario para publicar skill
// - RecyclerView con skills de otros usuarios
// - Campo de búsqueda
// - Llamar a RetrofitClient.apiService.createSkill()
// - Llamar a RetrofitClient.apiService.listSkills()
```

### 6. ProfileFragment.kt
```kotlin
// Fragment de perfil y configuración
// Debe incluir:
// - Nombre del usuario (editable)
// - Email (solo lectura)
// - Switch para recordatorios
// - Campo para hora de recordatorio
// - Botón de cerrar sesión
// - Llamar a sessionManager.clearSession() al cerrar sesión
```

---

## 🎨 Layouts XML que DEBES CREAR

### fragment_home.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <!-- Saludo -->
        <TextView
            android:id="@+id/tvUserName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Hola, Usuario"
            android:textSize="24sp"
            android:textStyle="bold" />

        <!-- Botones de acceso rápido -->
        <Button
            android:id="@+id/btnRegisterMood"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Registrar emoción"
            android:layout_marginTop="16dp" />

        <!-- Resumen de estadísticas -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Resumen últimos 7 días"
            android:textSize="18sp"
            android:textStyle="bold"
            android:layout_marginTop="24dp" />

        <ProgressBar
            android:id="@+id/progressStats"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center" />

        <TextView
            android:id="@+id/tvTotalRegistros"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Registros: 0" />

        <!-- Más elementos... -->

        <!-- Sección de Recomendaciones -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Recomendaciones para ti"
            android:textSize="18sp"
            android:textStyle="bold"
            android:layout_marginTop="24dp" />

        <ProgressBar
            android:id="@+id/progressRecomendaciones"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center" />

        <!-- Lista de recomendaciones -->
        
    </LinearLayout>
</ScrollView>
```

---

## 📞 Uso de AlarmScheduler para Llamadas SOS

### Programar una llamada

```kotlin
val alarmScheduler = AlarmScheduler(requireContext())

// Llamada todos los lunes a las 14:30
alarmScheduler.scheduleCall(
    id = 1,              // ID único
    telefono = "717003717",
    diaSemana = 1,       // 1=Lunes, null=todos los días
    hora = "14:30"
)
```

### Cancelar una llamada

```kotlin
alarmScheduler.cancelCall(id = 1)
```

---

## 🧪 Pruebas de la API

### Crear Usuario
```bash
POST http://localhost/backend/api/auth/register.php
{
  "nombre": "Test User",
  "email": "test@test.com",
  "password": "123456"
}
```

### Login
```bash
POST http://localhost/backend/api/auth/login.php
{
  "email": "test@test.com",
  "password": "123456"
}
```

### Registrar Mood
```bash
POST http://localhost/backend/api/moods/create.php
{
  "id_usuario": 1,
  "valor": 3,
  "actividad_realizada": "Paseo",
  "nota": "Me siento mejor"
}
```

---

## 🔧 Solución de Problemas Comunes

### Error: "Unable to resolve host"
**Solución:** Verifica que la URL en RetrofitClient.kt sea correcta y que XAMPP esté corriendo.

### Error: "Cleartext HTTP traffic not permitted"
**Solución:** Añade `android:usesCleartextTraffic="true"` en el manifest.

### Error: "Connection refused"
**Solución:** 
- Para emulador, usa `10.0.2.2`
- Para dispositivo físico, usa tu IP local (ipconfig en Windows)
- Verifica que Apache esté corriendo en XAMPP

### Error: "No se pueden programar alarmas"
**Solución:** Añade los permisos de SCHEDULE_EXACT_ALARM en el manifest.

---

## 📝 Notas Importantes

1. **Seguridad**: Este sistema NO usa tokens JWT. Para producción, implementa autenticación segura.

2. **Contraseñas**: Se hashean con `password_hash()` de PHP (seguro).

3. **SharedPreferences**: Solo guarda `id_usuario`, `nombre`, `email` y `tema`.

4. **Timestamps**: La BD usa DATETIME, no bigint.

5. **Fotos**: Se guardan URIs locales, no se suben al servidor.

6. **Red de Apoyo (Skills)**: Es independiente de los contactos SOS del usuario.

---

## 📚 Recursos Adicionales

- **Documentación API**: `backend/README.md`
- **Retrofit Guide**: https://square.github.io/retrofit/
- **Material Design**: https://material.io/develop/android

---

## ✅ Checklist de Implementación

- [x] Base de datos creada
- [x] Backend PHP funcionando
- [x] Retrofit configurado
- [x] Login funcional
- [ ] Registro de usuarios
- [ ] HomeFragment con recomendaciones
- [ ] EmotionEntryFragment
- [ ] ActivitiesFragment
- [ ] SosFragment con llamadas programadas
- [ ] SkillsFragment
- [ ] ProfileFragment
- [ ] Layouts XML completados
- [ ] Pruebas en dispositivo real

---

## 🎯 Próximos Pasos

1. Crea los Fragments faltantes siguiendo el patrón de HomeFragment
2. Crea los layouts XML correspondientes
3. Implementa RecyclerView adapters para listas
4. Prueba todas las funcionalidades
5. Añade validaciones de entrada
6. Mejora el diseño visual

---

**¿Necesitas ayuda con algún Fragment o funcionalidad específica?**
Puedo ayudarte a crear cualquier parte que necesites.
