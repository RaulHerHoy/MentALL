package com.example.mentall

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/* =========================================================
   Helper: Fondo degradado MentALL (mint)
   ========================================================= */
@Composable
private fun MentAllGradientBackground(content: @Composable () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        cs.primaryContainer,
                        cs.background
                    )
                )
            )
    ) { content() }
}

/* =========================================================
   Helper: Card con color "marca"
   ========================================================= */
@Composable
private fun BrandCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content
        )
    }
}

/* ---------------- LOGIN ---------------- */
@Composable
fun LoginScreen(onLogin: () -> Unit, onGoRegister: () -> Unit) {
    val context = LocalContext.current
    val saved = remember { RememberPrefs.load(context) }

    var email by remember { mutableStateOf(saved.email) }
    var pass by remember { mutableStateOf(saved.pass) }
    var rememberMe by remember { mutableStateOf(saved.remember) }

    MentAllGradientBackground {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background.copy(alpha = 0f)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Caja central para que quede "tarjeta" y no todo estirado
                ElevatedCard(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp)
                ) {
                    Column(
                        Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            "Iniciar sesión",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            leadingIcon = { Icon(Icons.Default.Email, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = pass,
                            onValueChange = { pass = it },
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                            Spacer(Modifier.width(8.dp))
                            Text("Recordar credenciales")
                        }

                        Button(
                            onClick = {
                                RememberPrefs.save(context, rememberMe, email, pass)
                                onLogin()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.Login, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Entrar")
                        }

                        TextButton(
                            onClick = onGoRegister,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Crear cuenta")
                        }
                    }
                }
            }
        }
    }
}

/* ---------------- REGISTER USER ---------------- */
@Composable
fun RegisterScreen(onRegisterDone: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val saved = remember { RememberPrefs.load(context) }

    var name by remember { mutableStateOf(saved.name) }
    var email by remember { mutableStateOf(saved.email) }
    var pass by remember { mutableStateOf(saved.pass) }
    var rememberMe by remember { mutableStateOf(saved.remember) }

    MentAllGradientBackground {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background.copy(alpha = 0f)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedCard(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp)
                ) {
                    Column(
                        Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
                            Text(
                                "Registro",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre") },
                            leadingIcon = { Icon(Icons.Default.Person, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            leadingIcon = { Icon(Icons.Default.Email, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = pass,
                            onValueChange = { pass = it },
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                            Spacer(Modifier.width(8.dp))
                            Text("Recordar credenciales")
                        }

                        Button(
                            onClick = {
                                RememberPrefs.save(context, rememberMe, email, pass, name)
                                onRegisterDone()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.Check, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Crear cuenta")
                        }
                    }
                }
            }
        }
    }
}
/* ---------------- HOME ---------------- */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onGoProfile: () -> Unit,
    onGoEmotionEntry: () -> Unit,
    onGoActivities: () -> Unit,
    onGoSos: () -> Unit
) {
    MentAllGradientBackground {
        Column(
            modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Home", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            // Perfil
            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onGoProfile() }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Person, contentDescription = null)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Perfil", fontWeight = FontWeight.SemiBold)
                        Text(
                            "Ajustes, contactos SOS, cerrar sesión",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null)
                }
            }

            // Check-in
            BrandCard {
                Text("Check-in rápido", fontWeight = FontWeight.SemiBold)
                Text("Registra cómo te sientes y una actividad.")
                Button(
                    onClick = onGoEmotionEntry,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.AddCircle, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Registrar emoción + actividad")
                }
            }

            // Actividades (lila) + SOS (rojo)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                ElevatedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFFEDE7F6),
                                    Color(0xFFD1C4E9)
                                )
                            )
                        )
                        .clickable { onGoActivities() },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.Transparent)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Icon(Icons.Default.ViewCarousel, null, tint = Color(0xFF4A148C))
                        Spacer(Modifier.height(8.dp))
                        Text("Actividades", fontWeight = FontWeight.SemiBold, color = Color(0xFF2E1A47))
                        Text("Cards + scroll", style = MaterialTheme.typography.labelSmall, color = Color(0xFF2E1A47))
                    }
                }

                ElevatedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFFFFCDD2),
                                    Color(0xFFEF9A9A)
                                )
                            )
                        )
                        .clickable { onGoSos() },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.Transparent)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Warning, null, tint = Color(0xFFB71C1C))
                        Spacer(Modifier.height(8.dp))
                        Text("SOS", fontWeight = FontWeight.SemiBold, color = Color(0xFF5C1212))
                        Text("Llamada ya / programada", style = MaterialTheme.typography.labelSmall, color = Color(0xFF5C1212))
                    }
                }
            }

            // Resumen (celeste)
            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFFE3F2FD),
                                Color(0xFFBBDEFB)
                            )
                        )
                    ),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.Transparent)
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Resumen", fontWeight = FontWeight.SemiBold, color = Color(0xFF0D47A1))
                    Text("Hoy: Regular · Actividad: Paseo corto · Nota: “Mejor que ayer”.", color = Color(0xFF0D47A1))
                    Text("Últimos 7 días: 3,4,2,3,5,4,3", color = Color(0xFF0D47A1))
                }
            }
        } // ✅ cierra Column
    }     // ✅ cierra MentAllGradientBackground
}         // ✅ cierra HomeScreen


/* ---------------- EMOTION + ACTIVITY + PHOTO ---------------- */
@Composable
fun EmotionEntryScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    var mood by remember { mutableStateOf(3) }
    var activity by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var photoState by remember { mutableStateOf("Sin foto") }

    MentAllGradientBackground {
        Column(
            modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Registro emoción + actividad",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            BrandCard {
                Text("¿Cómo te sientes hoy? (1–5)", fontWeight = FontWeight.SemiBold)
                Slider(
                    value = mood.toFloat(),
                    onValueChange = { mood = it.toInt().coerceIn(1, 5) },
                    valueRange = 1f..5f,
                    steps = 3
                )
                Text("Valor: $mood", style = MaterialTheme.typography.labelMedium)
            }

            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    OutlinedTextField(
                        value = activity,
                        onValueChange = { activity = it },
                        label = { Text("Actividad (ej: Paseo, Respiración, Gym…)") },
                        leadingIcon = { Icon(Icons.Default.SelfImprovement, null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Nota (opcional)") },
                        leadingIcon = { Icon(Icons.Default.EditNote, null) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            }

            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Foto", fontWeight = FontWeight.SemiBold)
                    Text(photoState, style = MaterialTheme.typography.labelMedium)

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = { photoState = "Foto seleccionada (mock)" },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PhotoLibrary, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Galería")
                        }
                        Button(
                            onClick = { photoState = "Foto tomada (mock)" },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.PhotoCamera, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Cámara")
                        }
                    }
                }
            }

            Button(
                onClick = { /* luego: guardar en BD/API */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Check, null)
                Spacer(Modifier.width(8.dp))
                Text("Guardar registro")
            }
        }
    }
}

/* ---------------- ACTIVITIES (scroll de cards) ---------------- */
data class ActivityCard(val title: String, val minutes: Int, val desc: String, val category: String)

@Composable
fun ActivitiesScreen(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    val items = remember {
        listOf(
            ActivityCard("Respiración 4-7-8", 3, "Inhala 4s · mantén 7s · exhala 8s · 4 repeticiones.", "Respiración"),
            ActivityCard("Paseo corto", 10, "Camina 10 min sin móvil si puedes.", "Hábitos"),
            ActivityCard("Escritura consciente", 5, "Escribe 3 preocupaciones y 1 acción mínima.", "Mindfulness"),
            ActivityCard("Body scan", 6, "Recorre el cuerpo y relaja tensiones.", "Mindfulness"),
            ActivityCard("Agua + pausa", 2, "Bebe agua y descansa 2 minutos reales.", "Hábitos"),
        )
    }
    val shown = items.filter { query.isBlank() || it.title.contains(query, true) }

    MentAllGradientBackground {
        Column(
            modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Actividades", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar actividad") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(shown) { a ->
                    ElevatedCard(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(a.title, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                                AssistChip(
                                    onClick = { },
                                    label = { Text("${a.minutes} min") },
                                    leadingIcon = { Icon(Icons.Default.Timer, null) }
                                )
                            }
                            Text(a.category, style = MaterialTheme.typography.labelMedium)
                            Text(a.desc)

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedButton(onClick = { }, shape = RoundedCornerShape(14.dp)) {
                                    Icon(Icons.Default.FavoriteBorder, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Guardar")
                                }
                                Button(
                                    onClick = { },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(Icons.Default.PlayArrow, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Empezar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ---------------- SOS (llamada ya + programada) ---------------- */
@Composable
fun SosScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    MentAllGradientBackground {
        Column(
            modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "SOS – Ayuda inmediata",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Emergencias
            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Emergencias", fontWeight = FontWeight.Bold)
                    Text("Si hay peligro inmediato para ti o para otros.")

                    Button(
                        onClick = {
                            val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                            context.startActivity(i)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.PhoneInTalk, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Llamar al 112")
                    }
                }
            }

            // Teléfono de la Esperanza
            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Apoyo emocional", fontWeight = FontWeight.Bold)
                    Text("Teléfono de la Esperanza\nAtención 24h · Gratuito · Confidencial")

                    Button(
                        onClick = {
                            val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:717003717"))
                            context.startActivity(i)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Llamar 717 003 717")
                    }
                }
            }

            // Red de apoyo
            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Red de apoyo", fontWeight = FontWeight.Bold)
                    Text("Personas que pueden ayudarte según sus habilidades o cercanía.")

                    SupportSkillRow("Persona de confianza", "Escucha y apoyo emocional", "600000000")
                    SupportSkillRow("Voluntario MentALL", "Acompañamiento emocional", "611111111")

                    Text(
                        "Puedes añadir más contactos desde tu perfil.",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        } // ✅ cierra Column
    }     // ✅ cierra MentAllGradientBackground
}         // ✅ cierra SosScreen
@Composable
fun SupportSkillRow(name: String, skill: String, phone: String) {
    val context = LocalContext.current
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Person, contentDescription = null)
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.SemiBold)
            Text(skill, style = MaterialTheme.typography.labelSmall)
        }
        IconButton(
            onClick = {
                val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                context.startActivity(i)
            }
        ) {
            Icon(Icons.Default.Phone, contentDescription = "Llamar")
        }
    }
}

/* ---------------- PROFILE ---------------- */
@Composable
fun ProfileScreen(modifier: Modifier = Modifier, onLogout: () -> Unit) {
    var name by remember { mutableStateOf("Raúl") }
    var reminders by remember { mutableStateOf(true) }
    var hour by remember { mutableStateOf("22:00") }

    MentAllGradientBackground {
        Column(
            modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Perfil", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, null)
                        Spacer(Modifier.width(10.dp))
                        Text("Recordatorios diarios")
                        Spacer(Modifier.weight(1f))
                        Switch(reminders, onCheckedChange = { reminders = it })
                    }
                    OutlinedTextField(
                        value = hour,
                        onValueChange = { hour = it },
                        label = { Text("Hora (HH:MM)") },
                        leadingIcon = { Icon(Icons.Default.Schedule, null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Cuenta", fontWeight = FontWeight.SemiBold)
                    Button(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Logout, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Cerrar sesión")
                    }
                }
            }
        }
    }
}

/* ---------------- SHARE SKILLS ---------------- */
data class Skill(val title: String, val level: String, val desc: String)

@Composable
fun SkillsScreen(modifier: Modifier = Modifier) {
    var mySkill by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("Básico") }
    var search by remember { mutableStateOf("") }

    val others = remember {
        mutableStateListOf(
            Skill("Kotlin", "Intermedio", "Te puedo ayudar con Compose y navegación."),
            Skill("Angular", "Avanzado", "Componentes, servicios, auth localStorage."),
            Skill("SQL", "Intermedio", "Tablas, joins, consultas para informes.")
        )
    }

    val filtered = others.filter { search.isBlank() || it.title.contains(search, true) }

    MentAllGradientBackground {
        Column(
            modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Compartir habilidades", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Publicar mi habilidad", fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = mySkill,
                        onValueChange = { mySkill = it },
                        label = { Text("Habilidad (ej: Matemáticas, Kotlin…)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Básico", "Intermedio", "Avanzado").forEach { opt ->
                            FilterChip(selected = level == opt, onClick = { level = opt }, label = { Text(opt) })
                        }
                    }
                    Button(
                        onClick = {
                            if (mySkill.isNotBlank()) {
                                others.add(0, Skill(mySkill, level, "Publicado por mí (mock)."))
                                mySkill = ""
                                level = "Básico"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Upload, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Publicar")
                    }
                }
            }

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Buscar habilidades de otros") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filtered) { s ->
                    ElevatedCard(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(s.title, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                                AssistChip(onClick = { }, label = { Text(s.level) }, leadingIcon = { Icon(Icons.Default.Star, null) })
                            }
                            Text(s.desc)
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedButton(onClick = { }, shape = RoundedCornerShape(14.dp)) {
                                    Icon(Icons.Default.Chat, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Chat")
                                }
                                Button(
                                    onClick = { },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(Icons.Default.PersonAdd, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Conectar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
