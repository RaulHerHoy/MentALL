-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 28-01-2026 a las 19:13:25
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

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
(1, 'Respiración 4-7-8', 'Inhala 4s, mantén 7s, exhala 8s. Repite 4 veces.', 'respiración', 3, NULL),
(2, 'Paseo corto', 'Camina 10 minutos sin móvil si es posible.', 'hábitos', 10, NULL),
(3, 'Escritura consciente', 'Escribe 3 preocupaciones y una acción pequeña.', 'mindfulness', 5, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contactos_ayuda`
--

CREATE TABLE `contactos_ayuda` (
  `id_contacto` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `es_emergencia` tinyint(1) DEFAULT 0,
  `orden` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `contactos_ayuda`
--

INSERT INTO `contactos_ayuda` (`id_contacto`, `nombre`, `telefono`, `descripcion`, `es_emergencia`, `orden`) VALUES
(1, 'Emergencias', '112', 'Número europeo de emergencias', 1, 0);

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
  `id_usuario` int(11) DEFAULT NULL,
  `valor` int(11) NOT NULL CHECK (`valor` between 1 and 5),
  `etiqueta` varchar(50) DEFAULT NULL,
  `nota` text DEFAULT NULL,
  `imagen_uri` text DEFAULT NULL,
  `created_at` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recomendaciones`
--

CREATE TABLE `recomendaciones` (
  `id_recomendacion` int(11) NOT NULL,
  `id_registro` int(11) NOT NULL,
  `id_actividad` int(11) NOT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `created_at` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `created_at` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  ADD PRIMARY KEY (`id_contacto`);

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
  ADD KEY `idx_mood_fecha` (`created_at`),
  ADD KEY `idx_mood_usuario_fecha` (`id_usuario`,`created_at`);

--
-- Indices de la tabla `recomendaciones`
--
ALTER TABLE `recomendaciones`
  ADD PRIMARY KEY (`id_recomendacion`),
  ADD KEY `fk_rec_registro` (`id_registro`),
  ADD KEY `fk_rec_actividad` (`id_actividad`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `actividades`
--
ALTER TABLE `actividades`
  MODIFY `id_actividad` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `contactos_ayuda`
--
ALTER TABLE `contactos_ayuda`
  MODIFY `id_contacto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `mood_registros`
--
ALTER TABLE `mood_registros`
  MODIFY `id_registro` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `recomendaciones`
--
ALTER TABLE `recomendaciones`
  MODIFY `id_recomendacion` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `mood_registros`
--
ALTER TABLE `mood_registros`
  ADD CONSTRAINT `fk_mood_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL;

--
-- Filtros para la tabla `recomendaciones`
--
ALTER TABLE `recomendaciones`
  ADD CONSTRAINT `fk_rec_actividad` FOREIGN KEY (`id_actividad`) REFERENCES `actividades` (`id_actividad`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_rec_registro` FOREIGN KEY (`id_registro`) REFERENCES `mood_registros` (`id_registro`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
