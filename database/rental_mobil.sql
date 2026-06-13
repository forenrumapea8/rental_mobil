DROP DATABASE IF EXISTS rental_mobil;
CREATE DATABASE rental_mobil CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE rental_mobil;

SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE admin (
  id_admin INT AUTO_INCREMENT PRIMARY KEY,
  nama VARCHAR(100) NOT NULL,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  status ENUM('Aktif','Nonaktif') NOT NULL DEFAULT 'Aktif',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE petugas (
  id_petugas INT AUTO_INCREMENT PRIMARY KEY,
  nama VARCHAR(100) NOT NULL,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  no_hp VARCHAR(20),
  alamat TEXT,
  status ENUM('Aktif','Nonaktif') NOT NULL DEFAULT 'Aktif',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE mobil (
  id_mobil INT AUTO_INCREMENT PRIMARY KEY,
  merk VARCHAR(100) NOT NULL,
  tipe VARCHAR(100),
  plat_nomor VARCHAR(20) NOT NULL UNIQUE,
  tahun VARCHAR(4),
  tarif_per_hari DECIMAL(12,2) NOT NULL DEFAULT 0,
  status ENUM('Tersedia','Dipinjam','Servis') NOT NULL DEFAULT 'Tersedia',
  INDEX idx_mobil_status(status),
  INDEX idx_mobil_plat(plat_nomor)
) ENGINE=InnoDB;

CREATE TABLE pelanggan (
  id_pelanggan INT AUTO_INCREMENT PRIMARY KEY,
  nama VARCHAR(100) NOT NULL,
  nik VARCHAR(30),
  no_hp VARCHAR(20),
  alamat TEXT,
  jenis_kelamin ENUM('Laki-laki','Perempuan') DEFAULT 'Laki-laki',
  INDEX idx_pelanggan_nama(nama),
  INDEX idx_pelanggan_hp(no_hp)
) ENGINE=InnoDB;

CREATE TABLE denda (
  id_denda INT AUTO_INCREMENT PRIMARY KEY,
  nama_denda VARCHAR(100) NOT NULL,
  nominal_per_hari DECIMAL(12,2) NOT NULL DEFAULT 0,
  status ENUM('Aktif','Nonaktif') NOT NULL DEFAULT 'Aktif',
  keterangan TEXT
) ENGINE=InnoDB;

CREATE TABLE rental (
  id_rental INT AUTO_INCREMENT PRIMARY KEY,
  id_pelanggan INT NOT NULL,
  id_mobil INT NOT NULL,
  id_petugas INT NULL,
  tanggal_pinjam DATE NOT NULL,
  tanggal_kembali DATE NOT NULL,
  lama_sewa INT NOT NULL DEFAULT 1,
  tarif_per_hari DECIMAL(12,2) NOT NULL DEFAULT 0,
  total DECIMAL(12,2) NOT NULL DEFAULT 0,
  status ENUM('Berjalan','Selesai','Batal') NOT NULL DEFAULT 'Berjalan',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_rental_status(status),
  INDEX idx_rental_tanggal(tanggal_pinjam,tanggal_kembali),
  CONSTRAINT fk_rental_pelanggan FOREIGN KEY(id_pelanggan) REFERENCES pelanggan(id_pelanggan) ON UPDATE CASCADE,
  CONSTRAINT fk_rental_mobil FOREIGN KEY(id_mobil) REFERENCES mobil(id_mobil) ON UPDATE CASCADE,
  CONSTRAINT fk_rental_petugas FOREIGN KEY(id_petugas) REFERENCES petugas(id_petugas) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE pengembalian (
  id_pengembalian INT AUTO_INCREMENT PRIMARY KEY,
  id_rental INT NOT NULL UNIQUE,
  tanggal_kembali_aktual DATE NOT NULL,
  terlambat_hari INT NOT NULL DEFAULT 0,
  denda DECIMAL(12,2) NOT NULL DEFAULT 0,
  total_akhir DECIMAL(12,2) NOT NULL DEFAULT 0,
  id_petugas INT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_pengembalian_tanggal(tanggal_kembali_aktual),
  CONSTRAINT fk_pengembalian_rental FOREIGN KEY(id_rental) REFERENCES rental(id_rental) ON UPDATE CASCADE,
  CONSTRAINT fk_pengembalian_petugas FOREIGN KEY(id_petugas) REFERENCES petugas(id_petugas) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS=1;

INSERT INTO admin(nama,username,password,status) VALUES
('Administrator','admin','admin','Aktif');

INSERT INTO petugas(nama,username,password,no_hp,alamat,status) VALUES
('Petugas Rental','petugas','petugas','081234567890','Kediri','Aktif'),
('Rina Petugas','rina','rina','081222333444','Kediri','Aktif');

INSERT INTO mobil(merk,tipe,plat_nomor,tahun,tarif_per_hari,status) VALUES
('Toyota','Avanza','AG 1234 AA','2021',350000,'Tersedia'),
('Honda','Brio','AG 5678 BB','2022',300000,'Tersedia'),
('Daihatsu','Xenia','AG 9012 CC','2020',325000,'Tersedia'),
('Mitsubishi','Xpander','AG 7788 DD','2023',450000,'Tersedia');

INSERT INTO pelanggan(nama,nik,no_hp,alamat,jenis_kelamin) VALUES
('Budi Santoso','3571010101010001','081111111111','Kediri','Laki-laki'),
('Siti Aminah','3571020202020002','082222222222','Nganjuk','Perempuan'),
('Ahmad Fauzi','3571030303030003','083333333333','Blitar','Laki-laki');

INSERT INTO denda(nama_denda,nominal_per_hari,status,keterangan) VALUES
('Denda Keterlambatan',50000,'Aktif','Denda otomatis per hari keterlambatan pengembalian mobil');
