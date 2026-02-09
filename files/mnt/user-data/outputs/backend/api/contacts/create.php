<?php
/**
 * Create Contact Endpoint
 * POST /api/contacts/create.php
 * Body: { "id_usuario": 1, "nombre": "María", "telefono": "600000000", "descripcion": "Amiga cercana" }
 */

require_once '../../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    sendError("Método no permitido", 405);
}

$data = json_decode(file_get_contents("php://input"));

if (empty($data->id_usuario) || empty($data->nombre) || empty($data->telefono)) {
    sendError("id_usuario, nombre y telefono son requeridos");
}

try {
    $database = new Database();
    $db = $database->getConnection();

    $es_emergencia = isset($data->es_emergencia) ? $data->es_emergencia : 0;
    $orden = isset($data->orden) ? $data->orden : 0;

    $query = "INSERT INTO contactos_ayuda (id_usuario, nombre, telefono, descripcion, es_emergencia, orden) 
              VALUES (:id_usuario, :nombre, :telefono, :descripcion, :es_emergencia, :orden)";
    
    $stmt = $db->prepare($query);
    $stmt->bindParam(':id_usuario', $data->id_usuario);
    $stmt->bindParam(':nombre', $data->nombre);
    $stmt->bindParam(':telefono', $data->telefono);
    $stmt->bindParam(':descripcion', $data->descripcion);
    $stmt->bindParam(':es_emergencia', $es_emergencia);
    $stmt->bindParam(':orden', $orden);
    
    if ($stmt->execute()) {
        $contactoId = $db->lastInsertId();

        sendSuccess("Contacto añadido exitosamente", [
            "id_contacto" => $contactoId,
            "nombre" => $data->nombre
        ]);
    } else {
        sendError("Error al añadir contacto", 500);
    }

} catch(Exception $e) {
    error_log("Create Contact Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
