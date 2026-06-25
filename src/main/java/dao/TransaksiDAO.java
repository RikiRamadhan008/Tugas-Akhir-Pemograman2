/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package dao;

import config.DBConnection;
import model.Transaksi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class TransaksiDAO {

    private final Connection conn;

    public TransaksiDAO() {
        this.conn = DBConnection.getInstance().getConnection();
    }

    public boolean save(Transaksi t) {
        String sql = "INSERT INTO transaksi "
                   + "(id_transaksi, id_pelanggan, jenis_laundry, berat, harga_per_kg, "
                   + " total_harga, tgl_masuk, tgl_selesai, status, catatan) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getIdTransaksi());
            ps.setString(2, t.getIdPelanggan());
            ps.setString(3, t.getJenisLaundry());
            ps.setDouble(4, t.getBerat());
            ps.setDouble(5, t.getHargaPerKg());
            ps.setDouble(6, t.getTotalHarga());
            ps.setDate(7, t.getTglMasuk());
            ps.setDate(8, t.getTglSelesai());
            ps.setString(9, t.getStatus());
            ps.setString(10, t.getCatatan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error save: " + e.getMessage());
            return false;
        }
    }

    public List<Transaksi> getAll() {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.id_transaksi, t.id_pelanggan, p.nama AS nama_pelanggan, "
                   + "t.jenis_laundry, t.berat, t.harga_per_kg, t.total_harga, "
                   + "t.tgl_masuk, t.tgl_selesai, t.status, t.catatan, t.created_at "
                   + "FROM transaksi t "
                   + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                   + "ORDER BY t.created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error getAll: " + e.getMessage());
        }
        return list;
    }

    public Transaksi getById(String idTransaksi) {
        String sql = "SELECT t.id_transaksi, t.id_pelanggan, p.nama AS nama_pelanggan, "
                   + "t.jenis_laundry, t.berat, t.harga_per_kg, t.total_harga, "
                   + "t.tgl_masuk, t.tgl_selesai, t.status, t.catatan, t.created_at "
                   + "FROM transaksi t "
                   + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                   + "WHERE t.id_transaksi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idTransaksi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error getById: " + e.getMessage());
        }
        return null;
    }

    public List<Transaksi> getUnpaid() {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.id_transaksi, t.id_pelanggan, p.nama AS nama_pelanggan, "
                   + "t.jenis_laundry, t.berat, t.harga_per_kg, t.total_harga, "
                   + "t.tgl_masuk, t.tgl_selesai, t.status, t.catatan, t.created_at "
                   + "FROM transaksi t "
                   + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                   + "LEFT JOIN pembayaran py ON t.id_transaksi = py.id_transaksi "
                   + "WHERE py.id_pembayaran IS NULL "
                   + "ORDER BY t.tgl_masuk DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error getUnpaid: " + e.getMessage());
        }
        return list;
    }


    public List<Transaksi> search(String keyword) {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.id_transaksi, t.id_pelanggan, p.nama AS nama_pelanggan, "
                   + "t.jenis_laundry, t.berat, t.harga_per_kg, t.total_harga, "
                   + "t.tgl_masuk, t.tgl_selesai, t.status, t.catatan, t.created_at "
                   + "FROM transaksi t "
                   + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                   + "WHERE p.nama LIKE ? OR t.id_transaksi LIKE ? "
                   + "ORDER BY t.created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error search: " + e.getMessage());
        }
        return list;
    }

 
    public List<Transaksi> filterByStatus(String status) {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.id_transaksi, t.id_pelanggan, p.nama AS nama_pelanggan, "
                   + "t.jenis_laundry, t.berat, t.harga_per_kg, t.total_harga, "
                   + "t.tgl_masuk, t.tgl_selesai, t.status, t.catatan, t.created_at "
                   + "FROM transaksi t "
                   + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                   + "WHERE t.status = ? "
                   + "ORDER BY t.created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error filterByStatus: " + e.getMessage());
        }
        return list;
    }

    public boolean update(Transaksi t) {
        String sql = "UPDATE transaksi SET id_pelanggan=?, jenis_laundry=?, berat=?, "
                   + "harga_per_kg=?, total_harga=?, tgl_masuk=?, tgl_selesai=?, "
                   + "status=?, catatan=? WHERE id_transaksi=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getIdPelanggan());
            ps.setString(2, t.getJenisLaundry());
            ps.setDouble(3, t.getBerat());
            ps.setDouble(4, t.getHargaPerKg());
            ps.setDouble(5, t.getTotalHarga());
            ps.setDate(6, t.getTglMasuk());
            ps.setDate(7, t.getTglSelesai());
            ps.setString(8, t.getStatus());
            ps.setString(9, t.getCatatan());
            ps.setString(10, t.getIdTransaksi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error update: " + e.getMessage());
            return false;
        }
    }

 
    public boolean updateStatus(String idTransaksi, String status) {
        String sql = "UPDATE transaksi SET status = ? WHERE id_transaksi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, idTransaksi);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error updateStatus: " + e.getMessage());
            return false;
        }
    }

   
    public boolean delete(String idTransaksi) {
        String sql = "DELETE FROM transaksi WHERE id_transaksi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idTransaksi);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error delete: " + e.getMessage());
            return false;
        }
    }

  
    public String generateId() {
        java.time.LocalDate today = java.time.LocalDate.now();
        String tanggal = today.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix  = "TRX-" + tanggal + "-";

        String sql = "SELECT MAX(CAST(SUBSTRING(id_transaksi, LENGTH(?) + 1) AS UNSIGNED)) AS max_id "
                   + "FROM transaksi WHERE id_transaksi LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix);
            ps.setString(2, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int maxId = rs.getInt("max_id");
                    return prefix + String.format("%03d", maxId + 1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error generateId: " + e.getMessage());
        }
        return prefix + "001";
    }

    public int getTotalTransaksi() {
        String sql = "SELECT COUNT(*) FROM transaksi";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error getTotalTransaksi: " + e.getMessage());
        }
        return 0;
    }

    public double getTotalPendapatan() {
        String sql = "SELECT COALESCE(SUM(total_harga), 0) FROM transaksi";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error getTotalPendapatan: " + e.getMessage());
        }
        return 0;
    }

 
    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM transaksi WHERE status = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO] Error countByStatus: " + e.getMessage());
        }
        return 0;
    }


    private Transaksi mapRow(ResultSet rs) throws SQLException {
        Transaksi t = new Transaksi();
        t.setIdTransaksi(rs.getString("id_transaksi"));
        t.setIdPelanggan(rs.getString("id_pelanggan"));
        t.setNamaPelanggan(rs.getString("nama_pelanggan"));
        t.setJenisLaundry(rs.getString("jenis_laundry"));
        t.setBerat(rs.getDouble("berat"));
        t.setHargaPerKg(rs.getDouble("harga_per_kg"));
        t.setTotalHarga(rs.getDouble("total_harga"));
        t.setTglMasuk(rs.getDate("tgl_masuk"));
        t.setTglSelesai(rs.getDate("tgl_selesai"));
        t.setStatus(rs.getString("status"));
        t.setCatatan(rs.getString("catatan"));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        return t;
    }
}
