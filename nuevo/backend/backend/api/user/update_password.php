<?php
/**
 * Endpoint para actualizar la contraseña del usuario
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
    !empty($data->password_actual) &&
    !empty($data->password_nueva)
){
    $id_usuario = $data->id_usuario;
    $password_actual = $data->password_actual;
    $password_nueva = $data->password_nueva;
    
    // Validar longitud de nueva contraseña
    if(strlen($password_nueva) < 6) {
        http_response_code(400);
        echo json_encode([
            "success" => false,
            "message" => "La nueva contraseña debe tener al menos 6 caracteres"
        ]);
        exit();
    }
    
    // Verificar contraseña actual
    $query = "SELECT password FROM usuarios WHERE id_usuario = :id_usuario";
    $stmt = $db->prepare($query);
    $stmt->bindParam(":id_usuario", $id_usuario);
    $stmt->execute();
    
    if($stmt->rowCount() > 0) {
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        $hashed_password = $row['password'];
        
        // Verificar que la contraseña actual sea correcta
        if(password_verify($password_actual, $hashed_password)) {
            // Hash de la nueva contraseña
            $nueva_password_hash = password_hash($password_nueva, PASSWORD_BCRYPT);
            
            // Actualizar contraseña
            $query = "UPDATE usuarios SET password = :password, updated_at = NOW() WHERE id_usuario = :id_usuario";
            $stmt = $db->prepare($query);
            $stmt->bindParam(":password", $nueva_password_hash);
            $stmt->bindParam(":id_usuario", $id_usuario);
            
            if($stmt->execute()){
                http_response_code(200);
                echo json_encode([
                    "success" => true,
                    "message" => "Contraseña actualizada correctamente"
                ]);
            } else {
                http_response_code(503);
                echo json_encode([
                    "success" => false,
                    "message" => "No se pudo actualizar la contraseña"
                ]);
            }
        } else {
            http_response_code(401);
            echo json_encode([
                "success" => false,
                "message" => "La contraseña actual es incorrecta"
            ]);
        }
    } else {
        http_response_code(404);
        echo json_encode([
            "success" => false,
            "message" => "Usuario no encontrado"
        ]);
    }
}
else{
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Datos incompletos. Se requiere id_usuario, password_actual y password_nueva"
    ]);
}
?>
