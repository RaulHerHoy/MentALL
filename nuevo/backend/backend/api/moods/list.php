<?php
/**
 * List Moods Endpoint
 * GET /api/moods/list.php?id_usuario=1&limit=20
 */

require_once '../../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    sendError("Método no permitido", 405);
}

if (empty($_GET['id_usuario'])) {
    sendError("id_usuario es requerido");
}

$id_usuario = intval($_GET['id_usuario']);
$limit = isset($_GET['limit']) ? intval($_GET['limit']) : 50;

try {
    $database = new Database();
    $db = $database->getConnection();

    $query = "SELECT id_registro, valor, etiqueta, actividad_realizada, nota, imagen_uri, 
              DATE_FORMAT(created_at, '%Y-%m-%d %H:%i:%s') as fecha
              FROM mood_registros 
              WHERE id_usuario = :id_usuario 
              ORDER BY created_at DESC 
              LIMIT :limit";
    
    $stmt = $db->prepare($query);
    $stmt->bindParam(':id_usuario', $id_usuario, PDO::PARAM_INT);
    $stmt->bindParam(':limit', $limit, PDO::PARAM_INT);
    $stmt->execute();

    $registros = $stmt->fetchAll();

    sendSuccess("Registros obtenidos", [
        "total" => count($registros),
        "registros" => $registros
    ]);

} catch(Exception $e) {
    error_log("List Moods Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
