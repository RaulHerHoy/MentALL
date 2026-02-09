<?php
/**
 * Create Mood Endpoint
 * POST /api/moods/create.php
 * Body: { 
 *   "id_usuario": 1, 
 *   "valor": 3, 
 *   "etiqueta": "Trabajo", 
 *   "actividad_realizada": "Paseo corto",
 *   "nota": "Me siento mejor después del paseo",
 *   "imagen_uri": "file:///storage/image123.jpg"
 * }
 */

require_once '../../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    sendError("Método no permitido", 405);
}

$data = json_decode(file_get_contents("php://input"));

if (empty($data->id_usuario) || empty($data->valor)) {
    sendError("id_usuario y valor son requeridos");
}

if ($data->valor < 1 || $data->valor > 5) {
    sendError("El valor debe estar entre 1 y 5");
}

try {
    $database = new Database();
    $db = $database->getConnection();

    // Insertar registro de mood
    $query = "INSERT INTO mood_registros 
              (id_usuario, valor, etiqueta, actividad_realizada, nota, imagen_uri) 
              VALUES (:id_usuario, :valor, :etiqueta, :actividad, :nota, :imagen)";
    
    $stmt = $db->prepare($query);
    $stmt->bindParam(':id_usuario', $data->id_usuario);
    $stmt->bindParam(':valor', $data->valor);
    $stmt->bindParam(':etiqueta', $data->etiqueta);
    $stmt->bindParam(':actividad', $data->actividad_realizada);
    $stmt->bindParam(':nota', $data->nota);
    $stmt->bindParam(':imagen', $data->imagen_uri);
    
    if ($stmt->execute()) {
        $registroId = $db->lastInsertId();

        // Generar recomendaciones basadas en el valor del mood
        $recomendaciones = [];
        
        if ($data->valor <= 2) {
            // Mood bajo: recomendar actividades calmantes
            $actividadesIds = [1, 3, 6]; // Respiración, Escritura, Meditación
        } elseif ($data->valor == 3) {
            // Mood regular: actividades variadas
            $actividadesIds = [2, 4, 5]; // Paseo, Body scan, Agua
        } else {
            // Mood alto: mantener la energía
            $actividadesIds = [7, 2]; // Ejercicio, Paseo
        }

        // Insertar recomendaciones
        foreach ($actividadesIds as $actId) {
            $queryRec = "INSERT INTO recomendaciones (id_registro, id_actividad, motivo) 
                        VALUES (:id_registro, :id_actividad, :motivo)";
            $stmtRec = $db->prepare($queryRec);
            $stmtRec->bindParam(':id_registro', $registroId);
            $stmtRec->bindParam(':id_actividad', $actId);
            
            $motivo = "Recomendada basada en tu estado de ánimo";
            $stmtRec->bindParam(':motivo', $motivo);
            $stmtRec->execute();
        }

        sendSuccess("Registro de emoción guardado", [
            "id_registro" => $registroId,
            "valor" => $data->valor
        ]);
    } else {
        sendError("Error al guardar el registro", 500);
    }

} catch(Exception $e) {
    error_log("Create Mood Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
