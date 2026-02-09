<?php
/**
 * Create Scheduled Call Endpoint
 * POST /api/calls/create.php
 * Body: { 
 *   "id_usuario": 1, 
 *   "telefono": "717003717", 
 *   "motivo": "Los lunes me siento peor",
 *   "dia_semana": 1,  // 1=Lunes, 2=Martes... 7=Domingo, null=todos los días
 *   "dia_mes": null,  // Día específico del mes (1-31), null si no aplica
 *   "hora": "14:30"   // Formato HH:MM
 * }
 */

require_once '../../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    sendError("Método no permitido", 405);
}

$data = json_decode(file_get_contents("php://input"));

if (empty($data->id_usuario) || empty($data->telefono) || empty($data->hora)) {
    sendError("id_usuario, telefono y hora son requeridos");
}

// Validar formato de hora
if (!preg_match('/^([01]?[0-9]|2[0-3]):[0-5][0-9]$/', $data->hora)) {
    sendError("Formato de hora inválido. Usar HH:MM");
}

try {
    $database = new Database();
    $db = $database->getConnection();

    $dia_semana = isset($data->dia_semana) ? $data->dia_semana : null;
    $dia_mes = isset($data->dia_mes) ? $data->dia_mes : null;

    $query = "INSERT INTO llamadas_programadas 
              (id_usuario, telefono, motivo, dia_semana, dia_mes, hora, activa) 
              VALUES (:id_usuario, :telefono, :motivo, :dia_semana, :dia_mes, :hora, 1)";
    
    $stmt = $db->prepare($query);
    $stmt->bindParam(':id_usuario', $data->id_usuario);
    $stmt->bindParam(':telefono', $data->telefono);
    $stmt->bindParam(':motivo', $data->motivo);
    $stmt->bindParam(':dia_semana', $dia_semana);
    $stmt->bindParam(':dia_mes', $dia_mes);
    $stmt->bindParam(':hora', $data->hora);
    
    if ($stmt->execute()) {
        $llamadaId = $db->lastInsertId();

        sendSuccess("Llamada programada exitosamente", [
            "id_llamada" => $llamadaId,
            "telefono" => $data->telefono,
            "hora" => $data->hora,
            "dia_semana" => $dia_semana,
            "dia_mes" => $dia_mes
        ]);
    } else {
        sendError("Error al programar llamada", 500);
    }

} catch(Exception $e) {
    error_log("Create Call Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
