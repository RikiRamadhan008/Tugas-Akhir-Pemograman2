/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class DBConnection {

    private static DBConnection instance;
    private Connection connection;

    private static final String PROPERTIES_FILE = "/db.properties";

    private DBConnection() {
        try {
            Properties props = new Properties();
            InputStream is = getClass().getResourceAsStream(PROPERTIES_FILE);
            if (is == null) {
                throw new RuntimeException("File db.properties tidak ditemukan di classpath!");
            }
            props.load(is);
            is.close();

            String url      = props.getProperty("db.url");
            String user     = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("[DB] Koneksi ke database berhasil.");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("[DB] Driver MySQL tidak ditemukan: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("[DB] Gagal terhubung ke database: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("[DB] Error konfigurasi database: " + e.getMessage(), e);
        }
    }

    public static synchronized DBConnection getInstance() {
        try {
            if (instance == null || instance.connection == null || instance.connection.isClosed()) {
                instance = new DBConnection();
            }
        } catch (SQLException e) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        if (instance != null && instance.connection != null) {
            try {
                if (!instance.connection.isClosed()) {
                    instance.connection.close();
                    System.out.println("[DB] Koneksi database ditutup.");
                }
            } catch (SQLException e) {
                System.err.println("[DB] Error menutup koneksi: " + e.getMessage());
            } finally {
                instance = null;
            }
        }
    }
}
