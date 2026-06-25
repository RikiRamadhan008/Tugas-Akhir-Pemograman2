/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package dao;

import config.DBConnection;
import model.Pembayaran;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class PembayaranDAO {

    private final Connection conn;

    public PembayaranDAO() {
        this.conn = DBConnection.getInstance().getConnection();
    }

   
    public boolean save(Pembayaran p) {
        String sql = "INSERT INTO pembayaran "
                   + "(id_pembayaran, id_transaksi, total_bayar, uang_bayar, kembalian, status_bayar) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getIdPembayaran());
            ps.setString(2, p.getIdTransaksi());
            ps.setDouble(3, p.getTotalBayar());
            ps.setDouble(4, p.getUangBayar());
            ps.setDouble(5, p.getKembalian());
            ps.setString(6, p.getStatusBayar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PembayaranDAO] Error save: " + e.getMessage());
            return false;
        }
    }

    
    public List<Pembayaran> getAll() {
        List<Pembayaran> list = new ArrayList<>();
        String sql = "SELECT py.id_pembayaran, py.id_transaksi, p.nama AS nama_pelanggan, "
                   + "t.jenis_laundry, py.total_bayar, py.uang_bayar, py.kembalian, "
                   + "py.tgl_bayar, py.status_bayar "
                   + "FROM pembayaran py "
                   + "JOIN transaksi t ON py.id_transaksi = t.id_transaksi "
                   + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                   + "ORDER BY py.tgl_bayar DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PembayaranDAO] Error getAll: " + e.getMessage());
        }
        return list;
    }

   
    public Pembayaran getByTransaksi(String idTransaksi) {
        String sql = "SELECT py.id_pembayaran, py.id_transaksi, p.nama AS nama_pelanggan, "
                   + "t.jenis_laundry, py.total_bayar, py.uang_bayar, py.kembalian, "
                   + "py.tgl_bayar, py.status_bayar "
                   + "FROM pembayaran py "
                   + "JOIN transaksi t ON py.id_transaksi = t.id_transaksi "
                   + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                   + "WHERE py.id_transaksi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idTransaksi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("[PembayaranDAO] Error getByTransaksi: " + e.getMessage());
        }
        return null;
    }

   
    public Pembayaran getById(String idPembayaran) {
        String sql = "SELECT py.id_pembayaran, py.id_transaksi, p.nama AS nama_pelanggan, "
                   + "t.jenis_laundry, py.total_bayar, py.uang_bayar, py.kembalian, "
                   + "py.tgl_bayar, py.status_bayar "
                   + "FROM pembayaran py "
                   + "JOIN transaksi t ON py.id_transaksi = t.id_transaksi "
                   + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                   + "WHERE py.id_pembayaran = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPembayaran);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("[PembayaranDAO] Error getById: " + e.getMessage());
        }
        return null;
    }

   
    public boolean isTransaksiSudahDibayar(String idTransaksi) {
        String sql = "SELECT COUNT(*) FROM pembayaran WHERE id_transaksi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idTransaksi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[PembayaranDAO] Error isTransaksiSudahDibayar: " + e.getMessage());
        }
        return false;
    }

    
    public String generateId() {
        java.time.LocalDate today = java.time.LocalDate.now();
        String tanggal = today.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix  = "PAY-" + tanggal + "-";

        String sql = "SELECT MAX(CAST(SUBSTRING(id_pembayaran, LENGTH(?) + 1) AS UNSIGNED)) AS max_id "
                   + "FROM pembayaran WHERE id_pembayaran LIKE ?";
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
            System.err.println("[PembayaranDAO] Error generateId: " + e.getMessage());
        }
        return prefix + "001";
    }


    public double getTotalPendapatanBulan(int tahun, int bulan) {
        String sql = "SELECT COALESCE(SUM(py.total_bayar), 0) "
                   + "FROM pembayaran py "
                   + "WHERE YEAR(py.tgl_bayar) = ? AND MONTH(py.tgl_bayar) = ? "
                   + "AND py.status_bayar = 'Lunas'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tahun);
            ps.setInt(2, bulan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("[PembayaranDAO] Error getTotalPendapatanBulan: " + e.getMessage());
        }
        return 0;
    }


    private Pembayaran mapRow(ResultSet rs) throws SQLException {
        Pembayaran p = new Pembayaran();
        p.setIdPembayaran(rs.getString("id_pembayaran"));
        p.setIdTransaksi(rs.getString("id_transaksi"));
        p.setNamaPelanggan(rs.getString("nama_pelanggan"));
        p.setJenisLaundry(rs.getString("jenis_laundry"));
        p.setTotalBayar(rs.getDouble("total_bayar"));
        p.setUangBayar(rs.getDouble("uang_bayar"));
        p.setKembalian(rs.getDouble("kembalian"));
        p.setTglBayar(rs.getTimestamp("tgl_bayar"));
        p.setStatusBayar(rs.getString("status_bayar"));
        return p;
    }
}
