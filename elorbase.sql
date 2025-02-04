-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 04-02-2025 a las 12:37:55
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
-- Base de datos: `elorbase`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `apellidos` varchar(50) DEFAULT NULL,
  `dni` varchar(9) DEFAULT NULL,
  `direccion` varchar(100) DEFAULT NULL,
  `telefono1` int(11) DEFAULT NULL,
  `telefono2` int(11) DEFAULT NULL,
  `tipo_id` int(11) NOT NULL,
  `argazkia` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `email`, `username`, `password`, `nombre`, `apellidos`, `dni`, `direccion`, `telefono1`, `telefono2`, `tipo_id`, `argazkia`) VALUES
(1, 'jorge@elorrieta-errekamari.com', 'jorge', '123456', 'jorge', 'gonzalez', '123456789', 'example 10', 666, NULL, 1, NULL),
(2, 'itziar@elorrieta-errekamari.com', 'itziar', '123456', 'itziar', 'regidor', '123456', 'example 10', 666, NULL, 2, NULL),
(3, 'alumno@elorrieta-errekamari.com', 'alumno1', '123', 'alumno', 'ejemplo', 'as', 'as', 666, NULL, 4, NULL),
(4, 'maitane@elorrieta-errekamari.com', 'maitane', '1234', 'maitane', 'ejemplo', '1234', 'as', NULL, NULL, 3, NULL),
(5, 'iker@elorrieta-errekamari.com', 'iker', '1234', 'iker', 'ejemplo', '1234', 'as', NULL, NULL, 3, NULL),
(6, 'asier@elorrieta-errekamari.com', 'asier', '1234', 'asier', 'ejemplo', '1234', 'as', NULL, NULL, 3, NULL),
(7, 'unai@elorrieta-errekamari.com', 'unai', '1234', 'unai', 'ejemplo', '1234', 'as', NULL, NULL, 3, NULL),
(8, 'roman@elorrieta-errekamari.com', 'roman', '1234', 'roman', 'ejemplo', '1234', 'as', NULL, NULL, 3, NULL),
(9, 'oscar@elorrieta-errekamari.com', 'oscar', '1234', 'oscar', 'ejemplo', '1234', 'as', NULL, NULL, 3, NULL),
(20, NULL, 'paco', 'pass', 'paco', 'apellido', 'dni', 'direccion', NULL, NULL, 4, NULL),
(21, NULL, 'paco', 'pass', 'paco', 'apellido', 'dni', 'direccion', NULL, NULL, 4, NULL),
(23, 'pepe@mail.com', 'eder', 'eder', 'eder', 'eder', '12345433t', 'eder', 121334565, NULL, 1, NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tipo_id` (`tipo_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`tipo_id`) REFERENCES `tipos` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
