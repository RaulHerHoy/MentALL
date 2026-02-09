<?php
/**
 * List Activities Endpoint
 * GET /api/activities/list.php
 */

require_once '../../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    sendError("Método no permitido", 405);
}

try {
    $database = new Database();
    $db = $database->getConnection();

    $query = "SELECT id_actividad, titulo, descripcion, categoria, duracion_min, enlace
              FROM actividades 
              ORDER BY categoria, titulo";
    
    $stmt = $db->prepare($query);
    $stmt->execute();

    $actividades = $stmt->fetchAll();

    sendSuccess("Actividades obtenidas", [
        "total" => count($actividades),
        "actividades" => $actividades
    ]);

} catch(Exception $e) {
    error_log("List Activities Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
