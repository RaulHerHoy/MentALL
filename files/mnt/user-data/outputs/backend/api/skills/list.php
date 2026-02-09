<?php
/**
 * List Skills Endpoint
 * GET /api/skills/list.php?search=kotlin&limit=20
 */

require_once '../../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    sendError("Método no permitido", 405);
}

$search = isset($_GET['search']) ? trim($_GET['search']) : '';
$limit = isset($_GET['limit']) ? intval($_GET['limit']) : 50;

try {
    $database = new Database();
    $db = $database->getConnection();

    if (!empty($search)) {
        $query = "SELECT s.id_skill, s.titulo, s.nivel, s.descripcion,
                  u.id_usuario, u.nombre as usuario_nombre,
                  DATE_FORMAT(s.created_at, '%Y-%m-%d') as fecha
                  FROM skills s
                  INNER JOIN usuarios u ON s.id_usuario = u.id_usuario
                  WHERE s.titulo LIKE :search OR s.descripcion LIKE :search
                  ORDER BY s.created_at DESC
                  LIMIT :limit";
        
        $stmt = $db->prepare($query);
        $searchParam = "%{$search}%";
        $stmt->bindParam(':search', $searchParam);
        $stmt->bindParam(':limit', $limit, PDO::PARAM_INT);
    } else {
        $query = "SELECT s.id_skill, s.titulo, s.nivel, s.descripcion,
                  u.id_usuario, u.nombre as usuario_nombre,
                  DATE_FORMAT(s.created_at, '%Y-%m-%d') as fecha
                  FROM skills s
                  INNER JOIN usuarios u ON s.id_usuario = u.id_usuario
                  ORDER BY s.created_at DESC
                  LIMIT :limit";
        
        $stmt = $db->prepare($query);
        $stmt->bindParam(':limit', $limit, PDO::PARAM_INT);
    }
    
    $stmt->execute();
    $skills = $stmt->fetchAll();

    sendSuccess("Skills obtenidas", [
        "total" => count($skills),
        "skills" => $skills
    ]);

} catch(Exception $e) {
    error_log("List Skills Error: " . $e->getMessage());
    sendError("Error en el servidor", 500);
}
?>
