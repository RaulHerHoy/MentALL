<?php
/**
 * Register Endpoint
 * POST /api/auth/register.php
 * Body: { "nombre": "Juan", "email": "juan@example.com", "password": "123456" }
 */

require_once '../../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    sendError("Método no permitido", 405);
}

// Obtener datos del body
$data = json_decode(file_get_contents("php://input"));

if (empty($data->nombre) || empty($data->email) || empty($data->password)) {
    sendError("Nombre, email y contraseña son requeridos");
}

// Validar email
if (!filter_var($data->email, FILTER_VALIDATE_EMAIL)) {
    sendError("Email no válido");
}

// Validar longitud de contraseña
if (strlen($data->password) < 6) {
    sendError("La contraseña debe tener al menos 6 caracteres");
}

try {
    $database = new Database();
    $db = $database->getConnection();

    // Verificar si el email ya existe
    $query = "SELECT id_usuario FROM usuarios WHERE email = :email LIMIT 1";
    $stmt = $db->prepare($query);
    $stmt->bindParam(':email', $data->email);
    $stmt->execute();

    if ($stmt->rowCount() > 0) {
        sendError("El email ya está registrado", 409);
    }

    // Hash de la contraseña
    $hashed_password = password_hash($data->password, PASSWORD_DEFAULT);

    // Insertar usuario
    $query = "INSERT INTO usuarios (nombre, email, password) VALUES (:nombre, :email, :password)";
    $stmt = $db->prepare($query);
    $stmt->bindParam(':nombre', $data->nombre);
    $stmt->bindParam(':email', $data->email);
    $stmt->bindParam(':password', $hashed_password);
    
    if ($stmt->execute()) {
        $userId = $db->lastInsertId();

        // Crear preferencias por defecto
        $query = "INSERT INTO preferencias_usuario (id_usuario) VALUES (:id_usuario)";
        $stmt = $db->prepare($query);
        $stmt->bindParam(':id_usuario', $userId);
        $stmt->execute();

        sendSuccess("Usuario registrado exitosamente", [
            "id_usuario" => $userId,
            "nombre" => $data->nombre,
            "email" => $data->email
        ]);
    } else {
        sendError("Error al crear el usuario", 500);
    }

} catch(Exception $e) {
    error_log("Register Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
