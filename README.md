# MentALL Backend API - Documentación

## 📋 Configuración Inicial

### 1. Instalar XAMPP
- Descarga XAMPP desde https://www.apachefriends.org
- Instala y ejecuta Apache y MySQL

### 2. Configurar Base de Datos
1. Abre phpMyAdmin (http://localhost/phpmyadmin)
2. Importa el archivo `mentall_database_updated.sql`
3. Verifica que la base de datos `mentall` se haya creado correctamente

### 3. Colocar Backend
1. Copia la carpeta `backend` en `C:/xampp/htdocs/`
2. La ruta final debe ser: `C:/xampp/htdocs/backend/`

### 4. Probar API
- URL base: `http://localhost/backend/api/`
- Prueba: `http://localhost/backend/api/activities/list.php`

---

## 🔗 Endpoints de la API

### 📱 Base URL
```
http://localhost/backend/api/
```

### 🔐 Autenticación

#### Login
```http
POST /auth/login.php
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "123456"
}

Response:
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "id_usuario": 1,
    "nombre": "Juan",
    "email": "user@example.com"
  }
}
```

#### Registro
```http
POST /auth/register.php
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "123456"
}

Response:
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "id_usuario": 1,
    "nombre": "Juan Pérez",
    "email": "juan@example.com"
  }
}
```

---

### 😊 Estados de Ánimo (Moods)

#### Crear Registro de Mood
```http
POST /moods/create.php
Content-Type: application/json

{
  "id_usuario": 1,
  "valor": 3,
  "etiqueta": "Trabajo",
  "actividad_realizada": "Paseo corto",
  "nota": "Me siento mejor",
  "imagen_uri": "file:///storage/image123.jpg"
}

Response:
{
  "success": true,
  "message": "Registro de emoción guardado",
  "data": {
    "id_registro": 15,
    "valor": 3
  }
}
```

#### Listar Moods
```http
GET /moods/list.php?id_usuario=1&limit=20

Response:
{
  "success": true,
  "message": "Registros obtenidos",
  "data": {
    "total": 5,
    "registros": [
      {
        "id_registro": 15,
        "valor": 3,
        "etiqueta": "Trabajo",
        "actividad_realizada": "Paseo corto",
        "nota": "Me siento mejor",
        "imagen_uri": "file:///...",
        "fecha": "2026-01-28 14:30:00"
      }
    ]
  }
}
```

#### Estadísticas de Moods
```http
GET /moods/stats.php?id_usuario=1&days=7

Response:
{
  "success": true,
  "message": "Estadísticas obtenidas",
  "data": {
    "dias_analizados": 7,
    "total_registros": 5,
    "promedio": 3.4,
    "ultimos_valores": [3, 4, 2, 3, 5],
    "ultimo_registro": {
      "valor": 3,
      "actividad_realizada": "Paseo corto",
      "nota": "Mejor que ayer",
      "fecha": "2026-01-28 14:30"
    }
  }
}
```

---

### 🏃 Actividades

#### Listar Actividades
```http
GET /activities/list.php

Response:
{
  "success": true,
  "message": "Actividades obtenidas",
  "data": {
    "total": 7,
    "actividades": [
      {
        "id_actividad": 1,
        "titulo": "Respiración 4-7-8",
        "descripcion": "Inhala 4s, mantén 7s, exhala 8s...",
        "categoria": "Respiración",
        "duracion_min": 3,
        "enlace": null
      }
    ]
  }
}
```

---

### 💡 Recomendaciones

#### Obtener Recomendaciones
```http
GET /recommendations/get.php?id_usuario=1

Response:
{
  "success": true,
  "message": "Recomendaciones basadas en tu estado",
  "data": {
    "total": 3,
    "recomendaciones": [
      {
        "id_actividad": 1,
        "titulo": "Respiración 4-7-8",
        "descripcion": "...",
        "categoria": "Respiración",
        "duracion_min": 3,
        "motivo": "Recomendada basada en tu estado de ánimo",
        "mood_valor": 2
      }
    ]
  }
}
```

---

### 🎓 Skills (Habilidades Compartidas)

#### Listar Skills
```http
GET /skills/list.php?search=kotlin&limit=20

Response:
{
  "success": true,
  "message": "Skills obtenidas",
  "data": {
    "total": 3,
    "skills": [
      {
        "id_skill": 1,
        "titulo": "Kotlin",
        "nivel": "Intermedio",
        "descripcion": "Te puedo ayudar con Compose",
        "id_usuario": 2,
        "usuario_nombre": "María",
        "fecha": "2026-01-28"
      }
    ]
  }
}
```

#### Publicar Skill
```http
POST /skills/create.php
Content-Type: application/json

{
  "id_usuario": 1,
  "titulo": "Python",
  "nivel": "Avanzado",
  "descripcion": "Puedo ayudarte con Django y Flask"
}

Response:
{
  "success": true,
  "message": "Skill publicada exitosamente",
  "data": {
    "id_skill": 5,
    "titulo": "Python",
    "nivel": "Avanzado"
  }
}
```

---

### 📞 Contactos de Ayuda

#### Listar Contactos
```http
GET /contacts/list.php?id_usuario=1

Response:
{
  "success": true,
  "message": "Contactos obtenidos",
  "data": {
    "total": 2,
    "contactos": [
      {
        "id_contacto": 1,
        "nombre": "María",
        "telefono": "600000000",
        "descripcion": "Amiga cercana",
        "es_emergencia": 0,
        "orden": 0
      }
    ]
  }
}
```

#### Añadir Contacto
```http
POST /contacts/create.php
Content-Type: application/json

{
  "id_usuario": 1,
  "nombre": "Pedro",
  "telefono": "611111111",
  "descripcion": "Compañero de trabajo"
}
```

---

### ⏰ Llamadas Programadas (SOS)

#### Programar Llamada
```http
POST /calls/create.php
Content-Type: application/json

{
  "id_usuario": 1,
  "telefono": "717003717",
  "motivo": "Los lunes me siento peor",
  "dia_semana": 1,
  "hora": "14:30"
}

Notas:
- dia_semana: 1=Lunes, 2=Martes, 3=Miércoles, 4=Jueves, 5=Viernes, 6=Sábado, 7=Domingo, null=todos los días
- dia_mes: 1-31 para día específico del mes, null si no aplica
- hora: formato HH:MM (24 horas)
```

#### Listar Llamadas
```http
GET /calls/list.php?id_usuario=1

Response:
{
  "success": true,
  "message": "Llamadas programadas",
  "data": {
    "total": 2,
    "llamadas": [
      {
        "id_llamada": 1,
        "telefono": "717003717",
        "motivo": "Los lunes me siento peor",
        "dia_semana": 1,
        "dia_mes": null,
        "hora": "14:30",
        "activa": 1,
        "descripcion": "Todos los Lunes a las 14:30"
      }
    ]
  }
}
```

#### Activar/Desactivar Llamada
```http
PUT /calls/toggle.php
Content-Type: application/json

{
  "id_llamada": 1,
  "activa": 0
}
```

---

## 🔧 Estructura de Respuestas

Todas las respuestas tienen el siguiente formato:

```json
{
  "success": true/false,
  "message": "Mensaje descriptivo",
  "data": { ... } // Opcional, solo si hay datos
}
```

---

## 📝 Notas Importantes

1. **CORS**: Está configurado para permitir peticiones desde cualquier origen
2. **Contraseñas**: Se hashean con `password_hash()` de PHP
3. **Timestamps**: Formato DATETIME de MySQL (YYYY-MM-DD HH:MM:SS)
4. **ID Usuario**: Se guarda en SharedPreferences en Android

---

## 🧪 Probar API con Thunder Client / Postman

### Usuario de Prueba
Crea un usuario primero:
```json
POST http://localhost/backend/api/auth/register.php
{
  "nombre": "Test User",
  "email": "test@test.com",
  "password": "123456"
}
```

Luego usa el `id_usuario` devuelto en las otras peticiones.
