-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 06. Nov 2025 um 17:28
-- Server-Version: 10.4.32-MariaDB
-- PHP-Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `ghostnetfishing`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ghost_net`
--

CREATE TABLE `ghost_net` (
  `id` bigint(20) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `size` float NOT NULL,
  `status` enum('BERGUNG_BEVORSTEHEND','GEBORGEN','GEMELDET','VERSCHOLLEN') DEFAULT NULL,
  `last_updated` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Daten für Tabelle `ghost_net`
--

INSERT INTO `ghost_net` (`id`, `latitude`, `longitude`, `size`, `status`, `last_updated`, `user_id`) VALUES
(90, 54.43, 13.6, 320, 'BERGUNG_BEVORSTEHEND', '2025-10-05 15:40:04.265003', 175),
(91, 54.178, 7.893, 120, 'BERGUNG_BEVORSTEHEND', '2025-10-05 15:40:28.467025', 176),
(92, 54.4475, 11.2, 480, 'GEBORGEN', '2025-10-05 16:06:02.547501', 183),
(93, 39.503, 3.245, 650, 'GEMELDET', '2025-10-05 15:31:24.587909', 163),
(94, 38.73, -27.25, 1100, 'GEMELDET', '2025-10-05 15:31:47.230677', 164),
(95, 43.51, 16.44, 220, 'BERGUNG_BEVORSTEHEND', '2025-10-05 16:06:57.105287', 184),
(96, 54.375, 18.9, 450, 'GEMELDET', '2025-10-05 15:34:33.525231', 166),
(97, 50.95, 1.87, 260, 'GEMELDET', '2025-10-05 15:34:54.686414', 167),
(98, 55.13, 15, 300, 'GEBORGEN', '2025-10-05 15:54:17.761459', 177),
(99, 43.5, 43.5, 540, 'GEMELDET', '2025-10-05 15:36:29.063613', 169),
(100, 21.8, -158.2, 130, 'GEMELDET', '2025-10-05 15:36:54.880463', 170),
(101, -34.55, 18.5, 890, 'GEMELDET', '2025-10-05 15:37:22.100893', 171),
(102, 43.5, 145.5, 410, 'GEMELDET', '2025-10-05 15:37:44.164075', 172),
(103, -36.4, 175.1, 950, 'GEMELDET', '2025-10-05 15:38:27.277622', 173),
(104, 47, -52.7, 180, 'GEMELDET', '2025-10-05 15:38:47.229262', 174),
(105, -12.1, 130.8, 340, 'GEMELDET', '2025-10-05 15:42:48.683172', 178),
(106, 19.7, -72, 270, 'GEMELDET', '2025-10-05 15:43:04.507041', 179),
(107, 66, 13.5, 600, 'GEMELDET', '2025-10-05 15:43:24.172905', 180),
(108, -0.9, -90.4, 150, 'GEMELDET', '2025-10-05 15:43:44.344710', 181),
(109, 12.3, 43.2, 820, 'GEMELDET', '2025-10-05 15:44:00.632221', 182),
(110, 35.68, 139.76, 111, 'GEMELDET', '2025-10-05 16:09:18.449540', 185),
(111, -31.9, 114.8, 310, 'GEMELDET', '2025-10-05 16:14:14.379997', 186),
(112, 28.5, -16.2, 560, 'GEMELDET', '2025-10-05 16:14:46.377436', 187),
(113, 32.05, -64.75, 190, 'GEMELDET', '2025-10-05 19:15:18.185794', 188);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `role` enum('BERGENDE_PERSON','MELDENDE_PERSON') DEFAULT NULL,
  `anonym` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `user`
--

INSERT INTO `user` (`id`, `name`, `telephone`, `role`, `anonym`) VALUES
(160, 'Lena Kraus', '12345', 'MELDENDE_PERSON', 0),
(161, 'Tom Schubert', '12345', 'MELDENDE_PERSON', 0),
(162, 'Marek Pohl', '12345', 'MELDENDE_PERSON', 0),
(163, 'Carla Neumann', '12345', 'MELDENDE_PERSON', 0),
(164, 'Beatriz Sousa', '12345', 'MELDENDE_PERSON', 0),
(165, 'Ivana Kovač', '12345', 'MELDENDE_PERSON', 0),
(166, 'Agnieszka Nowak', '12345', 'MELDENDE_PERSON', 0),
(167, 'Sophie Martin', '12345', 'MELDENDE_PERSON', 0),
(168, 'Niklas Jensen', '12345', 'MELDENDE_PERSON', 0),
(169, 'Aitor García', '12345', 'MELDENDE_PERSON', 0),
(170, 'Keanu Hale', '12345', 'MELDENDE_PERSON', 0),
(171, 'Thandi Mbeki', '12345', 'MELDENDE_PERSON', 0),
(172, 'Haru Sato', '12345', 'MELDENDE_PERSON', 0),
(173, 'Amelia Jones', '12345', 'MELDENDE_PERSON', 0),
(174, 'Liam O’Rourke', '12345', 'MELDENDE_PERSON', 0),
(175, 'Max Vogel', '12345', 'BERGENDE_PERSON', 0),
(176, 'Jana Berger', '12345', 'BERGENDE_PERSON', 0),
(177, 'Marek Pohl', '12345', 'BERGENDE_PERSON', 0),
(178, 'Mia Harper', '12345', 'MELDENDE_PERSON', 0),
(179, 'Carlos Méndez', '12345', 'MELDENDE_PERSON', 0),
(180, 'Eirik Hansen', '12345', 'MELDENDE_PERSON', 0),
(181, 'Mateo Rojas', '12345', 'MELDENDE_PERSON', 0),
(182, 'Amina Said', '12345', 'MELDENDE_PERSON', 0),
(183, 'Julian Kovač', '12345', 'BERGENDE_PERSON', 0),
(184, 'Tamara Rochè', '12345', 'BERGENDE_PERSON', 0),
(185, 'Tokio', '12', 'MELDENDE_PERSON', 0),
(186, NULL, NULL, 'MELDENDE_PERSON', 1),
(187, NULL, NULL, 'MELDENDE_PERSON', 1),
(188, 'Thomas Peterson', '12345', 'MELDENDE_PERSON', 0);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `ghost_net`
--
ALTER TABLE `ghost_net`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKh1l1y0aes31awppricyw60dkb` (`user_id`);

--
-- Indizes für die Tabelle `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `ghost_net`
--
ALTER TABLE `ghost_net`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=114;

--
-- AUTO_INCREMENT für Tabelle `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=189;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `ghost_net`
--
ALTER TABLE `ghost_net`
  ADD CONSTRAINT `FKh1l1y0aes31awppricyw60dkb` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
