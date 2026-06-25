/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class Transaksi {

    // Konstanta untuk jenis laundry
    public static final String JENIS_CUCI_KERING   = "Cuci Kering";
    public static final String JENIS_CUCI_SETRIKA   = "Cuci Setrika";
    public static final String JENIS_SETRIKA_SAJA   = "Setrika Saja";
    public static final String JENIS_EXPRESS        = "Express";

    // Konstanta untuk status transaksi
    public static final String STATUS_PROSES        = "Proses";
    public static final String STATUS_DICUCI        = "Dicuci";
    public static final String STATUS_DISETRIKA     = "Disetrika";
    public static final String STATUS_SELESAI       = "Selesai";
    public static final String STATUS_SUDAH_DIAMBIL = "Sudah Diambil";

    private String    idTransaksi;
    private String    idPelanggan;
    private String    namaPelanggan;    // Join dari tabel pelanggan (bukan kolom DB)
    private String    jenisLaundry;
    private double    berat;
    private double    hargaPerKg;
    private double    totalHarga;
    private Date      tglMasuk;
    private Date      tglSelesai;
    private String    status;
    private String    catatan;
    private Timestamp createdAt;


    public Transaksi() {}

  
    public Transaksi(String idTransaksi, String idPelanggan, String jenisLaundry,
                     double berat, double hargaPerKg, double totalHarga,
                     Date tglMasuk, Date tglSelesai, String status, String catatan) {
        this.idTransaksi  = idTransaksi;
        this.idPelanggan  = idPelanggan;
        this.jenisLaundry = jenisLaundry;
        this.berat        = berat;
        this.hargaPerKg   = hargaPerKg;
        this.totalHarga   = totalHarga;
        this.tglMasuk     = tglMasuk;
        this.tglSelesai   = tglSelesai;
        this.status       = status;
        this.catatan      = catatan;
    }


    public String getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(String idTransaksi) { this.idTransaksi = idTransaksi; }

    public String getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(String idPelanggan) { this.idPelanggan = idPelanggan; }

    public String getNamaPelanggan() { return namaPelanggan; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }

    public String getJenisLaundry() { return jenisLaundry; }
    public void setJenisLaundry(String jenisLaundry) { this.jenisLaundry = jenisLaundry; }

    public double getBerat() { return berat; }
    public void setBerat(double berat) { this.berat = berat; }

    public double getHargaPerKg() { return hargaPerKg; }
    public void setHargaPerKg(double hargaPerKg) { this.hargaPerKg = hargaPerKg; }

    public double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(double totalHarga) { this.totalHarga = totalHarga; }

    public void hitungTotalHarga() {
        this.totalHarga = this.berat * this.hargaPerKg;
    }

    public Date getTglMasuk() { return tglMasuk; }
    public void setTglMasuk(Date tglMasuk) { this.tglMasuk = tglMasuk; }

    public Date getTglSelesai() { return tglSelesai; }
    public void setTglSelesai(Date tglSelesai) { this.tglSelesai = tglSelesai; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return idTransaksi + " (" + (namaPelanggan != null ? namaPelanggan : idPelanggan) + ")";
    }
}
