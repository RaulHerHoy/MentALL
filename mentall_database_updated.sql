-- phpMyAdmin SQL Dump - MentALL v2.0
-- Base de datos actualizada con DATETIME y nuevas funcionalidades
-- Fecha: 28-01-2026

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `mentall`
--
CREATE DATABASE IF NOT EXISTS `mentall` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `mentall`;

-- --------------------------------------------------------

--
-- Tabla: usuarios
--
CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabla: actividades
--
CREATE TABLE `actividades` (
  `id_actividad` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  `duracion_min` int(11) DEFAULT NULL,
  `enlace` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_actividad`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Datos iniciales para actividades
--
INSERT INTO `actividades` (`titulo`, `descripcion`, `categoria`, `duracion_min`, `enlace`) VALUES
('Respiración 4-7-8', 'Inhala 4s, mantén 7s, exhala 8s. Repite 4 veces.', 'Respiración', 3, NULL),
('Paseo corto', 'Camina 10 minutos sin móvil si es posible.', 'Hábitos', 10, NULL),
('Escritura consciente', 'Escribe 3 preocupaciones y una acción pequeña.', 'Mindfulness', 5, NULL),
('Body scan', 'Recorre el cuerpo y relaja tensiones.', 'Mindfulness', 6, NULL),
('Agua + pausa', 'Bebe agua y descansa 2 minutos reales.', 'Hábitos', 2, NULL),
('Meditación guiada', 'Sesión corta de meditación para centrarte.', 'Mindfulness', 10, NULL),
('Ejercicio ligero', 'Estiramientos o yoga suave.', 'Ejercicio', 15, NULL);

-- --------------------------------------------------------

--
-- Tabla: moods_catalogo (estados de ánimo predefinidos)
--
CREATE TABLE `moods_catalogo` (
  `id_mood` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `valor` int(11) NOT NULL CHECK (`valor` BETWEEN 1 AND 5),
  `color_hex` varchar(7) DEFAULT NULL,
  `icono_res` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_mood`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Datos del catálogo de moods
--
INSERT INTO `moods_catalogo` (`nombre`, `valor`, `color_hex`, `icono_res`) VALUES
('Muy mal', 1, '#FF4D4D', 'ic_mood_1'),
('Mal', 2, '#FF944D', 'ic_mood_2'),
('Regular', 3, '#FFD24D', 'ic_mood_3'),
('Bien', 4, '#9BE15D', 'ic_mood_4'),
('Genial', 5, '#00D4A6', 'ic_mood_5');

-- --------------------------------------------------------

--
-- Tabla: mood_registros
--
CREATE TABLE `mood_registros` (
  `id_registro` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `valor` int(11) NOT NULL CHECK (`valor` BETWEEN 1 AND 5),
  `etiqueta` varchar(50) DEFAULT NULL,
  `actividad_realizada` varchar(200) DEFAULT NULL,
  `nota` text DEFAULT NULL,
  `imagen_uri` text DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_registro`),
  KEY `idx_mood_usuario_fecha` (`id_usuario`, `created_at`),
  KEY `idx_mood_fecha` (`created_at`),
  FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabla: recomendaciones
--
CREATE TABLE `recomendaciones` (
  `id_recomendacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_registro` int(11) NOT NULL,
  `id_actividad` int(11) NOT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_recomendacion`),
  KEY `fk_rec_registro` (`id_registro`),
  KEY `fk_rec_actividad` (`id_actividad`),
  FOREIGN KEY (`id_registro`) REFERENCES `mood_registros` (`id_registro`) ON DELETE CASCADE,
  FOREIGN KEY (`id_actividad`) REFERENCES `actividades` (`id_actividad`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabla: contactos_ayuda (red de apoyo personal del usuario)
--
CREATE TABLE `contactos_ayuda` (
  `id_contacto` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `es_emergencia` tinyint(1) DEFAULT 0,
  `orden` int(11) DEFAULT 0,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_contacto`),
  KEY `idx_contacto_usuario` (`id_usuario`),
  FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabla: skills (habilidades compartidas por usuarios)
--
CREATE TABLE `skills` (
  `id_skill` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `nivel` ENUM('Básico', 'Intermedio', 'Avanzado') NOT NULL DEFAULT 'Básico',
  `descripcion` text DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_skill`),
  KEY `idx_skill_usuario` (`id_usuario`),
  FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabla: llamadas_programadas (para SOS)
--
CREATE TABLE `llamadas_programadas` (
  `id_llamada` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `telefono` varchar(20) NOT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `dia_semana` int(11) DEFAULT NULL COMMENT '1=Lunes, 7=Domingo, NULL=todos los días',
  `dia_mes` int(11) DEFAULT NULL COMMENT 'Día específico del mes (1-31)',
  `hora` TIME NOT NULL,
  `activa` tinyint(1) DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_llamada`),
  KEY `idx_llamada_usuario` (`id_usuario`),
  FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabla: preferencias_usuario
--
CREATE TABLE `preferencias_usuario` (
  `id_preferencia` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `recordatorios_activos` tinyint(1) DEFAULT 1,
  `hora_recordatorio` TIME DEFAULT '22:00:00',
  `tema_oscuro` tinyint(1) DEFAULT 0,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_preferencia`),
  UNIQUE KEY `unique_usuario_pref` (`id_usuario`),
  FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
