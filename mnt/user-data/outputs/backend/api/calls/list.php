<?php
/**
 * List Scheduled Calls Endpoint
 * GET /api/calls/list.php?id_usuario=1
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

    $query = "SELECT id_llamada, telefono, motivo, dia_semana, dia_mes, 
              TIME_FORMAT(hora, '%H:%i') as hora, activa,
              DATE_FORMAT(created_at, '%Y-%m-%d') as fecha_creacion
              FROM llamadas_programadas 
              WHERE id_usuario = :id_usuario 
              ORDER BY activa DESC, hora ASC";
    
    $stmt = $db->prepare($query);
    $stmt->bindParam(':id_usuario', $id_usuario, PDO::PARAM_INT);
    $stmt->execute();

    $llamadas = $stmt->fetchAll();

    // Formatear descripción legible
    foreach ($llamadas as &$llamada) {
        $descripcion = "";
        
        if ($llamada['dia_mes']) {
            $descripcion = "Día " . $llamada['dia_mes'] . " de cada mes";
        } elseif ($llamada['dia_semana']) {
            $dias = ['', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'];
            $descripcion = "Todos los " . $dias[$llamada['dia_semana']];
        } else {
            $descripcion = "Todos los días";
        }
        
        $llamada['descripcion'] = $descripcion . " a las " . $llamada['hora'];
    }

    sendSuccess("Llamadas programadas", [
        "total" => count($llamadas),
        "llamadas" => $llamadas
    ]);

} catch(Exception $e) {
    error_log("List Calls Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
