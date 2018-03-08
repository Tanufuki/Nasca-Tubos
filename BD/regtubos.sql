-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 23-09-2015 a las 06:58:58
-- Versión del servidor: 5.6.17
-- Versión de PHP: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `regtubos`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE IF NOT EXISTS `cliente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rut` int(11) NOT NULL,
  `nombre` char(240) NOT NULL,
  `direccion` char(240) NOT NULL,
  `email` char(80) DEFAULT NULL,
  `visible` int(11) NOT NULL DEFAULT '1',
  `cuando` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_clientenombre` (`nombre`),
  UNIQUE KEY `idx_clienterut` (`rut`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=21 ;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`id`, `rut`, `nombre`, `direccion`, `email`, `visible`, `cuando`) VALUES
(3, 21480855, 'Mario Bross', 'Chock', 'tanufuki@gmail.cl', 0, '2015-06-26 19:21:25'),
(4, 10924592, 'Maria jimena', 'LLICO115', '', 1, '2015-06-26 19:22:44'),
(5, 3, 'Pablos', 'ple', '', 0, '2015-06-26 20:11:51'),
(6, 1, 'd2', 'd', '', 0, '2015-06-28 02:24:22'),
(7, 2, 'Anna', 'Macul', '', 0, '2015-06-28 03:07:32'),
(8, 9863795, 'La Prueba de fuego XD', 'Partido 6 -1', '', 1, '2015-06-30 02:12:08'),
(9, 1101961, 'Chock', 'Vespucio sur', 'ChockDoge@gmail.com', 1, '2015-06-30 02:18:06'),
(10, 1704837, 'jason', 'j', '', 0, '2015-06-30 02:28:24'),
(11, 12433720, 'Blue', 'b', '', 0, '2015-06-30 02:44:08'),
(12, 20250539, 'Nasca view', 'Nasca15', 'Nasca@gmai.com', 1, '2015-06-30 18:52:05'),
(13, 20747529, 'ChockF', 'Vespucio sur', 'ChockDoge@gmail.com', 1, '2015-06-30 19:11:14'),
(14, 13545570, 'Antonio Letho', 'dominical 10', '', 1, '2015-06-30 19:48:42'),
(15, 13179542, 'Mario bros', 'mario verde', '', 0, '2015-06-30 19:49:46'),
(17, 8, 'hey', 'h', '', 1, '2015-07-01 20:48:30'),
(18, 9, 'Pedro Letho', 'p', '', 1, '2015-07-01 21:34:55'),
(19, 18622056, 'Angelo Alexander Matias Candia Alessandrini', 'sdf', '', 1, '2015-07-01 21:41:31'),
(20, 0, '', '', '', 0, '2015-07-06 17:26:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_revision`
--

CREATE TABLE IF NOT EXISTS `detalle_revision` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_revision` int(11) NOT NULL,
  `numero` int(11) NOT NULL,
  `id_tipo_cilindro` int(11) NOT NULL,
  `fabricacion` date NOT NULL,
  `id_fabricante` int(11) NOT NULL,
  `IVhiloCuello` int(11) DEFAULT NULL,
  `IVExterior` int(11) DEFAULT NULL,
  `IVInterior` int(11) DEFAULT NULL,
  `id_norma` int(11) NOT NULL,
  `ultimaprueba` date NOT NULL,
  `PresionDeServicio` int(11) DEFAULT NULL,
  `PresionDePruebaEstampado` int(11) DEFAULT NULL,
  `VolCargaIndicada` double(8,2) DEFAULT NULL,
  `PresionPrueba` int(11) DEFAULT NULL,
  `DeformTotal` int(11) DEFAULT NULL,
  `DeformPermanente` int(11) DEFAULT NULL,
  `Elasticidad` int(11) DEFAULT NULL,
  `DeformPermPorcentaje` int(11) DEFAULT NULL,
  `pintura` int(11) DEFAULT NULL,
  `id_foto` int(11) NOT NULL,
  `cambio_valvula` int(11) DEFAULT NULL,
  `valvula` int(11) DEFAULT NULL,
  `manilla` int(11) DEFAULT NULL,
  `volante` int(11) DEFAULT NULL,
  `visible` int(11) NOT NULL DEFAULT '1',
  `cuando` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Aprobado` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `detalle_revision`
--

INSERT INTO `detalle_revision` (`id`, `id_revision`, `numero`, `id_tipo_cilindro`, `fabricacion`, `id_fabricante`, `IVhiloCuello`, `IVExterior`, `IVInterior`, `id_norma`, `ultimaprueba`, `PresionDeServicio`, `PresionDePruebaEstampado`, `VolCargaIndicada`, `PresionPrueba`, `DeformTotal`, `DeformPermanente`, `Elasticidad`, `DeformPermPorcentaje`, `pintura`, `id_foto`, `cambio_valvula`, `valvula`, `manilla`, `volante`, `visible`, `cuando`, `Aprobado`) VALUES
(1, 1, 123, 1, '2015-07-12', 11, 0, 0, 0, 2, '2015-07-10', 23, 23, 23.00, 23, 23, 0, 0, 0, 1, 9, 1, 1, 1, 0, 1, '2015-07-21 16:05:15', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `fabricante`
--

CREATE TABLE IF NOT EXISTS `fabricante` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codigo` char(10) NOT NULL,
  `nombre` char(40) NOT NULL,
  `visible` int(11) NOT NULL DEFAULT '1',
  `cuando` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_fabricante_nombre` (`nombre`),
  UNIQUE KEY `idx_fabricante_codigo` (`codigo`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Volcado de datos para la tabla `fabricante`
--

INSERT INTO `fabricante` (`id`, `codigo`, `nombre`, `visible`, `cuando`) VALUES
(1, 'phi', 'philip', 1, '2015-06-10 14:54:25'),
(2, 'Usuario', 'Usu', 1, '2015-07-05 18:51:37'),
(3, 'Codi', 'Codigo', 1, '2015-07-05 20:20:51'),
(4, '123', 'Co', 0, '2015-07-05 20:21:05'),
(5, 'Mch', 'Michael', 1, '2015-07-05 21:02:34'),
(6, 'nuevo', 'nuevo', 1, '2015-07-05 21:03:21'),
(7, 'b', 'B', 0, '2015-07-05 22:28:46'),
(8, 'iuhdsaihu', 'kds', 1, '2015-07-05 22:33:11'),
(9, 'kk', 'k', 1, '2015-07-05 22:37:12'),
(10, 'kkk', 'kkk', 1, '2015-07-05 22:37:32'),
(11, 'EXT', 'extintores', 1, '2015-07-14 19:02:45'),
(12, 'Tre', 'trevol', 1, '2015-07-20 14:58:39'),
(13, 'HM', 'Homero', 1, '2015-07-20 15:19:54');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `foto`
--

CREATE TABLE IF NOT EXISTS `foto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dir1` int(11) NOT NULL,
  `dir2` int(11) NOT NULL,
  `visible` int(11) NOT NULL DEFAULT '1',
  `cuando` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=27 ;

--
-- Volcado de datos para la tabla `foto`
--

INSERT INTO `foto` (`id`, `dir1`, `dir2`, `visible`, `cuando`) VALUES
(1, 111, 111, 1, '2015-06-10 14:55:03'),
(2, 123, 123, 0, '2015-06-10 15:05:16'),
(3, 230, 350, 1, '2015-07-01 16:39:10'),
(4, 234, 345, 1, '2015-07-01 16:41:58'),
(5, 999, 999, 1, '2015-07-01 16:43:47'),
(6, 234, 345, 1, '2015-07-01 16:44:27'),
(7, 23, 345, 0, '2015-07-01 16:44:46'),
(8, 300, 345, 1, '2015-07-01 16:45:11'),
(9, 103, 100, 1, '2015-07-01 16:50:36'),
(10, 223, 346, 1, '2015-07-01 16:52:46'),
(11, 900, 100, 1, '2015-07-01 16:53:54'),
(12, 1, 1, 0, '2015-07-01 16:54:45'),
(13, 1, 1, 0, '2015-07-01 16:55:30'),
(14, 500, 500, 1, '2015-07-01 21:05:20'),
(15, 1, 345, 1, '2015-07-01 21:46:59'),
(16, 23, 23, 0, '2015-07-01 21:47:09'),
(17, 45, 45, 1, '2015-07-01 21:47:17'),
(18, 234, 234, 1, '2015-07-06 00:41:12'),
(19, 234, 234, 1, '2015-07-06 00:41:20'),
(20, 550, 550, 1, '2015-07-06 14:00:00'),
(21, 550, 550, 1, '2015-07-06 14:01:12'),
(22, 23456, 2345, 1, '2015-07-09 15:50:51'),
(23, 234, 23, 1, '2015-07-09 16:57:32'),
(24, 666, 666, 1, '2015-07-17 19:17:07'),
(25, 999, 999, 1, '2015-07-17 19:17:12'),
(26, 999, 998, 1, '2015-07-17 19:17:36');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `norma`
--

CREATE TABLE IF NOT EXISTS `norma` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `norma` char(40) NOT NULL,
  `visible` int(11) NOT NULL DEFAULT '1',
  `cuando` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `norma` (`norma`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=86 ;

--
-- Volcado de datos para la tabla `norma`
--

INSERT INTO `norma` (`id`, `norma`, `visible`, `cuando`) VALUES
(1, 'Ext', 1, '2015-06-10 14:53:04'),
(2, 'H', 1, '2015-06-10 14:53:12'),
(3, 'Norma 10+', 1, '2015-06-28 17:10:39'),
(7, '9000', 1, '2015-06-28 17:15:40'),
(8, '9001', 0, '2015-06-28 17:21:26'),
(9, '9002', 1, '2015-06-28 17:22:02'),
(11, '9003', 1, '2015-06-28 17:23:01'),
(13, '1000', 0, '2015-06-28 17:31:40'),
(14, '1010', 0, '2015-06-28 17:31:45'),
(15, 'CGA -C1;', 1, '2015-06-28 17:32:29'),
(16, '6', 0, '2015-06-28 17:32:40'),
(21, '2', 0, '2015-06-28 17:39:36'),
(22, 'NoRMA123', 1, '2015-06-28 17:40:32'),
(23, 'Norma44', 1, '2015-06-28 18:00:26'),
(27, 'NCH 2244', 1, '2015-06-28 18:02:31'),
(28, '777', 1, '2015-06-28 18:02:40'),
(30, '8', 0, '2015-06-28 18:03:36'),
(34, '100', 0, '2015-06-28 18:06:08'),
(36, '10001+Â·$%&', 0, '2015-06-28 18:07:02'),
(41, '10013', 0, '2015-06-28 18:07:52'),
(42, 'Norma 2S', 1, '2015-06-28 18:32:44'),
(43, 'yf', 1, '2015-06-28 18:38:23'),
(44, 'jocob', 1, '2015-06-28 18:38:56'),
(45, 'josen', 0, '2015-06-28 18:45:37'),
(47, 'Norma 10000', 1, '2015-06-28 21:19:32'),
(48, 'Norma 4000', 1, '2015-06-28 21:30:42'),
(49, 'Norma 1', 1, '2015-06-28 21:38:14'),
(50, 'Norma DetalleR', 1, '2015-06-29 16:25:32'),
(51, '', 0, '2015-07-01 20:40:31'),
(52, 'sedfg', 1, '2015-07-01 21:42:09'),
(53, ' 100', 0, '2015-07-01 22:23:52'),
(54, 'Norma 8001', 1, '2015-07-05 12:30:31'),
(55, 'NormaP`rue', 1, '2015-07-17 19:15:57'),
(56, 'rtgjkg', 1, '2015-07-17 19:16:08'),
(57, 'NcH 2056', 1, '2015-07-22 21:40:42'),
(58, 'e', 1, '2015-07-26 23:09:13'),
(59, 'k', 1, '2015-07-26 23:09:18'),
(60, '344', 1, '2015-07-26 23:09:25'),
(61, 'Norma pdf', 1, '2015-07-26 23:09:31'),
(62, 'saifdhui', 1, '2015-07-26 23:11:15'),
(63, 'feojiws', 1, '2015-07-26 23:11:22'),
(64, 'frs', 1, '2015-07-26 23:11:27'),
(65, 'ksjfdnksjn', 1, '2015-07-26 23:11:33'),
(66, 'kasjdhnk', 1, '2015-07-26 23:11:49'),
(67, 'ednk', 1, '2015-07-26 23:11:54'),
(68, '4r5', 1, '2015-07-26 23:12:01'),
(69, 'efks', 1, '2015-07-26 23:12:06'),
(70, 'eknfdskjnhdihn', 1, '2015-07-26 23:12:12'),
(71, 'kakdn', 1, '2015-07-26 23:28:51'),
(72, 'khewhiwh', 1, '2015-07-26 23:28:59'),
(73, 'zdlnvlskn', 1, '2015-07-26 23:29:05'),
(74, 'skjdn', 1, '2015-07-26 23:29:12'),
(75, 'fknl', 1, '2015-07-26 23:29:24'),
(76, 'dsfjs', 1, '2015-07-26 23:29:41'),
(77, 'uygyuguy', 1, '2015-07-26 23:29:50'),
(78, 'nknkn', 1, '2015-07-26 23:29:57'),
(79, 'jnknj', 1, '2015-07-26 23:30:03'),
(80, '67890', 1, '2015-07-26 23:30:09'),
(81, 'gfhuk', 1, '2015-07-26 23:30:16'),
(82, 'cvfghjk', 1, '2015-07-26 23:30:22'),
(83, 'vbghjk', 1, '2015-07-26 23:30:28'),
(84, 'vghbjkli', 1, '2015-07-26 23:30:34'),
(85, 'hgjbkijl', 1, '2015-07-26 23:30:47');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `revision`
--

CREATE TABLE IF NOT EXISTS `revision` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` int(11) NOT NULL,
  `inicio` datetime NOT NULL,
  `termino` timestamp NULL DEFAULT NULL,
  `numero` int(11) DEFAULT NULL,
  `visible` int(11) NOT NULL DEFAULT '1',
  `cuando` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;

--
-- Volcado de datos para la tabla `revision`
--

INSERT INTO `revision` (`id`, `id_cliente`, `inicio`, `termino`, `numero`, `visible`, `cuando`) VALUES
(1, 19, '2015-07-09 02:32:02', '2015-07-13 22:00:00', 30010, 1, '2015-07-09 00:32:02'),
(2, 9, '2015-07-13 22:26:46', '2015-09-17 22:00:00', 2300, 1, '2015-07-13 20:26:46'),
(3, 13, '2015-07-13 23:16:18', '2015-07-16 22:00:00', 400, 0, '2015-07-13 21:16:18'),
(4, 14, '2015-07-20 21:39:34', NULL, 3, 0, '2015-07-20 19:39:34'),
(5, 14, '2015-07-20 21:40:00', NULL, 3, 0, '2015-07-20 19:40:00'),
(6, 8, '2015-07-20 21:40:13', '2015-07-27 22:00:00', 3, 1, '2015-07-20 19:40:13'),
(7, 4, '2015-07-20 22:05:57', '0000-00-00 00:00:00', 23456, 0, '2015-07-20 20:05:57'),
(8, 19, '2015-07-20 22:06:25', '2015-07-28 22:00:00', 3355, 1, '2015-07-20 20:06:25'),
(9, 14, '2015-07-21 00:44:43', NULL, 30, 1, '2015-07-20 22:44:43'),
(10, 13, '2015-07-22 21:13:25', '2015-06-30 22:00:00', 9999, 1, '2015-07-22 19:13:25'),
(11, 13, '2015-07-22 21:15:17', '2015-07-30 22:00:00', 99990, 1, '2015-07-22 19:15:17'),
(12, 19, '2015-07-22 22:30:57', '2015-07-07 22:00:00', 1000, 1, '2015-07-22 20:30:57'),
(13, 19, '2015-07-22 22:32:18', '2015-07-22 22:00:00', 1000, 1, '2015-07-22 20:32:18'),
(14, 19, '2015-07-22 22:32:40', '2015-07-30 22:00:00', 2000, 1, '2015-07-22 20:32:40'),
(15, 8, '2015-07-22 22:44:50', NULL, 12, 1, '2015-07-22 20:44:50'),
(16, 8, '2015-07-22 22:45:16', NULL, 12, 1, '2015-07-22 20:45:16'),
(17, 9, '2015-07-22 22:45:37', '2015-07-30 22:00:00', 55, 1, '2015-07-22 20:45:37');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_cilindro`
--

CREATE TABLE IF NOT EXISTS `tipo_cilindro` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codigo` char(10) NOT NULL,
  `nombre` char(40) NOT NULL,
  `visible` int(11) NOT NULL DEFAULT '1',
  `cuando` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_tipo_cilindro_nombre` (`nombre`),
  UNIQUE KEY `idx_tipo_cilindro_codigo` (`codigo`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

--
-- Volcado de datos para la tabla `tipo_cilindro`
--

INSERT INTO `tipo_cilindro` (`id`, `codigo`, `nombre`, `visible`, `cuando`) VALUES
(1, 'ext', 'extintor', 1, '2015-05-26 22:46:57'),
(2, 'ND', 'detalle revision', 0, '2015-06-29 16:25:45'),
(3, 'PHI', 'philip', 0, '2015-06-29 20:18:30'),
(4, 'ang+', 'angelo', 0, '2015-06-29 20:19:11'),
(5, 'H', 'Hidrogeno', 0, '2015-06-29 20:21:22'),
(6, '', '', 0, '2015-07-01 20:52:41'),
(7, '34', 'r', 0, '2015-07-01 20:53:04'),
(8, '345t6yu', '2345t6y', 0, '2015-07-06 18:42:25'),
(9, 'qwertghgre', 'Amoniaco', 1, '2015-07-19 15:52:48'),
(10, '2222222225', '23yrh8ecgbweugfbbegcwa', 1, '2015-07-19 16:06:27'),
(11, 'edsfd', 'ertghe', 1, '2015-07-19 19:12:48'),
(12, '2222222223', 'swjkn', 1, '2015-07-20 01:19:54');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codigo` char(10) NOT NULL,
  `nombre` char(40) NOT NULL,
  `clave` char(20) NOT NULL,
  `acceso` int(11) NOT NULL,
  `visible` int(11) NOT NULL DEFAULT '1',
  `cuando` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `codigo`, `nombre`, `clave`, `acceso`, `visible`, `cuando`) VALUES
(1, 'An.candia', 'Angelo', 'angelo100', 2, 1, '2015-05-26 20:48:11'),
(2, 'be.ja', 'benjamin', '12345', 0, 1, '2015-06-08 19:14:00'),
(3, 'P1.prueba', 'Prueba1', 'acceso', 1, 1, '2015-06-16 16:32:36');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE IF NOT EXISTS `usuarios` (
  `codigo` int(5) NOT NULL AUTO_INCREMENT,
  `nombres` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `telefono` varchar(10) NOT NULL,
  `ciudad` varchar(100) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=36 ;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`codigo`, `nombres`, `apellidos`, `telefono`, `ciudad`) VALUES
(34, 'pedro', 'rodriguez', '4003790', 'medellin'),
(35, 'andres', 'perez', '1234567', 'popayan');

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vw_cliente`
--
CREATE TABLE IF NOT EXISTS `vw_cliente` (
`id` int(11)
,`rut` int(11)
,`nombre` char(240)
,`direccion` char(240)
,`email` char(80)
,`cuando` timestamp
);
-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vw_detalle_revision`
--
CREATE TABLE IF NOT EXISTS `vw_detalle_revision` (
`id` int(11)
,`id_revision` int(11)
,`numero` int(11)
,`id_tipo_cilindro` int(11)
,`fabricacion` date
,`id_fabricante` int(11)
,`fabricante_nombre` char(40)
,`id_norma` int(11)
,`norma` char(40)
,`ultimaprueba` date
,`PresionDeServicio` int(11)
,`PresionDePruebaEstampado` int(11)
,`VolCargaIndicada` double(8,2)
,`PresionPrueba` int(11)
,`DeformTotal` int(11)
,`DeformPermanente` int(11)
,`Elasticidad` int(11)
,`DeformPermPorcentaje` int(11)
,`pintura` int(11)
,`id_foto` int(11)
,`ruta_foto` varchar(24)
,`cambio_valvula` int(11)
,`valvula` int(11)
,`manilla` int(11)
,`volante` int(11)
,`cuando` timestamp
,`IVhiloCuello` int(11)
,`IVExterior` int(11)
,`IVInterior` int(11)
,`Aprobado` int(11)
);
-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vw_fabricante`
--
CREATE TABLE IF NOT EXISTS `vw_fabricante` (
`id` int(11)
,`codigo` char(10)
,`nombre` char(40)
,`cuando` timestamp
);
-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vw_foto`
--
CREATE TABLE IF NOT EXISTS `vw_foto` (
`id` int(11)
,`dir1` int(11)
,`dir2` int(11)
,`ruta` varchar(24)
);
-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vw_norma`
--
CREATE TABLE IF NOT EXISTS `vw_norma` (
`id` int(11)
,`norma` char(40)
,`cuando` timestamp
);
-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vw_revision`
--
CREATE TABLE IF NOT EXISTS `vw_revision` (
`id` int(11)
,`id_cliente` int(11)
,`cliente_nombre` char(240)
,`inicio` datetime
,`termino` timestamp
,`numero` int(11)
);
-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vw_tipo_cilindro`
--
CREATE TABLE IF NOT EXISTS `vw_tipo_cilindro` (
`id` int(11)
,`codigo` char(10)
,`nombre` char(40)
,`cuando` timestamp
);
-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vw_usuario`
--
CREATE TABLE IF NOT EXISTS `vw_usuario` (
`id` int(11)
,`codigo` char(10)
,`nombre` char(40)
,`clave` char(20)
,`acceso` int(11)
,`cuando` timestamp
);
-- --------------------------------------------------------

--
-- Estructura para la vista `vw_cliente`
--
DROP TABLE IF EXISTS `vw_cliente`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vw_cliente` AS select `cliente`.`id` AS `id`,`cliente`.`rut` AS `rut`,`cliente`.`nombre` AS `nombre`,`cliente`.`direccion` AS `direccion`,`cliente`.`email` AS `email`,`cliente`.`cuando` AS `cuando` from `cliente` where (`cliente`.`visible` = 1);

-- --------------------------------------------------------

--
-- Estructura para la vista `vw_detalle_revision`
--
DROP TABLE IF EXISTS `vw_detalle_revision`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vw_detalle_revision` AS select `detalle_revision`.`id` AS `id`,`detalle_revision`.`id_revision` AS `id_revision`,`detalle_revision`.`numero` AS `numero`,`detalle_revision`.`id_tipo_cilindro` AS `id_tipo_cilindro`,`detalle_revision`.`fabricacion` AS `fabricacion`,`detalle_revision`.`id_fabricante` AS `id_fabricante`,`fabricante`.`nombre` AS `fabricante_nombre`,`detalle_revision`.`id_norma` AS `id_norma`,`norma`.`norma` AS `norma`,`detalle_revision`.`ultimaprueba` AS `ultimaprueba`,`detalle_revision`.`PresionDeServicio` AS `PresionDeServicio`,`detalle_revision`.`PresionDePruebaEstampado` AS `PresionDePruebaEstampado`,`detalle_revision`.`VolCargaIndicada` AS `VolCargaIndicada`,`detalle_revision`.`PresionPrueba` AS `PresionPrueba`,`detalle_revision`.`DeformTotal` AS `DeformTotal`,`detalle_revision`.`DeformPermanente` AS `DeformPermanente`,`detalle_revision`.`Elasticidad` AS `Elasticidad`,`detalle_revision`.`DeformPermPorcentaje` AS `DeformPermPorcentaje`,`detalle_revision`.`pintura` AS `pintura`,`detalle_revision`.`id_foto` AS `id_foto`,`vw_foto`.`ruta` AS `ruta_foto`,`detalle_revision`.`cambio_valvula` AS `cambio_valvula`,`detalle_revision`.`valvula` AS `valvula`,`detalle_revision`.`manilla` AS `manilla`,`detalle_revision`.`volante` AS `volante`,`detalle_revision`.`cuando` AS `cuando`,`detalle_revision`.`IVhiloCuello` AS `IVhiloCuello`,`detalle_revision`.`IVExterior` AS `IVExterior`,`detalle_revision`.`IVInterior` AS `IVInterior`,`detalle_revision`.`Aprobado` AS `Aprobado` from (((`detalle_revision` join `norma` on((`detalle_revision`.`id_norma` = `norma`.`id`))) join `fabricante` on((`detalle_revision`.`id_fabricante` = `fabricante`.`id`))) left join `vw_foto` on((`vw_foto`.`id` = `detalle_revision`.`id_foto`))) where (`detalle_revision`.`visible` = 1);

-- --------------------------------------------------------

--
-- Estructura para la vista `vw_fabricante`
--
DROP TABLE IF EXISTS `vw_fabricante`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vw_fabricante` AS select `fabricante`.`id` AS `id`,`fabricante`.`codigo` AS `codigo`,`fabricante`.`nombre` AS `nombre`,`fabricante`.`cuando` AS `cuando` from `fabricante` where (`fabricante`.`visible` = 1);

-- --------------------------------------------------------

--
-- Estructura para la vista `vw_foto`
--
DROP TABLE IF EXISTS `vw_foto`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vw_foto` AS select `foto`.`id` AS `id`,`foto`.`dir1` AS `dir1`,`foto`.`dir2` AS `dir2`,concat('/usrimg/',lpad(`foto`.`dir1`,3,'0'),'/',lpad(`foto`.`dir2`,3,'0'),'/',lpad(`foto`.`id`,8,'0')) AS `ruta` from `foto` where (`foto`.`visible` = 1);

-- --------------------------------------------------------

--
-- Estructura para la vista `vw_norma`
--
DROP TABLE IF EXISTS `vw_norma`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vw_norma` AS select `norma`.`id` AS `id`,`norma`.`norma` AS `norma`,`norma`.`cuando` AS `cuando` from `norma` where (`norma`.`visible` = 1);

-- --------------------------------------------------------

--
-- Estructura para la vista `vw_revision`
--
DROP TABLE IF EXISTS `vw_revision`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vw_revision` AS select `revision`.`id` AS `id`,`revision`.`id_cliente` AS `id_cliente`,`cliente`.`nombre` AS `cliente_nombre`,`revision`.`inicio` AS `inicio`,`revision`.`termino` AS `termino`,`revision`.`numero` AS `numero` from (`revision` join `cliente` on((`revision`.`id_cliente` = `cliente`.`id`))) where (`revision`.`visible` = 1);

-- --------------------------------------------------------

--
-- Estructura para la vista `vw_tipo_cilindro`
--
DROP TABLE IF EXISTS `vw_tipo_cilindro`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vw_tipo_cilindro` AS select `tipo_cilindro`.`id` AS `id`,`tipo_cilindro`.`codigo` AS `codigo`,`tipo_cilindro`.`nombre` AS `nombre`,`tipo_cilindro`.`cuando` AS `cuando` from `tipo_cilindro` where (`tipo_cilindro`.`visible` = 1);

-- --------------------------------------------------------

--
-- Estructura para la vista `vw_usuario`
--
DROP TABLE IF EXISTS `vw_usuario`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vw_usuario` AS select `usuario`.`id` AS `id`,`usuario`.`codigo` AS `codigo`,`usuario`.`nombre` AS `nombre`,`usuario`.`clave` AS `clave`,`usuario`.`acceso` AS `acceso`,`usuario`.`cuando` AS `cuando` from `usuario` where (`usuario`.`visible` = 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
