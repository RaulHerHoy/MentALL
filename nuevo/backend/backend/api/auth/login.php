<?php
/**
 * Login Endpoint
 * POST /api/auth/login.php
 * Body: { "email": "user@example.com", "password": "123456" }
 */

require_once '../../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    sendError("Método no permitido", 405);
}

// Obtener datos del body
$data = json_decode(file_get_contents("php://input"));

if (empty($data->email) || empty($data->password)) {
    sendError("Email y contraseña son requeridos");
}

try {
    $database = new Database();
    $db = $database->getConnection();

    // Buscar usuario por email
    $query = "SELECT id_usuario, nombre, email, password FROM usuarios WHERE email = :email LIMIT 1";
    $stmt = $db->prepare($query);
    $stmt->bindParam(':email', $data->email);
    $stmt->execute();

    if ($stmt->rowCount() === 0) {
        sendError("Credenciales incorrectas", 401);
    }

    $user = $stmt->fetch();

    // Verificar contraseña
    if (!password_verify($data->password, $user['password'])) {
        sendError("Credenciales incorrectas", 401);
    }

    // Login exitoso
    sendSuccess("Login exitoso", [
        "id_usuario" => $user['id_usuario'],
        "nombre" => $user['nombre'],
        "email" => $user['email']
    ]);

} catch(Exception $e) {
    error_log("Login Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>