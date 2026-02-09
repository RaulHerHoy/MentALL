<?php
/**
 * Get Recommendations Endpoint
 * GET /api/recommendations/get.php?id_usuario=1
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

    // Obtener recomendaciones del último registro de mood
    $query = "SELECT r.id_recomendacion, r.motivo,
              a.id_actividad, a.titulo, a.descripcion, a.categoria, a.duracion_min, a.enlace,
              m.valor as mood_valor,
              DATE_FORMAT(r.created_at, '%Y-%m-%d %H:%i') as fecha_recomendacion
              FROM recomendaciones r
              INNER JOIN actividades a ON r.id_actividad = a.id_actividad
              INNER JOIN mood_registros m ON r.id_registro = m.id_registro
              WHERE m.id_usuario = :id_usuario
              ORDER BY r.created_at DESC
              LIMIT 5";
    
    $stmt = $db->prepare($query);
    $stmt->bindParam(':id_usuario', $id_usuario, PDO::PARAM_INT);
    $stmt->execute();

    $recomendaciones = $stmt->fetchAll();

    if (count($recomendaciones) === 0) {
        // Si no hay recomendaciones, devolver actividades aleatorias
        $queryRandom = "SELECT id_actividad, titulo, descripcion, categoria, duracion_min, enlace
                       FROM actividades 
                       ORDER BY RAND() 
                       LIMIT 3";
        $stmtRandom = $db->prepare($queryRandom);
        $stmtRandom->execute();
        $actividades = $stmtRandom->fetchAll();

        sendSuccess("Recomendaciones generales", [
            "total" => count($actividades),
            "recomendaciones" => array_map(function($act) {
                return [
                    "id_actividad" => $act['id_actividad'],
                    "titulo" => $act['titulo'],
                    "descripcion" => $act['descripcion'],
                    "categoria" => $act['categoria'],
                    "duracion_min" => $act['duracion_min'],
                    "enlace" => $act['enlace'],
                    "motivo" => "Actividad recomendada para ti"
                ];
            }, $actividades)
        ]);
    } else {
        sendSuccess("Recomendaciones basadas en tu estado", [
            "total" => count($recomendaciones),
            "recomendaciones" => $recomendaciones
        ]);
    }

} catch(Exception $e) {
    error_log("Get Recommendations Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
