-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 09-02-2026 a las 17:41:43
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

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
-- Estructura de tabla para la tabla `actividades`
--

CREATE TABLE `actividades` (
  `id_actividad` int(11) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  `duracion_min` int(11) DEFAULT NULL,
  `enlace` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `actividades`
--

INSERT INTO `actividades` (`id_actividad`, `titulo`, `descripcion`, `categoria`, `duracion_min`, `enlace`) VALUES
(1, 'Respiración 4-7-8', 'Inhala 4s, mantén 7s, exhala 8s. Repite 4 veces.', 'Respiración', 3, NULL),
(2, 'Paseo corto', 'Camina 10 minutos sin móvil si es posible.', 'Hábitos', 10, NULL),
(3, 'Escritura consciente', 'Escribe 3 preocupaciones y una acción pequeña.', 'Mindfulness', 5, NULL),
(4, 'Body scan', 'Recorre el cuerpo y relaja tensiones.', 'Mindfulness', 6, NULL),
(5, 'Agua + pausa', 'Bebe agua y descansa 2 minutos reales.', 'Hábitos', 2, NULL),
(6, 'Meditación guiada', 'Sesión corta de meditación para centrarte.', 'Mindfulness', 10, NULL),
(7, 'Ejercicio ligero', 'Estiramientos o yoga suave.', 'Ejercicio', 15, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contactos_ayuda`
--

CREATE TABLE `contactos_ayuda` (
  `id_contacto` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `es_emergencia` tinyint(1) DEFAULT 0,
  `orden` int(11) DEFAULT 0,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `contactos_ayuda`
--

INSERT INTO `contactos_ayuda` (`id_contacto`, `id_usuario`, `nombre`, `telefono`, `descripcion`, `es_emergencia`, `orden`, `created_at`) VALUES
(1, 4, 'Mamá', '600123123', NULL, 0, 0, '2026-02-05 00:05:47'),
(2, 4, 'Mamá', '600123123', NULL, 0, 0, '2026-02-05 00:06:56'),
(3, 4, 'Mamá', '600123123', NULL, 0, 0, '2026-02-05 00:15:24'),
(4, 5, 'Mamá', '600123123', NULL, 0, 0, '2026-02-05 00:19:38');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `llamadas_programadas`
--

CREATE TABLE `llamadas_programadas` (
  `id_llamada` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `telefono` varchar(20) NOT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `dia_semana` int(11) DEFAULT NULL COMMENT '1=Lunes, 7=Domingo, NULL=todos los días',
  `dia_mes` int(11) DEFAULT NULL COMMENT 'Día específico del mes (1-31)',
  `hora` time NOT NULL,
  `activa` tinyint(1) DEFAULT 1,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `llamadas_programadas`
--

INSERT INTO `llamadas_programadas` (`id_llamada`, `id_usuario`, `telefono`, `motivo`, `dia_semana`, `dia_mes`, `hora`, `activa`, `created_at`) VALUES
(1, 2, '717003717', 'Llamada programada desde la app', 5, NULL, '14:30:00', 1, '2026-02-04 22:14:15'),
(2, 2, '659763145', 'Llamada programada desde la app', 7, NULL, '14:30:00', 1, '2026-02-04 22:34:54'),
(3, 2, '717003717', 'Llamada programada desde la app', 3, NULL, '13:30:00', 1, '2026-02-04 23:05:15'),
(4, 4, '112', NULL, NULL, NULL, '00:08:00', 1, '2026-02-05 00:13:52'),
(5, 4, '112', NULL, NULL, NULL, '00:08:00', 1, '2026-02-05 00:17:53'),
(6, 5, '112', NULL, NULL, NULL, '00:08:00', 1, '2026-02-05 00:19:38');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `moods_catalogo`
--

CREATE TABLE `moods_catalogo` (
  `id_mood` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `valor` int(11) NOT NULL CHECK (`valor` between 1 and 5),
  `color_hex` varchar(7) DEFAULT NULL,
  `icono_res` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `moods_catalogo`
--

INSERT INTO `moods_catalogo` (`id_mood`, `nombre`, `valor`, `color_hex`, `icono_res`) VALUES
(1, 'Muy mal', 1, '#FF4D4D', 'ic_mood_1'),
(2, 'Mal', 2, '#FF944D', 'ic_mood_2'),
(3, 'Regular', 3, '#FFD24D', 'ic_mood_3'),
(4, 'Bien', 4, '#9BE15D', 'ic_mood_4'),
(5, 'Genial', 5, '#00D4A6', 'ic_mood_5');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mood_registros`
--

CREATE TABLE `mood_registros` (
  `id_registro` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `valor` int(11) NOT NULL CHECK (`valor` between 1 and 5),
  `etiqueta` varchar(50) DEFAULT NULL,
  `actividad_realizada` varchar(200) DEFAULT NULL,
  `nota` text DEFAULT NULL,
  `imagen_uri` text DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `mood_registros`
--

INSERT INTO `mood_registros` (`id_registro`, `id_usuario`, `valor`, `etiqueta`, `actividad_realizada`, `nota`, `imagen_uri`, `created_at`) VALUES
(1, 2, 2, NULL, 'android no funciona', 'quiero que funcione', 'mock_camera_uri', '2026-02-04 22:54:26'),
(2, 4, 4, NULL, NULL, 'Día productivo', NULL, '2026-02-05 00:11:27'),
(3, 4, 4, NULL, NULL, 'Día productivo', NULL, '2026-02-05 00:17:18'),
(4, 5, 4, NULL, NULL, 'Día productivo', NULL, '2026-02-05 00:19:37');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `preferencias_usuario`
--

CREATE TABLE `preferencias_usuario` (
  `id_preferencia` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `recordatorios_activos` tinyint(1) DEFAULT 1,
  `hora_recordatorio` time DEFAULT '22:00:00',
  `tema_oscuro` tinyint(1) DEFAULT 0,
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `preferencias_usuario`
--

INSERT INTO `preferencias_usuario` (`id_preferencia`, `id_usuario`, `recordatorios_activos`, `hora_recordatorio`, `tema_oscuro`, `updated_at`) VALUES
(1, 1, 1, '22:00:00', 0, '2026-02-04 17:55:28'),
(2, 2, 1, '22:00:00', 0, '2026-02-04 18:13:32'),
(3, 3, 1, '22:00:00', 0, '2026-02-04 23:35:36'),
(4, 4, 1, '22:00:00', 0, '2026-02-05 00:04:12'),
(5, 5, 1, '22:00:00', 0, '2026-02-05 00:19:37');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recomendaciones`
--

CREATE TABLE `recomendaciones` (
  `id_recomendacion` int(11) NOT NULL,
  `id_registro` int(11) NOT NULL,
  `id_actividad` int(11) NOT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `recomendaciones`
--

INSERT INTO `recomendaciones` (`id_recomendacion`, `id_registro`, `id_actividad`, `motivo`, `created_at`) VALUES
(1, 1, 1, 'Recomendada basada en tu estado de ánimo', '2026-02-04 22:54:26'),
(2, 1, 3, 'Recomendada basada en tu estado de ánimo', '2026-02-04 22:54:26'),
(3, 1, 6, 'Recomendada basada en tu estado de ánimo', '2026-02-04 22:54:26'),
(4, 2, 7, 'Recomendada basada en tu estado de ánimo', '2026-02-05 00:11:28'),
(5, 2, 2, 'Recomendada basada en tu estado de ánimo', '2026-02-05 00:11:28'),
(6, 3, 7, 'Recomendada basada en tu estado de ánimo', '2026-02-05 00:17:18'),
(7, 3, 2, 'Recomendada basada en tu estado de ánimo', '2026-02-05 00:17:18'),
(8, 4, 7, 'Recomendada basada en tu estado de ánimo', '2026-02-05 00:19:37'),
(9, 4, 2, 'Recomendada basada en tu estado de ánimo', '2026-02-05 00:19:37');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `skills`
--

CREATE TABLE `skills` (
  `id_skill` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `nivel` enum('Básico','Intermedio','Avanzado') NOT NULL DEFAULT 'Básico',
  `descripcion` text DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `skills`
--

INSERT INTO `skills` (`id_skill`, `id_usuario`, `titulo`, `nivel`, `descripcion`, `created_at`) VALUES
(1, 2, 'kotlin', 'Intermedio', 'Enseo kotlin', '2026-02-04 22:59:38'),
(2, 4, 'Respiración', 'Básico', NULL, '2026-02-05 00:13:37'),
(3, 4, 'Respiración', 'Básico', NULL, '2026-02-05 00:18:45'),
(4, 5, 'Respiración', 'Básico', NULL, '2026-02-05 00:19:38');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `nombre`, `email`, `password`, `created_at`, `updated_at`) VALUES
(1, 'p', 'p@prueba.com', '$2y$10$cm1.uni5KRzTxTZaUMWvP.P8FK8qu8su4O3WGrDxbJFRkN6KWrxS6', '2026-02-04 17:55:28', '2026-02-04 17:55:28'),
(2, 'isaBEL', 'isa@isa.com', '$2y$10$g3FtcQyC8jeBvQphTraFkOcDpODjYr2WKrolxk.7yg4z/gBl4WGPG', '2026-02-04 18:13:32', '2026-02-04 22:52:16'),
(3, 'Test User', 'testuser@example.com', '$2y$10$YUEwQ4m7QwAVCNVop8DZN.USO9Fhy9NUgGSZtOfRJmxgU7GjTnh8i', '2026-02-04 23:35:36', '2026-02-04 23:35:36'),
(4, 'Raul Herrero', 'raul@test.com', '$2y$10$WXgeT/cfB5l/xvH.MgpUMOuQ4a.Lja0O9siZ0HH7.YBgUTz.Z3bVK', '2026-02-05 00:04:12', '2026-02-05 00:16:45'),
(5, 'Raul Herrero', 'raul2@test.com', '$2y$10$8BG4qrS0MGgcfTy/iClsu.zgQs0QaVU80kvJfHfRt0lgk2CHZA/ya', '2026-02-05 00:19:37', '2026-02-05 00:19:37');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `actividades`
--
ALTER TABLE `actividades`
  ADD PRIMARY KEY (`id_actividad`);

--
-- Indices de la tabla `contactos_ayuda`
--
ALTER TABLE `contactos_ayuda`
  ADD PRIMARY KEY (`id_contacto`),
  ADD KEY `idx_contacto_usuario` (`id_usuario`);

--
-- Indices de la tabla `llamadas_programadas`
--
ALTER TABLE `llamadas_programadas`
  ADD PRIMARY KEY (`id_llamada`),
  ADD KEY `idx_llamada_usuario` (`id_usuario`);

--
-- Indices de la tabla `moods_catalogo`
--
ALTER TABLE `moods_catalogo`
  ADD PRIMARY KEY (`id_mood`);

--
-- Indices de la tabla `mood_registros`
--
ALTER TABLE `mood_registros`
  ADD PRIMARY KEY (`id_registro`),
  ADD KEY `idx_mood_usuario_fecha` (`id_usuario`,`created_at`),
  ADD KEY `idx_mood_fecha` (`created_at`);

--
-- Indices de la tabla `preferencias_usuario`
--
ALTER TABLE `preferencias_usuario`
  ADD PRIMARY KEY (`id_preferencia`),
  ADD UNIQUE KEY `unique_usuario_pref` (`id_usuario`);

--
-- Indices de la tabla `recomendaciones`
--
ALTER TABLE `recomendaciones`
  ADD PRIMARY KEY (`id_recomendacion`),
  ADD KEY `fk_rec_registro` (`id_registro`),
  ADD KEY `fk_rec_actividad` (`id_actividad`);

--
-- Indices de la tabla `skills`
--
ALTER TABLE `skills`
  ADD PRIMARY KEY (`id_skill`),
  ADD KEY `idx_skill_usuario` (`id_usuario`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `actividades`
--
ALTER TABLE `actividades`
  MODIFY `id_actividad` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `contactos_ayuda`
--
ALTER TABLE `contactos_ayuda`
  MODIFY `id_contacto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `llamadas_programadas`
--
ALTER TABLE `llamadas_programadas`
  MODIFY `id_llamada` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `moods_catalogo`
--
ALTER TABLE `moods_catalogo`
  MODIFY `id_mood` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `mood_registros`
--
ALTER TABLE `mood_registros`
  MODIFY `id_registro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `preferencias_usuario`
--
ALTER TABLE `preferencias_usuario`
  MODIFY `id_preferencia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `recomendaciones`
--
ALTER TABLE `recomendaciones`
  MODIFY `id_recomendacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `skills`
--
ALTER TABLE `skills`
  MODIFY `id_skill` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `contactos_ayuda`
--
ALTER TABLE `contactos_ayuda`
  ADD CONSTRAINT `contactos_ayuda_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE;

--
-- Filtros para la tabla `llamadas_programadas`
--
ALTER TABLE `llamadas_programadas`
  ADD CONSTRAINT `llamadas_programadas_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE;

--
-- Filtros para la tabla `mood_registros`
--
ALTER TABLE `mood_registros`
  ADD CONSTRAINT `mood_registros_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE;

--
-- Filtros para la tabla `preferencias_usuario`
--
ALTER TABLE `preferencias_usuario`
  ADD CONSTRAINT `preferencias_usuario_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE;

--
-- Filtros para la tabla `recomendaciones`
--
ALTER TABLE `recomendaciones`
  ADD CONSTRAINT `recomendaciones_ibfk_1` FOREIGN KEY (`id_registro`) REFERENCES `mood_registros` (`id_registro`) ON DELETE CASCADE,
  ADD CONSTRAINT `recomendaciones_ibfk_2` FOREIGN KEY (`id_actividad`) REFERENCES `actividades` (`id_actividad`) ON DELETE CASCADE;

--
-- Filtros para la tabla `skills`
--
ALTER TABLE `skills`
  ADD CONSTRAINT `skills_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
