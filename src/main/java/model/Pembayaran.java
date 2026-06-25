/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package model;

import java.sql.Timestamp;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class Pembayaran {

    private String    idPembayaran;
    private String    idTransaksi;
    private double    totalBayar;
    private double    uangBayar;
    private double    kembalian;
    private Timestamp tglBayar;
    private String    statusBayar;


    private String    namaPelanggan;
    private String    jenisLaundry;

    public Pembayaran() {}

 
    public Pembayaran(String idPembayaran, String idTransaksi,
                      double totalBayar, double uangBayar,
                      double kembalian, String statusBayar) {
        this.idPembayaran = idPembayaran;
        this.idTransaksi  = idTransaksi;
        this.totalBayar   = totalBayar;
        this.uangBayar    = uangBayar;
        this.kembalian    = kembalian;
        this.statusBayar  = statusBayar;
    }

 

    public String getIdPembayaran() { return idPembayaran; }
    public void setIdPembayaran(String idPembayaran) { this.idPembayaran = idPembayaran; }

    public String getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(String idTransaksi) { this.idTransaksi = idTransaksi; }

    public double getTotalBayar() { return totalBayar; }
    public void setTotalBayar(double totalBayar) { this.totalBayar = totalBayar; }

    public double getUangBayar() { return uangBayar; }
    public void setUangBayar(double uangBayar) { this.uangBayar = uangBayar; }

    public double getKembalian() { return kembalian; }
    public void setKembalian(double kembalian) { this.kembalian = kembalian; }

    public Timestamp getTglBayar() { return tglBayar; }
    public void setTglBayar(Timestamp tglBayar) { this.tglBayar = tglBayar; }

    public String getStatusBayar() { return statusBayar; }
    public void setStatusBayar(String statusBayar) { this.statusBayar = statusBayar; }

    public String getNamaPelanggan() { return namaPelanggan; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }

    public String getJenisLaundry() { return jenisLaundry; }
    public void setJenisLaundry(String jenisLaundry) { this.jenisLaundry = jenisLaundry; }


    public double hitungKembalian() {
        this.kembalian = this.uangBayar - this.totalBayar;
        return this.kembalian;
    }

    @Override
    public String toString() {
        return "Pembayaran{id='" + idPembayaran + "', transaksi='" + idTransaksi
                + "', total=" + totalBayar + ", status='" + statusBayar + "'}";
    }
}
