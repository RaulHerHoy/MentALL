<?php
/**
 * Toggle Call Endpoint
 * PUT /api/calls/toggle.php
 * Body: { "id_llamada": 1, "activa": 0 }
 */

require_once '../../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'PUT') {
    sendError("Método no permitido", 405);
}

$data = json_decode(file_get_contents("php://input"));

if (!isset($data->id_llamada) || !isset($data->activa)) {
    sendError("id_llamada y activa son requeridos");
}

try {
    $database = new Database();
    $db = $database->getConnection();

    $query = "UPDATE llamadas_programadas 
              SET activa = :activa 
              WHERE id_llamada = :id_llamada";
    
    $stmt = $db->prepare($query);
    $stmt->bindParam(':activa', $data->activa);
    $stmt->bindParam(':id_llamada', $data->id_llamada);
    
    if ($stmt->execute()) {
        $estado = $data->activa ? "activada" : "desactivada";
        sendSuccess("Llamada " . $estado . " exitosamente");
    } else {
        sendError("Error al actualizar llamada", 500);
    }

} catch(Exception $e) {
    error_log("Toggle Call Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
