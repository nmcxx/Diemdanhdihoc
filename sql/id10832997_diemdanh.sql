-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost
-- Thời gian đã tạo: Th1 27, 2021 lúc 03:50 AM
-- Phiên bản máy phục vụ: 10.3.16-MariaDB
-- Phiên bản PHP: 7.3.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `id10832997_diemdanh`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `BuoiHoc`
--

CREATE TABLE `BuoiHoc` (
  `idbuoihoc` int(11) NOT NULL,
  `idlophoc` int(11) NOT NULL,
  `sobuoi` int(11) DEFAULT NULL,
  `madiemdanh` varchar(13) COLLATE utf8_unicode_ci NOT NULL,
  `hethan` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `BuoiHoc`
--

INSERT INTO `BuoiHoc` (`idbuoihoc`, `idlophoc`, `sobuoi`, `madiemdanh`, `hethan`) VALUES
(1, 6, 1, 'f3tLt5', 1),
(8, 6, 2, 'f3tLt4', 1),
(9, 6, 3, 'f3t4t5', 1),
(10, 7, 1, '412d3', 1),
(15, 6, 4, 'f3tLt5', 1),
(21, 7, 2, '4758b1', 0),
(22, 6, 5, '4e758d', 1),
(23, 6, 6, '98a93b', 0);

--
-- Bẫy `BuoiHoc`
--
DELIMITER $$
CREATE TRIGGER `trg_BuoiHoc_BuoiHocMoi` BEFORE INSERT ON `BuoiHoc` FOR EACH ROW BEGIN
	set new.madiemdanh = (SELECT SUBSTR(CONCAT(MD5(RAND()),MD5(RAND())),1,6));
	set new.sobuoi=(select COUNT(*) from `BuoiHoc` where idlophoc=new.idlophoc)+1;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `LopHoc`
--

CREATE TABLE `LopHoc` (
  `idlophoc` int(11) NOT NULL,
  `malop` varchar(7) COLLATE utf8_unicode_ci NOT NULL,
  `username` varchar(17) COLLATE utf8_unicode_ci NOT NULL,
  `tenlop` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `LopHoc`
--

INSERT INTO `LopHoc` (`idlophoc`, `malop`, `username`, `tenlop`) VALUES
(6, 'JSAEM3', 'can', 'Phân tích và thiết kế thuật toán'),
(7, '1d414F', 'can', 'lap trinh c');

--
-- Bẫy `LopHoc`
--
DELIMITER $$
CREATE TRIGGER `trg_LopHoc_BuoiHocDau` AFTER INSERT ON `LopHoc` FOR EACH ROW BEGIN
	set @key = (SELECT SUBSTR(CONCAT(MD5(RAND()),MD5(RAND())),1,6));
	INSERT INTO `BuoiHoc` (`idbuoihoc`, `idlophoc`, `sobuoi`, `madiemdanh`, `hethan`) VALUES (NULL,new.idlophoc,'1',@key,'0');
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `SVDD`
--

CREATE TABLE `SVDD` (
  `idsvdd` int(11) NOT NULL,
  `idbuoihoc` int(11) NOT NULL,
  `username` varchar(17) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `SVDD`
--

INSERT INTO `SVDD` (`idsvdd`, `idbuoihoc`, `username`) VALUES
(1, 1, 'f'),
(2, 1, 'g'),
(3, 1, 'da'),
(4, 1, 'manhcan'),
(5, 8, 'f'),
(6, 9, 'da'),
(7, 9, 'manhcan'),
(8, 9, 'g'),
(9, 9, 'f'),
(10, 15, 'da'),
(11, 22, 'can1zx'),
(12, 22, 'da');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `SVLH`
--

CREATE TABLE `SVLH` (
  `idsvlh` int(11) NOT NULL,
  `idlophoc` int(11) NOT NULL,
  `username` varchar(17) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `SVLH`
--

INSERT INTO `SVLH` (`idsvlh`, `idlophoc`, `username`) VALUES
(3, 6, 'da'),
(4, 6, 'manhcan'),
(5, 6, 'f'),
(6, 6, 'g'),
(9, 6, 'hiimgosu'),
(12, 6, 'daa'),
(13, 6, 'czz'),
(14, 6, 'canda'),
(15, 6, 'can1zx'),
(18, 7, 'da'),
(20, 7, ''),
(28, 6, 'can1'),
(32, 6, 'a'),
(33, 6, 'c'),
(34, 6, 'cancc'),
(35, 6, 'can');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `TaiKhoan`
--

CREATE TABLE `TaiKhoan` (
  `username` varchar(17) COLLATE utf8_unicode_ci NOT NULL,
  `password` text COLLATE utf8_unicode_ci NOT NULL,
  `usertype` tinyint(1) NOT NULL,
  `hoten` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `TaiKhoan`
--

INSERT INTO `TaiKhoan` (`username`, `password`, `usertype`, `hoten`) VALUES
('\0\0\0\0\0\0\0\0\0', '\0\0\0\0\0\0\0\0\0', 0, ''),
('', '', 0, ''),
('\0.\0', '\0.\0', 0, ''),
('\'testtt\'', '\'czcx\'', 0, '\'testtt\''),
('a', 'a', 0, ''),
('aaxccv', 'mz', 0, 'mz'),
('axccv', 'mz', 0, 'mz'),
('\0\0\0\0\0b	\0', '\0\0\0\0\0b	\0', 0, ''),
('b', 'b', 0, ''),
('c', 'c', 0, ''),
('can', 'can', 1, 'Mạnh Cần'),
('can1', 'can1', 0, ''),
('can1zx', 'czxa', 0, 'can1zx'),
('can2', 'can2', 0, ''),
('can3', 'can3', 0, ''),
('can4', '', 0, ''),
('cancc', 'cancc', 0, 'Tồ Tẽt'),
('canda', 'canca', 0, 'canda'),
('cann', 'cann', 0, ''),
('czz', '', 0, 'czz'),
('d', 'd', 0, ''),
('da', 'da', 0, 'Gosu viet nam'),
('daa', 'de', 0, ''),
('\0\0\0\0\0e	\0', '\0\0\0\0\0e	\0', 0, ''),
('e', 'e', 0, ''),
('f', 'f', 0, ''),
('g', 'g', 0, ''),
('hiimgosu', 'hiimgosu', 0, 'Văn Mạnh'),
('manhcan', 'manh can', 0, ''),
('manhcan1', 'manhcan', 0, ''),
('manhcan2', 'manhcan', 0, ''),
('manhcan4', 'manhcaN!@#$!@$!@', 0, ''),
('manhcan5', 'manhcaN d d3 121s s', 0, ''),
('mz', 'mz', 0, 'mz'),
('nnn', 'nnn', 0, ''),
('testtac', 'dcz', 0, 'testtac'),
('testtt', 'adascx', 0, 'testtt');

--
-- Bẫy `TaiKhoan`
--
DELIMITER $$
CREATE TRIGGER `trg_TaiKhoan_reg` AFTER INSERT ON `TaiKhoan` FOR EACH ROW begin 
#	insert into TTTK(username) VALUES(new.username);
end
$$
DELIMITER ;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `BuoiHoc`
--
ALTER TABLE `BuoiHoc`
  ADD PRIMARY KEY (`idbuoihoc`),
  ADD KEY `FK_BuoiHoc_idlophoc` (`idlophoc`);

--
-- Chỉ mục cho bảng `LopHoc`
--
ALTER TABLE `LopHoc`
  ADD PRIMARY KEY (`idlophoc`),
  ADD KEY `FK_LopHoc_username` (`username`);

--
-- Chỉ mục cho bảng `SVDD`
--
ALTER TABLE `SVDD`
  ADD PRIMARY KEY (`idsvdd`),
  ADD KEY `FK_SVDD_idbuoihoc` (`idbuoihoc`),
  ADD KEY `FK_SVDDc_username` (`username`);

--
-- Chỉ mục cho bảng `SVLH`
--
ALTER TABLE `SVLH`
  ADD PRIMARY KEY (`idsvlh`),
  ADD KEY `FK_SVLH_idlophoc` (`idlophoc`),
  ADD KEY `FK_SVLH_username` (`username`);

--
-- Chỉ mục cho bảng `TaiKhoan`
--
ALTER TABLE `TaiKhoan`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `BuoiHoc`
--
ALTER TABLE `BuoiHoc`
  MODIFY `idbuoihoc` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT cho bảng `LopHoc`
--
ALTER TABLE `LopHoc`
  MODIFY `idlophoc` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT cho bảng `SVDD`
--
ALTER TABLE `SVDD`
  MODIFY `idsvdd` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT cho bảng `SVLH`
--
ALTER TABLE `SVLH`
  MODIFY `idsvlh` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `BuoiHoc`
--
ALTER TABLE `BuoiHoc`
  ADD CONSTRAINT `FK_BuoiHoc_idlophoc` FOREIGN KEY (`idlophoc`) REFERENCES `LopHoc` (`idlophoc`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `LopHoc`
--
ALTER TABLE `LopHoc`
  ADD CONSTRAINT `FK_LopHoc_username` FOREIGN KEY (`username`) REFERENCES `TaiKhoan` (`username`);

--
-- Các ràng buộc cho bảng `SVDD`
--
ALTER TABLE `SVDD`
  ADD CONSTRAINT `FK_SVDD_idbuoihoc` FOREIGN KEY (`idbuoihoc`) REFERENCES `BuoiHoc` (`idbuoihoc`),
  ADD CONSTRAINT `FK_SVDDc_username` FOREIGN KEY (`username`) REFERENCES `TaiKhoan` (`username`);

--
-- Các ràng buộc cho bảng `SVLH`
--
ALTER TABLE `SVLH`
  ADD CONSTRAINT `FK_SVLH_idlophoc` FOREIGN KEY (`idlophoc`) REFERENCES `LopHoc` (`idlophoc`),
  ADD CONSTRAINT `FK_SVLH_username` FOREIGN KEY (`username`) REFERENCES `TaiKhoan` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
