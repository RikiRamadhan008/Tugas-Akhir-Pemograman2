/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package dao;

import config.DBConnection;
import model.Pelanggan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class PelangganDAO {

    private final Connection conn;

    public PelangganDAO() {
        this.conn = DBConnection.getInstance().getConnection();
    }

   
    public boolean save(Pelanggan pelanggan) {
        String sql = "INSERT INTO pelanggan (id_pelanggan, nama, no_hp, alamat) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pelanggan.getIdPelanggan());
            ps.setString(2, pelanggan.getNama());
            ps.setString(3, pelanggan.getNoHp());
            ps.setString(4, pelanggan.getAlamat());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PelangganDAO] Error save: " + e.getMessage());
            return false;
        }
    }

   
    public List<Pelanggan> getAll() {
        List<Pelanggan> list = new ArrayList<>();
        String sql = "SELECT id_pelanggan, nama, no_hp, alamat, created_at "
                   + "FROM pelanggan ORDER BY id_pelanggan";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PelangganDAO] Error getAll: " + e.getMessage());
        }
        return list;
    }

  
    public Pelanggan getById(String idPelanggan) {
        String sql = "SELECT id_pelanggan, nama, no_hp, alamat, created_at "
                   + "FROM pelanggan WHERE id_pelanggan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPelanggan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[PelangganDAO] Error getById: " + e.getMessage());
        }
        return null;
    }

    
    public List<Pelanggan> search(String keyword) {
        List<Pelanggan> list = new ArrayList<>();
        String sql = "SELECT id_pelanggan, nama, no_hp, alamat, created_at "
                   + "FROM pelanggan "
                   + "WHERE nama LIKE ? OR id_pelanggan LIKE ? OR no_hp LIKE ? "
                   + "ORDER BY nama";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[PelangganDAO] Error search: " + e.getMessage());
        }
        return list;
    }

   
    public boolean update(Pelanggan pelanggan) {
        String sql = "UPDATE pelanggan SET nama = ?, no_hp = ?, alamat = ? "
                   + "WHERE id_pelanggan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pelanggan.getNama());
            ps.setString(2, pelanggan.getNoHp());
            ps.setString(3, pelanggan.getAlamat());
            ps.setString(4, pelanggan.getIdPelanggan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PelangganDAO] Error update: " + e.getMessage());
            return false;
        }
    }

  
    public boolean delete(String idPelanggan) {
        String sql = "DELETE FROM pelanggan WHERE id_pelanggan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPelanggan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PelangganDAO] Error delete: " + e.getMessage());
            return false;
        }
    }

  
    public String generateId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(id_pelanggan, 5) AS UNSIGNED)) AS max_id FROM pelanggan";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                return String.format("PLG-%03d", maxId + 1);
            }
        } catch (SQLException e) {
            System.err.println("[PelangganDAO] Error generateId: " + e.getMessage());
        }
        return "PLG-001";
    }

    
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM pelanggan";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[PelangganDAO] Error countAll: " + e.getMessage());
        }
        return 0;
    }

   
    private Pelanggan mapRow(ResultSet rs) throws SQLException {
        return new Pelanggan(
            rs.getString("id_pelanggan"),
            rs.getString("nama"),
            rs.getString("no_hp"),
            rs.getString("alamat"),
            rs.getTimestamp("created_at")
        );
    }
}
