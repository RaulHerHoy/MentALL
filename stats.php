<?php
/**
 * Mood Stats Endpoint
 * GET /api/moods/stats.php?id_usuario=1&days=7
 */

require_once '../../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    sendError("Método no permitido", 405);
}

if (empty($_GET['id_usuario'])) {
    sendError("id_usuario es requerido");
}

$id_usuario = intval($_GET['id_usuario']);
$days = isset($_GET['days']) ? intval($_GET['days']) : 7;

try {
    $database = new Database();
    $db = $database->getConnection();

    // Últimos N días de moods
    $query = "SELECT valor, DATE_FORMAT(created_at, '%Y-%m-%d') as fecha
              FROM mood_registros 
              WHERE id_usuario = :id_usuario 
              AND created_at >= DATE_SUB(NOW(), INTERVAL :days DAY)
              ORDER BY created_at ASC";
    
    $stmt = $db->prepare($query);
    $stmt->bindParam(':id_usuario', $id_usuario, PDO::PARAM_INT);
    $stmt->bindParam(':days', $days, PDO::PARAM_INT);
    $stmt->execute();

    $registros = $stmt->fetchAll();

    // Calcular promedio
    $valores = array_column($registros, 'valor');
    $promedio = count($valores) > 0 ? round(array_sum($valores) / count($valores), 1) : 0;

    // Último registro
    $queryLast = "SELECT valor, actividad_realizada, nota, 
                  DATE_FORMAT(created_at, '%Y-%m-%d %H:%i') as fecha
                  FROM mood_registros 
                  WHERE id_usuario = :id_usuario 
                  ORDER BY created_at DESC LIMIT 1";
    
    $stmtLast = $db->prepare($queryLast);
    $stmtLast->bindParam(':id_usuario', $id_usuario, PDO::PARAM_INT);
    $stmtLast->execute();
    $ultimo = $stmtLast->fetch();

    sendSuccess("Estadísticas obtenidas", [
        "dias_analizados" => $days,
        "total_registros" => count($registros),
        "promedio" => $promedio,
        "ultimos_valores" => $valores,
        "ultimo_registro" => $ultimo ?: null
    ]);

} catch(Exception $e) {
    error_log("Stats Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
