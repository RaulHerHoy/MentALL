# 🚀 GUÍA DE INICIO RÁPIDO - MentALL

## ⏱️ Setup en 5 Minutos

### 1. Backend (2 minutos)
```bash
1. Instala XAMPP
2. Inicia Apache y MySQL
3. Abre http://localhost/phpmyadmin
4. Importa: mentall_database_updated.sql
5. Copia carpeta "backend" a C:/xampp/htdocs/
6. Prueba: http://localhost/backend/api/activities/list.php
```

### 2. Android App (3 minutos)
```bash
1. Abre "android_app" en Android Studio
2. Edita RetrofitClient.kt:
   - Para emulador: BASE_URL = "http://10.0.2.2/backend/api/"
   - Para físico: BASE_URL = "http://TU_IP/backend/api/"
3. Añade permisos en AndroidManifest.xml (ver README)
4. Run App
```

---

## 🎯 Lo que YA FUNCIONA

✅ **Backend PHP completo:**
- Login/Register con validación
- Crear y listar moods
- Estadísticas de moods
- Actividades
- Recomendaciones automáticas
- Skills compartidas
- Contactos SOS
- Llamadas programadas

✅ **Android App base:**
- Login funcional con API
- SessionManager (SharedPreferences)
- Retrofit configurado
- MainActivity con BottomNavigation
- HomeFragment con stats y recomendaciones
- AlarmScheduler para llamadas

---

## 🔴 Lo que DEBES COMPLETAR

### Archivos Android por Crear:

1. **RegisterActivity.kt**
   - Copia LoginActivity.kt
   - Cambia `login()` por `register()`
   - Añade campo de nombre

2. **EmotionEntryFragment.kt**
   ```kotlin
   // Slider 1-5, campos actividad/nota, botón foto
   // Llamar: RetrofitClient.apiService.createMood()
   ```

3. **ActivitiesFragment.kt**
   ```kotlin
   // RecyclerView con actividades
   // Llamar: RetrofitClient.apiService.listActivities()
   ```

4. **SosFragment.kt**
   ```kotlin
   // Botones 112 y Teléfono Esperanza
   // Programar llamadas con AlarmScheduler
   ```

5. **SkillsFragment.kt**
   ```kotlin
   // Form + RecyclerView de skills
   // Llamar: createSkill() y listSkills()
   ```

6. **ProfileFragment.kt**
   ```kotlin
   // Editar nombre, logout
   // sessionManager.clearSession()
   ```

### Layouts XML por Crear:
- fragment_home.xml
- fragment_emotion_entry.xml
- fragment_activities.xml
- fragment_sos.xml
- fragment_skills.xml
- fragment_profile.xml
- fragment_register.xml

---

## 📝 Patrón a Seguir

**Todos los Fragments siguen este patrón:**

```kotlin
class MiFragment : Fragment() {
    private var _binding: FragmentMiBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var sessionManager: SessionManager
    
    override fun onCreateView(...): View {
        _binding = FragmentMiBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, ...) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        
        loadData()
    }
    
    private fun loadData() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.miMetodo()
                // Procesar respuesta
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

---

## 🧪 Probar Backend SIN la App

Usa **Thunder Client** (extensión VS Code) o **Postman**:

```
POST http://localhost/backend/api/auth/register.php
{
  "nombre": "Test",
  "email": "test@test.com",
  "password": "123456"
}

Guarda el id_usuario de la respuesta.

POST http://localhost/backend/api/moods/create.php
{
  "id_usuario": 1,
  "valor": 4,
  "nota": "Me siento bien"
}

GET http://localhost/backend/api/recommendations/get.php?id_usuario=1
```

---

## 🔧 Problemas Comunes

**"Unable to resolve host"**
→ Revisa BASE_URL en RetrofitClient.kt

**"Connection refused"**
→ Usa `10.0.2.2` para emulador, tu IP local para físico

**"Cleartext HTTP not permitted"**
→ Añade `android:usesCleartextTraffic="true"` en manifest

**"ViewBinding not found"**
→ Sincroniza Gradle, Build > Clean Project

---

## 📂 Estructura de Archivos Entregados

```
📁 outputs/
├── backend/                    ✅ Backend PHP completo
│   ├── config/
│   ├── api/
│   └── README.md
├── android_app/                ✅ App Android base
│   ├── app/
│   │   ├── build.gradle.kts
│   │   └── src/main/
│   │       ├── java/
│   │       └── res/
├── mentall_database_updated.sql ✅ Base de datos
├── README_IMPLEMENTACION_COMPLETA.md ✅ Guía detallada
└── INICIO_RAPIDO.md (este archivo)
```

---

## ⚡ Empezar AHORA

1. **Instala XAMPP** → Importa BD → Copia backend
2. **Abre Android Studio** → Abre android_app
3. **Cambia IP** en RetrofitClient.kt
4. **Corre la app** → Login: test@test.com / 123456
5. **Crea los Fragments** que faltan (usa HomeFragment como guía)

---

**¿Listo? ¡Empieza por crear RegisterActivity.kt y el resto de Fragments!**

📧 Si tienes dudas, revisa README_IMPLEMENTACION_COMPLETA.md
