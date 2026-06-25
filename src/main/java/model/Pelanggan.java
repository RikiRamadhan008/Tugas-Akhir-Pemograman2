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
public class Pelanggan {

    private String    idPelanggan;
    private String    nama;
    private String    noHp;
    private String    alamat;
    private Timestamp createdAt;


    public Pelanggan() {}

  
    public Pelanggan(String nama, String noHp, String alamat) {
        this.nama   = nama;
        this.noHp   = noHp;
        this.alamat = alamat;
    }


    public Pelanggan(String idPelanggan, String nama, String noHp, String alamat, Timestamp createdAt) {
        this.idPelanggan = idPelanggan;
        this.nama        = nama;
        this.noHp        = noHp;
        this.alamat      = alamat;
        this.createdAt   = createdAt;
    }


    public String getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(String idPelanggan) { this.idPelanggan = idPelanggan; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNoHp() { return noHp; }
    public void setNoHp(String noHp) { this.noHp = noHp; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return idPelanggan + " - " + nama;
    }
}
