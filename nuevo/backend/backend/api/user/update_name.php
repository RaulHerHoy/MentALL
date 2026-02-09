<?php
/**
 * Endpoint para actualizar el nombre del usuario
 * Método: PUT
 * Requisito TFG: API externa para gestión de perfil
 */

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: PUT");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once '../../config/database.php';

// Obtener conexión a BD
$database = new Database();
$db = $database->getConnection();

// Obtener datos del PUT
$data = json_decode(file_get_contents("php://input"));

// Validar datos recibidos
if(
    !empty($data->id_usuario) &&
    !empty($data->nombre)
){
    $id_usuario = $data->id_usuario;
    $nombre = trim($data->nombre);
    
    // Validar nombre
    if(strlen($nombre) < 2) {
        http_response_code(400);
        echo json_encode([
            "success" => false,
            "message" => "El nombre debe tener al menos 2 caracteres"
        ]);
        exit();
    }
    
    // Actualizar nombre
    $query = "UPDATE usuarios SET nombre = :nombre, updated_at = NOW() WHERE id_usuario = :id_usuario";
    
    $stmt = $db->prepare($query);
    $stmt->bindParam(":nombre", $nombre);
    $stmt->bindParam(":id_usuario", $id_usuario);
    
    if($stmt->execute()){
        // Obtener datos actualizados del usuario
        $query = "SELECT id_usuario, nombre, email FROM usuarios WHERE id_usuario = :id_usuario";
        $stmt = $db->prepare($query);
        $stmt->bindParam(":id_usuario", $id_usuario);
        $stmt->execute();
        
        $user = $stmt->fetch(PDO::FETCH_ASSOC);
        
        http_response_code(200);
        echo json_encode([
            "success" => true,
            "message" => "Nombre actualizado correctamente",
            "data" => $user
        ]);
    } else {
        http_response_code(503);
        echo json_encode([
            "success" => false,
            "message" => "No se pudo actualizar el nombre"
        ]);
    }
}
else{
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Datos incompletos. Se requiere id_usuario y nombre"
    ]);
}
?>
