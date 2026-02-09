<?php
/**
 * List Contacts Endpoint
 * GET /api/contacts/list.php?id_usuario=1
 */

require_once '../../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    sendError("Método no permitido", 405);
}

if (empty($_GET['id_usuario'])) {
    sendError("id_usuario es requerido");
}

$id_usuario = intval($_GET['id_usuario']);

try {
    $database = new Database();
    $db = $database->getConnection();

    $query = "SELECT id_contacto, nombre, telefono, descripcion, es_emergencia, orden
              FROM contactos_ayuda 
              WHERE id_usuario = :id_usuario 
              ORDER BY orden ASC, nombre ASC";
    
    $stmt = $db->prepare($query);
    $stmt->bindParam(':id_usuario', $id_usuario, PDO::PARAM_INT);
    $stmt->execute();

    $contactos = $stmt->fetchAll();

    sendSuccess("Contactos obtenidos", [
        "total" => count($contactos),
        "contactos" => $contactos
    ]);

} catch(Exception $e) {
    error_log("List Contacts Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
