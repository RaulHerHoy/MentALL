<?php
/**
 * Create Skill Endpoint
 * POST /api/skills/create.php
 * Body: { "id_usuario": 1, "titulo": "Kotlin", "nivel": "Intermedio", "descripcion": "Puedo ayudarte con..." }
 */

require_once '../../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    sendError("Método no permitido", 405);
}

$data = json_decode(file_get_contents("php://input"));

if (empty($data->id_usuario) || empty($data->titulo) || empty($data->nivel)) {
    sendError("id_usuario, titulo y nivel son requeridos");
}

// Validar nivel
$niveles_validos = ['Básico', 'Intermedio', 'Avanzado'];
if (!in_array($data->nivel, $niveles_validos)) {
    sendError("Nivel debe ser: Básico, Intermedio o Avanzado");
}

try {
    $database = new Database();
    $db = $database->getConnection();

    $query = "INSERT INTO skills (id_usuario, titulo, nivel, descripcion) 
              VALUES (:id_usuario, :titulo, :nivel, :descripcion)";
    
    $stmt = $db->prepare($query);
    $stmt->bindParam(':id_usuario', $data->id_usuario);
    $stmt->bindParam(':titulo', $data->titulo);
    $stmt->bindParam(':nivel', $data->nivel);
    $stmt->bindParam(':descripcion', $data->descripcion);
    
    if ($stmt->execute()) {
        $skillId = $db->lastInsertId();

        sendSuccess("Skill publicada exitosamente", [
            "id_skill" => $skillId,
            "titulo" => $data->titulo,
            "nivel" => $data->nivel
        ]);
    } else {
        sendError("Error al publicar la skill", 500);
    }

} catch(Exception $e) {
    error_log("Create Skill Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
