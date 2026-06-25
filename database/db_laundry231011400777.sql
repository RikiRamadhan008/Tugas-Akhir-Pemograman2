-- ================================================================
-- MyLaundry Database Script
-- Versi    : 1.0
-- Dibuat   : 2026-06-20
-- Deskripsi: Script lengkap pembuatan database MyLaundry
-- ================================================================

-- Buat database jika belum ada
CREATE DATABASE IF NOT EXISTS mylaundry
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE mylaundry;

-- ----------------------------------------------------------------
-- Hapus tabel yang ada (urutan terbalik karena FK)
-- ----------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS pembayaran;
DROP TABLE IF EXISTS transaksi;
DROP TABLE IF EXISTS pelanggan;
DROP TABLE IF EXISTS user;
SET FOREIGN_KEY_CHECKS = 1;

-- ================================================================
-- TABLE: user
-- Menyimpan data pengguna aplikasi (admin/kasir)
-- ================================================================
CREATE TABLE user (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    username     VARCHAR(50)  NOT NULL UNIQUE COMMENT 'Username untuk login',
    password     VARCHAR(32)  NOT NULL COMMENT 'Password dalam format MD5',
    nama_lengkap VARCHAR(100) NOT NULL COMMENT 'Nama lengkap pengguna',
    role         ENUM('admin', 'kasir') NOT NULL DEFAULT 'kasir' COMMENT 'Level akses',
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Data pengguna aplikasi';

-- ================================================================
-- TABLE: pelanggan
-- Menyimpan data pelanggan laundry
-- ================================================================
CREATE TABLE pelanggan (
    id_pelanggan VARCHAR(10)  PRIMARY KEY COMMENT 'Format: PLG-001',
    nama         VARCHAR(100) NOT NULL COMMENT 'Nama lengkap pelanggan',
    no_hp        VARCHAR(15)  NOT NULL COMMENT 'Nomor handphone',
    alamat       TEXT COMMENT 'Alamat lengkap pelanggan',
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Data pelanggan laundry';

-- ================================================================
-- TABLE: transaksi
-- Menyimpan data transaksi laundry
-- ================================================================
CREATE TABLE transaksi (
    id_transaksi  VARCHAR(20)    PRIMARY KEY COMMENT 'Format: TRX-YYYYMMDD-001',
    id_pelanggan  VARCHAR(10)    NOT NULL COMMENT 'Referensi ke tabel pelanggan',
    jenis_laundry ENUM('Cuci Kering', 'Cuci Setrika', 'Setrika Saja', 'Express') NOT NULL,
    berat         DECIMAL(5,2)   NOT NULL COMMENT 'Berat dalam Kilogram',
    harga_per_kg  DECIMAL(10,2)  NOT NULL COMMENT 'Harga per kilogram dalam Rupiah',
    total_harga   DECIMAL(10,2)  NOT NULL COMMENT 'Total = berat x harga_per_kg',
    tgl_masuk     DATE           NOT NULL COMMENT 'Tanggal laundry masuk',
    tgl_selesai   DATE           COMMENT 'Perkiraan tanggal selesai',
    status        ENUM('Proses', 'Dicuci', 'Disetrika', 'Selesai', 'Sudah Diambil')
                  NOT NULL DEFAULT 'Proses',
    catatan       TEXT COMMENT 'Catatan tambahan',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaksi_pelanggan
        FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id_pelanggan)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Data transaksi laundry';

-- ================================================================
-- TABLE: pembayaran
-- Menyimpan data pembayaran dari transaksi
-- ================================================================
CREATE TABLE pembayaran (
    id_pembayaran VARCHAR(20)   PRIMARY KEY COMMENT 'Format: PAY-YYYYMMDD-001',
    id_transaksi  VARCHAR(20)   NOT NULL UNIQUE COMMENT 'Satu transaksi, satu pembayaran',
    total_bayar   DECIMAL(10,2) NOT NULL COMMENT 'Jumlah yang harus dibayar',
    uang_bayar    DECIMAL(10,2) NOT NULL COMMENT 'Uang yang diberikan pelanggan',
    kembalian     DECIMAL(10,2) NOT NULL COMMENT 'Kembalian = uang_bayar - total_bayar',
    tgl_bayar     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status_bayar  ENUM('Lunas', 'Belum Lunas') NOT NULL DEFAULT 'Lunas',
    CONSTRAINT fk_pembayaran_transaksi
        FOREIGN KEY (id_transaksi) REFERENCES transaksi(id_transaksi)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Data pembayaran laundry';

-- ================================================================
-- INDEX untuk performa query
-- ================================================================
CREATE INDEX idx_transaksi_status     ON transaksi(status);
CREATE INDEX idx_transaksi_tgl_masuk  ON transaksi(tgl_masuk);
CREATE INDEX idx_transaksi_pelanggan  ON transaksi(id_pelanggan);
CREATE INDEX idx_pembayaran_transaksi ON pembayaran(id_transaksi);
CREATE INDEX idx_pelanggan_nama       ON pelanggan(nama);

-- ================================================================
-- DATA DEFAULT
-- ================================================================

-- Akun pengguna default
-- Password admin: admin123  (MD5: 0192023a7bbd73250516f069df18b500)
-- Password kasir: kasir123  (MD5: 5c84a879a91a93e95bc17bfd65e3c3c7)
INSERT INTO user (username, password, nama_lengkap, role) VALUES
('admin', MD5('admin123'), 'Administrator', 'admin'),
('kasir', MD5('kasir123'), 'Kasir Utama', 'kasir');

-- Data pelanggan contoh
INSERT INTO pelanggan (id_pelanggan, nama, no_hp, alamat) VALUES
('PLG-001', 'Ahmad Fauzi',    '081234567890', 'Jl. Mawar No. 12, Kel. Menteng, Jakarta Pusat'),
('PLG-002', 'Siti Rahayu',   '082345678901', 'Jl. Melati No. 5, Kel. Harapan, Bekasi Utara'),
('PLG-003', 'Budi Santoso',  '083456789012', 'Jl. Anggrek No. 8, Kel. Cipondoh, Tangerang'),
('PLG-004', 'Dewi Lestari',  '084567890123', 'Jl. Kenanga No. 3, Kel. Pasar Minggu, Jakarta Selatan'),
('PLG-005', 'Eko Prasetyo',  '085678901234', 'Jl. Dahlia No. 15, Kel. Cilincing, Jakarta Utara');

-- Data transaksi contoh
INSERT INTO transaksi (id_transaksi, id_pelanggan, jenis_laundry, berat, harga_per_kg, total_harga, tgl_masuk, tgl_selesai, status, catatan) VALUES
('TRX-20260620-001', 'PLG-001', 'Cuci Setrika',  3.50, 7000,  24500, '2026-06-20', '2026-06-22', 'Proses',       'Pakaian putih dipisah'),
('TRX-20260620-002', 'PLG-002', 'Cuci Kering',   2.00, 5000,  10000, '2026-06-20', '2026-06-21', 'Selesai',      NULL),
('TRX-20260620-003', 'PLG-003', 'Express',        1.50, 15000, 22500, '2026-06-20', '2026-06-20', 'Sudah Diambil',NULL),
('TRX-20260619-001', 'PLG-004', 'Setrika Saja',  4.00, 4000,  16000, '2026-06-19', '2026-06-20', 'Selesai',      NULL),
('TRX-20260619-002', 'PLG-005', 'Cuci Setrika',  5.50, 7000,  38500, '2026-06-19', '2026-06-21', 'Dicuci',       'Jemput antar');

-- Data pembayaran contoh
INSERT INTO pembayaran (id_pembayaran, id_transaksi, total_bayar, uang_bayar, kembalian, status_bayar) VALUES
('PAY-20260620-001', 'TRX-20260620-002', 10000, 10000,  0,    'Lunas'),
('PAY-20260620-002', 'TRX-20260620-003', 22500, 25000,  2500, 'Lunas'),
('PAY-20260619-001', 'TRX-20260619-001', 16000, 20000,  4000, 'Lunas');

-- ================================================================
-- VIEWS untuk Laporan
-- ================================================================

-- View laporan transaksi lengkap
CREATE OR REPLACE VIEW v_laporan_transaksi AS
SELECT
    t.id_transaksi,
    p.nama AS nama_pelanggan,
    p.no_hp,
    t.jenis_laundry,
    t.berat,
    t.harga_per_kg,
    t.total_harga,
    t.tgl_masuk,
    t.tgl_selesai,
    t.status,
    CASE WHEN py.id_pembayaran IS NOT NULL THEN 'Lunas' ELSE 'Belum Lunas' END AS status_bayar,
    py.uang_bayar,
    py.kembalian,
    py.tgl_bayar
FROM transaksi t
JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan
LEFT JOIN pembayaran py ON t.id_transaksi = py.id_transaksi
ORDER BY t.tgl_masuk DESC;

-- View pendapatan per bulan
CREATE OR REPLACE VIEW v_pendapatan_bulanan AS
SELECT
    YEAR(tgl_masuk)  AS tahun,
    MONTH(tgl_masuk) AS bulan,
    MONTHNAME(tgl_masuk) AS nama_bulan,
    COUNT(*)         AS total_transaksi,
    SUM(total_harga) AS total_pendapatan
FROM transaksi
GROUP BY YEAR(tgl_masuk), MONTH(tgl_masuk)
ORDER BY tahun DESC, bulan DESC;

-- ================================================================
-- END OF SCRIPT
-- ================================================================
SELECT 'Database MyLaundry berhasil dibuat!' AS pesan;
