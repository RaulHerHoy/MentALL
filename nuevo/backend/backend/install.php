<?php
/**
 * INSTALADOR Y VERIFICADOR DE BACKEND
 * Ejecuta este archivo para verificar que todo esté correcto
 * URL: http://localhost/backend/install.php
 */

header('Content-Type: text/html; charset=UTF-8');
?>
<!DOCTYPE html>
<html>
<head>
    <title>MentALL Backend - Instalación</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
        .success { color: green; font-weight: bold; }
        .error { color: red; font-weight: bold; }
        .warning { color: orange; font-weight: bold; }
        .section { background: #f5f5f5; padding: 15px; margin: 10px 0; border-radius: 5px; }
        h2 { border-bottom: 2px solid #333; padding-bottom: 10px; }
        code { background: #e0e0e0; padding: 2px 5px; border-radius: 3px; }
    </style>
</head>
<body>
    <h1>🚀 MentALL Backend - Instalador</h1>
    
    <?php
    // Verificación de archivos
    $basePath = __DIR__;
    $errors = [];
    $warnings = [];
    $success = [];
    
    // 1. Verificar estructura de carpetas
    echo "<div class='section'>";
    echo "<h2>📁 Verificación de Estructura</h2>";
    
    $requiredDirs = [
        'config',
        'api',
        'api/auth',
        'api/moods',
        'api/activities',
        'api/recommendations',
        'api/skills',
        'api/contacts',
        'api/calls'
    ];
    
    foreach ($requiredDirs as $dir) {
        $path = $basePath . '/' . $dir;
        if (is_dir($path)) {
            echo "<p class='success'>✓ Carpeta existe: $dir</p>";
        } else {
            echo "<p class='error'>✗ Falta carpeta: $dir</p>";
            $errors[] = "Falta la carpeta: $dir";
            // Intentar crear
            if (mkdir($path, 0755, true)) {
                echo "<p class='success'>  → Carpeta creada automáticamente</p>";
            }
        }
    }
    echo "</div>";
    
    // 2. Verificar archivos críticos
    echo "<div class='section'>";
    echo "<h2>📄 Verificación de Archivos</h2>";
    
    $requiredFiles = [
        'config/database.php' => 'Configuración de base de datos',
        'api/auth/login.php' => 'Endpoint de login',
        'api/auth/register.php' => 'Endpoint de registro',
        'api/moods/create.php' => 'Crear mood',
        'api/moods/list.php' => 'Listar moods',
        'api/activities/list.php' => 'Listar actividades'
    ];
    
    foreach ($requiredFiles as $file => $desc) {
        $path = $basePath . '/' . $file;
        if (file_exists($path)) {
            echo "<p class='success'>✓ $desc: <code>$file</code></p>";
        } else {
            echo "<p class='error'>✗ Falta: <code>$file</code> ($desc)</p>";
            $errors[] = "Falta el archivo: $file";
        }
    }
    echo "</div>";
    
    // 3. Verificar conexión a base de datos
    echo "<div class='section'>";
    echo "<h2>🗄️ Verificación de Base de Datos</h2>";
    
    try {
        $conn = new PDO("mysql:host=localhost", "root", "");
        echo "<p class='success'>✓ Conexión a MySQL exitosa</p>";
        
        // Verificar si existe la base de datos mentall
        $result = $conn->query("SHOW DATABASES LIKE 'mentall'");
        if ($result->rowCount() > 0) {
            echo "<p class='success'>✓ Base de datos 'mentall' existe</p>";
            
            // Verificar tablas
            $conn = new PDO("mysql:host=localhost;dbname=mentall", "root", "");
            $tables = ['usuarios', 'actividades', 'mood_registros', 'skills', 'contactos_ayuda'];
            
            foreach ($tables as $table) {
                $result = $conn->query("SHOW TABLES LIKE '$table'");
                if ($result->rowCount() > 0) {
                    echo "<p class='success'>  ✓ Tabla '$table' existe</p>";
                } else {
                    echo "<p class='error'>  ✗ Falta tabla: $table</p>";
                    $errors[] = "Falta la tabla: $table - Importa el archivo SQL";
                }
            }
            
        } else {
            echo "<p class='error'>✗ Base de datos 'mentall' NO existe</p>";
            echo "<p class='warning'>⚠ Debes importar el archivo <code>mentall_database_updated.sql</code> en phpMyAdmin</p>";
            $errors[] = "Base de datos 'mentall' no existe";
        }
        
    } catch (PDOException $e) {
        echo "<p class='error'>✗ Error de conexión: " . $e->getMessage() . "</p>";
        $errors[] = "No se puede conectar a MySQL: " . $e->getMessage();
    }
    echo "</div>";
    
    // 4. Probar endpoints
    echo "<div class='section'>";
    echo "<h2>🔗 Endpoints Disponibles</h2>";
    
    $baseUrl = "http://" . $_SERVER['HTTP_HOST'] . dirname($_SERVER['PHP_SELF']);
    $endpoints = [
        'api/activities/list.php' => 'Listar Actividades',
        'api/auth/login.php' => 'Login (POST)',
        'api/auth/register.php' => 'Registro (POST)'
    ];
    
    foreach ($endpoints as $endpoint => $name) {
        $url = str_replace('/install.php', '/' . $endpoint, $baseUrl);
        echo "<p>→ <strong>$name:</strong> <a href='$url' target='_blank'>$url</a></p>";
    }
    echo "</div>";
    
    // 5. Resumen
    echo "<div class='section'>";
    echo "<h2>📊 Resumen</h2>";
    
    if (count($errors) == 0) {
        echo "<p class='success'>✅ ¡Todo está correctamente configurado!</p>";
        echo "<p>Puedes empezar a usar la API.</p>";
        echo "<p><strong>URL Base:</strong> <code>" . str_replace('/install.php', '/api/', $baseUrl) . "</code></p>";
    } else {
        echo "<p class='error'>❌ Se encontraron " . count($errors) . " errores:</p>";
        echo "<ul>";
        foreach ($errors as $error) {
            echo "<li class='error'>$error</li>";
        }
        echo "</ul>";
        
        echo "<h3>🔧 Pasos para solucionar:</h3>";
        echo "<ol>";
        echo "<li>Asegúrate de que la carpeta <code>backend</code> esté completa en <code>C:\\xampp\\htdocs\\backend\\</code></li>";
        echo "<li>Importa <code>mentall_database_updated.sql</code> en phpMyAdmin</li>";
        echo "<li>Verifica que Apache y MySQL estén corriendo en XAMPP</li>";
        echo "<li>Recarga esta página para verificar de nuevo</li>";
        echo "</ol>";
    }
    echo "</div>";
    
    // 6. Instrucciones de uso
    echo "<div class='section'>";
    echo "<h2>📖 Instrucciones de Uso</h2>";
    echo "<h3>1. Para Android (Emulador):</h3>";
    echo "<code>BASE_URL = \"http://10.0.2.2/backend/api/\"</code>";
    
    echo "<h3>2. Para Android (Dispositivo físico):</h3>";
    echo "<p>Encuentra tu IP con <code>ipconfig</code> en CMD:</p>";
    
    // Intentar obtener IP local
    $localIP = gethostbyname(gethostname());
    echo "<code>BASE_URL = \"http://$localIP/backend/api/\"</code>";
    
    echo "<h3>3. Probar con Postman/Thunder Client:</h3>";
    echo "<pre>
POST http://localhost/backend/api/auth/register.php
{
  \"nombre\": \"Test User\",
  \"email\": \"test@test.com\",
  \"password\": \"123456\"
}
</pre>";
    echo "</div>";
    ?>
</body>
</html>